package utilities;

import java.io.Serializable;
import java.util.ArrayList;

import data.Item;

public class Recommendation implements Serializable{
	
	private ArrayList<Pair<Item, Double>> recommendedItems;
	
	public Recommendation(ArrayList<Pair<Item, Double>> recommendedItems)
	{
		this.recommendedItems= recommendedItems;
	}

	public ArrayList<Pair<Item, Double>> getRecommendedItems() {
		return recommendedItems;
	}
	
	

}
