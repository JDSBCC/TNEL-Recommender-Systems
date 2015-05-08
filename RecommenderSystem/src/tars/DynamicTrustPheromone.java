package tars;

import utilities.Statistics;

public class DynamicTrustPheromone {
	private int user_u_id, user_v_id;
	
	public DynamicTrustPheromone(int user_u_id, int user_v_id, double [][] matrix){
		this.user_u_id=user_u_id;
		this.user_v_id=user_v_id;
		
		double sim = similarity(matrix);
		System.out.println("sim("+this.user_u_id+","+this.user_v_id+") = " + sim);
	}
	
	public double similarity(double [][] matrix){
		Statistics statu=new Statistics(matrix[user_u_id]);
		Statistics statv=new Statistics(matrix[user_v_id]);
		double average_u = statu.getMean();
		double average_v = statv.getMean();
		
		double sum=0;
		for(int j=0; j<matrix[0].length; j++){//o problema e o -1
			if(matrix[user_u_id][j]!=-1 && matrix[user_v_id][j]!=-1){
				sum+=(matrix[user_u_id][j]-average_u)*(matrix[user_v_id][j]-average_v);
			}
		}
		
		double deviation_u = statu.getStdDev();
		double deviation_v = statv.getStdDev();

		double res = sum/(deviation_u*deviation_v);
		
		if(res>0 && res<=1){
			return res;
		}
		return 0;
	}
	
	public double confidence(){
		return 0;
	}
	
	public double trustIntensity(){
		return 0;
	}
	
	public void update(){
		
	}

}
