package _2D;

import Jcg.geometry.Point_;
import Jcg.geometry.Point_2;
import Jcg.geometry.Point_3;

/**
 * @author Luca Castelli Aleardi (Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public class SlowRangeSearch<X extends Point_> implements RangeSearch<X> {

	PointCloud N;
	
	public SlowRangeSearch(PointCloud N) {
		this.N=N;
	}
	
	/**
	 * Linear time search (exhaustive search)
	 * Useful for point clouds of small size
	 */
	public PointCloud OrthogonalRangeSearch(X q, double sqRad) {
		PointCloud res = null;
		for (PointCloud n = N; n != null; n = n.next)
			if (PointCloud.myDistance(n.p,new Point_2(q)) < sqRad)
				res = new PointCloud (n.p, res, false);  // do not duplicate point
		return res;
	}

}
