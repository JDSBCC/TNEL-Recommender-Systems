package algorithms;


public class JaccardMetricItemBased {

	public Double result(int itemA, int itemB, Double[][] userItemRatingMatrix)
	{
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

		int numberUsersRatedItemA= 0;
		int numberUsersRatedItemB= 0;
		int numberCoratedItems= 0;

		for(int i= 0; i < itemARatings.length; i++)
		{
			// corated items
			// update Ua,b; Ua; Ub
			if(itemARatings[i] != null && itemBRatings[i] != null)
			{
				numberCoratedItems++;
				numberUsersRatedItemA++;
				numberUsersRatedItemB++;
			}
			else
			{
				// users who have rated item A
				// update Ua
				if(itemARatings[i] != null)
					numberUsersRatedItemA++;

				// users who have rated item B
				// update Ub
				if(itemBRatings[i] != null)
					numberUsersRatedItemB++;

			}


		}

		if(numberCoratedItems == 0)
			return null;
		else
		{
			// (Ua,b) / (Ua + Ub - Ua,b)
			return (double) ( numberCoratedItems / ((double)(numberUsersRatedItemA + numberUsersRatedItemB - numberCoratedItems)) );
		}


	}

}
