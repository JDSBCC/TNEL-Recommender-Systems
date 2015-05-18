package tsf;

public class JaccardMetricUserBased {
	
	public Double result(int userA, int userB, Double[][] userItemRatingMatrix)
	{
		Double[] userARatings= userItemRatingMatrix[userA];
		Double[] userBRatings= userItemRatingMatrix[userB];
		
		int numberItemsRatedUserA= 0;
		int numberItemsRatedUserB= 0;
		int numberCoratedItems= 0;
		
		for(int i= 0; i < userARatings.length; i++)
		{
			// corated items
			// update Ia,b; Ia; Ib
			if(userARatings[i] != null && userBRatings[i] != null)
			{
				numberCoratedItems++;
				numberItemsRatedUserA++;
				numberItemsRatedUserB++;
			}
			else
			{
				// items rated by user A
				// update Ia
				if(userARatings[i] != null)
					numberItemsRatedUserA++;
				
				// items rated by user B
				// update Ib
				if(userBRatings[i] != null)
					numberItemsRatedUserB++;
				
			}
			
			
		}
		
		if(numberCoratedItems == 0)
			return null;
		else
		{
			// (Ia,b) / (Ia + Ib - Ia,b)
			return (double) ( numberCoratedItems / ((double)(numberItemsRatedUserA + numberItemsRatedUserB - numberCoratedItems)) );
		}

		
	}

}
