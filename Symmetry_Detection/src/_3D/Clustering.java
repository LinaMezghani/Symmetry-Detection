package _3D;

import Jcg.geometry.Point_3;
import Jcg.geometry.Point_d;

/**
 * Interface defining methods for clustering of point clouds
 * 
 * @author Luca Castelli Aleardi and Steve Oudot(Ecole Polytechnique, INF562)
 * @version jan 2014
 */
interface Clustering {
	
	/**
	 * Perform main clustering computation
	 */
	public Point_3[] computeClusters();
	
	/**
	 * Return the description of clusters
	 * 
	 * @return Cluster<Point_d> the description of the partition of points into clusters
	 */
	public Cluster<Point_3> getClusters();
	
}


