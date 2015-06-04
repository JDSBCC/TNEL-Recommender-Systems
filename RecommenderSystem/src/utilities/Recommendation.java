package utilities;

import java.io.Serializable;
import java.util.ArrayList;

import data.Item;

public class Recommendation implements Serializable{
	
	private ArrayList<Item> recommendedItems;
	
	public Recommendation(ArrayList<Item> recommendedItems)
	{
		this.recommendedItems= recommendedItems;
	}

	public ArrayList<Item> getRecommendedItems() {
		return recommendedItems;
	}
	
	

}
