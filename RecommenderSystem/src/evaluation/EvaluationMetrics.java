package evaluation;

import java.util.ArrayList;

import data.Rating;

public class EvaluationMetrics {
	
	public double meanAbsoluteError(Double[][] predictionsMatrix, ArrayList<Rating> testData)
	{
		double sum = 0.0;
		int n = testData.size();
		
		for(int i = 0; i < n; i++)
		{
//			System.out.println("userId= " + testData.get(i).getUser().getId() + " / itemId= " + testData.get(i).getItem().getId() + "Reality= " + testData.get(i).getRating() + " / Prediction= " + predictionsMatrix[testData.get(i).getUser().getId() - 1][testData.get(i).getItem().getId() - 1] + " ==> CurrentMAE= " + (sum / (double)(i+1)));
			if(predictionsMatrix[testData.get(i).getUser().getId() - 1][testData.get(i).getItem().getId() - 1] == null)
				sum += 5;
			else
				sum += Math.abs(testData.get(i).getRating() - predictionsMatrix[testData.get(i).getUser().getId() - 1][testData.get(i).getItem().getId() - 1]);
		}
		
		return sum / (double) n;
	}

}
