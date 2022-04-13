package graphtea.extensions.reports.topological;

import graphtea.extensions.AlgorithmUtils;
import graphtea.extensions.generators.CompleteGraphGenerator;
import graphtea.extensions.reports.ChromaticNumber;
import graphtea.extensions.reports.RandomMatching;
import graphtea.extensions.reports.basicreports.Diameter;
import graphtea.extensions.reports.basicreports.NumOfVerticesWithDegK;
import graphtea.extensions.reports.basicreports.NumOfTriangles;
import graphtea.extensions.reports.clique.MaxCliqueSize;
import graphtea.graph.graph.Edge;
import graphtea.graph.graph.GraphModel;
import graphtea.graph.graph.RenderTable;
import graphtea.graph.graph.Vertex;
import graphtea.platform.lang.CommandAttitude;
import graphtea.plugins.main.ui.TableRenderer;
import graphtea.plugins.reports.extension.GraphReportExtension;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * @author Ali Rostami
 */

@CommandAttitude(name = "VeIndex", abbreviation = "_VeIndex")
public class VeIndex implements GraphReportExtension<RenderTable> {
    public String getName() {
        return "VeIndex";
    }


    public String getDescription() {
        return "VeIndex";
    }

    public RenderTable calculate(GraphModel g) {
        ZagrebIndexFunctions zif = new ZagrebIndexFunctions(g);
        ZagrebIndexFunctions zifL = new ZagrebIndexFunctions(AlgorithmUtils.createLineGraph(g));
        RenderTable ret = new RenderTable();
        Vector<String> titles = new Vector<>();
        titles.add(" m ");
        // titles.add(" Max Planar ");
        titles.add(" n ");
        titles.add(" Zagreb ");
        titles.add(" Ve ");
        titles.add(" t ");
        titles.add(" VE ");
        titles.add(" V. Degrees ");

        ret.setTitles(titles);

        double maxDeg = 0;
        double maxDeg2 = 0;
        double minDeg = Integer.MAX_VALUE;
        double minDeg2 = AlgorithmUtils.getMinNonPendentDegree(g);

        ArrayList<Integer> al = AlgorithmUtils.getDegreesList(g);
        Collections.sort(al);
        maxDeg = al.get(al.size()-1);
        if(al.size()-2>=0) maxDeg2 = al.get(al.size()-2);
        else maxDeg2 = maxDeg;
        minDeg = al.get(0);
        if(maxDeg2 == 0) maxDeg2=maxDeg;

        double a=0;
        double b=0;
        double c=0;
        double d=0;
        int p = NumOfVerticesWithDegK.numOfVerticesWithDegK(g, 1);
        int t = NumOfTriangles.getNumOfTriangles(g);
        int VE = (int)zif.getFirstZagreb(1) - t;
        for(Vertex v : g) {
            if(g.getDegree(v)==maxDeg) a++;
            if(g.getDegree(v)==minDeg) b++;
            if(g.getDegree(v)==maxDeg2) c++;
            if(g.getDegree(v)==minDeg2) d++;
        }
        if(maxDeg==minDeg) b=0;
        if(maxDeg==maxDeg2) c=0;

        double m = g.getEdgesCount();
        double n = g.getVerticesCount();

        double maxEdge = 0;
        double maxEdge2 = 0;
        double minEdge = Integer.MAX_VALUE;

        ArrayList<Integer> all = new ArrayList<>();
        for(Edge e : g.getEdges()) {
            int f = g.getDegree(e.source) +
                    g.getDegree(e.target) - 2;
            all.add(f);
        }
        Collections.sort(all);
        maxEdge = all.get(all.size()-1);
        if(all.size()-2>=0) maxEdge2 = all.get(all.size()-2);
        else maxEdge2 = maxEdge;
        minEdge = all.get(0);



        double maxDel = 0;
        double maxDel2 = 0;
        double minDel = Integer.MAX_VALUE;

        ArrayList<Integer> all1 = new ArrayList<>();
        for(Edge e : g.getEdges()) {
            int f1 = ((2*(g.getDegree(e.source) * g.getDegree(e.target) ) )/((g.getDegree(e.source) + g.getDegree(e.target) )*(g.getDegree(e.source) + g.getDegree(e.target) ))) ;
            all1.add(f1);
        }
        Collections.sort(all1);
        maxDel = all1.get(all1.size()-1);
        if(all1.size()-2>=0) maxDel2 = all1.get(all1.size()-2);
        else maxDel2 = maxDel;
        minDel = all1.get(0);
        if(maxDel2 == 0) maxDel2=maxDel;


        double M12=zif.getSecondZagreb(1);
        double M21=zif.getFirstZagreb(1);



        List<Integer>[] gg = new List[g.getVerticesCount()];
        for (int i = 0; i < g.getVerticesCount(); i++) {
            gg[i] = new ArrayList<>();
        }

        for(Edge e : g.getEdges()) {
            gg[e.source.getId()].add(e.target.getId());
        }


        Vector<Object> v = new Vector<>();
        v.add(m);

        v.add(n);
        v.add(M21);
        v.add(M21-(3*t));

        v.add(t);
        v.add(VE);
        v.add(al.toString());

        ret.add(v);
        return ret;
    }

    @Override
    public String getCategory() {
        return "Verification-Degree";
    }
}



