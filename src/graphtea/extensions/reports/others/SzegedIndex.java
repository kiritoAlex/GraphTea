// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/
package graphtea.extensions.reports.others;

import graphtea.extensions.algorithms.shortestpath.algs.FloydWarshall;
import graphtea.graph.graph.Edge;
import graphtea.graph.graph.GraphModel;
import graphtea.platform.lang.CommandAttitude;
import graphtea.plugins.reports.extension.GraphReportExtension;

/**
 * @author Ali Rostami

 */


@CommandAttitude(name = "szeged_index", abbreviation = "_SzegedIndex")
public class SzegedIndex implements GraphReportExtension<Integer> {
    public String getName() {
        return "Szeged Index";
    }

    public String getDescription() {
        return "Szeged Index";
    }

    public Integer calculate(GraphModel g) {
        FloydWarshall fw = new FloydWarshall();
        int[][] dists = fw.getAllPairsShortestPathWithoutWeight(g);
        int sum = 0;
        for(Edge e : g.getEdges()) {
            int u = e.source.getId();
            int v = e.target.getId();
            int nu = 0, nv = 0;
            for(int i=0;i<dists[0].length;i++) {
                if(dists[u][i] > dists[v][i]) nu++;
                if(dists[u][i] < dists[v][i]) nv++;
            }
            sum += nu * nv;
        }
        return sum;
    }

	@Override
	public String getCategory() {
		return "Topological Indices-Distance";
	}
}
