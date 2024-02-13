// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/

package graphtea.plugins.commandline;

import bsh.Interpreter;
import bsh.util.NameCompletion;
import graphtea.plugins.commandline.util.codecompletionutils.CodeCompletionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;


/**
 * @author Mohammad Ali Rostami
 * @email rostamiev@gmail.com
 */

public class ShellCodeCompletion implements NameCompletion {
    public HashMap<String, Method> commands;
    public HashMap<String, String> abbrs;
    Interpreter interpreter;
    private final HashMap<String, Class> ext_commands;

    public ShellCodeCompletion(Interpreter interpreter
            , HashMap<String, Method> commands,
              HashMap<String, String> abbrs,
              HashMap<String, Class> ext_commands) {
        this.commands = commands;
        this.abbrs = abbrs;
        this.ext_commands = ext_commands;
        this.interpreter = interpreter;
    }

    public String[] completeName(String part) {
        Vector<String> ret = new Vector<>();
        if (part.startsWith("_")) {
            ret = CodeCompletionUtils.complete(abbrs, part);
        } else if (part.endsWith("(")) {
            ret = CodeCompletionUtils.complete(part, interpreter, commands, ext_commands);
        } else if (part.contains(".")) {
            ret = CodeCompletionUtils.complete(part, interpreter);
        } else {
            for (String temp : commands.keySet())
                if (temp.startsWith(part))
                    ret.add(temp);
            for (String temp : ext_commands.keySet())
                if (temp.startsWith(part))
                    ret.add(temp);
        }

        String[] res = new String[ret.size()];
        for (int i = 0; i < ret.size(); i++)
            res[i] = ret.get(i);
        return res;
    }
}
