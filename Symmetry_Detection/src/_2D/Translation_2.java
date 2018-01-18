package _2D ;

import Jcg.geometry.*;

/**
 * Define a translation in 2D
 *
 * @author Luca Castelli Aleardi (INF555, 2014)
 */
public class Translation_2 {

	Vector_2 v;
	
	/**
	 * Initialize the translation
	 */
	public Translation_2(Vector_2 v) {
		this.v=v;
	}

	/**
	 * Translate point p by vector v (return a new point q)
	 */
	public Point_2 transform(Point_2 p) {
		return p.sum(this.v);
	}

}
