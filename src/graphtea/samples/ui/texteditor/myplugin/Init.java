// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/

package graphtea.samples.ui.texteditor.myplugin;

import graphtea.platform.core.BlackBoard;
import graphtea.platform.core.exception.ExceptionHandler;
import graphtea.platform.plugin.PluginInterface;
import graphtea.samples.ui.texteditor.myplugin.actions.Utils;
import graphtea.ui.UI;
import graphtea.ui.UIUtils;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.IOException;

public class Init implements PluginInterface {
    public void init(BlackBoard blackboard) {
        //set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionHandler.catchException(e);
        }

        //load xml file
        BlackBoard blackBoard = new BlackBoard();
        UI u = new UI(blackBoard);
        try {
            u.loadXML("/myui.xml", u.getClass());
        } catch (IOException | SAXException e) {
            ExceptionHandler.catchException(e);
        }

        //gets the component which is created when loading status bar.
        //according to myui.xml it's id is "statusbar"
        final JLabel lbl = (JLabel) UIUtils.getComponent(blackBoard, "statusbar");
        final JTextArea editor = Utils.getMainEditor(blackBoard);

        //sets the lbl to show current row and colomn of caret in text editor
        editor.addCaretListener(e -> {
            try {
                int caretPosition = editor.getCaretPosition();
                int line = editor.getLineOfOffset(caretPosition);
                int col = caretPosition - editor.getLineStartOffset(line);
                lbl.setText(line + ":" + col);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        });

    }
}