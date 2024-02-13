package graphtea.extensions.algorithms.shortestpath;

import graphtea.graph.GraphUtils;
import graphtea.graph.graph.Edge;
import graphtea.graph.graph.GraphModel;
import graphtea.graph.graph.Vertex;
import graphtea.library.exceptions.InvalidVertexException;
import graphtea.platform.core.BlackBoard;
import graphtea.plugins.algorithmanimator.core.GraphAlgorithm;
import graphtea.plugins.algorithmanimator.extension.AlgorithmExtension;

import java.util.*;

/**
 *  This method finds the shortest path from a vertex to all vertices
 *  of a graph.
 *
 * Created by rostam on 06.03.15.
 * @author M. Ali Rostami
 */
public class DijkstraAlgorithm extends GraphAlgorithm implements AlgorithmExtension {
    public DijkstraAlgorithm(BlackBoard blackBoard) {
        super(blackBoard);
    }

    /**
     * This method finds a reference array using Dijkstra algorithm
     * from which, one can find
     * the shortest paths of all vertices of a graph from an arbitrary
     * given vertex.
     *
     * graph  Graph object to be searched.
     * vertex The source of the paths.
     * Creates Vector of vertices that for each i, it has a reference to
     *         the vertex, before the vertex with ID number i in the shortest path
     *         from "vertex" to i, or null if there is no such vertex.
     * @throws InvalidVertexException if the supplied vertices are invalid.
     */
    @Override
    public void doAlgorithm() {
        step("Start of the algorithm.") ;
        GraphModel graph = graphData.getGraph();
        Vertex startVertex = requestVertex(graph,"Select a starting vertex.");
        startVertex.setColor(6);
        Vertex targetVertex = requestVertex(graph,"Select a target vertex.");
        targetVertex.setColor(4);
        step("<br/>");
        step("We are searching now for the minimum path from the starting " +
                "vertex to the target vertex. The red vertex is the current vertex");
        step("---------------------------------------------------------------------<br/>");

        final int[] dist = new int[graph.getVerticesCount()];
        //the edge connected to i'th vertex
        final HashMap<Vertex, Edge> edges = new HashMap<>();

        HashMap<Vertex, Vertex> prev = new HashMap<>();

        Arrays.fill(dist, Integer.MAX_VALUE);

        dist[startVertex.getId()] = 0;

        class VertexComparator implements Comparator<Vertex> {
            public int compare(Vertex o1, Vertex o2) {
                return Integer.compare(dist[o1.getId()], dist[o2.getId()]);
            }
        }

        VertexComparator vComp = new VertexComparator();

        //selected vertices
        HashSet<Vertex> selectedVertices = new HashSet<>();

        PriorityQueue<Vertex> Q = new PriorityQueue<>(1, vComp);

        Q.add(startVertex);
        startVertex.setMark(true);
        while (!Q.isEmpty()) {
            Vertex vMin = Q.poll();
            vMin.setMark(true);
            Edge edg = edges.get(vMin);
            if (edg != null)
                edg.setMark(true);

            if(vMin.getId() != startVertex.getId())
                    vMin.setColor(2);

            selectedVertices.add(vMin);
            Iterator<Edge> iet = graph.edgeIterator(vMin);

            //step("Consider the neighbour vertex with the minimum weight.");
            //step("");
            step("Compute the distance from the starting node until the neighbours" +
                    " of the node " + vMin.getLabel() + " .");
            step("");
            GraphUtils gu = new GraphUtils();

            while ((iet.hasNext())) {
                Edge edge = iet.next();
                Vertex target = vMin == edge.source ? edge.target : edge.source;
                int tmp = dist[vMin.getId()] + edge.getWeight();
                if (!selectedVertices.contains(target)) {
                    GraphUtils.setMessage("Currently computed distance:"
                                    + tmp,
                            graphData.getBlackboard(), true);
                    step("<br/>");
                    if (dist[target.getId()] > dist[vMin.getId()] + edge.getWeight()) {
                        dist[target.getId()] = dist[vMin.getId()] + edge.getWeight();
                        edge.setMark(true);
                        edge.setColor(3);
                        if(prev.containsKey(target))
                            graph.getEdge(target,prev.get(target)).setColor(8);
                        edges.put(target, edge);
                        target.setMark(true);
                        if(target.getId() != targetVertex.getId())
                            target.setColor(5);
                        Q.add(target);
                        prev.put(edge.target, edge.source);
                    }
                }

                if(edge.target.getId() == targetVertex.getId()
                        || edge.source.getId() == targetVertex.getId()) {
                    GraphUtils.setMessage("The minimum path is found: " + dist[targetVertex.getId()],
                            graphData.getBlackboard(),true);
                    step("The minimum path is found: " + dist[targetVertex.getId()]);


                    Q.clear();
                    break;
                }
            }
        }

        step("");
        step("");
        step("");
        step("End of the algorithm.") ;
    }

    @Override
    public String getName() {
        return "Dijkstra Algorithm";
    }

    @Override
    public String getDescription() {
        return "Dijkstra Algorithm";
    }
}