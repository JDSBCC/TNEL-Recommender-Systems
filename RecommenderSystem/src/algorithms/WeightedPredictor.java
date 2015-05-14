package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import utilities.Pair;

public class WeightedPredictor {
	
	public Double deviationFromMeanUserBasedApproach(int userA, int item, Double[][] userItemRatingMatrix, Double[][] eucfMatrix, Double[][] trustMatrix)
	{
		// user x line 
		Double[] userARatings= userItemRatingMatrix[userA];
		
		// sum user x ratings
		double ratingSumUserA= 0;

		int numberUserARatedItems= 0;
		
		for(int i= 0; i < userARatings.length; i++)
		{
			// items rated by user A
			if(userARatings[i] != null)
			{
				numberUserARatedItems++;

				ratingSumUserA += userARatings[i];
			}
		}
		
		//average rating values of user x on all items that he has rated, independently of other users ratings
		//mean(Rx)
		double meanUserA= (double) (ratingSumUserA / ((double) numberUserARatedItems));
		
		// Similarity part

		double similarityNumerator= 0.0;
		double similarityDenominator= 0.0;

		// N^(eUCF)
		Integer[] selectedNeighborsBySimilarity= topNMethod(3, userA, eucfMatrix[userA]);

		for(int i= 0; i < selectedNeighborsBySimilarity.length; i++)
		{

			// eUCFa,b
			Double similarityValue= eucfMatrix[userA][selectedNeighborsBySimilarity[i]];

			if(similarityValue != null && Math.abs(similarityValue) >= 0.00001)
			{
				// user x line 
				Double[] userBRatings= userItemRatingMatrix[selectedNeighborsBySimilarity[i]];

				// sum user x ratings
				double ratingSumUserB= 0;

				int numberUserBRatedItems= 0;

				Double userBRatingOnTargetItem= null;

				for(int k= 0; k < userBRatings.length; k++)
				{
					// items rated by user B
					if(userBRatings[k] != null)
					{
						numberUserBRatedItems++;

						ratingSumUserB += userBRatings[k];
					}

					if(k == item)
						userBRatingOnTargetItem= userBRatings[k];

				}

				if(userBRatingOnTargetItem != null)
				{

					//average rating values of user x on all items that he has rated, independently of other users ratings
					//mean(Rx)
					double meanUserB= (double) (ratingSumUserB / ((double) numberUserBRatedItems));

					// sum(eUCFa,b * (Rb,x - mean(Rb)))
					similarityNumerator += similarityValue * (userBRatingOnTargetItem - meanUserB);

					// sum(eUCFa,b)
					similarityDenominator += similarityValue;
				}

			}



		}
		
		
		
		
		
		// Trust part

		double trustNumerator= 0.0;
		double trustDenominator= 0.0;

		// N^(Trust a,b)
		Integer[] selectedNeighborsByTrust= topNMethod(3, userA, trustMatrix[userA]);

		for(int i= 0; i < selectedNeighborsByTrust.length; i++)
		{

			// Trust a,b
			Double trustValue= trustMatrix[userA][selectedNeighborsByTrust[i]];

			if(trustValue != null && Math.abs(trustValue) >= 0.00001)
			{
				// user x line 
				Double[] userBRatings= userItemRatingMatrix[selectedNeighborsByTrust[i]];

				// sum user x ratings
				double ratingSumUserB= 0;

				int numberUserBRatedItems= 0;

				Double userBRatingOnTargetItem= null;

				for(int k= 0; k < userBRatings.length; k++)
				{
					// items rated by user B
					if(userBRatings[k] != null)
					{
						numberUserBRatedItems++;

						ratingSumUserB += userBRatings[k];
					}

					if(k == item)
						userBRatingOnTargetItem= userBRatings[k];

				}

				if(userBRatingOnTargetItem != null)
				{

					//average rating values of user x on all items that he has rated, independently of other users ratings
					//mean(Rx)
					double meanUserB= (double) (ratingSumUserB / ((double) numberUserBRatedItems));

					//sum(Trust a,b * (Rb,x - mean(Rb)))
					trustNumerator += trustValue * (userBRatingOnTargetItem - meanUserB);

					//sum(Trust a,b)
					trustDenominator += trustValue;
				}

			}

		}

		return meanUserA + ( ( similarityNumerator + trustNumerator ) / (double)( similarityDenominator + trustDenominator ) );
		
		
		
	}
	
	public Double deviationFromMeanItemBasedApproachWithReputation(int userA, int item, Double[][] userItemRatingMatrix, Double[][] eicfMatrix, Double[][] semanticSimilarityMatrix, Double[] itemReputation)
	{
		// item x column 
		Double[] itemARatings= new Double[userItemRatingMatrix.length];

		for(int i = 0; i < userItemRatingMatrix.length; i++)
		{
			itemARatings[i] = userItemRatingMatrix[i][item];
		}

		// sum user x ratings
		double ratingSumItemA= 0;

		int numberItemARatings= 0;

		for(int i= 0; i < itemARatings.length; i++)
		{
			// items rated by user A
			if(itemARatings[i] != null)
			{
				numberItemARatings++;

				ratingSumItemA += itemARatings[i];
			}
		}

		//average rating values of user x on all items that he has rated, independently of other users ratings
		//mean(Rx)
		double meanItemA= (double) (ratingSumItemA / ((double) numberItemARatings));

		// Item-Based Similarity part

		double similarityNumerator= 0.0;
		double similarityDenominator= 0.0;

		// N^(eICF)
		Integer[] selectedNeighborsByItemBasedSimilarity= topNMethod(4, item, eicfMatrix[item]);

		////////////////

//		for(int w = 0; w < selectedNeighborsByItemBasedSimilarity.length; w++)
//		{
//			System.out.print(selectedNeighborsByItemBasedSimilarity[w] + ", ");
//		}
//		System.out.println("");

		///////////////

		for(int i= 0; i < selectedNeighborsByItemBasedSimilarity.length; i++)
		{

			// eICFx,y
			Double similarityValue= eicfMatrix[item][selectedNeighborsByItemBasedSimilarity[i]];

			if(similarityValue != null && Math.abs(similarityValue) >= 0.00001)
			{
				// item b column 
				Double[] itemBRatings= new Double[userItemRatingMatrix.length];

				for(int a = 0; a < userItemRatingMatrix.length; a++)
				{
					itemBRatings[a] = userItemRatingMatrix[a][selectedNeighborsByItemBasedSimilarity[i]];
				}

				// sum user x ratings
				double ratingSumItemB= 0;

				int numberItemBRatings= 0;

				Double itemBRatingOnTargetUser= null;

				for(int k= 0; k < itemBRatings.length; k++)
				{
					// items rated by user B
					if(itemBRatings[k] != null)
					{
						numberItemBRatings++;

						ratingSumItemB += itemBRatings[k];
					}

					if(k == userA)
						itemBRatingOnTargetUser= itemBRatings[k];

				}

				if(itemBRatingOnTargetUser != null)
				{

					//average rating values of user x on all items that he has rated, independently of other users ratings
					//mean(Rx)
					double meanItemB= (double) (ratingSumItemB / ((double) numberItemBRatings));

					// sum(eICFx,y * (Ra,y - mean(Ry)) * IRy)
					similarityNumerator += similarityValue * (itemBRatingOnTargetUser - meanItemB) * itemReputation[selectedNeighborsByItemBasedSimilarity[i]];

					// sum(eICFx,y + IRy)
					similarityDenominator += similarityValue + itemReputation[selectedNeighborsByItemBasedSimilarity[i]];
				}

			}



		}



		// Semantic Similarity (SS) part

		double ssNumerator= 0.0;
		double ssDenominator= 0.0;

		// N^(SemanticSimilarity)
		Integer[] selectedNeighborsBySS= topNMethod(4, item, semanticSimilarityMatrix[item]);
		
		////////////////
		
//		for(int w = 0; w < selectedNeighborsBySS.length; w++)
//		{
//			System.out.print(selectedNeighborsBySS[w] + ", ");
//		}
//		System.out.println("");
		
		///////////////

		for(int i= 0; i < selectedNeighborsBySS.length; i++)
		{

			// SSim x,y
			Double ssValue= semanticSimilarityMatrix[item][selectedNeighborsBySS[i]];
			
			System.out.println("I'm neighbor " + selectedNeighborsBySS[i]);
//			System.out.println(ssValue);
			System.out.println("");
			

			if(ssValue != null && Math.abs(ssValue) >= 0.00001)
			{
				// item b column 
				Double[] itemBRatings= new Double[userItemRatingMatrix.length];

				for(int a = 0; a < userItemRatingMatrix.length; a++)
				{
					itemBRatings[a] = userItemRatingMatrix[a][selectedNeighborsBySS[i]];
					
				}


				// sum user x ratings
				double ratingSumItemB= 0;

				int numberItemBRatings= 0;

				Double itemBRatingByTargetUser= null;

				for(int k= 0; k < itemBRatings.length; k++)
				{
					// items rated by user B
					if(itemBRatings[k] != null)
					{
						numberItemBRatings++;

						ratingSumItemB += itemBRatings[k];
					}

					if(k == userA)
						itemBRatingByTargetUser= itemBRatings[k];

				}

				if(itemBRatingByTargetUser != null)
				{

					//average rating values of user x on all items that he has rated, independently of other users ratings
					//mean(Ry)
					double meanUserB= (double) (ratingSumItemB / ((double) numberItemBRatings));

					//sum(SSim x,y * (Ra,y - mean(Ry)) * IRy)
					ssNumerator += ssValue * (itemBRatingByTargetUser - meanUserB) * itemReputation[selectedNeighborsBySS[i]];

					//sum(SSim x,y + IRy)
					ssDenominator += ssValue + itemReputation[selectedNeighborsBySS[i]];
				}

			}

		}
		
		System.out.println("meanItem= " + meanItemA);
		System.out.println("similarityNumerator= " + similarityNumerator);
		System.out.println("ssNumerator= " + ssNumerator);
		System.out.println("similarityDenominator= " + similarityDenominator);
		System.out.println("ssDenominator= " + ssDenominator);

		return meanItemA + ( ( similarityNumerator + ssNumerator ) / (double)( similarityDenominator + ssDenominator ) );



	}
	
	public Integer[] topNMethod(int n, int userA, Double[] line)
	{

		
		ArrayList<Pair<Integer,Double>> pairIndexValuelist= new ArrayList<Pair<Integer, Double>>();
		
		for(int i = 0; i < line.length; i++)
		{
			pairIndexValuelist.add(new Pair<Integer, Double>(i, line[i]));
		}


		Collections.sort(pairIndexValuelist, new Comparator<Pair<Integer, Double>>() {
			@Override
			public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
				if (o1.getSecond() == null && o2.getSecond() == null) {
					return 0;
				}
				if (o1.getSecond() == null) {
					return 1;
				}
				if (o2.getSecond() == null) {
					return -1;
				}
				return o1.getSecond().compareTo(o2.getSecond()) * (-1);
			}
		});
		
		Integer[] topNElements= new Integer[n];
		
		int counter= 0;
		
		
		for(int i = 0; i < pairIndexValuelist.size() && counter < n; i++)
		{
			if(pairIndexValuelist.get(i).getFirst() != userA)
			{
				topNElements[counter]= pairIndexValuelist.get(i).getFirst();
				counter++;
			}
		}

		return topNElements;
	}

}
