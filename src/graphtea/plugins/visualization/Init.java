// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/
package graphtea.plugins.visualization;

import graphtea.platform.core.BlackBoard;
import graphtea.platform.core.exception.ExceptionHandler;
import graphtea.platform.extension.ExtensionLoader;
import graphtea.platform.plugin.PluginInterface;
import graphtea.plugins.visualization.corebasics.extension.VisualizationExtensionHandler;
import graphtea.ui.UI;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author Rouzbeh
 */
public class Init implements PluginInterface {
    static {
        ExtensionLoader.registerExtensionHandler(new VisualizationExtensionHandler());

    }

    public void init(BlackBoard blackboard) {
        UI ui = blackboard.getData(UI.name);
        try {
            ui.addXML("/graphtea/plugins/visualization/VisualizationUI.xml", getClass());
        } catch (IOException e) {
            ExceptionHandler.catchException(e);
            System.out.println("xml file was not found , or IO error");

        } catch (SAXException e) {
            ExceptionHandler.catchException(e);
        }
    }
}
