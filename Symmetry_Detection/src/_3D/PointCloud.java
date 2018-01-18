package _3D;

import Jcg.geometry.*;

/**
 * Class representing a point cloud (as a linked list)
 * 
 * @author Luca Castelli Aleardi and Steve Oudot (Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public class PointCloud {

    public Point_3 p;
    public PointCloud next;

    /**
     * Constructor: add a new point to the cloud (copying or not)
     */
    public PointCloud (Point_3 p, PointCloud n, boolean copy) {
    	if (copy)
    		this.p = new Point_3 (p);
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
    
    
    public static double myDistance(Point_3 p, Point_3 q){
  		double dX=p.x - q.x;
  	    double dY=Math.min(p.y-q.y,2*Math.PI -p.y+q.y);
  	    double dZ=p.z-q.z;
  	    return Math.sqrt(dX*dX+dY*dY+dZ*dZ);
  	}
    
	   /*
	   * Compute the mean of a point cloud 
	   */
	  public static Point_3 mean (PointCloud N) {
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

		return new Point_3 (coords[0],coords[1],coords[2]);
	  }

	   /*
	   * Compute the bounding box of a point cloud 
	   */
	  public static double[] boundingBox(PointCloud N) {
		  if(N==null) {
			  System.out.println("point cloud empty");
			  return null;
		  }
		  int dim=N.p.dimension();
		  double[] box=new double[2*dim];
		  
		  Point_3 point=N.p;
		  for(int i=0;i<dim;i++) {
			  box[i]=point.getCartesian(i).doubleValue();
			  box[dim+i]=point.getCartesian(i).doubleValue();
		  }
	      	
		  PointCloud t=N;
		  while(t!=null) {
			  point=t.p;
			  for(int i=0;i<dim;i++) {
				  // updating minimum values
				  if(point.getCartesian(i).doubleValue()<box[i]) box[i]=point.getCartesian(i).doubleValue();
				  // updating maximum values
				  if(point.getCartesian(i).doubleValue()>box[i+dim]) box[i+dim]=point.getCartesian(i).doubleValue();
			  }
			  t=t.next;
		  }
	      return box;
	  }

    /**
     * return a point cloud of n random points (in the unit hyper-square in dimension dim)
     */
    public static PointCloud randomPoints(int n, int dim) {
    	PointCloud result=null;
    	for (int i=0; i<n; i++) {
    	    double[] c = new double[dim];
    	    for (int j=0; j<dim; ++j)
    	    	c[j] = 1.*Math.random();
    	    result= new PointCloud (new Point_3 (c[0],c[1],c[2]), result, true);
    	}
    	System.out.println("Generated point cloud from random points in dimension "+dim);
    	return result;
    }

    /**
     * return a point cloud of n random points sampled on a circle 
     * (according to normal distribution)
     */
    public static PointCloud randomPointsOnCircle(int n, int dim) {
    	PointCloud result=null;
    	double R=1.;
    	for (int i=0; i<n; i++) {
    	    double[] c = new double[dim];
    	    double radius=0.2;
    	    double angle=Math.random()*2*Math.PI;
    	    for (int j=0; j<dim; ++j) {    	
    	    	c[j] = -radius+radius*2.*Math.random();
    	    }
    	    c[0]=c[0]+R*Math.cos(angle);
    	    c[1]=c[1]+R*Math.sin(angle);
    	    result= new PointCloud (new Point_3 (c[0],c[1],c[2]), result, true);
    	}
    	System.out.println("Generated point cloud from random points in dimension "+dim);
    	return result;
    }

    /**
     * Return a random value according to the normal distribution
     */
    public double randNorm(double mean, double sigma) {
        double u = Math.sqrt(-2.0*Math.log(Math.random()));
        return (u+mean)*sigma;
    }
    
    //--MODIF
    //Renvoit un tableau des size 1ers �l�ments de N (si size = |N|, convertit
    //le PointCloud en tableau de Point_3
    //Pr�-condition : size <= |N|
    public static Point_3[] copy(PointCloud N, int size){
    	Point_3[] res = new Point_3[size];
    	for(int i=0; i<size; i++){
    		res[i]=N.p;
    		N=N.next;
    	}
    	return res;
    }
    
    public PointSet toPointSet(){
    	//converts PointCloud to PointSet
    	PointSet ps = new PointSet() ;
    	for (PointCloud n = this; n != null; n = n.next){
    		ps.add(n.p) ;
    	}
    	return ps ;
    }
    
    public static void main(String[ ] args) throws Exception {
    	PointCloud N2d=null, N3d = null;
    	N2d=randomPoints(40,2);
    	N3d=randomPoints(40,3);
    	
    	Draw.draw2D(N2d, "2D point cloud test");
    	Draw.draw3D(N3d);
    }

	public static PointCloud transformPointCloud(PointCloud N,
			Translation_3 translation) {
		System.out.print("Applying rigid transformation to point set...");
		PointCloud result=null;
		
		for(PointCloud n = N ; n != null ; n = n.next) {
			Point_3 z=translation.transform(n.p);
			result = new PointCloud(z,result,true) ;
		}
		System.out.println("done");
		return result ;
	}
}
