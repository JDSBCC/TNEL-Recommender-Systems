package recommendersystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import tsf.AdjustedCosineSimilarity;
import tsf.BinaryJaccardSimilarityMetric;
import tsf.ItemReputation;
import tsf.JaccardMetricItemBased;
import tsf.JaccardMetricUserBased;
import tsf.MeanSquaredDifferencesMethod;
import tsf.PearsonCorrelation;
import tsf.TrustSemanticFusion;
import tsf.WeightedMeanAggregationMethod;
import tsf.WeightedPredictor;
import data.Genre;
import data.Item;
import data.Rating;
import data.User;
import evaluation.EvaluationMetrics;
import tars.DynamicTrustPheromone;
import tars.RecommendationProcess;
import utilities.File;
import utilities.Matrix;


public class RecommenderSystem {
	
	public static ArrayList<User> users= new ArrayList<User>();
	public static ArrayList<Item> items= new ArrayList<Item>();
	public static ArrayList<Genre> genres= new ArrayList<Genre>();
	public static ArrayList<Rating> ratings= new ArrayList<Rating>();
	
	public static Double [][] tars_ratings;

	public static void main(String[] args) {
		System.out.println("Recommender System");
		
		//Read data from database
		File file = new File();
		file.read("user");
		file.read("genre");
		file.read("item");
		file.read("data");
		System.out.println("Database readed");

		//Get Ratings Matrix
		System.out.println("Ratings Matrix");
		Double [][]matrix = Matrix.getRatingsMatrix(943, 1682, 100000);
		
		FileWriter ratingsMatrixFile= null;

		try {
			ratingsMatrixFile = new FileWriter("ratingsMatrix.csv");
			
			for(int i= 0; i < matrix.length; i++)
			{
				for(int j= 0; j < matrix[i].length; j++)
				{
					ratingsMatrixFile.append(String.valueOf(matrix[i][j]));
					ratingsMatrixFile.append(",");
				}
				ratingsMatrixFile.append("\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Matrix.printMatrix(matrix);
		
//		//Get Normalized Ratings Matrix
//		System.out.println("Normalized Ratings Matrix");
//		tars_ratings = Matrix.getNormalizedRatingsMatrix(matrix);
//		Matrix.printMatrix(tars_ratings);
//		
//		//Testing TARS
//		DynamicTrustPheromone dtp = new DynamicTrustPheromone(4, tars_ratings);
//		RecommendationProcess rp = new RecommendationProcess(1,dtp, tars_ratings);
//		System.out.println("Trust Intensity calculated!");
//		Matrix.printMatrix(tars_ratings);
		
		// obtain binaryItemTaxonomyMatrix
		
		Double[][] binaryItemTaxonomyMatrix = Matrix.getItemTaxonomyMatrix();
		
		FileWriter binaryItemTaxonomyMatrixFile= null;

		try {
			binaryItemTaxonomyMatrixFile = new FileWriter("binaryItemTaxonomyMatrix.csv");
			
			for(int i= 0; i < binaryItemTaxonomyMatrix.length; i++)
			{
				for(int j= 0; j < binaryItemTaxonomyMatrix[i].length; j++)
				{
					binaryItemTaxonomyMatrixFile.append(String.valueOf(binaryItemTaxonomyMatrix[i][j]));
					binaryItemTaxonomyMatrixFile.append(",");
				}
				binaryItemTaxonomyMatrixFile.append("\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Matrix.printMatrix(binaryItemTaxonomyMatrix);
		
		System.out.println("");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("");
		
		//Recommender System TSF
		test3();
		
		
//		
//		long startTime = System.nanoTime();
//		
//		test4(matrix, binaryItemTaxonomyMatrix);
//
//		long endTime = System.nanoTime();
//		
//		long duration = (endTime - startTime) / 1000000000;
//		
//		System.out.println("");
//		System.out.println("Time= " + duration);
		
		
//		for(int x = 80000; x < ratings.size(); x++)
//		{
//			System.out.println(ratings.get(x).getRating());
//		}
//
//		
//		maeMetric();
		
		
	}
	
	public static Double[][] getSupplierBuyerRatingMatrix()
	{

		Double[][] supplierBuyerRatingMatrix= new Double [4][6];

		supplierBuyerRatingMatrix[0][2]= 3.0;

		supplierBuyerRatingMatrix[1][0]= 4.0;
		supplierBuyerRatingMatrix[1][1]= 3.0;
		supplierBuyerRatingMatrix[1][2]= 4.0;
		supplierBuyerRatingMatrix[1][3]= 2.0;

		supplierBuyerRatingMatrix[2][0]= 4.0;
		supplierBuyerRatingMatrix[2][1]= 2.0;
		supplierBuyerRatingMatrix[2][3]= 4.0;
		supplierBuyerRatingMatrix[2][5]= 4.0;

		supplierBuyerRatingMatrix[3][1]= 1.0;
		supplierBuyerRatingMatrix[3][4]= 4.0;
		supplierBuyerRatingMatrix[3][5]= 5.0;

		return supplierBuyerRatingMatrix;
		
	}
	
	public static Double[][] getBinaryItemTaxonomyMatrix()
	{

		Double[][] binaryItemTaxonomyMatrix= new Double [6][8];

		binaryItemTaxonomyMatrix[0][0]= 0.0;
		binaryItemTaxonomyMatrix[0][1]= 0.0;
		binaryItemTaxonomyMatrix[0][2]= 0.0;
		binaryItemTaxonomyMatrix[0][3]= 0.0;
		binaryItemTaxonomyMatrix[0][4]= 0.0;		
		binaryItemTaxonomyMatrix[0][5]= 1.0;
		binaryItemTaxonomyMatrix[0][6]= 0.0;
		binaryItemTaxonomyMatrix[0][7]= 1.0;

		binaryItemTaxonomyMatrix[1][0]= 1.0;
		binaryItemTaxonomyMatrix[1][1]= 0.0;
		binaryItemTaxonomyMatrix[1][2]= 1.0;
		binaryItemTaxonomyMatrix[1][3]= 1.0;
		binaryItemTaxonomyMatrix[1][4]= 0.0;
		binaryItemTaxonomyMatrix[1][5]= 0.0;
		binaryItemTaxonomyMatrix[1][6]= 0.0;
		binaryItemTaxonomyMatrix[1][7]= 1.0;

		binaryItemTaxonomyMatrix[2][0]= 1.0;
		binaryItemTaxonomyMatrix[2][1]= 1.0;
		binaryItemTaxonomyMatrix[2][2]= 0.0;
		binaryItemTaxonomyMatrix[2][3]= 1.0;
		binaryItemTaxonomyMatrix[2][4]= 1.0;
		binaryItemTaxonomyMatrix[2][5]= 0.0;
		binaryItemTaxonomyMatrix[2][6]= 0.0;
		binaryItemTaxonomyMatrix[2][7]= 0.0;

		binaryItemTaxonomyMatrix[3][0]= 1.0;
		binaryItemTaxonomyMatrix[3][1]= 1.0;
		binaryItemTaxonomyMatrix[3][2]= 0.0;
		binaryItemTaxonomyMatrix[3][3]= 0.0;
		binaryItemTaxonomyMatrix[3][4]= 1.0;
		binaryItemTaxonomyMatrix[3][5]= 0.0;
		binaryItemTaxonomyMatrix[3][6]= 1.0;
		binaryItemTaxonomyMatrix[3][7]= 1.0;
		
		binaryItemTaxonomyMatrix[4][0]= 0.0;
		binaryItemTaxonomyMatrix[4][1]= 1.0;
		binaryItemTaxonomyMatrix[4][2]= 1.0;
		binaryItemTaxonomyMatrix[4][3]= 0.0;
		binaryItemTaxonomyMatrix[4][4]= 0.0;
		binaryItemTaxonomyMatrix[4][5]= 0.0;
		binaryItemTaxonomyMatrix[4][6]= 1.0;
		binaryItemTaxonomyMatrix[4][7]= 1.0;
		
		binaryItemTaxonomyMatrix[5][0]= 1.0;
		binaryItemTaxonomyMatrix[5][1]= 0.0;
		binaryItemTaxonomyMatrix[5][2]= 0.0;
		binaryItemTaxonomyMatrix[5][3]= 0.0;
		binaryItemTaxonomyMatrix[5][4]= 1.0;
		binaryItemTaxonomyMatrix[5][5]= 0.0;
		binaryItemTaxonomyMatrix[5][6]= 0.0;
		binaryItemTaxonomyMatrix[5][7]= 1.0;

		return binaryItemTaxonomyMatrix;
		
	}

	// test User-Based Trust-enhanced CF Module
	public static void test1()
	{
		
		
		Double[][] supplierBuyerRatingMatrix= getSupplierBuyerRatingMatrix();
		
		PearsonCorrelation pearsonCorrelation= new PearsonCorrelation();
		JaccardMetricUserBased jaccardMetric= new JaccardMetricUserBased();
		
		System.out.println("Applying Pearson Correlation and Jaccard Metric:");
		System.out.println("");
		
		// Buyer-Buyer enhanced user-based CF similarity matrix
		Double[][] eucfMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix.length];
		
		// obtain Buyer-Buyer enhanced user-based CF similarity matrix
		for(int i = 0; i < supplierBuyerRatingMatrix.length; i++)
		{
			for(int j= 0; j < supplierBuyerRatingMatrix.length; j++)
			{

				if(pearsonCorrelation.result(i, j, supplierBuyerRatingMatrix) == null || jaccardMetric.result(i, j, supplierBuyerRatingMatrix) == null)
					eucfMatrix[i][j]= null;
				else				
					eucfMatrix[i][j]= pearsonCorrelation.result(i, j, supplierBuyerRatingMatrix) * jaccardMetric.result(i, j, supplierBuyerRatingMatrix);
			}
			
			
		}
		
		// print Buyer-Buyer enhanced user-based CF similarity matrix
		System.out.println("B-B enhanced user-based CF similarity matrix:");
		System.out.println("");
		
		for(int i = 0; i < eucfMatrix.length; i++)
		{
			for(int j= 0; j < eucfMatrix.length; j++)
			{
				if(j == 0)
					System.out.print("[");

				System.out.print(" " + eucfMatrix[i][j] + ",");
			}
			
			System.out.println("]");
			
		}
		
		// B-B direct implicit trust matrix

		Double[][] trustMatrix= new Double [supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix.length];
	
		MeanSquaredDifferencesMethod msdm= new MeanSquaredDifferencesMethod();
		
		// obtain B-B direct implicit trust matrix
		for(int i = 0; i < supplierBuyerRatingMatrix.length; i++)
		{
			for(int j= 0; j < supplierBuyerRatingMatrix.length; j++)
			{

				if(msdm.result(i, j, supplierBuyerRatingMatrix) == null || jaccardMetric.result(i, j, supplierBuyerRatingMatrix) == null)
					trustMatrix[i][j]= null;
				else				
					trustMatrix[i][j]= msdm.result(i, j, supplierBuyerRatingMatrix) * jaccardMetric.result(i, j, supplierBuyerRatingMatrix);
			}


		}
		
		//print B-B direct implicit trust matrix
		System.out.println("");
		System.out.println("B-B direct implicit trust matrix:");
		
		for(int i = 0; i < trustMatrix.length; i++)
		{
			for(int j= 0; j < trustMatrix[i].length; j++)
			{
				if(j==0)
					System.out.print("[ ");

				System.out.print(trustMatrix[i][j] + ", ");
			}
			System.out.println("]");
		}

		//B-B propagated implicit trust
		WeightedMeanAggregationMethod wmam= new WeightedMeanAggregationMethod(0.15, 2);

		// obtain B-B propagated implicit trust
		for(int i = 0; i < trustMatrix.length; i++)
		{
			for(int j= 0; j < trustMatrix[i].length; j++)
			{

				if(trustMatrix[i][j] == null)
				{
					Double result= wmam.result(i, j, trustMatrix);
					trustMatrix[i][j]= result;
				}
				
			}

		}
		
		//print B-B propagated implicit trust matrix
		System.out.println("");
		System.out.println("B-B propagated implicit trust matrix:");
		for(int i = 0; i < trustMatrix.length; i++)
		{
			for(int j= 0; j < trustMatrix[i].length; j++)
			{
				if(j==0)
					System.out.print("[ ");
				
				System.out.print(trustMatrix[i][j] + ", ");
			}
			System.out.println("]");
		}
		
		System.out.println("");
		System.out.println("TeCF predicted supplier-buyer matrix:");
		
		
		Double[][] tecfMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix[0].length];
		
		for(int i= 0; i < tecfMatrix.length; i++)
		{
			for(int j= 0; j < tecfMatrix[i].length; j++)
			{
				if(supplierBuyerRatingMatrix[i][j] != null)
					tecfMatrix[i][j] = supplierBuyerRatingMatrix[i][j];
				else
				{
					WeightedPredictor wp= new WeightedPredictor();
					
					tecfMatrix[i][j] = wp.deviationFromMeanUserBasedApproach(i, j, supplierBuyerRatingMatrix, eucfMatrix, trustMatrix);
				}
			}
		}
		
		// show TeCF predicted supplier-buyer matrix
		for(int i= 0; i < tecfMatrix.length; i++)
		{
			for(int j= 0; j < tecfMatrix[i].length; j++)
			{
				if(j==0)
					System.out.print("[ ");
				
				System.out.print(tecfMatrix[i][j] + ", ");
			}
			System.out.println("]");
		}

		
		
	}

	// test Item-Based Semantic-enhanced CF Module 
	public static void test2()
	{
	
		Double[][] supplierBuyerRatingMatrix= getSupplierBuyerRatingMatrix();
		Double[][] binaryItemTaxonomyMatrix= getBinaryItemTaxonomyMatrix();
		
		AdjustedCosineSimilarity adjustedCosineSimilarity= new AdjustedCosineSimilarity();
		JaccardMetricItemBased jaccardMetric= new JaccardMetricItemBased();

		System.out.println("Applying Adjusted Cosine Similarity Measure and Jaccard Metric [Item Based]:");
		System.out.println("");

		// Supplier-Supplier enhanced item-based CF similarity matrix
		Double[][] itemSimilarityMatrix = new Double[supplierBuyerRatingMatrix[0].length][supplierBuyerRatingMatrix[0].length];
		
		
		for(int i = 0; i < itemSimilarityMatrix.length; i++)
		{
			for(int j= 0; j < itemSimilarityMatrix[i].length; j++)
			{
				if(adjustedCosineSimilarity.result(i, j, supplierBuyerRatingMatrix) == null || jaccardMetric.result(i, j, supplierBuyerRatingMatrix) == null)
					itemSimilarityMatrix[i][j]= null;
				else				
					itemSimilarityMatrix[i][j]= adjustedCosineSimilarity.result(i, j, supplierBuyerRatingMatrix) * jaccardMetric.result(i, j, supplierBuyerRatingMatrix);
			}


		}

		
		// print Supplier-Supplier enhanced item-based CF similarity matrix
		System.out.println("Supplier-Supplier enhanced item-based CF similarity matrix:");
		System.out.println("");
		
		for(int i = 0; i < itemSimilarityMatrix.length; i++)
		{
			for(int j= 0; j < itemSimilarityMatrix.length; j++)
			{
				if(j == 0)
					System.out.print("[");

				System.out.print(" " + itemSimilarityMatrix[i][j] + ",");
			}
			
			System.out.println("]");
			
		}
		
		// Supplier-Supplier semantic similarity matrix
		
		BinaryJaccardSimilarityMetric bjsm= new BinaryJaccardSimilarityMetric();

		System.out.println("");
		System.out.println("");
		System.out.println("Applying Binary Jaccard Similarity Metric:");
		System.out.println("");

		// Supplier-Supplier semantic similarity matrix
		Double[][] bjsmMatrix = new Double[binaryItemTaxonomyMatrix.length][binaryItemTaxonomyMatrix.length];
		
		for(int i = 0; i < bjsmMatrix.length; i++)
		{
			for(int j= 0; j < bjsmMatrix[i].length; j++)
			{
				bjsmMatrix[i][j]= bjsm.result(i, j, binaryItemTaxonomyMatrix);
			}
		}
		
		// print Supplier-Supplier semantic similarity matrix
		for(int i = 0; i < bjsmMatrix.length; i++)
		{
			for(int j= 0; j < bjsmMatrix[i].length; j++)
			{
				if(j == 0)
					System.out.print("[ ");
				System.out.print(bjsmMatrix[i][j] + ", ");
				
			}
			System.out.println("]");
			
		}
		
		// Supplier Reputation Matrix
		
		ItemReputation itemReputation= new ItemReputation();

		System.out.println("");
		System.out.println("");
		System.out.println("Applying Item Reputation:");
		System.out.println("");

		// Supplier-Supplier semantic similarity matrix
		Double[] itemReputationMatrix = new Double[supplierBuyerRatingMatrix[0].length];
		
		for(int i = 0; i < supplierBuyerRatingMatrix[0].length; i++)
		{
			itemReputationMatrix[i]= itemReputation.result(i, supplierBuyerRatingMatrix);
		}

		// print Supplier-Supplier semantic similarity matrix
		
		for(int j= 0; j < itemReputationMatrix.length; j++)
		{
			if(j == 0)
				System.out.print("[ ");
			System.out.print(itemReputationMatrix[j] + ", ");

		}
		System.out.println("]");
		
		
		System.out.println("");
		System.out.println("SeCF predicted rating matrix:");
		
		
		Double[][] secfMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix[0].length];
		
		for(int i= 0; i < secfMatrix.length; i++)
		{
			for(int j= 0; j < secfMatrix[i].length; j++)
			{
				if(supplierBuyerRatingMatrix[i][j] != null)
					secfMatrix[i][j] = supplierBuyerRatingMatrix[i][j];
				else
				{
					WeightedPredictor wp= new WeightedPredictor();
					
					secfMatrix[i][j] = wp.deviationFromMeanItemBasedApproachWithReputation(i, j, supplierBuyerRatingMatrix, itemSimilarityMatrix, bjsmMatrix, itemReputationMatrix);
				}
			}
		}
		
		// show TeCF predicted supplier-buyer matrix
		for(int i= 0; i < secfMatrix.length; i++)
		{
			for(int j= 0; j < secfMatrix[i].length; j++)
			{
				if(j==0)
					System.out.print("[ ");
				
				System.out.print(secfMatrix[i][j] + ", ");
			}
			System.out.println("]");
		}
		
		
	}
	
	
	// test Prediction Fusion Module
	public static void test3()
	{

		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////               User-based Trust-enhanced CF Module             /////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////

		Double[][] supplierBuyerRatingMatrix= getSupplierBuyerRatingMatrix();

		PearsonCorrelation pearsonCorrelation= new PearsonCorrelation();
		JaccardMetricUserBased jaccardMetric= new JaccardMetricUserBased();

		System.out.println("Applying Pearson Correlation and Jaccard Metric:");
		System.out.println("");

		// Buyer-Buyer enhanced user-based CF similarity matrix
		Double[][] eucfMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix.length];

		// obtain Buyer-Buyer enhanced user-based CF similarity matrix
		for(int i = 0; i < supplierBuyerRatingMatrix.length; i++)
		{
			for(int j= 0; j < supplierBuyerRatingMatrix.length; j++)
			{

				if(pearsonCorrelation.result(i, j, supplierBuyerRatingMatrix) == null || jaccardMetric.result(i, j, supplierBuyerRatingMatrix) == null)
					eucfMatrix[i][j]= null;
				else				
					eucfMatrix[i][j]= pearsonCorrelation.result(i, j, supplierBuyerRatingMatrix) * jaccardMetric.result(i, j, supplierBuyerRatingMatrix);
			}


		}

		// print Buyer-Buyer enhanced user-based CF similarity matrix
		System.out.println("B-B enhanced user-based CF similarity matrix:");
		System.out.println("");

		for(int i = 0; i < eucfMatrix.length; i++)
		{
			for(int j= 0; j < eucfMatrix.length; j++)
			{
				if(j == 0)
					System.out.print("[");

				System.out.print(" " + eucfMatrix[i][j] + ",");
			}

			System.out.println("]");

		}

		// B-B direct implicit trust matrix

		Double[][] trustMatrix= new Double [supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix.length];

		MeanSquaredDifferencesMethod msdm= new MeanSquaredDifferencesMethod();

		// obtain B-B direct implicit trust matrix
		for(int i = 0; i < supplierBuyerRatingMatrix.length; i++)
		{
			for(int j= 0; j < supplierBuyerRatingMatrix.length; j++)
			{

				if(msdm.result(i, j, supplierBuyerRatingMatrix) == null || jaccardMetric.result(i, j, supplierBuyerRatingMatrix) == null)
					trustMatrix[i][j]= null;
				else				
					trustMatrix[i][j]= msdm.result(i, j, supplierBuyerRatingMatrix) * jaccardMetric.result(i, j, supplierBuyerRatingMatrix);
			}


		}

		//print B-B direct implicit trust matrix
		System.out.println("");
		System.out.println("B-B direct implicit trust matrix:");

		for(int i = 0; i < trustMatrix.length; i++)
		{
			for(int j= 0; j < trustMatrix[i].length; j++)
			{
				if(j==0)
					System.out.print("[ ");

				System.out.print(trustMatrix[i][j] + ", ");
			}
			System.out.println("]");
		}

		//B-B propagated implicit trust
		WeightedMeanAggregationMethod wmam= new WeightedMeanAggregationMethod(0.15, 2);

		// obtain B-B propagated implicit trust
		for(int i = 0; i < trustMatrix.length; i++)
		{
			for(int j= 0; j < trustMatrix[i].length; j++)
			{

				if(trustMatrix[i][j] == null)
				{
					Double result= wmam.result(i, j, trustMatrix);
					trustMatrix[i][j]= result;
				}

			}

		}

		//print B-B propagated implicit trust matrix
		System.out.println("");
		System.out.println("B-B propagated implicit trust matrix:");
		for(int i = 0; i < trustMatrix.length; i++)
		{
			for(int j= 0; j < trustMatrix[i].length; j++)
			{
				if(j==0)
					System.out.print("[ ");

				System.out.print(trustMatrix[i][j] + ", ");
			}
			System.out.println("]");
		}

		System.out.println("");
		System.out.println("TeCF predicted supplier-buyer matrix:");


		Double[][] tecfMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix[0].length];

		for(int i= 0; i < tecfMatrix.length; i++)
		{
			for(int j= 0; j < tecfMatrix[i].length; j++)
			{
				if(supplierBuyerRatingMatrix[i][j] != null)
					tecfMatrix[i][j] = supplierBuyerRatingMatrix[i][j];
				else
				{
					WeightedPredictor wp= new WeightedPredictor();

					tecfMatrix[i][j] = wp.deviationFromMeanUserBasedApproach(i, j, supplierBuyerRatingMatrix, eucfMatrix, trustMatrix);
				}
			}
		}

		// show TeCF predicted supplier-buyer matrix
		for(int i= 0; i < tecfMatrix.length; i++)
		{
			for(int j= 0; j < tecfMatrix[i].length; j++)
			{
				if(j==0)
					System.out.print("[ ");

				System.out.print(tecfMatrix[i][j] + ", ");
			}
			System.out.println("]");
		}
		
		
		
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////            Item-based Similarity-enhanced CF Module            ////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////

		
		
		//Double[][] supplierBuyerRatingMatrix= getSupplierBuyerRatingMatrix();
		Double[][] binaryItemTaxonomyMatrix= getBinaryItemTaxonomyMatrix();
		
		AdjustedCosineSimilarity adjustedCosineSimilarity= new AdjustedCosineSimilarity();
		JaccardMetricItemBased jaccardMetricItemBased= new JaccardMetricItemBased();

		System.out.println("Applying Adjusted Cosine Similarity Measure and Jaccard Metric [Item Based]:");
		System.out.println("");

		// Supplier-Supplier enhanced item-based CF similarity matrix
		Double[][] itemSimilarityMatrix = new Double[supplierBuyerRatingMatrix[0].length][supplierBuyerRatingMatrix[0].length];
		
		
		for(int i = 0; i < itemSimilarityMatrix.length; i++)
		{
			for(int j= 0; j < itemSimilarityMatrix[i].length; j++)
			{
				if(adjustedCosineSimilarity.result(i, j, supplierBuyerRatingMatrix) == null || jaccardMetricItemBased.result(i, j, supplierBuyerRatingMatrix) == null)
					itemSimilarityMatrix[i][j]= null;
				else				
					itemSimilarityMatrix[i][j]= adjustedCosineSimilarity.result(i, j, supplierBuyerRatingMatrix) * jaccardMetricItemBased.result(i, j, supplierBuyerRatingMatrix);
			}


		}

		
		// print Supplier-Supplier enhanced item-based CF similarity matrix
		System.out.println("Supplier-Supplier enhanced item-based CF similarity matrix:");
		System.out.println("");
		
		for(int i = 0; i < itemSimilarityMatrix.length; i++)
		{
			for(int j= 0; j < itemSimilarityMatrix.length; j++)
			{
				if(j == 0)
					System.out.print("[");

				System.out.print(" " + itemSimilarityMatrix[i][j] + ",");
			}
			
			System.out.println("]");
			
		}
		
		// Supplier-Supplier semantic similarity matrix
		
		BinaryJaccardSimilarityMetric bjsm= new BinaryJaccardSimilarityMetric();

		System.out.println("");
		System.out.println("");
		System.out.println("Applying Binary Jaccard Similarity Metric:");
		System.out.println("");

		// Supplier-Supplier semantic similarity matrix
		Double[][] bjsmMatrix = new Double[binaryItemTaxonomyMatrix.length][binaryItemTaxonomyMatrix.length];
		
		for(int i = 0; i < bjsmMatrix.length; i++)
		{
			for(int j= 0; j < bjsmMatrix[i].length; j++)
			{
				bjsmMatrix[i][j]= bjsm.result(i, j, binaryItemTaxonomyMatrix);
			}
		}
		
		// print Supplier-Supplier semantic similarity matrix
		for(int i = 0; i < bjsmMatrix.length; i++)
		{
			for(int j= 0; j < bjsmMatrix[i].length; j++)
			{
				if(j == 0)
					System.out.print("[ ");
				System.out.print(bjsmMatrix[i][j] + ", ");
				
			}
			System.out.println("]");
			
		}
		
		// Supplier Reputation Matrix
		
		ItemReputation itemReputation= new ItemReputation();

		System.out.println("");
		System.out.println("");
		System.out.println("Applying Item Reputation:");
		System.out.println("");

		// Supplier-Supplier semantic similarity matrix
		Double[] itemReputationMatrix = new Double[supplierBuyerRatingMatrix[0].length];
		
		for(int i = 0; i < supplierBuyerRatingMatrix[0].length; i++)
		{
			itemReputationMatrix[i]= itemReputation.result(i, supplierBuyerRatingMatrix);
		}

		// print Supplier-Supplier semantic similarity matrix
		
		for(int j= 0; j < itemReputationMatrix.length; j++)
		{
			if(j == 0)
				System.out.print("[ ");
			System.out.print(itemReputationMatrix[j] + ", ");

		}
		System.out.println("]");
		
		
		System.out.println("");
		System.out.println("SeCF predicted rating matrix:");
		
		
		Double[][] secfMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix[0].length];
		
		for(int i= 0; i < secfMatrix.length; i++)
		{
			for(int j= 0; j < secfMatrix[i].length; j++)
			{
				if(supplierBuyerRatingMatrix[i][j] != null)
					secfMatrix[i][j] = supplierBuyerRatingMatrix[i][j];
				else
				{
					WeightedPredictor wp= new WeightedPredictor();
					
					secfMatrix[i][j] = wp.deviationFromMeanItemBasedApproachWithReputation(i, j, supplierBuyerRatingMatrix, itemSimilarityMatrix, bjsmMatrix, itemReputationMatrix);
				}
			}
		}
		
		// show TeCF predicted supplier-buyer matrix
		for(int i= 0; i < secfMatrix.length; i++)
		{
			for(int j= 0; j < secfMatrix[i].length; j++)
			{
				if(j==0)
					System.out.print("[ ");
				
				System.out.print(secfMatrix[i][j] + ", ");
			}
			System.out.println("]");
		}
		
		
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		///////////////               Predictions Fusion Module             /////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		

		TrustSemanticFusion trustSemanticFusion = new TrustSemanticFusion();
		
		System.out.println("");
		System.out.println("");
		System.out.println("Trust Semantic Fusion -> Predicted User-Item Rating Matrix:");
		System.out.println("");

		// Predicted User-Item Rating Matrix
		Double[][] predictedUserItemRatingMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix[0].length];
		
		for(int i= 0; i < predictedUserItemRatingMatrix.length; i++)
		{
			for(int j= 0; j < predictedUserItemRatingMatrix[i].length; j++)
			{
				if(supplierBuyerRatingMatrix[i][j] != null)
					predictedUserItemRatingMatrix[i][j] = supplierBuyerRatingMatrix[i][j];
				else
				{
					predictedUserItemRatingMatrix[i][j] = trustSemanticFusion.prediction(i, j, tecfMatrix, secfMatrix);
				}
			}
		}
		
		// print Predicted User-Item Rating Matrix
		for(int i= 0; i < predictedUserItemRatingMatrix.length; i++)
		{
			for(int j= 0; j < predictedUserItemRatingMatrix[i].length; j++)
			{
				if(j==0)
					System.out.print("[ ");
				
				System.out.print(predictedUserItemRatingMatrix[i][j] + ", ");
			}
			System.out.println("]");
		}

	}
	
	// test Prediction Fusion Module
	public static Double[][] test4(Double[][] supplierBuyerRatingMatrix, Double[][] binaryItemTaxonomyMatrix)
	{

		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////               User-based Trust-enhanced CF Module             /////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////

//		Double[][] supplierBuyerRatingMatrix= getSupplierBuyerRatingMatrix();

		PearsonCorrelation pearsonCorrelation= new PearsonCorrelation();
		JaccardMetricUserBased jaccardMetric= new JaccardMetricUserBased();

		System.out.println("Applying Pearson Correlation and Jaccard Metric:");
		System.out.println("");

		// Buyer-Buyer enhanced user-based CF similarity matrix
		Double[][] eucfMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix.length];

		// obtain Buyer-Buyer enhanced user-based CF similarity matrix
		for(int i = 0; i < supplierBuyerRatingMatrix.length; i++)
		{
			for(int j= 0; j < supplierBuyerRatingMatrix.length; j++)
			{

				if(pearsonCorrelation.result(i, j, supplierBuyerRatingMatrix) == null || jaccardMetric.result(i, j, supplierBuyerRatingMatrix) == null)
					eucfMatrix[i][j]= null;
				else				
					eucfMatrix[i][j]= pearsonCorrelation.result(i, j, supplierBuyerRatingMatrix) * jaccardMetric.result(i, j, supplierBuyerRatingMatrix);
			}


		}

		// print Buyer-Buyer enhanced user-based CF similarity matrix
		System.out.println("B-B enhanced user-based CF similarity matrix:");
		System.out.println("");

//		for(int i = 0; i < eucfMatrix.length; i++)
//		{
//			for(int j= 0; j < eucfMatrix.length; j++)
//			{
//				if(j == 0)
//					System.out.print("[");
//
//				System.out.print(" " + eucfMatrix[i][j] + ",");
//			}
//
//			System.out.println("]");
//
//		}
		
		// Write on file
		
		FileWriter eucfMatrixFile= null;
		
		try {
			eucfMatrixFile = new FileWriter("eucfMatrix.csv");

			for(int i = 0; i < eucfMatrix.length; i++)
			{
				for(int j= 0; j < eucfMatrix.length; j++)
				{
					eucfMatrixFile.append(String.valueOf(eucfMatrix[i][j]));
					eucfMatrixFile.append(",");
				}

				eucfMatrixFile.append("\n");

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// B-B direct implicit trust matrix

		Double[][] trustMatrix= new Double [supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix.length];

		MeanSquaredDifferencesMethod msdm= new MeanSquaredDifferencesMethod();

		// obtain B-B direct implicit trust matrix
		for(int i = 0; i < supplierBuyerRatingMatrix.length; i++)
		{
			for(int j= 0; j < supplierBuyerRatingMatrix.length; j++)
			{

				if(msdm.result(i, j, supplierBuyerRatingMatrix) == null || jaccardMetric.result(i, j, supplierBuyerRatingMatrix) == null)
					trustMatrix[i][j]= null;
				else				
					trustMatrix[i][j]= msdm.result(i, j, supplierBuyerRatingMatrix) * jaccardMetric.result(i, j, supplierBuyerRatingMatrix);
			}


		}

		//print B-B direct implicit trust matrix
		System.out.println("");
		System.out.println("B-B direct implicit trust matrix:");
		
		FileWriter trustMatrixFile= null;
		
		try 
		{
			trustMatrixFile = new FileWriter("trustMatrix.csv");

			for(int i = 0; i < trustMatrix.length; i++)
			{
				for(int j= 0; j < trustMatrix[i].length; j++)
				{

					trustMatrixFile.append(String.valueOf(trustMatrix[i][j]));
					trustMatrixFile.append(",");
				}
				trustMatrixFile.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//B-B propagated implicit trust
		WeightedMeanAggregationMethod wmam= new WeightedMeanAggregationMethod(0.15, 3);

		// obtain B-B propagated implicit trust
		for(int i = 0; i < trustMatrix.length; i++)
		{
			for(int j= 0; j < trustMatrix[i].length; j++)
			{

				if(trustMatrix[i][j] == null)
				{
					Double result= wmam.result(i, j, trustMatrix);
					trustMatrix[i][j]= result;
				}

			}

		}

		//print B-B propagated implicit trust matrix
		System.out.println("");
		System.out.println("B-B propagated implicit trust matrix:");

		FileWriter trustPropagationMatrixFile= null;

		try 
		{
			trustPropagationMatrixFile = new FileWriter("trustPropagationMatrix.csv");

			for(int i = 0; i < trustMatrix.length; i++)
			{
				for(int j= 0; j < trustMatrix[i].length; j++)
				{

					trustPropagationMatrixFile.append(String.valueOf(trustMatrix[i][j]));
					trustPropagationMatrixFile.append(",");
				}
				trustPropagationMatrixFile.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("");
		System.out.println("TeCF predicted supplier-buyer matrix:");


		Double[][] tecfMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix[0].length];

		for(int i= 0; i < tecfMatrix.length; i++)
		{
			for(int j= 0; j < tecfMatrix[i].length; j++)
			{
				if(supplierBuyerRatingMatrix[i][j] != null)
					tecfMatrix[i][j] = supplierBuyerRatingMatrix[i][j];
				else
				{
					WeightedPredictor wp= new WeightedPredictor();

					tecfMatrix[i][j] = wp.deviationFromMeanUserBasedApproach(i, j, supplierBuyerRatingMatrix, eucfMatrix, trustMatrix);
				}
			}
		}

		// show TeCF predicted supplier-buyer matrix
		
		FileWriter tecfMatrixFile= null;

		try 
		{
			tecfMatrixFile = new FileWriter("tecfMatrix.csv");

			for(int i= 0; i < tecfMatrix.length; i++)
			{
				for(int j= 0; j < tecfMatrix[i].length; j++)
				{

					tecfMatrixFile.append(String.valueOf(tecfMatrix[i][j]));
					tecfMatrixFile.append(",");
				}
				tecfMatrixFile.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////            Item-based Similarity-enhanced CF Module            ////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////

		
		
		//Double[][] supplierBuyerRatingMatrix= getSupplierBuyerRatingMatrix();
		//Double[][] binaryItemTaxonomyMatrix= getBinaryItemTaxonomyMatrix();
		
		AdjustedCosineSimilarity adjustedCosineSimilarity= new AdjustedCosineSimilarity();
		JaccardMetricItemBased jaccardMetricItemBased= new JaccardMetricItemBased();

		System.out.println("Applying Adjusted Cosine Similarity Measure and Jaccard Metric [Item Based]:");
		System.out.println("");

		// Supplier-Supplier enhanced item-based CF similarity matrix
		Double[][] itemSimilarityMatrix = new Double[supplierBuyerRatingMatrix[0].length][supplierBuyerRatingMatrix[0].length];
		
		
		for(int i = 0; i < itemSimilarityMatrix.length; i++)
		{
			for(int j= 0; j < itemSimilarityMatrix[i].length; j++)
			{
				if(adjustedCosineSimilarity.result(i, j, supplierBuyerRatingMatrix) == null || jaccardMetricItemBased.result(i, j, supplierBuyerRatingMatrix) == null)
					itemSimilarityMatrix[i][j]= null;
				else				
					itemSimilarityMatrix[i][j]= adjustedCosineSimilarity.result(i, j, supplierBuyerRatingMatrix) * jaccardMetricItemBased.result(i, j, supplierBuyerRatingMatrix);
			}


		}

		
		// print Supplier-Supplier enhanced item-based CF similarity matrix
		System.out.println("Supplier-Supplier enhanced item-based CF similarity matrix:");
		System.out.println("");
		
		FileWriter itemSimilarityMatrixFile= null;

		try 
		{
			itemSimilarityMatrixFile = new FileWriter("itemSimilarityMatrix.csv");

			for(int i = 0; i < itemSimilarityMatrix.length; i++)
			{
				for(int j= 0; j < itemSimilarityMatrix.length; j++)
				{
					

					itemSimilarityMatrixFile.append(String.valueOf(itemSimilarityMatrix[i][j]));
					itemSimilarityMatrixFile.append(",");
				}

				itemSimilarityMatrixFile.append("\n");

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Supplier-Supplier semantic similarity matrix
		
		BinaryJaccardSimilarityMetric bjsm= new BinaryJaccardSimilarityMetric();

		System.out.println("");
		System.out.println("");
		System.out.println("Applying Binary Jaccard Similarity Metric:");
		System.out.println("");

		// Supplier-Supplier semantic similarity matrix
		Double[][] bjsmMatrix = new Double[binaryItemTaxonomyMatrix.length][binaryItemTaxonomyMatrix.length];
		
		for(int i = 0; i < bjsmMatrix.length; i++)
		{
			for(int j= 0; j < bjsmMatrix[i].length; j++)
			{
				bjsmMatrix[i][j]= bjsm.result(i, j, binaryItemTaxonomyMatrix);
			}
		}
		
		// print Supplier-Supplier semantic similarity matrix
		
		FileWriter bjsmMatrixFile= null;

		try 
		{
			bjsmMatrixFile = new FileWriter("bjsmMatrix.csv");

			for(int i = 0; i < bjsmMatrix.length; i++)
			{
				for(int j= 0; j < bjsmMatrix[i].length; j++)
				{
					
					bjsmMatrixFile.append(String.valueOf(bjsmMatrix[i][j]));
					bjsmMatrixFile.append(",");

				}
				bjsmMatrixFile.append("\n");

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Supplier Reputation Matrix
		
		ItemReputation itemReputation= new ItemReputation();

		System.out.println("");
		System.out.println("");
		System.out.println("Applying Item Reputation:");
		System.out.println("");

		// Supplier-Supplier semantic similarity matrix
		Double[] itemReputationMatrix = new Double[supplierBuyerRatingMatrix[0].length];
		
		for(int i = 0; i < supplierBuyerRatingMatrix[0].length; i++)
		{
			itemReputationMatrix[i]= itemReputation.result(i, supplierBuyerRatingMatrix);
		}

		// print Supplier-Supplier semantic similarity matrix
		
		FileWriter itemReputationMatrixFile= null;

		try 
		{
			itemReputationMatrixFile = new FileWriter("itemReputationMatrix.csv");

			for(int j= 0; j < itemReputationMatrix.length; j++)
			{
				
				itemReputationMatrixFile.append(String.valueOf(itemReputationMatrix[j]));
				itemReputationMatrixFile.append(",");

			}
			itemReputationMatrixFile.append("\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("");
		System.out.println("SeCF predicted rating matrix:");
		
		
		Double[][] secfMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix[0].length];
		
		for(int i= 0; i < secfMatrix.length; i++)
		{
			for(int j= 0; j < secfMatrix[i].length; j++)
			{
				if(supplierBuyerRatingMatrix[i][j] != null)
					secfMatrix[i][j] = supplierBuyerRatingMatrix[i][j];
				else
				{
					WeightedPredictor wp= new WeightedPredictor();
					
					secfMatrix[i][j] = wp.deviationFromMeanItemBasedApproachWithReputation(i, j, supplierBuyerRatingMatrix, itemSimilarityMatrix, bjsmMatrix, itemReputationMatrix);
				}
			}
		}
		
		// show TeCF predicted supplier-buyer matrix
		
		FileWriter secfMatrixFile= null;

		try 
		{
			secfMatrixFile = new FileWriter("secfMatrix.csv");

			for(int i= 0; i < secfMatrix.length; i++)
			{
				for(int j= 0; j < secfMatrix[i].length; j++)
				{

					secfMatrixFile.append(String.valueOf(secfMatrix[i][j]));
					secfMatrixFile.append(",");
				}
				secfMatrixFile.append("\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		///////////////               Predictions Fusion Module             /////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		

		TrustSemanticFusion trustSemanticFusion = new TrustSemanticFusion();
		
		System.out.println("");
		System.out.println("");
		System.out.println("Trust Semantic Fusion -> Predicted User-Item Rating Matrix:");
		System.out.println("");

		// Predicted User-Item Rating Matrix
		Double[][] predictedUserItemRatingMatrix = new Double[supplierBuyerRatingMatrix.length][supplierBuyerRatingMatrix[0].length];
		
		for(int i= 0; i < predictedUserItemRatingMatrix.length; i++)
		{
			for(int j= 0; j < predictedUserItemRatingMatrix[i].length; j++)
			{
				if(supplierBuyerRatingMatrix[i][j] != null)
					predictedUserItemRatingMatrix[i][j] = supplierBuyerRatingMatrix[i][j];
				else
				{
					predictedUserItemRatingMatrix[i][j] = trustSemanticFusion.prediction(i, j, tecfMatrix, secfMatrix);
				}
			}
		}
		
		
		FileWriter predictedUserItemRatingMatrixFile= null;

		try 
		{
			predictedUserItemRatingMatrixFile = new FileWriter("predictedUserItemRatingMatrix.csv");

			// print Predicted User-Item Rating Matrix
			for(int i= 0; i < predictedUserItemRatingMatrix.length; i++)
			{
				for(int j= 0; j < predictedUserItemRatingMatrix[i].length; j++)
				{
					predictedUserItemRatingMatrixFile.append(String.valueOf(predictedUserItemRatingMatrix[i][j]));
					predictedUserItemRatingMatrixFile.append(",");
				}
				predictedUserItemRatingMatrixFile.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return predictedUserItemRatingMatrix;


	}
	
	public static void maeMetric()
	{
		//Get Ratings Matrix
		System.out.println("Ratings Matrix");
		Double [][]matrix = Matrix.getRatingsMatrix(943, 1682, 80000);

		FileWriter ratingsMatrixFile= null;

		try {
			ratingsMatrixFile = new FileWriter("ratingsMatrix.csv");

			for(int i= 0; i < matrix.length; i++)
			{
				for(int j= 0; j < matrix[i].length; j++)
				{
					ratingsMatrixFile.append(String.valueOf(matrix[i][j]));
					ratingsMatrixFile.append(",");
				}
				ratingsMatrixFile.append("\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		// obtain binaryItemTaxonomyMatrix

		Double[][] binaryItemTaxonomyMatrix = Matrix.getItemTaxonomyMatrix();

		FileWriter binaryItemTaxonomyMatrixFile= null;

		try {
			binaryItemTaxonomyMatrixFile = new FileWriter("binaryItemTaxonomyMatrix.csv");

			for(int i= 0; i < binaryItemTaxonomyMatrix.length; i++)
			{
				for(int j= 0; j < binaryItemTaxonomyMatrix[i].length; j++)
				{
					binaryItemTaxonomyMatrixFile.append(String.valueOf(binaryItemTaxonomyMatrix[i][j]));
					binaryItemTaxonomyMatrixFile.append(",");
				}
				binaryItemTaxonomyMatrixFile.append("\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//Matrix.printMatrix(binaryItemTaxonomyMatrix);

		System.out.println("");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("");

		//Recommender System TSF
		//				test3();



		long startTime = System.nanoTime();
		
		EvaluationMetrics evaluationMetrics= new EvaluationMetrics();

		Double[][] predictionsMatrix= test4(matrix, binaryItemTaxonomyMatrix);

		long endTime = System.nanoTime();

		long duration = (endTime - startTime) / 1000000000;
		
		ArrayList<Rating> testData= new ArrayList<Rating>();
		
		for(int x = 80000; x < ratings.size(); x++)
		{
			testData.add(ratings.get(x));
		}
		
		double maeValue = evaluationMetrics.meanAbsoluteError(predictionsMatrix, testData);

		System.out.println("");
		System.out.println("Time= " + duration);
		System.out.println("");
		System.out.println("");
		System.out.println("MAE= " + maeValue);
		
	}

}
