package utilities;

import java.text.DecimalFormat;

import recommendersystem.RecommenderSystem;

public class Matrix {
	
	//Matrix form
	//  i1  i2  i3  i4
	//u1
	//u2
	//u3
	
	public static Double [][] getRatingsMatrix(){//-1 significa que nao tem calssificaçao
		Double[][]matrix=new Double[/*943*/10][/*1682*/10];//so para ser facil testar
		
		for(int i=0; i<RecommenderSystem.ratings.size(); i++){
			int userId=RecommenderSystem.ratings.get(i).getUser().getId();
			int itemId=RecommenderSystem.ratings.get(i).getItem().getId();
			if(userId<10 && itemId<10){//so para teste-para nao dar erro
				matrix[userId-1][itemId-1]=(double)RecommenderSystem.ratings.get(i).getRating();
			}
		}
		return matrix;
	}
	
	public static Double [][] getNormalizedRatingsMatrix(Double[][]matrix){//-1=sem classificaçao
		Double [][]new_matrix = new Double[matrix.length][matrix[0].length];
		for(int i=0; i<matrix.length; i++){
			double rating_sum = getRatingItemSum(matrix[i]);
			for(int j=0; j<matrix[0].length; j++){
				double rating = 0;
				if(matrix[i][j]==null){
					continue;
				}else{
					rating=matrix[i][j];
				}
				new_matrix[i][j]=rating/rating_sum;
			}
		}
		return new_matrix;
	}
	
	private static Double getRatingItemSum(Double []matrix){
		double sum=0;
		for(int k=0; k<matrix.length; k++){
			if(matrix[k]!=null)
				sum+=matrix[k];
		}
		return sum;
	}
	
	public static void printMatrix(Double [][]matrix){
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[i].length; j++){
				if(matrix[i][j]!=null){
					DecimalFormat oneDigit = new DecimalFormat("#,##0.0000");
			    	System.out.print(oneDigit.format(matrix[i][j]) + "  ");
				}else{
					System.out.print("null    ");
				}
			}
		    System.out.print("\n");
		}
	}

}
