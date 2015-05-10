package recommendersystem;

import java.util.ArrayList;

import data.Genre;
import data.Item;
import data.Rating;
import data.User;
import tars.DynamicTrustPheromone;
import tars.RecommendationProcess;
import utilities.File;
import utilities.Matrix;

public class RecommenderSystem {
	
	public static ArrayList<User> users= new ArrayList<User>();
	public static ArrayList<Item> items= new ArrayList<Item>();
	public static ArrayList<Genre> genres= new ArrayList<Genre>();
	public static ArrayList<Rating> ratings= new ArrayList<Rating>();

	public static void main(String[] args) {
		System.out.println("Recommender System");
		
		//Read data from database
		File file = new File();
		file.read("user");
		file.read("genre");
		file.read("item");
		file.read("data");
		System.out.println("Database readed");
		
		//Get Ratings Matrix
		System.out.println("Ratings Matrix");
		double [][]matrix = Matrix.getRatingsMatrix();
		//Matrix.printMatrix(matrix);
		
		//Get Normalized Ratings Matrix
		System.out.println("Normalized Ratings Matrix");
		double [][]n_matrix = Matrix.getNormalizedRatingsMatrix(matrix);
		Matrix.printMatrix(n_matrix);
		
		//Testing TARS
		DynamicTrustPheromone dtp = new DynamicTrustPheromone(4, n_matrix);
		RecommendationProcess rp = new RecommendationProcess(dtp);
	}

}
