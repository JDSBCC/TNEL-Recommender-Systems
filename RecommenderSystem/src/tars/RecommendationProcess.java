package tars;

import java.util.ArrayList;
import java.util.Collections;
import utilities.TreeNode;

public class RecommendationProcess {
	
	private DynamicTrustPheromone dtp;
	
	private TreeNode s;
	private int level;
	private ArrayList<TrustworthyFriend> tf;
	
	public RecommendationProcess(DynamicTrustPheromone dtp){
		
		this.dtp=dtp;
		
		initialization();
		bestNeighborhoodSelection();
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
	
	public void aggregatingRatings(){
		
	}
}
