package tars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import utilities.Pair;
import utilities.Statistics;
import utilities.TreeNode;

public class RecommendationProcess {
	
	private DynamicTrustPheromone dtp;
	private int item;
	
	private TreeNode s, root;
	private int level;
	private ArrayList<TrustworthyFriend> tf;
	
	private TreeNode nodeWithRec;
	private ArrayList<Integer> top_U;
	
	public RecommendationProcess(int item, DynamicTrustPheromone dtp, Double [][]ratings){
		
		this.dtp=dtp;
		this.item=item;
		
		initialization();
		bestNeighborhoodSelection(ratings);
		if(nodeWithRec!=null){
			generateRecommendation(ratings);
		}
	}
	
	public void initialization(){
		s = dtp.getActiveUserNode();
		root=s;
		level=1;
	}
	
	public void bestNeighborhoodSelection(Double [][]ratings){
		tf=new ArrayList<TrustworthyFriend>();
		
		for(int i=0; i<s.children().length;i++){
			tf.add(new TrustworthyFriend(s.children()[i], prob(dtp.getTrustMatrix()[s.getUserObject()][s.children()[i].getUserObject()])));
		}
		
		Collections.sort(tf, Collections.reverseOrder());
		for(int i=0; i<tf.size();i++){
			System.out.println("tf = "+tf.get(i).getNode().getUserObject()+"-"+tf.get(i).getProb());
		}
		
		nodeWithRec = aggregatingRatings(ratings);
	}
	
	public double prob(double trustIntensity){
		return Math.max(trustIntensity, 1/level);
	}
	
	public TreeNode aggregatingRatings(Double [][]ratings){
		top_U = top_U(dtp.getTrustMatrix(), 10);
		if(top_U.size()==1){
			for(int i=0; i<tf.size();i++){
				if(userRateTheItem(tf.get(i).getNode().getUserObject(), ratings)){
					return tf.get(i).getNode();
				}
			}
		}
		
		TreeNode temp=null;
		double maior=-1;
		for(int i=0; i<tf.size();i++){
			for(int j=0; j<top_U.size(); j++){
				if(tf.get(i).getNode().getUserObject()==top_U.get(j)){
					if(tf.get(i).getProb()>=maior){
						if(userRateTheItem(tf.get(i).getNode().getUserObject(), ratings)){
							maior=tf.get(i).getProb();
							temp=tf.get(i).getNode();
							System.out.println("temp = "+tf.get(i).getNode().getUserObject());
						}
					}
				}
			}
		}

		if(temp==null && !tf.isEmpty()){
			level++;
			s=tf.get(0).getNode();
			bestNeighborhoodSelection(ratings);
		}
		return temp;
	}
	
	public ArrayList<Integer> top_U(Double [][] trustMatrix, int n){
		top_U = new ArrayList<Integer>();
		ArrayList<Pair<Integer, Double> > temp = new ArrayList<Pair<Integer, Double> >();
		
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
	
	public boolean userRateTheItem(int user, Double [][]ratings){
		for(int i=0; i<ratings.length; i++){
			if(ratings[user][item]!=null){
				return true;
			}
		}
		return false;
	}
	
	public void generateRecommendation(Double [][]ratings){
		Statistics stati=new Statistics(ratings[root.getUserObject()]);
		Statistics statj=new Statistics(ratings[nodeWithRec.getUserObject()]);
		double ari = stati.getMean();
		double arj = statj.getMean();
		
		double sum1=0, sum2=0;
		System.out.println("top_U.size() = "+top_U.size());
		for(int j=0; j<top_U.size();j++){
			if(dtp.getTrustMatrix()[root.getUserObject()][top_U.get(j)]!=null && ratings[top_U.get(j)][item]!=null){
				sum1 += dtp.getTrustMatrix()[root.getUserObject()][top_U.get(j)]*(ratings[top_U.get(j)][item]-arj);
				sum2 += dtp.getTrustMatrix()[root.getUserObject()][top_U.get(j)];
				System.out.println("sum2 = "+sum2);
				System.out.println("top_U.get(j) = "+top_U.get(j));
			}
		}
		
		ratings[root.getUserObject()][1] = ari+(sum1/sum2);
	}
}
