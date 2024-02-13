// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/

package graphtea.graph.ui;

import graphtea.platform.core.BlackBoard;
import graphtea.platform.core.exception.ExceptionHandler;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author Azin Azadi
 */
class DialogLinkHandler implements HyperlinkHandler {

    public void handle(String url, BlackBoard b, URL currentURL) {
        String path = currentURL.getProtocol() + "://" + currentURL.getPath();
        String title = url.substring(url.indexOf(',') + 1);
        if (!url.toLowerCase().startsWith("http:"))
            url = url.substring(0, url.indexOf(','));
        url = path.substring(0, path.lastIndexOf("/")) + "/" + url;
        try {
            showPageInDialog(title, b, new URL(url));
        } catch (MalformedURLException e) {
            ExceptionHandler.catchException(e);
        }

    }

    /**
     * shows the given page in a new dialog,
     * Note that the used html viewer is GHTMLPageComponent, which is internally
     * uses a JEditorPane,
     */
    private static void showPageInDialog(String title, BlackBoard blackboard, URL page) {
        JFrame f = new JFrame(title);
        GHTMLPageComponent browserPane = new GHTMLPageComponent(blackboard);
        browserPane.setPage(page);
        f.add(browserPane);
        f.setVisible(true);
        f.setSize(700, 500);
        f.validate();
        f.setResizable(false);
    }

}
