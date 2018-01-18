package _2D ;

//import Jcg.viewer.old.Fenetre;
import Jcg.geometry.*;

/**
 * Class representing a point cloud (as a linked list)
 * 
 * @author Luca Castelli Aleardi and Steve Oudot (Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public class PointCloud {

    public Point_2 p;
    public PointCloud next;

    /**
     * Constructor: add a new point to the cloud (copying or not)
     */
    PointCloud (Point_2 p, PointCloud n, boolean copy) {
    	if (copy)
    		this.p = new Point_2 (p);
    	else
    		this.p = p;
    	next = n;
    }

    public String toString () {
	String s = "";
	for (PointCloud n = this; n != null; n = n.next)
	    s += n.p.toString() + "\n";
	return s;
    }
    
    /**
     * return the size (number of points)
     */
    public static int size(PointCloud N) {
    	int size = 0;
    	for (PointCloud n = N; n != null; n = n.next) size++;
    	return size;
    }
    
	   /*
	   * Compute the mean of a point cloud 
	   */
	  public static Point_2 mean (PointCloud N) {
		int dim = N.p.dimension();
		double[] coords = new double [dim];
		int totalWeight = 0;
		
		for (PointCloud n = N; n != null; n=n.next) {
		    totalWeight++;
		    for (int i=0; i<dim; i++)
			coords[i] += n.p.getCartesian(i).doubleValue();
		}
		for (int i=0; i<dim; i++)
			coords[i] /= totalWeight;

		return new Point_2(new Point_d (coords));
	  }

	public static double myDistance(Point_2 p, Point_2 q) {
		double dX=p.x - q.x;
  	    double dY=Math.min(Math.abs(p.y-q.y),Math.abs(2*Math.PI -p.y+q.y));
  	    return Math.sqrt(dX*dX+dY*dY);
	}

	public PointSet toPointSet(){
    	//converts PointCloud to PointSet
    	PointSet ps = new PointSet() ;
    	for (PointCloud n = this; n != null; n = n.next){
    		ps.add(n.p) ;
    	}
    	return ps ;
    }

    public static Point_2[] copy(PointCloud N, int size){
    	Point_2[] res = new Point_2[size];
    	for(int i=0; i<size; i++){
    		res[i]=N.p;
    		N=N.next;
    	}
    	return res;
    }
    
	public static PointCloud transformPointCloud(PointCloud N, Translation_2 translation) {
		System.out.print("Applying rigid transformation to point set...");
		PointCloud result=null;
		
		for(PointCloud n = N ; n != null ; n = n.next) {
			Point_2 z=translation.transform(n.p);
			result = new PointCloud(z,result,true) ;
		}
		System.out.println("done");
		return result ;
	}
}
