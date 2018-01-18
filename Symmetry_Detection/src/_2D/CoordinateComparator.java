package _2D ;

import java.util.*;

import Jcg.geometry.Point_;

/**
 * Class allowing to compare two points according to a given coordinate
 * 
 * @author Luca Castelli Aleardi (Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public class CoordinateComparator<X extends Point_> implements Comparator<X>{
	
	int direction; // indicates which coordinates are to be compared
	
	public CoordinateComparator(int dir) {
		this.direction=dir;
	}
	
	/**
	 * Compare two points according to their i-th coordinate (where i=direction)
	 * @param p the first point to compare
	 * @param q second point to compare
	 * @return return -1, 0 or +1, depending whether p<q
	 */
	public int compare(X p, X q) {
		if(p.getCartesian(this.direction).doubleValue()<q.getCartesian(this.direction).doubleValue())
			return -1;
		else if(p.getCartesian(this.direction).doubleValue()>q.getCartesian(this.direction).doubleValue()) 
			return 1;
		return 0;
	}
	
}
