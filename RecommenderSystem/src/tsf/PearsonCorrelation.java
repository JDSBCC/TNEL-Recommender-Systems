package tsf;

import java.util.ArrayList;

public class PearsonCorrelation {
	
	public Double result(int userA, int userB, Double[][] userItemRatingMatrix)
	{

		if(userA == userB)
			return 1.0;
		else
		{

			// initializations

			// user x line 
			Double[] userARatings= userItemRatingMatrix[userA];
			Double[] userBRatings= userItemRatingMatrix[userB];

			// sum user x ratings
			double ratingSumUserA= 0;
			double ratingSumUserB= 0;

			int numberUserARatedItems= 0;
			int numberUserBRatedItems= 0;

			int numberCoratedItems= 0;

			// auxiliary array
			ArrayList<Double> calcArrayUserA= new ArrayList<Double>();
			ArrayList<Double> calcArrayUserB= new ArrayList<Double>();

			for(int i= 0; i < userARatings.length; i++)
			{
				// corated items
				if(userARatings[i] != null && userBRatings[i] != null)
				{

					double userARating= userARatings[i];
					double userBRating= userBRatings[i];

					numberUserARatedItems++;
					numberUserBRatedItems++;

					numberCoratedItems++;

					calcArrayUserA.add(userARating);
					calcArrayUserB.add(userBRating);

					ratingSumUserA += userARating;
					ratingSumUserB += userBRating;

				}
				else
				{
					// items rated by user A
					if(userARatings[i] != null)
					{
						numberUserARatedItems++;

						ratingSumUserA += userARatings[i];
					}

					// items rated by user B
					if(userBRatings[i] != null)
					{
						numberUserBRatedItems++;

						ratingSumUserB += userBRatings[i];
					}
				}
			}

			// If user x and user y haven't corated items return null
			if(numberCoratedItems == 0)
				return null;
			else
			{

				//average rating values of user x on all items that he has rated, independently of user y ratings
				//mean(Rx)
				double meanUserA= (double) (ratingSumUserA / ((double) numberUserARatedItems));
				double meanUserB= (double) (ratingSumUserB / ((double) numberUserBRatedItems));

				//update auxiliary array "calcArray"	
				// Sum (Rx,i - mean(Rx))
				for(int i= 0; i < calcArrayUserA.size(); i++)
				{
					calcArrayUserA.set(i, calcArrayUserA.get(i) - meanUserA);
					calcArrayUserB.set(i, calcArrayUserB.get(i) - meanUserB);
				}

				// calculate numerator
				// Sum(((Rx,i - mean(Rx)) * ((Ry,i - mean(Ry)))
				double numerator= 0.0;

				for(int i = 0; i < numberCoratedItems; i++)
				{
					numerator += (double)(calcArrayUserA.get(i) * calcArrayUserB.get(i));
				}


				// calculate denominator

				// Sum( (Rx,i - mean(Rx))^2)
				double quadraticSumUserA= 0.0;
				double quadraticSumUserB= 0.0;

				for(int i = 0; i < numberCoratedItems; i++)
				{
					quadraticSumUserA += calcArrayUserA.get(i) * calcArrayUserA.get(i);
					quadraticSumUserB += calcArrayUserB.get(i) * calcArrayUserB.get(i);
				}

				// sqrt( (Sum( (Rx,i - mean(Rx))^2)) * (Sum( (Ry,i - mean(Ry))^2)) )
				double denominator = Math.sqrt(quadraticSumUserA) * Math.sqrt(quadraticSumUserB);


				if(denominator == 0.0 && numerator == 0.0)
					return 0.0;
				else
					return (double) (numerator / ((double) denominator));

			}
		}
	}

}
