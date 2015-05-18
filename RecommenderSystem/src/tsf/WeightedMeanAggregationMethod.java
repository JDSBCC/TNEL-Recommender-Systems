package tsf;

// Trust propagation method
// input: implicit trust matrix
// returns inferred trust value between users that have not direct trust values
public class WeightedMeanAggregationMethod {
	
	// tunable trust filter threshold applied to ensure that non-trustworthy users, as defined by 
	// the trust filter, are not allowed to participate in the trust propagation process. Thus,
	// users who have a trust value below the threshold are considered as untrustworthy users, and 
	// cannot influence the trust propagation process.
	private double lambda;
	
	// tunable trust propagation limit that is used to control the maximum distance from 
	// the source user to where the trust is propagated.
	private double MPDist;
	
	public WeightedMeanAggregationMethod(double lambda, double MPDist)
	{
		this.lambda= lambda;
		this.MPDist= MPDist;
	}
	
	public Double result(int userA, int userB, Double[][] trustMatrix)
	{
		return propagateTrust(2, userA, userB, trustMatrix);
	}
	
	public Double propagateTrust(int currentDistance, int userA, int userB, Double[][] trustMatrix)
	{
		if(currentDistance > this.MPDist + 1)
		{
			// Maximum distance reached without reached target user (userB)
			return 0.0;
		}
		else
		{
			
			if(trustMatrix[userA][userB] != null)
			{
				//if exists a direct trust value from userA to userB returns this value
				//and stop recursive calls avoiding trust propagation cycles
				return trustMatrix[userA][userB];
			}
			else
			{
				Double[] trustValuesUserA= trustMatrix[userA];

				double sum= 0.0;
				double directTrustSum= 0.0;

				for(int i = 0; i < trustValuesUserA.length; i++)
				{
					// for all adjacent user to userA which have direct trust values
					if(trustValuesUserA[i] != null && i != userA)
					{
						// no trusted users are ignored in this propagation process
						// DTrusta,b >=  lambda
						if(trustValuesUserA[i] >= this.lambda)
						{
							double oldSum= sum;
							
							// equation numerator
							// sum( DTrusta,b * (PTrustb,c * BETAd) )
							sum += (trustValuesUserA[i]) * (propagateTrust(currentDistance + 1, i, userB, trustMatrix) * betaValue(currentDistance));
							
							// equation denominator
							// sum (DTrusta,b)
							// just if recursive call haven't returned zero -> meaning that this path doesn't reaches
							// the target user (userB) so will not count in the sum
							if( Math.abs((oldSum - sum)) > 0.00001)
							{
								// checks if recursive call have returned zero or not without calling it again
								directTrustSum += trustValuesUserA[i];
							}
						}
					}
				}

				if(directTrustSum == 0.0)
					return 0.0;
				else 
					return sum / (double) directTrustSum;
			}
		}
	}
	
	// trust metric MoleTrust
	// used to ensure that trust decreases along the propagation (trust scores from directly trusted 
	// neighbors, at a close propagation distance, will have more weights)
	public double betaValue(int currentDistance)
	{
		return (this.MPDist - currentDistance + 1)/ ((double)(this.MPDist));
	}

}
