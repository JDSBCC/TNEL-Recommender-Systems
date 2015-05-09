package tars;

import java.util.ArrayList;

import utilities.Matrix;
import utilities.Statistics;
import utilities.TreeNode;

public class DynamicTrustPheromone {
	
	private double [][] trustMatrix;
	private int activeUser;
	TreeNode root;
	private ArrayList<TreeNode> graph;
	
	public DynamicTrustPheromone(int activeUser, double [][] matrix){
		
		this.activeUser=activeUser;
		root = new TreeNode(activeUser);//root
		graph=new ArrayList<TreeNode>();
		
		//initilize trustMatrix
		trustMatrix = new double[matrix.length][matrix.length];
		Matrix.initMatrix(-1, trustMatrix);
		
		//calculate trust intensity
		for(int i=0; i<matrix.length;i++){
			for(int j =0 ; j<matrix.length;j++){
				if(i!=j){
					double sim = similarity(i, j, matrix);
					//System.out.println("sim("+this.user_u_id+","+this.user_v_id+") = " + sim);
					
					double conf = confidence(i, j, matrix);
					//System.out.println("conf("+this.user_v_id+"|"+this.user_u_id+") = " + conf);
					
					trustMatrix[i][j] = trustIntensity(sim, conf);
					//System.out.println("t("+i+","+j+") = " + trustMatrix[i][j]);
				}
			}
		}
		Matrix.printMatrix(trustMatrix);
		
		createGraph();
		for(int i=0; i<graph.size();i++){
			System.out.println("graph_node = "+graph.get(i).getUserObject());
			if(graph.get(i).hasChildren()){
				for(int j=0; j<graph.get(i).children().length;j++){
					System.out.println("child["+j+"] = "+graph.get(i).children()[j].getUserObject());
				}
			}
		}
	}
	
	public double similarity(int user_u_id, int user_v_id, double [][] matrix){
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
	
	public double confidence(int user_u_id, int user_v_id, double [][] matrix){
		double count=0, count2=0;
		for(int j=0; j<matrix[0].length; j++){//o problema e o -1
			if(matrix[user_u_id][j]!=-1 && matrix[user_v_id][j]!=-1){
				count++;
			}
			if(matrix[user_u_id][j]!=-1){
				count2++;
			}
		}
		if(count2==0){
			return 0;
		}
		return count/count2;
	}
	
	public double trustIntensity(double sim, double conf){
		if(sim!=0 && conf !=0){
			return (2*sim*conf)/(sim+conf);
		}else if(sim==0 && conf!=0){
			return conf * 0.01;//k=0.01
		}
		return 0;
	}
	
	public void createGraph(){
		ArrayList<Integer> node_temp = new ArrayList<Integer>();
		
		for(int i=0; i<trustMatrix.length; i++){
			if(trustMatrix[activeUser][i]>0){//trust_node
				TreeNode temp = new TreeNode(i);
				root.add(temp);
				graph.add(temp);
			}else{//non_trust_node
				if(activeUser!=i){
					node_temp.add(i);
				}
			}
		}
		createSubGraph(node_temp);
	}
	
	public void createSubGraph(ArrayList<Integer> node_temp){
		int count=0;
		ArrayList<Integer> node_temp2 = new ArrayList<Integer>();
		for(int i=0; i<node_temp.size(); i++){
			double value = -1;
			TreeNode node = null;
			for(int j=0; j<root.children().length; j++){
				if(trustMatrix[root.children()[j].getUserObject()][node_temp.get(i)]>0){
					if(trustMatrix[root.children()[j].getUserObject()][node_temp.get(i)]>value){
						value=trustMatrix[root.children()[j].getUserObject()][node_temp.get(i)];
						node = root.children()[j];
					}
				}
			}
			if(value!=-1 && node!=null){//trust_node
				TreeNode temp = new TreeNode(node_temp.get(i));
				node.add(temp);
				graph.add(temp);
			}else{//non_trust_node
				count++;
				node_temp2.add(node_temp.get(i));
			}
		}
		if(count==node_temp.size()){
			return;
		}else{
			createSubGraph(node_temp2);
		}
	}
	
	public void update(){
		
	}

}
