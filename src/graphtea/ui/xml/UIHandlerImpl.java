// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/
package graphtea.ui.xml;

import graphtea.platform.StaticUtils;
import graphtea.platform.core.AbstractAction;
import graphtea.platform.core.BlackBoard;
import graphtea.platform.core.exception.ExceptionHandler;
import graphtea.platform.extension.Extension;
import graphtea.platform.extension.ExtensionLoader;
import graphtea.platform.lang.Pair;
import graphtea.platform.preferences.lastsettings.StorableOnExit;
import graphtea.ui.UIUtils;
import graphtea.ui.actions.UIEventData;
import graphtea.ui.components.*;
import graphtea.ui.components.gmenu.GMenuBar;
import graphtea.ui.components.gmenu.GMenuItem;
import graphtea.ui.components.gmenu.KeyBoardShortCut;
import graphtea.ui.components.gmenu.KeyBoardShortCutProvider;
import graphtea.ui.components.gsidebar.GSidebar;
import graphtea.ui.extension.AbstractExtensionAction;
import graphtea.ui.extension.UIActionExtensionAction;
import org.xml.sax.Attributes;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author Azin Azadi, M. Ali Rostami
 */

public class UIHandlerImpl implements UIHandler, StorableOnExit {

    public static final boolean DEBUG = false;
    public GToolbar toolbar;
    public BlackBoard blackboard;
    Class resourceClass;
    HashMap<String, graphtea.platform.core.AbstractAction> actions;
    /**
     * determines the character which if put before a character in the string of the label, that character will be set to it's mnemonics
     */
    public static final char menueIndexChar = '_';

    public UIHandlerImpl(GFrame gFrame, BlackBoard bb
            , HashMap<String, graphtea.platform.core.AbstractAction> actions, Class resClass) {
        this.blackboard = bb;
        this.toolbar = gFrame.getToolbar();
        this.sidebar = gFrame.getSidebar();
        this.statusbar = gFrame.getStatusbar();
        //this.blackboard = gFrame.blackboard;
        this.menubar = gFrame.getMenu();
        this.frame = gFrame;
        this.actions = actions;
        this.resourceClass = resClass;
//        toolbar.add(lastToolbar);
    }

    //**********      TOOLBARS handling  ---------------------
    private JToolBar lastToolbar = new JToolBar();
    private int lastToolbarPlace;

    public void start_toolbar(final Attributes meta) {
//        lastToolbar = new JToolBar();
//        lastToolbar.setFloatable(false);
        lastToolbar = toolbar.createToolBar();
        lastToolbarPlace = extractPlace(meta);
        if (DEBUG) System.err.println("start_toolbar: " + meta);
    }

    public void handle_tool(final Attributes meta) {
        String label = meta.getValue("label");
        String icon = meta.getValue("image");
        String action = meta.getValue("action");
        String _place = meta.getValue("place");
        int place = -1;
        if (_place != null)
            place = Integer.parseInt(_place);
        GButton b;

        if (resourceClass != null && icon != null)
            System.out.println("[handle_tool]" + icon + " : " + resourceClass.getResource(icon));

        if (icon == null || icon.equals("") || resourceClass == null ||
                resourceClass.getResource(icon) == null)
            b = new GButton(label, icon, blackboard, action);
        else
            b = new GButton(label, resourceClass.getResource(icon), blackboard, action);
        b.setBorderPainted(false);
//        b.setBorder(new EmptyBorder(0, 1, 0, 2));

        lastToolbar.add(b);
        if (DEBUG) System.err.println("handle_tool: " + meta);
    }

    public void end_toolbar()  {
        lastToolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.addIndexed(lastToolbar, lastToolbarPlace);
//        toolbar.add(lastToolbar);
        if (DEBUG) System.err.println("end_toolbar()");
    }

    public void start_toolbars(Attributes meta) {
//        lastToolbar = toolbar.getLastToolBar();
    }

    public void end_toolbars() {
    }

    //***************** status handling --------------------
    public GStatusBar statusbar;

    public void handle_bar(Attributes meta) {
        String clazz = meta.getValue("class");
        String id = meta.getValue("id");
        System.out.println("Adding the Bar with id:" + id + " ,class:" + clazz);
        Component component = getComponent(clazz);
        //we put the component in the blackboad and later (in Actions) we can fetch it if we like, i guess that it is the only way to do the loading of side bar and actions dynamically
        UIUtils.setComponent(blackboard, id, component);
        statusbar.addComponent(component);
    }

    //*****************menu handling------------------------
    public GMenuBar menubar;
    private final GFrame frame;
    private JMenu currentMenu;

    public void start_menues(Attributes meta) {
    }

    private Pair<Integer, String> extractLabelInfo(String label) {
        int index = Math.max(label.indexOf(menueIndexChar), 0);
        label = label.replace(menueIndexChar + "", "");
        return new Pair<>(index, label);
    }

    int lastMenuPlace;

    public void start_submenu(final Attributes meta)  {
        String label = meta.getValue("label");
        String accel = meta.getValue("accelerator");
        Pair<Integer, String> lInfo = extractLabelInfo(label);
        lastMenuPlace = extractPlace(meta);
        int index = lInfo.first;
        label = lInfo.second;
        currentMenu = menubar.getUniqueMenu(label, lastMenuPlace);
//        currentMenu = new JMenu(label);

        KeyBoardShortCut shortcut = KeyBoardShortCutProvider.registerKeyBoardShortcut(accel, label, index);
        if (shortcut != null) {
            if (!shortcut.isAccelerator()) {
                currentMenu.setMnemonic(shortcut.getKeyMnemonic());
                currentMenu.setDisplayedMnemonicIndex(shortcut.getKeyWordIndex());
            }
        }
        if (DEBUG) System.err.println("start_submenu: " + meta);
    }

    public void handle_menu(final Attributes meta) {
        String label = meta.getValue("label");
        String action = meta.getValue("action");
        String accel = meta.getValue("accelerator");
        int place = extractPlace(meta);
        if (label.equals("seperator_menu")) {
            JSeparator js = new JSeparator(JSeparator.HORIZONTAL);
            GMenuBar.insert(currentMenu, js, place);
            return;
        }
        
        Pair<Integer, String> lInfo = extractLabelInfo(label);
        int index = lInfo.first;
        label = lInfo.second;

        GMenuItem item;
        /*
         * extension handling part:
         * if the menu action was an extension removes the menu
         * that the extension created in UI and set it to this menu.
         */
        
        graphtea.platform.core.AbstractAction targetAction = actions.get(action);
        
        if (targetAction instanceof AbstractExtensionAction) {
            AbstractExtensionAction targetExt = (AbstractExtensionAction) targetAction;
            item = targetExt.menuItem;
            /*
             * set the label properties according to XML
             */
            item.setText(label);
            //todo: BUG the mnemotic doesn't set
            KeyBoardShortCut shortcut = null;
            String desc =targetExt.getTarget().getDescription();
            if(desc != null && desc.contains("HotKey:(")) {
                String tmp = desc.substring(desc.indexOf("HotKey:(") + 1);
                tmp = tmp.substring(0,tmp.indexOf(")"));
                shortcut = KeyBoardShortCutProvider.registerKeyBoardShortcut(tmp, label, index);
            }

            if(shortcut != null) {
                item.setAccelerator(KeyStroke.getKeyStroke(shortcut.getKeyEvent(), shortcut.getKeyModifiers()));
                item.setDisplayedMnemonicIndex(shortcut.getKeyWordIndex());
                item.setMnemonic(shortcut.getKeyMnemonic());
            }

        } else {
            item = new GMenuItem(label, action, blackboard, accel, index);
        }

        GMenuBar.insert(currentMenu, item, place);
        if (DEBUG) System.err.println("handle_menu: " + meta);
    }

    private int extractPlace(Attributes meta) {
        String _place = meta.getValue("place");
        int place = -1;     //place -1 means no idea given for place,
        try {
            if (_place != null)
                place = Integer.parseInt(_place);
        }
        catch (NumberFormatException e) {
            System.err.println("the place given for menu " + meta.getValue("label") + " is not a valid number:" + _place);
        }
        return place;
    }

    public void end_submenu()  {
//        currentMenu.add(new JSeparator(JSeparator.VERTICAL));
//        GMenuBar.insert(currentMenu, new JSeparator(JSeparator.VERTICAL), -1);
        //todo: add ability of adding JSeperator to UI
        if (DEBUG) System.err.println("end_submenu()");
    }

    public void end_menues()  {
        //pak kardan e menu e action e ezafe
        GMenuBar menu = frame.getMenu();
        for (int i = 0; i < menu.getMenuCount(); i++) {
            if (menu.getMenu(i).getText().equals("Operators")
                    && menu.getMenu(i).getSubElements().length == 1) {
                menu.remove(i);
                return;
            }
        }
    }
//***************** action handling ----------------------

// If action has a group, then its default value for enable will be true,
// else it will true if enable property equals to true.

    public void handle_action(Attributes meta) {
        String clazz = meta.getValue("class");
        String id = meta.getValue("id");
        String group = meta.getValue("group");
//todo: is it good to remove the action wich loaded twice, (2 of same action are working together)
        System.err.println("  Adding action " + clazz + " (" + id + "," + group + ") ...");

        Class<Extension> clazzz;
        try {
            clazzz = (Class<Extension>) Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            System.err.println("the given class name can't be loaded: " + clazz);
            ExceptionHandler.catchException(e);
            return;
        }
        graphtea.platform.core.AbstractAction x = loadAbstractAction(clazz);

/*
 * handling extensions here
 * they are not AbstractAction, but their handlers should
 * return an AbstractAction(if it support the extension)
 * and after that the program know the
 * extension by that AbstractAction
 */
//        if (clazzz.isAssignableFrom(Extension.class)) {

//        for (ExtensionHandler s : ExtensionLoader.getRegisteredExtensionHandlers()) {
        if (x == null) {
            Extension e = ExtensionLoader.loadExtension(clazzz);
//            SETTINGS.registerSetting(e,"Extention Options");     //Moved to Extension Loader
            if (e != null)
                x = ExtensionLoader.handleExtension(blackboard, e);
        }
//            x = s.handle(blackboard, clazzz);
//        b = b | x != null;
        if (x != null) {
            if (id == null) {
                id = x.getLastListenedEventKey();
                id = id.replaceFirst(UIEventData.name(""), "");
            }
            if (x instanceof UIActionExtensionAction) {
                UIActionExtensionAction action = (UIActionExtensionAction) x;
                action.setUIEvent(id);
            }
            addAction(id, x, group);
//            }
//        }
//        }
//        if (b) {
//            return;
        }
        if (x == null) {
            System.err.println("Error while loading " + clazz + ". skipped.");
            StaticUtils.addExceptiontoLog(new Throwable("Error while loading " + clazz + ". skipped."), blackboard);
            return; //error, skip it.
        }
        addAction(id, x, group);
    }

    private void addAction(String id, graphtea.platform.core.AbstractAction x, String group) {
        if ((id != null) && !id.equals(""))
            actions.put(id, x);
//        if (group != null && !group.equals("")) {
//            //configuration age group vojood nadashte bashe khodesh ijadesh mikone. inja be ghole omid "error prone hast"
//            //todo: bara hamin be zehnam resid ke biaim bebinim esme gorooha masalan age kamtar az 2harf ekhtelaf daran, pas ehtemalan eshtebahe typi boode , ie jooraii kashf konim eroro :D
//            conf.addToGroup(group, x);
//        }
    }

    //****************** side bar handling -------------------------
    public GSidebar sidebar;

    public void handle_sidebar(Attributes meta) {
        String image = meta.getValue("image") + "";//to getting it not null
        String clazz = meta.getValue("class");
        String id = meta.getValue("id");
        String label = meta.getValue("label");

        Component component = getComponent(clazz);
        UIUtils.setComponent(blackboard, id, component);
        if (resourceClass == null || resourceClass.getResource(image) == null) {
            sidebar.addButton(image, component, label);
        } else
            sidebar.addButton(resourceClass.getResource(image), component, label);
    }

//************* body handling ------------------------------------
    public void handle_body(Attributes meta) {
        String clazz = meta.getValue("class");
        String id = meta.getValue("id");
        Component gci = getComponent(clazz);
        frame.getBody().setBodyPane(gci);
        UIUtils.setComponent(blackboard, id, gci);
    }

//************** utilities +++++++++++++++++++++

    AbstractAction loadAbstractAction(String abstractActionclazz) {
        if (!(abstractActionclazz == null) && !(abstractActionclazz.equals(""))) {
            Class t = clazz2Class(abstractActionclazz);
            if (graphtea.platform.core.AbstractAction.class.isAssignableFrom(t)) {
                Object[] o = {blackboard};
                try {
                    Constructor c = t.getConstructor(BlackBoard.class);
                    Object o1 = c.newInstance(o);
                    return (graphtea.platform.core.AbstractAction) o1;
                }
                catch (Exception e) {
//                    System.err.println("Error while loading " + clazz);
                    ExceptionHandler.catchException(e);
                }
            }
        }
//        System.err.println("Error while loading " + clazz);
        return null;
    }

    //todo: it is possible to also get a component from xml by it's direct class name, like javax.swing.JLabel . but i decided not to do it for cleaner codes! i am not sure is it good or not?
    Component getComponent(String GComponentInterfaceClassName) {
        if (!(GComponentInterfaceClassName == null) && !(GComponentInterfaceClassName.equals(""))) {
            Class t = clazz2Class(GComponentInterfaceClassName);
            Constructor c = null;
            Object[] o = {blackboard};
            try {
                c = t.getConstructor(BlackBoard.class);
            } catch (NoSuchMethodException e) {
                try {
                    c = t.getConstructor();
                    o = new Object[]{};
                } catch (NoSuchMethodException e1) {
                    System.err.println("the clazz " + GComponentInterfaceClassName + "does not have a constructor(blackboard) or constructor(), how can i load it?");
                    e1.printStackTrace();
                }
//                ExceptionHandler.catchException(e);
            }
            //if it had a constructor(blackboard)
            try {
                Object o1 = c.newInstance(o);
                if (o1 instanceof GComponentInterface) {
                    //load was successfull
                    return ((GComponentInterface) o1).getComponent(blackboard);
                } else {
                    System.err.println("the class " + GComponentInterfaceClassName + " doesn't implement the interface GComponentInterface, so it can't be put on the UI.");
                }
            } catch (InstantiationException | IllegalAccessException e) {
                System.err.println("There was an error while initializing the class" + GComponentInterfaceClassName + "may be in it's constructor or in one of classes it instantiate in its constructor");
                ExceptionHandler.catchException(e);
            } catch (InvocationTargetException e) {
                System.err.println("There was an error while initializing the class" + GComponentInterfaceClassName + "may be in it's constructor or in one of classes it instantiate in its constructor");
                e.getTargetException().printStackTrace();
            }
        }
        return null;
    }

    private Class clazz2Class(String clazz) {
        Class t = null;
        try {
            t = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            System.err.println("the Class" + clazz + "didn't found in class path");
            ExceptionHandler.catchException(e);
        }
        return t;
    }

}