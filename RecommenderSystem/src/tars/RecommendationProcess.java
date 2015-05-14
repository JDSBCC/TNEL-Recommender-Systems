package tars;

import java.util.ArrayList;
import java.util.Collections;
import utilities.TreeNode;

public class RecommendationProcess {
	
	private DynamicTrustPheromone dtp;
	
	private TreeNode s;
	private int level;
	private ArrayList<TrustworthyFriend> tf;
	
	public RecommendationProcess(DynamicTrustPheromone dtp, double [][]ratings){
		
		this.dtp=dtp;
		
		initialization();
		bestNeighborhoodSelection();
		aggregatingRatings(ratings);
	}
	
	public void initialization(){
		s = dtp.getActiveUserNode();
		level=1;
	}
	
	public void bestNeighborhoodSelection(){
		tf=new ArrayList<TrustworthyFriend>();
		
		for(int i=0; i<s.children().length;i++){
			tf.add(new TrustworthyFriend(s.children()[i], prob(dtp.getTrustMatrix()[s.getUserObject()][s.children()[i].getUserObject()])));
		}
		
		Collections.sort(tf, Collections.reverseOrder());
		for(int i=0; i<tf.size();i++){
			System.out.println("tf = "+tf.get(i).getProb());
		}
	}
	
	public double prob(double trustIntensity){
		//System.out.println("ti = "+trustIntensity);
		return Math.max(trustIntensity, 1/level);
	}
	
	public void aggregatingRatings(double [][]ratings){
		ArrayList<Integer> top_U = top_U(dtp.getTrustMatrix(),1);
		//System.out.println("top_U = " + top_U.size());
		if(top_U.size()==1){
			
		}else{
			
		}
	}
	
	public ArrayList<Integer> top_U(double [][] trustMatrix, int item){
		ArrayList<Integer> top_U = new ArrayList<Integer>();
		double maior = -1;
		
		for(int i = 0; i<trustMatrix.length;i++){
			if(trustMatrix[i][item]>=maior){
				maior=trustMatrix[i][item];
			}
		}
		for(int i = 0; i<trustMatrix.length;i++){
			if(trustMatrix[i][item]==maior){
				top_U.add(i);
			}
		}
		
		return top_U;
	}
}
