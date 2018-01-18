package _2D ;

import java.util.HashMap;

import Jcg.geometry.Point_2;

/**
 * This class describes how points are associated to a cluster (throw hashing tables)
 * 
 * @author Luca Castelli Aleardi (Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public class Cluster<X extends Point_2> {
	
	private HashMap<X,Integer> clusters=new HashMap<X,Integer>(); // describes how points are assigned to clusters
	
	/**
	 * Assign a point to a given cluster
	 * 
	 * @param p the point to add to a cluster
	 * @param i the index of the cluster
	 */
	public void addToCluster(X p, int i) {
		if(this.clusters.containsKey(p)==false) {
			this.clusters.put(p, i);
			return;
		}
		//this.clusters.remove(p);
		this.clusters.put(p, i);
	}
	
	/**
	 * Get the index of the cluster containing a given point
	 * 
	 * @param p the point
	 * @return i the index of the i-th cluster containing 'p'. Return -1 if 'p' is not assigned to a cluster
	 */
	public int getClusterIndex(X p) {
		if(this.clusters.containsKey(p))
			return this.clusters.get(p);
		return -1;
	}
}
