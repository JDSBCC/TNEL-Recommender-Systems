package utilities;

import java.text.DecimalFormat;

import recommendersystem.RecommenderSystem;

public class Matrix {
	
	//Matrix form
	//  i1  i2  i3  i4
	//u1
	//u2
	//u3
	
	public static double [][] getRatingsMatrix(){//0 significa que nao tem calssificaçao
		double[][]matrix=new double[/*943*/5][/*1682*/5];//so para ser facil testar
		
		for(int i=0; i<RecommenderSystem.ratings.size(); i++){
			int userId=RecommenderSystem.ratings.get(i).getUser().getId();
			int itemId=RecommenderSystem.ratings.get(i).getItem().getId(); 
			if(userId<5 && itemId<5){//so para teste-para nao dar erro
				matrix[userId-1][itemId-1]=RecommenderSystem.ratings.get(i).getRating();
			}
		}
		return matrix;
	}
	
	public static double [][] getNormalizedRatingsMatrix(double[][]matrix){//sem classificaçao corresponde a -1
		double [][]new_matrix = new double[matrix.length][matrix[0].length];
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[0].length; j++){
				double rating=matrix[i][j];
				double rating_sum = getRatingItemSum(matrix[i]);
				new_matrix[i][j]=rating/rating_sum;
				if(Double.isNaN(new_matrix[i][j])){
					new_matrix[i][j]=-1;
				}
			}
		}
		return new_matrix;
	}
	
	private static double getRatingItemSum(double []matrix){
		double sum=0;
		for(int k=0; k<matrix.length; k++){
			sum+=matrix[k];
		}
		return sum;
	}
	
	public static void printMatrix(double [][]matrix){
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[i].length; j++){
				DecimalFormat oneDigit = new DecimalFormat("#,##0.00");
			    System.out.print(oneDigit.format(matrix[i][j]) + "  ");
			}
		    System.out.print("\n");
		}
	}

}
