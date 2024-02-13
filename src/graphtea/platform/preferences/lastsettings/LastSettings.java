// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/
package graphtea.platform.preferences.lastsettings;

import graphtea.platform.StaticUtils;
import graphtea.platform.attribute.AttributeListener;
import graphtea.platform.attribute.NotifiableAttributeSetImpl;
import graphtea.platform.core.exception.ExceptionHandler;
import graphtea.platform.parameter.Parameter;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

/**
 * @author Rouzbeh Ebrahimi
 */
public class LastSettings implements AttributeListener {
    private final HashSet<Object> registeredObjects = new HashSet<>();
    private final java.util.prefs.Preferences builtInPrefs = java.util.prefs.Preferences.userRoot();
    private final java.util.prefs.Preferences graphPrefs = builtInPrefs.node("graph");
    private final HashSet<Class> registeredClasses = new HashSet<>();
    private final File file = new File("prefs");
    {
        file.mkdir();
    }

    public LastSettings() {
        try {
            File file = new File(this.file, "graph.xml");
            FileInputStream is = new FileInputStream(file);
            Preferences.importPreferences(is);
            is.close();

        } catch (IOException | InvalidPreferencesFormatException e) {
            ExceptionHandler.catchException(e);
        }
    }

    public void registerSetting(Object o) {
        if (!registeredClasses.contains(o.getClass())) {
            registeredClasses.add(o.getClass());
            registeredObjects.add(o);
            loadSettings(o);
        }
    }

    private void saveField(Field f, java.util.prefs.Preferences t, Object o) {
        try {
            String key = f.getName();
            Object value = f.get(o);
            t.put(key, value.toString());
        } catch (IllegalAccessException e) {
            ExceptionHandler.catchException(e);
        }

    }

    private ByteArrayOutputStream convertObjectToByteArray(Object value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            oos.close();
        } catch (IOException e) {
            ExceptionHandler.catchException(e);
        }
        return baos;
    }

    private void loadField(Field f, java.util.prefs.Preferences t, Object o) {
        String key = f.getName();
        Object value = t.get(key, null);
        String m = f.getType().toString();
        Object obj = StaticUtils.fromString(m.substring(6), value.toString());
        try {
            if (obj != null)
                f.set(o, obj);
        } catch (IllegalAccessException e) {
            ExceptionHandler.catchException(e);
        }

    }

    private void loadSettings(Object o) {
        try {

            String objectName = o.getClass().getName();

            java.util.prefs.Preferences t = graphPrefs.node(objectName);
            t.put("object", objectName);
            for (Field f : o.getClass().getFields()) {
                UserModifiableProperty anot = f.getAnnotation(UserModifiableProperty.class);
                Parameter anot2 = f.getAnnotation(Parameter.class);
                if (anot != null || anot2 != null) {
                    loadField(f, t, o);
                }

            }
//            return GAttrFrame.showEditDialog(p, true).getReturnStatus();
//            performLoadJob();
        }
        catch (Exception e) {
            ExceptionHandler.catchException(e);
        }
    }

    private NotifiableAttributeSetImpl refactorSerializables(NotifiableAttributeSetImpl x) {
        NotifiableAttributeSetImpl y = new NotifiableAttributeSetImpl();
        Map<String, Object> map = x.getAttrs();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = map.get(key);
            if (value instanceof Serializable) {
                y.put(key, value);
            }
        }
        return y;
    }

    public void attributeUpdated(String name, Object oldVal, Object newVal) {
        try {
            oldVal.getClass().getDeclaredField(name).set(oldVal, newVal);
        } catch (IllegalAccessException | NoSuchFieldException e) {
//            ExceptionHandler.catchException(e);
        }
    }


    public void saveSettings() {
        for (Object o : registeredObjects) {
            String objectName = o.getClass().getName();
            java.util.prefs.Preferences t = graphPrefs.node(objectName);
            t.put("object", objectName);
            for (Field f : o.getClass().getFields()) {
                UserModifiableProperty anot = f.getAnnotation(UserModifiableProperty.class);
                Parameter anot2 = f.getAnnotation(Parameter.class);
                if (anot != null || anot2 != null) {
                    try {
                        saveField(f, t, f.get(o));
                    } catch (IllegalAccessException e) {
                        ExceptionHandler.catchException(e);
                    }
                }

            }
        }
        try {
            graphPrefs.exportSubtree(new FileOutputStream(new File(file, "graph.xml")));
//            graphPrefs.exportNode(new FileOutputStream(new File("/graph.xml")));
        } catch (IOException | BackingStoreException e) {
            e.printStackTrace();
        }

    }

}
