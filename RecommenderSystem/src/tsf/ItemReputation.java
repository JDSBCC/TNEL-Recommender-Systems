package tsf;

public class ItemReputation {
	
	public Double result(int itemA, Double[][] userItemRatingMatrix)
	{
		int totalNumberOfUsers= userItemRatingMatrix.length;
		int numberUsersRatedItemA= 0;
		double sumRatingItemA= 0.0;

		for(int i = 0; i < totalNumberOfUsers; i++)
		{
			Double itemRating = userItemRatingMatrix[i][itemA];
			
			if(itemRating != null)
			{
				numberUsersRatedItemA++;
				sumRatingItemA += itemRating;
			}
		}
		
		//average rating values of item x by all users who have rated it
		//mean(Ry)
		double meanItemA= (double) (sumRatingItemA / ((double) numberUsersRatedItemA));
		
		if(totalNumberOfUsers == 0)
			return null;
		else
			return (numberUsersRatedItemA / (double) totalNumberOfUsers ) * meanItemA;



	}

}
