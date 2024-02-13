 package graphtea.extensions.reports.topological;

 import Jama.EigenvalueDecomposition;
 import Jama.Matrix;
 import graphtea.extensions.AlgorithmUtils;
 import graphtea.extensions.algorithms.shortestpath.algs.FloydWarshall;
 import graphtea.extensions.reports.others.Eccentricity;
 import graphtea.graph.graph.Edge;
 import graphtea.graph.graph.GraphModel;
 import graphtea.graph.graph.Vertex;
 import graphtea.library.algorithms.LibraryUtils;

 import java.util.ArrayList;

/**
 * Created by rostam on 27.01.15.
 * Class containg the functions for computation of Zagreb indices
 */
public class ZagrebIndexFunctions {
    GraphModel g;

    public ZagrebIndexFunctions(GraphModel g) {
        this.g = g;
    }
    
    public double getEnegry() {
        Matrix A = g.getWeightedAdjacencyMatrix();
        EigenvalueDecomposition ed = A.eig();
        double[] rv = ed.getRealEigenvalues();
        double sum=0;
    	
    	
    Double[] prv = new Double[rv.length];
    for(int i=0;i<rv.length;i++) {
        prv[i] = Math.abs(rv[i]);
        sum += prv[i];
             }
    return sum;
    }   
  


    public double getInverseSumIndegIndex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += (g.getDegree(e.source)*g.getDegree(e.target)*1.0)
                    /(g.getDegree(e.source) + g.getDegree(e.target));
        }
        return ret;
    }
    
    
    double getInverseSumIndegCoindex() {
        double  InverseSumIndeg = 0;

        GraphModel g2 = (GraphModel) LibraryUtils.complement(g);
        for (Edge e : g2.getEdges()) {
            int v1 = g.getDegree(g.getVertex(e.source.getId()));
            int v2 = g.getDegree(g.getVertex(e.target.getId()));
           InverseSumIndeg += ( (v1 * v2)/(v1  +  v2));
        }

        return InverseSumIndeg;
    }
    
    public double getAlbCoindex() {
        double  albco = 0;

        GraphModel g2 = (GraphModel) LibraryUtils.complement(g);
        for (Edge e : g2.getEdges()) {
            int v1 = g.getDegree(g.getVertex(e.source.getId()));
            int v2 = g.getDegree(g.getVertex(e.target.getId()));
            albco += Math.abs((v1*1.0) - (v2*1.0) );
        }

        return albco;
    }
       
    
   
    public double getSigmaindex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += ( ((g.getDegree(e.source)*1.0) - (g.getDegree(e.target)*1.0))*((g.getDegree(e.source)*1.0) - (g.getDegree(e.target)*1.0)) );
        }
        return ret;
    }
	
	
	    public double getAlbertson() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += Math.abs(((g.getDegree(e.source)*g.getDegree(e.source)*1.0)) - ((g.getDegree(e.target)*g.getDegree(e.target)*1.0)) );
        }
        return ret;
    }
    
  public double getirregularity() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += Math.abs((g.getDegree(e.source)*1.0) - (g.getDegree(e.target)*1.0));
        }
        return ret;
    }

    
    public double getAugumentedZagrebIndex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += Math.pow((g.getDegree(e.source)*g.getDegree(e.target)*1.0)
                    /(g.getDegree(e.source) + g.getDegree(e.target)-2), 3);
        }
        return ret;
    }
    
    public double getSDDIndex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += ( ( (g.getDegree(e.source)*g.getDegree(e.source)*1.0) + (g.getDegree(e.target)*g.getDegree(e.target)*1.0) ) 
            		/(g.getDegree(e.source)*g.getDegree(e.target)*1.0));
        }
        return ret;
    }
    
    double getSDDCoindex() {
        double  SDD = 0;

        GraphModel g2 = (GraphModel) LibraryUtils.complement(g);
        for (Edge e : g2.getEdges()) {
            int v1 = g.getDegree(g.getVertex(e.source.getId()));
            int v2 = g.getDegree(g.getVertex(e.target.getId()));
            SDD += (((v1*v1*1.0)+(v2*v2*1.0))/(v1*v2*1.0));
        }

        return SDD;
    }
    
    public double getAGIndex() {
    	double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += ((g.getDegree(e.source) + g.getDegree(e.target))/(2*Math.sqrt(g.getDegree(e.source)*g.getDegree(e.target)*1.0)));
        }
        return ret;
    }

    public double getPBIndex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret +=  ((Math.sqrt(g.getDegree(e.source)*1.0)) + (Math.sqrt(g.getDegree(e.target))*1.0))*
            		(1.0/Math.sqrt(g.getDegree(e.source)*g.getDegree(e.target)*1.0))*(1.0/(Math.sqrt(g.getDegree(e.source) + g.getDegree(e.target) - 2))) ;
        }
        return ret;
    }
    
    
    public double getABCindex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += Math.sqrt((g.getDegree(e.source) + g.getDegree(e.target) - 2)
                    /(g.getDegree(e.source)*g.getDegree(e.target)*1.0));
        }
        return ret;
    }
    
    
    public double getCheck() {
        double c = 0;
        for (Edge e : g.getEdges()) {
            c +=
                     ((1.0/Math.min(g.getDegree(e.source), g.getDegree(e.target))) -(1.0/Math.max(g.getDegree(e.source), g.getDegree(e.target))) );
        }

        return c;
    }


    public double getVariationRandicIndex() {
        double V = 0;
        for(Edge e : g.getEdges()) {
            V += 1.0/(Math.max(g.getDegree(e.source), g.getDegree(e.target)));
        }
        return V;
    }

    public double getEdgeDegree(double alpha) {
        double edge_degree = 0;
        for (Edge e : g.getEdges()) {
            edge_degree +=
                    Math.pow(
                            g.getDegree(e.source) +
                                    g.getDegree(e.target) - 2, alpha);
        }

        return edge_degree;
    }


    public double getHyperZagrebIndex() {
        double hz = 0;
        for(Edge e : g.getEdges()) {
            hz += Math.pow(
                    g.getDegree(e.source) + g.getDegree(e.target)
                    ,2);
        }
        return hz;
    }

    public double getFirstZagreb(double alpha) {
        double first_zagreb = 0;
        for (Vertex v : g.vertices()) {
            first_zagreb += Math.pow(g.getDegree(v), alpha + 1);
        }
        return first_zagreb;
    }
    
    public double getMultiplicativeFirstZagreb(double alpha) {
        double multi_first_zagreb = 1;
        for (Vertex v : g.vertices()) {
            multi_first_zagreb *= Math.pow(g.getDegree(v), alpha + 1);
        }
        return multi_first_zagreb;
    }
    
   
    
    public double getexpHarmonicIndex() {
        double ret = 0;
        for (Edge e : g.getEdges()) {
            ret+=Math.exp(2./(g.getDegree(e.source)+g.getDegree(e.target)));
        }
        return ret;
    }
    
    public double getexpGAindex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += Math.exp((2*Math.sqrt(g.getDegree(e.source)*g.getDegree(e.target)*1.0))
                    /(g.getDegree(e.source) + g.getDegree(e.target)));
        }
        return ret;
    }
    
    public double getexpSDDIndex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += Math.exp(( ( (g.getDegree(e.source)*g.getDegree(e.source)*1.0) + (g.getDegree(e.target)*g.getDegree(e.target)*1.0) ) 
            		/(g.getDegree(e.source)*g.getDegree(e.target)*1.0)) );
        }
        return ret;
    }
    
    
    public double getexpABCindex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret +=Math.exp(Math.sqrt((g.getDegree(e.source) + g.getDegree(e.target) - 2)
                    /(g.getDegree(e.source)*g.getDegree(e.target)*1.0)) );
        }
        return ret;
    }

    public double getexpAugumentedZagrebIndex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += Math.exp(Math.pow((g.getDegree(e.source)*g.getDegree(e.target)*1.0)
                    /(g.getDegree(e.source) + g.getDegree(e.target)-2), 3) );
        }
        return ret;
    }
    
    

    public int getConnectionNumber(Vertex v) {
        int count = 0;
        for(Vertex n : g.directNeighbors(v)) {
            for(Vertex nn : g.directNeighbors(n)) {
                if(v.getId() != nn.getId()) {
                    count++;
                }
            }
        }
        return count;
    }

    public double getModifiedFirstZagrebConnection() {
        double first_zagreb = 0;
        for (Vertex v : g.vertices()) {
            first_zagreb += g.getDegree(v)*getConnectionNumber(v);
        }
        return first_zagreb;
    }

    public double getThirdZagreb() {
        double ret = 0;
        for (Edge e : g.getEdges()) {
            ret += Math.abs(g.getDegree(e.source) -
                    g.getDegree(e.target));
        }

        return ret;
    }

    public double getexpSecondZagreb(double alpha) {
        double exp_second_zagreb = 0;
        for (Edge e : g.getEdges()) {
            exp_second_zagreb +=
                    Math.exp(Math.pow(
                            g.getDegree(e.source) *
                                    g.getDegree(e.target), alpha ));
        }

        return exp_second_zagreb;
    }
    
    
    public double getexpFirstZagreb(double alpha) {
        double exp_first_zagreb = 0;
        for (Vertex v : g.vertices()) {
            exp_first_zagreb += Math.exp(Math.pow(g.getDegree(v), alpha + 1));
        }        
        return exp_first_zagreb;
   }

    public double getSecondZagreb(double alpha) {
        double second_zagreb = 0;
        for (Edge e : g.getEdges()) {
            second_zagreb +=
                    Math.pow(
                            g.getDegree(e.source) *
                                    g.getDegree(e.target), alpha);
        }

        return second_zagreb;
    }
    
    public double getexpInvEdge(double alpha) {
        double check = 0;
        for (Edge e : g.getEdges()) {
            check += Math.exp(Math.pow(g.getDegree(e.source), alpha)+ Math.pow(g.getDegree(e.target), alpha) ) ;
        }        
        return check;
   }
    
    
    public double getRM2() {
        double RM2 = 0;
        for (Edge e : g.getEdges()) {
            RM2 += ((g.getDegree(e.source)-1)*(g.getDegree(e.target)-1));
        }

        return RM2;
    }

    public double getFirstReZagreb(double alpha) {
        double first_re_zagreb = 0;
        for (Edge e : g.getEdges()) {

            int d = g.getDegree(e.source) +
                    g.getDegree(e.target) - 2;

            first_re_zagreb += Math.pow(d, alpha + 1);
        }
        return first_re_zagreb;
    }

    public double getFirstReZagrebCoindex(double alpha) {
        double ret = 0;
        if(g.getEdgesCount()==1) return ret;
        GraphModel lg = AlgorithmUtils.createLineGraph(g);
        GraphModel clg = (GraphModel) LibraryUtils.complement(lg);

        for (Edge e : clg.getEdges()) {
            int v1 = lg.getDegree(lg.getVertex(e.source.getId()));
            int v2 = lg.getDegree(lg.getVertex(e.target.getId()));
            ret += Math.pow(v1, alpha) + Math.pow(v2, alpha);
        }

        return ret;
    }


    public double getSecondReZagrebCoindex(double alpha) {
        double ret = 0;
        if(g.getEdgesCount()==1) return ret;
        GraphModel lg = AlgorithmUtils.createLineGraph(g);
        GraphModel clg = (GraphModel) LibraryUtils.complement(lg);

        for (Edge e : clg.getEdges()) {
            int v1 = lg.getDegree(lg.getVertex(e.source.getId()));
            int v2 = lg.getDegree(lg.getVertex(e.target.getId()));
            ret += Math.pow(v1*v2, alpha);
        }

        return ret;
    }

    double getSecondReZagreb(double alpha) {
        double second_re_zagreb = 0;
        ArrayList<Edge> eds = new ArrayList<>();
        for (Edge ee : g.getEdges()) {
            eds.add(ee);
        }
        for (Edge e1 : eds) {
            for (Edge e2 : eds) {
                if (edge_adj(e1, e2)) {
                    int d1 = g.getDegree(e1.source) +
                            g.getDegree(e1.target) - 2;

                    int d2 = g.getDegree(e2.source) +
                            g.getDegree(e2.target) - 2;

                    second_re_zagreb += Math.pow(d1 * d2, alpha);
                }
            }
        }

        second_re_zagreb /= 2;
        return second_re_zagreb;
    }
    
    double getSecondHyperZagreb(double alpha) {
        double second_hyper_zagreb = 0;
        ArrayList<Edge> eds = new ArrayList<>();
        for (Edge ee : g.getEdges()) {
            eds.add(ee);
        }
        for (Edge e1 : eds) {
            for (Edge e2 : eds) {
                if (edge_adj(e1, e2)) {
                    int d1 = g.getDegree(e1.source) +
                            g.getDegree(e1.target);

                    int d2 = g.getDegree(e2.source) +
                            g.getDegree(e2.target);

                    second_hyper_zagreb += Math.pow(d1 * d2, alpha);
                }
            }
        }

        second_hyper_zagreb /= 2;
        return second_hyper_zagreb;
    }
    

    double getFirstZagrebCoindex(double alpha) {
        double first_zagreb = 0;

        GraphModel g2 = (GraphModel) LibraryUtils.complement(g);
        for (Edge e : g2.getEdges()) {
            int v1 = g.getDegree(g.getVertex(e.source.getId()));
            int v2 = g.getDegree(g.getVertex(e.target.getId()));
            first_zagreb += Math.pow(v1, alpha) + Math.pow(v2, alpha);
        }

        return first_zagreb;
    }
    
    double getHarmonicCoindex() {
        double harmonic = 0;

        GraphModel g2 = (GraphModel) LibraryUtils.complement(g);
        for (Edge e : g2.getEdges()) {
            int v1 = g.getDegree(g.getVertex(e.source.getId()));
            int v2 = g.getDegree(g.getVertex(e.target.getId()));
            harmonic += ( (2.0)/(v1  +  v2));
        }
        return harmonic;
    }
    
    public double getGACoindex() {
        double ret = 0;
        
        GraphModel g2 = (GraphModel) LibraryUtils.complement(g);
        for (Edge e : g2.getEdges()) {
            int v1 = g.getDegree(g.getVertex(e.source.getId()));
            int v2 = g.getDegree(g.getVertex(e.target.getId()));
            ret += ((2*Math.sqrt(v1*v2*1.0))/(v1  +  v2));
        }
        return ret;
    }

    double getSecondZagrebCoindex(double alpha) {
        double second_zagreb = 0;

        GraphModel g2 = (GraphModel) LibraryUtils.complement(g);

        for (Edge e : g2.getEdges()) {
            int v1 = g.getDegree(g.getVertex(e.source.getId()));
            int v2 = g.getDegree(g.getVertex(e.target.getId()));
            second_zagreb += Math.pow(v1*v2, alpha);
        }

        return second_zagreb;
    }
    
 
    public double getFirstZagrebSelectedEdges(double alpha) {
        double first_zagreb = 0;
        for (Vertex v : g.vertices()) {
            for (Vertex nv : g.getNeighbors(v))
                if (g.getEdge(v, nv).isSelected()) {
                    first_zagreb += Math.pow(g.getDegree(v), alpha + 1);
                    break;
                }
        }
        return first_zagreb;
    }

    public double getSecondZagrebSelectedEdges(double alpha) {
        double second_zagreb = 0;
        for (Edge e : g.getEdges()) {
            if (e.isSelected()) {
                second_zagreb +=
                        Math.pow(
                                g.getDegree(e.source) *
                                        g.getDegree(e.target), alpha);
            }
        }
        return second_zagreb;
    }

    public double getFirstReZagrebSelectedEdges(double alpha) {
        double first_re_zagreb = 0;
        for (Edge e : g.getEdges()) {
            if (e.isSelected()) {
                int d = g.getDegree(e.source) +
                        g.getDegree(e.target) - 2;

                first_re_zagreb += Math.pow(d, alpha + 1);
            }
        }
        return first_re_zagreb;
    }

    double getSecondReZagrebSelectedEdges(double alpha) {
        double second_re_zagreb = 0;
        ArrayList<Edge> eds = new ArrayList<>();
        for (Edge ee : g.getEdges()) {
            eds.add(ee);
        }
        for (Edge e1 : eds) {
            for (Edge e2 : eds) {
                if (e1.isSelected() && e2.isSelected()) {
                    if (edge_adj(e1, e2)) {
                        int d1 = g.getDegree(e1.source) +
                                g.getDegree(e1.target) - 2;

                        int d2 = g.getDegree(e2.source) +
                                g.getDegree(e2.target) - 2;

                        second_re_zagreb += Math.pow(d1 * d2, alpha);
                    }
                }
            }
        }

        second_re_zagreb /= 2;
        return second_re_zagreb;
    }

    double getFirstZagrebCoindexSelectedEdges(double alpha) {
        double first_zagreb = 0;

        boolean cond1 = false, cond2=false;
        for (Vertex v1 : g.getVertexArray()) {
            for (Vertex v2 : g.getVertexArray()) {

                for(Vertex nv1 : g.getNeighbors(v1)) {
                    if(g.getEdge(v1,nv1).isSelected()) {
                        cond1 = true;
                        break;
                    }
                }

                for(Vertex nv2 : g.getNeighbors(v2)) {
                    if(g.getEdge(v2,nv2).isSelected()) {
                        cond2 = true;
                        break;
                    }
                }

                if(cond1 && cond2) {
                    if (v1.getId() != v2.getId()) {
                        if (!g.isEdge(v1, v2)) {
                            first_zagreb += Math.pow(g.getDegree(v1), alpha) +
                                    Math.pow(g.getDegree(v2), alpha);
                        }
                    }
                }
            }
        }

        first_zagreb /= 2;

        return first_zagreb;
    }

    double getSecondZagrebCoindexSelectedEdges(double alpha) {
        double second_zagreb = 0;

        boolean cond1 = false, cond2 = false;
        for (Vertex v1 : g.getVertexArray()) {
            for (Vertex v2 : g.getVertexArray()) {
                for (Vertex nv1 : g.getNeighbors(v1)) {
                    if (g.getEdge(v1, nv1).isSelected()) {
                        cond1 = true;
                        break;
                    }
                }

                for (Vertex nv2 : g.getNeighbors(v2)) {
                    if (g.getEdge(v2, nv2).isSelected()) {
                        cond2 = true;
                        break;
                    }
                }
                if (cond1 && cond2) {
                    if (v1.getId() != v2.getId()) {
                        if (!g.isEdge(v1, v2)) {
                            second_zagreb += Math.pow(g.getDegree(v1) *
                                    g.getDegree(v2), alpha);

                        }
                    }
                }
            }
        }

        second_zagreb /= 2;
        return second_zagreb;
    }

    public double getFirstVariableZagrebIndex(double alpha) {
        double ret = 0;
        for(Vertex v : g) {
            ret += Math.pow(g.getDegree(v),
                    2*alpha);
        }
        return ret;
    }

    public double getSecondVariableZagrebIndex(double alpha) {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            double degs = g.getDegree(e.source);
            double degt = g.getDegree(e.target);
            ret+= Math.pow(degs*degt,alpha);
        }
        return ret;
    }

    public double getSecondMixZagrebIndex(double alpha, double beta) {
        double ret=0;
        for(Edge e : g.getEdges()) {
            ret+=Math.pow(g.getDegree(e.source),alpha)*Math.pow(g.getDegree(e.target),beta) +
                    Math.pow(g.getDegree(e.source),beta)*Math.pow(g.getDegree(e.target),alpha);
        }
        return ret;
    }


    public double getGeneralSumConnectivityIndex(double alpha) {
        double ret = 0;
        for (Edge e : g.getEdges()) {
            ret+=Math.pow(g.getDegree(e.source)+g.getDegree(e.target),alpha);
        }
        return ret;
    }

    public double getHarmonicIndex() {
        double ret = 0;
        for (Edge e : g.getEdges()) {
            ret+=2./(g.getDegree(e.source)+g.getDegree(e.target));
        }
        return ret;
    }


    public double getFirstPathZagrebIndex(double alpha) {
        double ret = 0;
        GraphModel g2 = AlgorithmUtils.createLineGraph(g);
        for(Edge e : g2.getEdges()) {
            Vertex src = e.source;
            Vertex tgt = e.target;
            Edge e1= (Edge) src.getProp().obj;
            Edge e2= (Edge) tgt.getProp().obj;
            if(e1.source.getId()==e2.source.getId()) {
                ret+=Math.pow(g.getDegree(e1.target),alpha-1);
                ret+=Math.pow(g.getDegree(e2.target),alpha-1);
            }

            if(e1.target.getId()==e2.source.getId()) {
                ret += Math.pow(g.getDegree(e1.source), alpha - 1);
                ret += Math.pow(g.getDegree(e2.target), alpha - 1);
            }

            if(e1.source.getId()==e2.target.getId()) {
                ret+=Math.pow(g.getDegree(e1.target),alpha-1);
                ret+=Math.pow(g.getDegree(e2.source),alpha-1);
            }

            if(e1.target.getId()==e2.target.getId()) {
                ret+=Math.pow(g.getDegree(e1.source),alpha-1);
                ret+=Math.pow(g.getDegree(e2.source),alpha-1);
            }
        }
        return ret;
    }

    public double getSecondPathZagrebIndex(double alpha) {
        double ret = 0;
        GraphModel g2 = AlgorithmUtils.createLineGraph(g);
        for(Edge e : g2.getEdges()) {
            Vertex src = e.source;
            Vertex tgt = e.target;
            Edge e1= (Edge) src.getProp().obj;
            Edge e2= (Edge) tgt.getProp().obj;
            if(e1.source.getId()==e2.source.getId()) {
                ret+=Math.pow(g.getDegree(e1.target)*g.getDegree(e2.target),alpha);
            }

            if(e1.target.getId()==e2.source.getId()) {
                ret += Math.pow(g.getDegree(e1.source)*g.getDegree(e2.target),alpha);
            }

            if(e1.source.getId()==e2.target.getId()) {
                ret+=Math.pow(g.getDegree(e1.target)*g.getDegree(e2.source),alpha);
            }

            if(e1.target.getId()==e2.target.getId()) {
                ret+=Math.pow(g.getDegree(e1.source)*g.getDegree(e2.source),alpha);
            }
        }
        return ret;
    }

    private boolean edge_adj(Edge e1,Edge e2) {
        if(e1.source.getId()==e2.source.getId()  &&
                e1.target.getId()==e2.target.getId()) return false;
        else if(e1.target.getId()==e2.source.getId() &&
                e1.source.getId()==e2.target.getId()) return false;
        else if(e1.source.getId() == e2.source.getId()) return true;
        else if(e1.source.getId() == e2.target.getId()) return true;
        else if(e1.target.getId() == e2.source.getId()) return true;
        else if(e1.target.getId() == e2.target.getId()) return true;
        return false;
    }

    public double getGAindex() {
        double ret = 0;
        for(Edge e : g.getEdges()) {
            ret += (2*Math.sqrt(g.getDegree(e.source)*g.getDegree(e.target)*1.0))
                    /(g.getDegree(e.source) + g.getDegree(e.target));
        }
        return ret;
    }

    public double getInverseEdgeDegree() {
        double edge_degree = 0;
        for (Edge e : g.getEdges()) {
            edge_degree +=
                    Math.pow( (  ( g.getDegree(e.source) + g.getDegree(e.target) - 2  )  ), -1);
        }

        return edge_degree;
    }

    public double getRandicEnergy(GraphModel g) {
        Matrix m = new Matrix(g.getVerticesCount(),g.getVerticesCount());
        init(m,0);
        for(Edge e : g.getEdges()) m.set(e.source.getId(), e.target.getId(), getSecondZagreb(-0.5));
        return sumOfEigValues(m);
    }

    public double getZagrebEnergyZ1(GraphModel g) {
        Matrix m = new Matrix(g.getVerticesCount(),g.getVerticesCount());
        init(m,0);
        for(Edge e : g.getEdges()) m.set(e.source.getId(), e.target.getId(), getFirstPathZagrebIndex(1));
        return sumOfEigValues(m);
    }

    public double getZagrebEnergyZ2(GraphModel g) {
        Matrix m = new Matrix(g.getVerticesCount(),g.getVerticesCount());
        init(m,0);
        for(Edge e : g.getEdges()) m.set(e.source.getId(), e.target.getId(), getSecondZagreb(1));
        return sumOfEigValues(m);
    }

    public void init(Matrix m, double d) {
        for(int i = 0;i < m.getRowDimension();i++)
            for(int j=0;j < m.getColumnDimension();j++)
                m.set(i,j,d);
    }

    public double sumOfEigValues(Matrix m) {
        double[] eig = m.eig().getRealEigenvalues();
        double sum = 0;
        for(double d : eig) sum += d;
        return sum;
    }

    public double getFirstZagrebEccentricity(GraphModel g) {
        double first_zagreb_eccentricity = 0;
        FloydWarshall fw = new FloydWarshall();
        int[][] dist = fw.getAllPairsShortestPathWithoutWeight(g);
        for (Vertex v : g.vertices()) {
            first_zagreb_eccentricity += Math.pow(Eccentricity.eccentricity(g, v.getId(), dist), 2);
        }
        return first_zagreb_eccentricity;
    }

    public double getSecondZagrebEccentricity(GraphModel g) {
        double second_zagreb_eccentricity = 0;
        FloydWarshall fw = new FloydWarshall();
        int[][] dist = fw.getAllPairsShortestPathWithoutWeight(g);
        for(Edge e : g.getEdges()) {
                second_zagreb_eccentricity +=
                        Eccentricity.eccentricity(g, e.source.getId(), dist)*
                                Eccentricity.eccentricity(g, e.target.getId(), dist);
        }
        return second_zagreb_eccentricity;
    }
}
