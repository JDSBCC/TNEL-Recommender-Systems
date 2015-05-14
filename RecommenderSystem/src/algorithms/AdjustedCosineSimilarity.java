package algorithms;

import java.util.ArrayList;


public class AdjustedCosineSimilarity {
	
	public Double result(int itemA, int itemB, Double[][] userItemRatingMatrix)
	{

		if(itemA == itemB)
			return 1.0;
		else
		{

			// initializations

			// item x column 
			Double[] itemARatings= new Double[userItemRatingMatrix.length];
			Double[] itemBRatings= new Double[userItemRatingMatrix.length];
	
			for(int i = 0; i < userItemRatingMatrix.length; i++)
			{
				itemARatings[i] = userItemRatingMatrix[i][itemA];
			}

			for(int i = 0; i < userItemRatingMatrix.length; i++)
			{
				itemBRatings[i] = userItemRatingMatrix[i][itemB];
			}

			int numberCoratedItems= 0;

			// auxiliary array
			ArrayList<Double> calcArrayItemA= new ArrayList<Double>();
			ArrayList<Double> calcArrayItemB= new ArrayList<Double>();
			
			ArrayList<Double> userUMeanArray= new ArrayList<Double>();
			
			

			for(int u= 0; u < itemARatings.length; u++)
			{
				// corated items
				if(itemARatings[u] != null && itemBRatings[u] != null)
				{

					double itemARating= itemARatings[u];
					double itemBRating= itemBRatings[u];

					numberCoratedItems++;

					calcArrayItemA.add(itemARating);
					calcArrayItemB.add(itemBRating);
					
					// calculate the mean rating value of user u on all items that he (user u) have rated
					// mean(Ru)
					Double[] userURatings= userItemRatingMatrix[u];
					double userURatingSum= 0.0;
					int userUItemsRated= 0;
					
					//for all user u ratings
					for(int i= 0; i < userURatings.length; i++)
					{
						if(userURatings[i] != null)
						{
							userURatingSum += userURatings[i];
							userUItemsRated++;
						}
					}
					
					userUMeanArray.add( (double) (userURatingSum / ((double) userUItemsRated)) );


				}
				
			}

			// If item x and item y haven't corated users then return null
			if(numberCoratedItems == 0)
				return null;
			else
			{

				//update auxiliary array "calcArray"	
				// Sum (Ru,x - mean(Ru))
				for(int i= 0; i < calcArrayItemA.size(); i++)
				{
					calcArrayItemA.set(i, calcArrayItemA.get(i) - userUMeanArray.get(i));
					calcArrayItemB.set(i, calcArrayItemB.get(i) - userUMeanArray.get(i));
				}

				// calculate numerator
				// Sum(((Ru,x - mean(Ru)) * ((Ru,y - mean(Ru)))
				double numerator= 0.0;

				for(int i = 0; i < numberCoratedItems; i++)
				{
					numerator += (double)(calcArrayItemA.get(i) * calcArrayItemB.get(i));
				}


				// calculate denominator

				// Sum( (Ru,x - mean(Ru))^2)
				double quadraticSumUserA= 0.0;
				// Sum( (Ru,y - mean(Ru))^2)
				double quadraticSumUserB= 0.0;

				for(int i = 0; i < numberCoratedItems; i++)
				{
					quadraticSumUserA += calcArrayItemA.get(i) * calcArrayItemA.get(i);
					quadraticSumUserB += calcArrayItemB.get(i) * calcArrayItemB.get(i);
				}

				// sqrt( (Sum( (Ru,x - mean(Ru))^2)) * (Sum( (Ru,y - mean(Ru))^2)) )
				double denominator = Math.sqrt(quadraticSumUserA) * Math.sqrt(quadraticSumUserB);


				if(denominator == 0.0 && numerator == 0.0)
					return 0.0;
				else
					return (double) (numerator / ((double) denominator));

			}
		}
	}

}
