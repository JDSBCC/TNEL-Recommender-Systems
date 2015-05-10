package tars;

import utilities.TreeNode;

public class TrustworthyFriend implements Comparable<TrustworthyFriend>{
	private TreeNode node;
	private double prob;
	
	public TrustworthyFriend(TreeNode node, double prob){
		this.node=node;
		this.prob=prob;
	}
	
	public TreeNode getNode(){
		return node;
	}
	
	public double getProb(){
		return prob;
	}
	
	@Override
	public int compareTo(TrustworthyFriend tf) {
		if(prob<tf.prob){
			return -1;
		}else if(prob>tf.prob){
			return 1;
		}
		return 0;
	}
}
