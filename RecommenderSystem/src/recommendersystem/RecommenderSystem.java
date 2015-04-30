package recommendersystem;

import java.util.ArrayList;

import data.Genre;
import data.Item;
import data.Rating;
import data.User;
import utilities.File;

public class RecommenderSystem {
	
	public static ArrayList<User> users= new ArrayList<User>();
	public static ArrayList<Item> items= new ArrayList<Item>();
	public static ArrayList<Genre> genres= new ArrayList<Genre>();
	public static ArrayList<Rating> ratings= new ArrayList<Rating>();

	public static void main(String[] args) {
		System.out.println("Recommender System");
		
		File file = new File();
		file.read("user");
		file.read("genre");
		file.read("item");
		file.read("data");
		
		System.out.println("Database readed");
	}

}
