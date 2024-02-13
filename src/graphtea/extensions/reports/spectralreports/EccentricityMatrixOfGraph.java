package graphtea.extensions.reports.spectralreports;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import graphtea.extensions.AlgorithmUtils;
import graphtea.extensions.algorithms.shortestpath.algs.FloydWarshall;
import graphtea.graph.graph.GraphModel;
import graphtea.plugins.reports.extension.GraphReportExtension;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Description here.
 *
 * @author Hooman Mohajeri Moghaddam
 */
public class EccentricityMatrixOfGraph implements GraphReportExtension<ArrayList<String>>  {
	boolean inDegree;

	private ArrayList<String> showEccentricityMatrix(Matrix A)
	{
		ArrayList<String> result = new ArrayList<>();

		double[][] Lap = A.getArray();

		result.add("Eccentricity Matrix:");
		for(double[] k : Lap)
		{
			result.add( Arrays.toString(k));
		}
		return result;
	}


	/**
	 * Gets the eigen values and vectors of the graph and returns them as an array of strings.
	 * @param matrix the Adjacency matrix of the graph
	 * @return Eigen values and vectors
	 */
	private ArrayList<String> getEigenValuesAndVectors(Matrix matrix)
	{
		ArrayList<String> result = new ArrayList<>();
		result.add("Eigen Value Decomposition:");
		EigenvalueDecomposition ed = matrix.eig();
		double[] rv = ed.getRealEigenvalues();
		double[] iv = ed.getImagEigenvalues();
		for (int i = 0; i < rv.length; i++)
			if (iv[i] != 0)
				result.add("" + AlgorithmUtils.round(rv[i], 5) + " + " + AlgorithmUtils.round(iv[i], 5) + "i");
			else
				result.add("" + AlgorithmUtils.round(rv[i], 5));
		result.add("Eigen Vectors:\n");
		double[][] eigenVectors = ed.getV().getArray();
		for (double[] eigenVector : eigenVectors) result.add(Arrays.toString(AlgorithmUtils.round(eigenVector, 5)));
		return result;
	}

	public String getName() {
		return "Spectrum of Eccentricity Matrix";
	}

	public String getDescription() {
		return "The eccentricity matrix associated with the graph";
	}

	public ArrayList<String> calculate(GraphModel g) {
		try {
			if(g.isDirected())
			{
				int a = JOptionPane.showOptionDialog(null, "Do you want to use in or out degrees for calculation of the eccentricity Matrix?", "Eccentricity Matrix", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"In Degrees", "Out Degrees"}, "In Degrees");

				if (a== -1)
					return null;
				else inDegree = a == 0;
			}
			FloydWarshall fw = new FloydWarshall();
			Matrix A = eccentricityMatrix(g, fw.getAllPairsShortestPathWithoutWeight(g));
			ArrayList<String> calc = new ArrayList<>(showEccentricityMatrix(A));
			calc.addAll(getEigenValuesAndVectors(A));
			return(calc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public int eccentricity(GraphModel g, int v, int[][] dist) {
		int max_dist = 0;
		for(int j=0;j < g.getVerticesCount();j++) {
			if(max_dist < dist[v][j]) {
				max_dist = dist[v][j];
			}
		}
		return max_dist;
	}

	public Matrix eccentricityMatrix(GraphModel g, int[][] dist) {
		Matrix m = new Matrix(g.getVerticesCount(),g.getVerticesCount());
		for(int i=0;i < g.getVerticesCount();i++) {
			for(int j=0;j < g.getVerticesCount();j++) {
				int ecc_i = eccentricity(g, i, dist);
				int ecc_j = eccentricity(g, j, dist);
				if(dist[i][j] == Math.min(ecc_i, ecc_j)) {
					m.set(i,j,dist[i][j]);
				}
			}
		}
		return m;
	}

	@Override
	public String getCategory() {
		return "Spectral";
	}

}
