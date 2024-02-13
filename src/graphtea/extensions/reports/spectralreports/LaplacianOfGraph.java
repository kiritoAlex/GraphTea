package graphtea.extensions.reports.spectralreports;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import graphtea.extensions.AlgorithmUtils;
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
public class LaplacianOfGraph implements GraphReportExtension<ArrayList<String>>  {


	boolean inDegree;

	/**
	 * Undirected Laplacian.
	 * @param A the Adjacency matrix of the graph
	 * @return Laplacian of the graph
	 */
	private Matrix getLaplacian(Matrix A)
	{
		//double[][] res=new double[g.numOfVertices()][g.numOfVertices()];


		int n=A.getArray().length;
		double[][] ATemp = A.getArray();

		Matrix D = new Matrix(n,n);
		double[][] DTemp = D.getArray();
		int sum; 
		if(inDegree)
		{
			for(int i=0; i<n ; i++ )
			{
				sum = 0 ;
				for(int j=0; j<n ; j++)
				{
					sum+=ATemp[j][i];
				}
				DTemp[i][i]=sum;
			}
		}
		else
		{
			for(int i=0; i<n ; i++ )
			{
				sum = 0 ;
				for(int j=0; j<n ; j++)
				{
					sum+=ATemp[i][j];
				}
				DTemp[i][i]=sum;
			}
		}

		return D.minus(A);
	}

	//
	private ArrayList<String> ShowLaplacian(Matrix A)
	{
		ArrayList<String> result = new ArrayList<>();

		double[][] Lap = getLaplacian(A).getArray();

		result.add("Laplacian Matrix:");
		for(double[] k : Lap)
		{
			result.add( Arrays.toString(k));
		}
		return result;
	}


	/**
	 * Gets the eigen values and vectors of the graph and returns them as an array of strings.
	 * @param matrix the Adjacency matrix of the graph
	 * @return Laplacian of the graph
	 */
	private ArrayList<String> getEigenValuesAndVectors(Matrix matrix)
	{
		ArrayList<String> result = new ArrayList<>();
		result.add("Eigen Value Decomposition:");
		EigenvalueDecomposition ed = getLaplacian(matrix).eig();
		double[] rv = ed.getRealEigenvalues();
		double[] iv = ed.getImagEigenvalues();
		for (int i = 0; i < rv.length; i++)
			if (iv[i] != 0)
				result.add("" + AlgorithmUtils.round(rv[i], 10) + " + " + AlgorithmUtils.round(iv[i], 10) + "i");
			else
				result.add("" + AlgorithmUtils.round(rv[i], 10));
		result.add("Eigen Vectors:\n");
		double[][] eigenVectors = ed.getV().getArray();
        for (double[] eigenVector : eigenVectors) result.add(Arrays.toString(AlgorithmUtils.round(eigenVector, 10)));
		return result;
	}

	public String getName() {
		return "Spectrum of Laplacian";
	}

	public String getDescription() {
		return "The Laplacian matrix associated with the graph";
	}

	public ArrayList<String> calculate(GraphModel g) {

		try {
			if(g.isDirected())
			{
				int a = JOptionPane.showOptionDialog(null, "Do you want to use in or out degrees for calculation of the Laplacian Matrix?", "Laplacian", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"In Degrees", "Out Degrees"}, "In Degrees");

				if (a== -1)
					return null;
				else inDegree = a == 0;
			}
			Matrix A = g.getWeightedAdjacencyMatrix();
			ArrayList<String> calc = new ArrayList<>(ShowLaplacian(A));
			calc.addAll(getEigenValuesAndVectors(A));
			return(calc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public String getCategory() {
		return "Spectral";
	}

}
