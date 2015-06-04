package recommendersystem;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.StaleProxyException;
import jade.wrapper.AgentContainer;

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
import utilities.File;
import utilities.Matrix;
import agents.ClientAgent;
import agents.RecommenderAgent;
import data.Genre;
import data.Item;
import data.Rating;
import data.User;
import evaluation.EvaluationMetrics;
import ginterface.Rating_menu;


public class RecommenderSystem {
	
	public static ArrayList<User> users= new ArrayList<User>();
	public static ArrayList<Item> items= new ArrayList<Item>();
	public static ArrayList<Genre> genres= new ArrayList<Genre>();
	public static ArrayList<Rating> ratings= new ArrayList<Rating>();
	
	public static Double [][] tars_ratings;
	
	private static Runtime runtime;

	private static AgentContainer container;

	protected static RecommenderAgent recommenderAgent;

	public static ClientAgent clientAgent;
		
	public static final int activeUser = 4;
	public static final int num_users = 10;
	public static final int num_items = 20;
	

	public static void main(String[] args) {
		
		//System.out.println("Recommender System");
		
		//Read data from database
		File file = new File();
		file.read("user");
		file.read("genre");
		file.read("item");
		file.read("data");
		//System.out.println("Database readed");
		

		//Get Ratings Matrix
		//System.out.println("Ratings Matrix");
//		Double [][]matrix = Matrix.getRatingsMatrix(943, 1682, 100000);
		
		Double [][]matrix = Matrix.getRatingsMatrix(num_users, num_items, ratings.size());
		
		Double[][] binaryItemTaxonomyMatrix = Matrix.getItemTaxonomyMatrix(20, 19);
		
		runtime = jade.core.Runtime.instance();
		
		// Add agent
		Profile profile = new ProfileImpl();
		profile.setParameter("gui", "false");
		profile.setParameter("port", "8000");

		container = runtime.createMainContainer(profile);

		recommenderAgent = new RecommenderAgent();
		try
		{
			container.acceptNewAgent("RecommenderAgent", recommenderAgent).start();
		}
		catch (StaleProxyException e)
		{
			e.printStackTrace();
		}
		
		clientAgent = new ClientAgent();
		try
		{
			container.acceptNewAgent("ClientAgent", clientAgent).start();
		}
		catch (StaleProxyException e)
		{
			e.printStackTrace();
		}
		
		
		
		//Rating rating = new Rating("");
		Rating_menu.launch(Rating_menu.class);
	}
	
	public static int getRatingsByUserItem(int user, Item item){
		for(int i=0; i<ratings.size(); i++){
			if(ratings.get(i).getUser().getId()==user && ratings.get(i).getItem().getId()==item.getId()){
				return ratings.get(i).getRating();
			}
		}
		return 0;
	}
	
	public static Double[] activeUserTSFPredictions(int activeUser, Double[][] supplierBuyerRatingMatrix, Double[][] binaryItemTaxonomyMatrix)
	{

		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////               User-based Trust-enhanced CF Module             /////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////

		//		Double[][] supplierBuyerRatingMatrix= getSupplierBuyerRatingMatrix();

		PearsonCorrelation pearsonCorrelation= new PearsonCorrelation();
		JaccardMetricUserBased jaccardMetric= new JaccardMetricUserBased();

		//System.out.println("Applying Pearson Correlation and Jaccard Metric:");
		//System.out.println("");

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
		//System.out.println("B-B enhanced user-based CF similarity matrix:");
		//System.out.println("");

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

//		// Write on file
//
//		FileWriter eucfMatrixFile= null;
//
//		try {
//			eucfMatrixFile = new FileWriter("eucfMatrix.csv");
//
//			for(int i = 0; i < eucfMatrix.length; i++)
//			{
//				for(int j= 0; j < eucfMatrix.length; j++)
//				{
//					eucfMatrixFile.append(String.valueOf(eucfMatrix[i][j]));
//					eucfMatrixFile.append(",");
//				}
//
//				eucfMatrixFile.append("\n");
//
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

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
		//System.out.println("");
		//System.out.println("B-B direct implicit trust matrix:");

//		FileWriter trustMatrixFile= null;
//
//		try 
//		{
//			trustMatrixFile = new FileWriter("trustMatrix.csv");
//
//			for(int i = 0; i < trustMatrix.length; i++)
//			{
//				for(int j= 0; j < trustMatrix[i].length; j++)
//				{
//
//					trustMatrixFile.append(String.valueOf(trustMatrix[i][j]));
//					trustMatrixFile.append(",");
//				}
//				trustMatrixFile.append("\n");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

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
		//System.out.println("");
		//System.out.println("B-B propagated implicit trust matrix:");

//		FileWriter trustPropagationMatrixFile= null;
//
//		try 
//		{
//			trustPropagationMatrixFile = new FileWriter("trustPropagationMatrix.csv");
//
//			for(int i = 0; i < trustMatrix.length; i++)
//			{
//				for(int j= 0; j < trustMatrix[i].length; j++)
//				{
//
//					trustPropagationMatrixFile.append(String.valueOf(trustMatrix[i][j]));
//					trustPropagationMatrixFile.append(",");
//				}
//				trustPropagationMatrixFile.append("\n");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		//System.out.println("");
		//System.out.println("TeCF predicted supplier-buyer matrix:");


		Double[] tecfLine = new Double[supplierBuyerRatingMatrix[0].length];

		for(int j= 0; j < tecfLine.length; j++)
		{
			
				if(supplierBuyerRatingMatrix[activeUser][j] != null)
					tecfLine[j] = supplierBuyerRatingMatrix[activeUser][j];
				else
				{
					WeightedPredictor wp= new WeightedPredictor();

					tecfLine[j] = wp.deviationFromMeanUserBasedApproach(activeUser, j, supplierBuyerRatingMatrix, eucfMatrix, trustMatrix);
				}
			
		}

		// show TeCF predicted supplier-buyer matrix

		FileWriter tecfMatrixFile= null;

//		try 
//		{
//			tecfMatrixFile = new FileWriter("tecfMatrix.csv");
//
//			for(int i= 0; i < tecfMatrix.length; i++)
//			{
//				for(int j= 0; j < tecfMatrix[i].length; j++)
//				{
//
//					tecfMatrixFile.append(String.valueOf(tecfMatrix[i][j]));
//					tecfMatrixFile.append(",");
//				}
//				tecfMatrixFile.append("\n");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}



		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////            Item-based Similarity-enhanced CF Module            ////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////



		//Double[][] supplierBuyerRatingMatrix= getSupplierBuyerRatingMatrix();
		//Double[][] binaryItemTaxonomyMatrix= getBinaryItemTaxonomyMatrix();

		AdjustedCosineSimilarity adjustedCosineSimilarity= new AdjustedCosineSimilarity();
		JaccardMetricItemBased jaccardMetricItemBased= new JaccardMetricItemBased();

		//System.out.println("Applying Adjusted Cosine Similarity Measure and Jaccard Metric [Item Based]:");
		//System.out.println("");

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
		//System.out.println("Supplier-Supplier enhanced item-based CF similarity matrix:");
		//System.out.println("");

//		FileWriter itemSimilarityMatrixFile= null;
//
//		try 
//		{
//			itemSimilarityMatrixFile = new FileWriter("itemSimilarityMatrix.csv");
//
//			for(int i = 0; i < itemSimilarityMatrix.length; i++)
//			{
//				for(int j= 0; j < itemSimilarityMatrix.length; j++)
//				{
//
//
//					itemSimilarityMatrixFile.append(String.valueOf(itemSimilarityMatrix[i][j]));
//					itemSimilarityMatrixFile.append(",");
//				}
//
//				itemSimilarityMatrixFile.append("\n");
//
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// Supplier-Supplier semantic similarity matrix

		BinaryJaccardSimilarityMetric bjsm= new BinaryJaccardSimilarityMetric();

		//System.out.println("");
		//System.out.println("");
		//System.out.println("Applying Binary Jaccard Similarity Metric:");
		//System.out.println("");

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

//		FileWriter bjsmMatrixFile= null;
//
//		try 
//		{
//			bjsmMatrixFile = new FileWriter("bjsmMatrix.csv");
//
//			for(int i = 0; i < bjsmMatrix.length; i++)
//			{
//				for(int j= 0; j < bjsmMatrix[i].length; j++)
//				{
//
//					bjsmMatrixFile.append(String.valueOf(bjsmMatrix[i][j]));
//					bjsmMatrixFile.append(",");
//
//				}
//				bjsmMatrixFile.append("\n");
//
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// Supplier Reputation Matrix

		ItemReputation itemReputation= new ItemReputation();

		//System.out.println("");
		//System.out.println("");
		//System.out.println("Applying Item Reputation:");
		//System.out.println("");

		// Supplier-Supplier semantic similarity matrix
		Double[] itemReputationMatrix = new Double[supplierBuyerRatingMatrix[0].length];

		for(int i = 0; i < supplierBuyerRatingMatrix[0].length; i++)
		{
			itemReputationMatrix[i]= itemReputation.result(i, supplierBuyerRatingMatrix);
		}

		// print Supplier-Supplier semantic similarity matrix

		FileWriter itemReputationMatrixFile= null;

//		try 
//		{
//			itemReputationMatrixFile = new FileWriter("itemReputationMatrix.csv");
//
//			for(int j= 0; j < itemReputationMatrix.length; j++)
//			{
//
//				itemReputationMatrixFile.append(String.valueOf(itemReputationMatrix[j]));
//				itemReputationMatrixFile.append(",");
//
//			}
//			itemReputationMatrixFile.append("\n");
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


		//System.out.println("");
		//System.out.println("SeCF predicted rating matrix:");


		Double[] secfLine = new Double[supplierBuyerRatingMatrix[0].length];

		for(int j= 0; j < secfLine.length; j++)
		{
			
				if(supplierBuyerRatingMatrix[activeUser][j] != null)
					secfLine[j] = supplierBuyerRatingMatrix[activeUser][j];
				else
				{
					WeightedPredictor wp= new WeightedPredictor();

					secfLine[j] = wp.deviationFromMeanItemBasedApproachWithReputation(activeUser, j, supplierBuyerRatingMatrix, itemSimilarityMatrix, bjsmMatrix, itemReputationMatrix);
				}
			
		}

		// show TeCF predicted supplier-buyer matrix

//		FileWriter secfMatrixFile= null;
//
//		try 
//		{
//			secfMatrixFile = new FileWriter("secfMatrix.csv");
//
//			for(int i= 0; i < secfMatrix.length; i++)
//			{
//				for(int j= 0; j < secfMatrix[i].length; j++)
//				{
//
//					secfMatrixFile.append(String.valueOf(secfMatrix[i][j]));
//					secfMatrixFile.append(",");
//				}
//				secfMatrixFile.append("\n");
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		///////////////               Predictions Fusion Module             /////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////


		TrustSemanticFusion trustSemanticFusion = new TrustSemanticFusion();

		//System.out.println("");
		//System.out.println("");
		//System.out.println("Trust Semantic Fusion -> Predicted User-Item Rating Matrix:");
		//System.out.println("");

		// Predicted User-Item Rating Matrix
		Double[] predictedUserItemRatingLine = new Double[supplierBuyerRatingMatrix[0].length];

		for(int j= 0; j < predictedUserItemRatingLine.length; j++)
		{
			
				if(supplierBuyerRatingMatrix[activeUser][j] != null)
					predictedUserItemRatingLine[j] = supplierBuyerRatingMatrix[activeUser][j];
				else
				{
					predictedUserItemRatingLine[j] = trustSemanticFusion.prediction(activeUser, j, tecfLine, secfLine);
				}
			
		}


//		FileWriter predictedUserItemRatingMatrixFile= null;
//
//		try 
//		{
//			predictedUserItemRatingMatrixFile = new FileWriter("predictedUserItemRatingMatrix.csv");
//
//			// print Predicted User-Item Rating Matrix
//			for(int i= 0; i < predictedUserItemRatingMatrix.length; i++)
//			{
//				for(int j= 0; j < predictedUserItemRatingMatrix[i].length; j++)
//				{
//					predictedUserItemRatingMatrixFile.append(String.valueOf(predictedUserItemRatingMatrix[i][j]));
//					predictedUserItemRatingMatrixFile.append(",");
//				}
//				predictedUserItemRatingMatrixFile.append("\n");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return predictedUserItemRatingLine;


	}
	


}
