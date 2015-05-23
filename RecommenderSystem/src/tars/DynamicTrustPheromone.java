package tars;

import java.util.ArrayList;

import utilities.Matrix;
import utilities.Statistics;
import utilities.TreeNode;

public class DynamicTrustPheromone {
	
	private Double[][] trustMatrix;
	private int activeUser;
	TreeNode root;
	private ArrayList<TreeNode> graph;
	
	public DynamicTrustPheromone(int activeUser, Double [][] matrix){
		
		this.activeUser=activeUser;
		
		//initilize trustMatrix
		trustMatrix = new Double[matrix.length][matrix.length];
		
		//calculate trust intensity
		for(int i=0; i<trustMatrix.length;i++){
			for(int j =0 ; j<trustMatrix.length;j++){
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
		System.out.println("Trust Intensity calculated!");
		Matrix.printMatrix(trustMatrix);
		
		createGraph();
		/*for(int i=0; i<graph.size();i++){
			if(graph.get(i).hasChildren()){
				for(int j=0; j<graph.get(i).children().length;j++){
					System.out.println(graph.get(i).getUserObject()+" -- " + graph.get(i).children()[j].getUserObject() + ";");
				}
			}
		}*/
	}
	
	public double similarity(int user_u_id, int user_v_id, Double [][] matrix){
		Statistics statu=new Statistics(matrix[user_u_id]);
		Statistics statv=new Statistics(matrix[user_v_id]);
		double average_u = statu.getMean();
		double average_v = statv.getMean();
		
		double sum=0;
		for(int j=0; j<matrix[0].length; j++){//o problema e o -1
			if(matrix[user_u_id][j]!=null && matrix[user_v_id][j]!=null){
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
	
	public double confidence(int user_u_id, int user_v_id, Double [][] matrix){
		double count=0, count2=0;
		for(int j=0; j<matrix[0].length; j++){//o problema e o -1
			if(matrix[user_u_id][j]!=null && matrix[user_v_id][j]!=null){
				count++;
			}
			if(matrix[user_u_id][j]!=null){
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
		ArrayList<TreeNode> trust_node = new ArrayList<TreeNode>();
		ArrayList<Integer> non_trust_node = new ArrayList<Integer>();
		
		root = new TreeNode(activeUser);//root
		graph=new ArrayList<TreeNode>();
		graph.add(root);
		
		trust_node.add(root);
		
		for(int i=0; i<trustMatrix.length; i++){
			if(activeUser!=i){
				non_trust_node.add(i);
			}
		}
		createSubGraph(trust_node, non_trust_node);
	}
	
	public void createSubGraph(ArrayList<TreeNode> trust_node, ArrayList<Integer> non_trust_node){
		ArrayList<TreeNode> trust_node2 = new ArrayList<TreeNode>();
		ArrayList<Integer> non_trust_node2 = new ArrayList<Integer>();
		
		for(int i=0; i<non_trust_node.size(); i++){
			double value = -1;
			TreeNode node = null;
			for(int j=0; j<trust_node.size(); j++){
				if(trustMatrix[trust_node.get(j).getUserObject()][non_trust_node.get(i)]>0){
					if(trustMatrix[trust_node.get(j).getUserObject()][non_trust_node.get(i)]>value){
						value=trustMatrix[trust_node.get(j).getUserObject()][non_trust_node.get(i)];
						node = trust_node.get(j);
					}
				}
			}
			if(value!=-1 && node!=null){//trust_node
				TreeNode temp = new TreeNode(non_trust_node.get(i));
				node.add(temp);
				graph.add(temp);
				trust_node2.add(temp);
			}else{//non_trust_node
				non_trust_node2.add(non_trust_node.get(i));
			}
		}
		if(trust_node2.isEmpty()){
			return;
		}
		createSubGraph(trust_node2, non_trust_node2);
	}
	
	public void update(){
		
	}
	
	public Double[][] getTrustMatrix(){
		return trustMatrix;
	}
	
	public ArrayList<TreeNode> getGraph(){
		return graph;
	}
	
	public TreeNode getActiveUserNode(){
		return root;
	}

}
