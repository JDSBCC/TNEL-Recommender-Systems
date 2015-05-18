package tsf;

import java.util.ArrayList;


public class MeanSquaredDifferencesMethod {
	
	//Assumption: userA is the active user
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

				// calculate numerator
				// Sum( (Pa,i - Ra,i) ^ 2 ), where i belongs to [1; Ia,b]
				double numerator= 0.0;

				for(int i = 0; i < numberCoratedItems; i++)
				{
					//Predicted Rating -> Resnick's Prediction Method: Pa,i= mean(Ra) + (Rb,i - mean(Rb))
					//where Pa,i belongs to [0,5]
					// In order to the final result belong to the interval [0,1] will be necessary to normalize
					// Pa,i and Ra,i
					double predictedRealDifference= 
							maxMinNormalizationMethod(0, 5, ( meanUserA + ( calcArrayUserB.get(i) - meanUserB ) ), 0, 1) 
							- maxMinNormalizationMethod(0, 5, calcArrayUserA.get(i), 0, 1);
					

					numerator += predictedRealDifference * predictedRealDifference; 
				}

				//MSDa,b = (1 - ( (Sum( (Pa,i - Ra,i) ^ 2 )) / (Ia,b)))
				return (double) ((1.0) - (numerator / ((double) numberCoratedItems)));

			}
		}
	}
	
	private double maxMinNormalizationMethod(double min, double max, double value, double newMin, double newMax)
	{
		return (((value - min)/(double)(max - min)) * (newMax - newMin)) + newMin; 
	}

}
