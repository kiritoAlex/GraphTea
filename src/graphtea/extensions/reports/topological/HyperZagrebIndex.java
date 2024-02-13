// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/
package graphtea.extensions.reports.topological;

import graphtea.graph.graph.GraphModel;
import graphtea.platform.lang.CommandAttitude;
import graphtea.plugins.reports.extension.GraphReportExtension;

import java.util.ArrayList;

/**
 * @author Ali Rostami

 */

@CommandAttitude(name = "hyper_zagreb_index", abbreviation = "_zi")
public class HyperZagrebIndex implements GraphReportExtension<ArrayList<String>> {
    public String getName() {
        return "Hyper Zagreb Index";
    }

    public String getDescription() {
        return "Hyper Zagreb Index";
    }

    public ArrayList<String> calculate(GraphModel g) {
        ArrayList<String> out = new ArrayList<>();
        ZagrebIndexFunctions zif = new ZagrebIndexFunctions(g);
        out.add("Hyper Zagreb Index : " + zif.getGeneralSumConnectivityIndex(2));
        return out;
    }

    public String checkParameters() {
        return null;
    }

    @Override
    public String getCategory() {
        return "Topological Indices-Zagreb Indices";
    }
}