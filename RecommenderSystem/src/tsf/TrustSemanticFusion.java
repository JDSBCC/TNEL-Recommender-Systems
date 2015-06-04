package tsf;

public class TrustSemanticFusion {
	
	public Double prediction(int user, int item, Double[][] userTrustModulePredictionMatrix, Double[][] itemSemanticModulePredictionMatrix)
	{
		Double userTrustPrediction= userTrustModulePredictionMatrix[user][item];
		Double itemSemanticPrediction= itemSemanticModulePredictionMatrix[user][item];
		
		if(userTrustPrediction != null && itemSemanticPrediction != null)
		{

			if((Math.abs(userTrustPrediction) <= 0.00001) && (Math.abs(itemSemanticPrediction) <= 0.00001))
			{
				return (Double) 0.0;
			}
			else if((Math.abs(userTrustPrediction) >= 0.00001) && (Math.abs(itemSemanticPrediction) >= 0.00001))
			{
				return (2.0 * (userTrustPrediction) * (itemSemanticPrediction)) / (double) (userTrustPrediction + itemSemanticPrediction);
			}
			else if(Math.abs(userTrustPrediction) >= 0.00001)
				return userTrustPrediction;
			else if(Math.abs(itemSemanticPrediction) >= 0.00001)
				return itemSemanticPrediction;
			else
				return null;
		}
		else
			return null;
	}

	
	public Double prediction(int user, int item, Double[] userTrustModulePredictionLine, Double[] itemSemanticModulePredictionLine)
	{
		Double userTrustPrediction= userTrustModulePredictionLine[item];
		Double itemSemanticPrediction= itemSemanticModulePredictionLine[item];
		
		if(userTrustPrediction != null && itemSemanticPrediction != null)
		{

			if((Math.abs(userTrustPrediction) <= 0.00001) && (Math.abs(itemSemanticPrediction) <= 0.00001))
			{
				return (Double) 0.0;
			}
			else if((Math.abs(userTrustPrediction) >= 0.00001) && (Math.abs(itemSemanticPrediction) >= 0.00001))
			{
				return (2.0 * (userTrustPrediction) * (itemSemanticPrediction)) / (double) (userTrustPrediction + itemSemanticPrediction);
			}
			else if(Math.abs(userTrustPrediction) >= 0.00001)
				return userTrustPrediction;
			else if(Math.abs(itemSemanticPrediction) >= 0.00001)
				return itemSemanticPrediction;
			else
				return null;
		}
		else
			return null;
	}
	
}
