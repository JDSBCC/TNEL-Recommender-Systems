package tars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import utilities.Pair;
import utilities.TreeNode;

public class RecommendationProcess {
	
	private DynamicTrustPheromone dtp;
	
	private TreeNode s;
	private int level;
	private ArrayList<TrustworthyFriend> tf;
	
	public RecommendationProcess(DynamicTrustPheromone dtp, Double [][]ratings){
		
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
			System.out.println("tf = "+tf.get(i).getNode().getUserObject()+"-"+tf.get(i).getProb());
		}
	}
	
	public double prob(double trustIntensity){
		return Math.max(trustIntensity, 1/level);
	}
	
	public TreeNode aggregatingRatings(Double [][]ratings){
		ArrayList<Integer> top_U = top_U(dtp.getTrustMatrix(),1, 10);
		System.out.println("top_U = " + top_U.size());
		if(top_U.size()==1){
			return tf.get(0).getNode();
		}
		
		TreeNode temp=null;
		double maior=-1;
		for(int i=0; i<tf.size();i++){
			for(int j=0; j<top_U.size(); j++){
				if(tf.get(i).getNode().getUserObject()==top_U.get(j)){
					if(tf.get(i).getProb()>=maior){
						maior=tf.get(i).getProb();
						temp=tf.get(i).getNode();
						System.out.println("temp = "+tf.get(i).getNode().getUserObject());
					}
				}
			}
		}
		
		return temp;
	}
	
	public ArrayList<Integer> top_U(Double [][] trustMatrix, int item, int n){
		
		ArrayList<Pair<Integer, Double> > temp = new ArrayList<Pair<Integer, Double> >();
		ArrayList<Integer> top_U = new ArrayList<Integer>();
		
		for(int i = 0; i<trustMatrix.length;i++){
			if(trustMatrix[i][item] != null){
				temp.add(new Pair<Integer, Double>(i, trustMatrix[i][item]));
			}
		}
		
		Collections.sort(temp, new Comparator<Pair<Integer, Double>>() {
			@Override
			public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
				if (o1.getSecond() == null && o2.getSecond() == null) {
					return 0;
				}
				if (o1.getSecond() == null) {
					return 1;
				}
				if (o2.getSecond() == null) {
					return -1;
				}
				return o1.getSecond().compareTo(o2.getSecond()) * (-1);
			}
		});
		int num = n<temp.size()?n:temp.size();
		for(int i = 0; i<num;i++){
			top_U.add(temp.get(i).getFirst());
		}
		
		return top_U;
	}
}
