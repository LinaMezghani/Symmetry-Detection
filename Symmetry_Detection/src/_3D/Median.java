package _3D;

import Jcg.geometry.Point_3;
import Jcg.geometry.Point_d;

/**
 * Interface defining methods for computing point medians
 * 
 * @author Luca Castelli Aleardi (Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public interface Median {

	/**
	 * Compute the median of a set of points
	 * 
	 * @param cutDim defines the cut direction, {0,1,2} => {x,y,z}
	 * @return Point_d the median point according to the given direction
	 */    
	public Point_3 findMedian(int cutDim);

}
