package tsf;

public class BinaryJaccardSimilarityMetric {
	
	public Double result(int itemA, int itemB, Double[][] binaryItemTaxonomyMatrix)
	{
		Double[] itemACategoryVector= new Double[binaryItemTaxonomyMatrix[0].length];
		Double[] itemBCategoryVector= new Double[binaryItemTaxonomyMatrix[0].length];

		itemACategoryVector= binaryItemTaxonomyMatrix[itemA];
		itemBCategoryVector= binaryItemTaxonomyMatrix[itemB];

		int c11= 0;
		int c01= 0;
		int c10= 0;

		for(int i= 0; i < itemACategoryVector.length; i++)
		{
			
			if(itemACategoryVector[i] == 1 && itemBCategoryVector[i] == 1)
			{
				c11++;
			}
			else if(itemACategoryVector[i] == 0 && itemBCategoryVector[i] == 1)
			{
				c01++;
			}
			else if(itemACategoryVector[i] == 1 && itemBCategoryVector[i] == 0)
			{
				c10++;
			}


		}

		return c11 / (double) (c01 + c10 + c11);


	}

}
