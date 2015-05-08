package utilities;

import java.text.DecimalFormat;

import recommendersystem.RecommenderSystem;

public class Matrix {
	
	//Matrix form
	//  i1  i2  i3  i4
	//u1
	//u2
	//u3
	
	private static void initMatrix(int num, double[][]matrix){
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[i].length; j++){
				matrix[i][j]=num;
			}
		}
	}
	
	public static double [][] getRatingsMatrix(){//-1 significa que nao tem calssificaçao
		double[][]matrix=new double[/*943*/10][/*1682*/10];//so para ser facil testar
		initMatrix(-1, matrix);
		
		for(int i=0; i<RecommenderSystem.ratings.size(); i++){
			int userId=RecommenderSystem.ratings.get(i).getUser().getId();
			int itemId=RecommenderSystem.ratings.get(i).getItem().getId();
			if(userId<10 && itemId<10){//so para teste-para nao dar erro
				matrix[userId-1][itemId-1]=RecommenderSystem.ratings.get(i).getRating();
			}
		}
		return matrix;
	}
	
	public static double [][] getNormalizedRatingsMatrix(double[][]matrix){//-1=sem classificaçao
		double [][]new_matrix = new double[matrix.length][matrix[0].length];
		initMatrix(-1, new_matrix);
		for(int i=0; i<matrix.length; i++){
			double rating_sum = getRatingItemSum(matrix[i]);
			for(int j=0; j<matrix[0].length; j++){
				double rating = 0;
				if(matrix[i][j]==-1){
					continue;
				}else{
					rating=matrix[i][j];
				}
				new_matrix[i][j]=rating/rating_sum;
			}
		}
		return new_matrix;
	}
	
	private static double getRatingItemSum(double []matrix){
		double sum=0;
		for(int k=0; k<matrix.length; k++){
			sum+=matrix[k]==-1?0:matrix[k];
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
