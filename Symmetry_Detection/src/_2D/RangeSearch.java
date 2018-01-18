package _2D ;

import Jcg.geometry.Point_;

/**
 * Define methods for range search queries
 * 
 * @author Luca Castelli Aleardi (Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public interface RangeSearch<X extends Point_> {
	
    /**
     * Range search: return the list of nearest points to a given query point q.
     * The output is the set of points at distance at most sqRad from q.
     */    
    public PointCloud OrthogonalRangeSearch (X q, double sqRad);
    
}
