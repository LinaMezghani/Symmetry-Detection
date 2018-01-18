package _2D ;


import Jcg.geometry.PointCloud_2;
import Jcg.geometry.PointCloud_3;
import Jcg.geometry.Point_2;
import Jcg.geometry.Point_d;

import java.util.*;

/**
 * Define methods for manipulating a point cloud in 3D
 *
 * @author Luca Castelli Aleardi (INF555, 2014)
 */
public class PointSet extends PointCloud_2 {
	
	public PointSet() {
		super();
	}
	
	/**
	 * Load a point cloud from a text file
	 */
	public PointSet(String filename) {
		super();
    	System.out.print("Creating a point cloud (from OFF file): ");
    	System.out.println(filename);
    	
    	double x, y, z;
    	Jcg.io.IO.readTextFile(filename);
    	String line;
            
            line=Jcg.io.IO.readLine(); // first line is empty
            line=Jcg.io.IO.readLine();
            String[] w=Jcg.io.IO.wordsFromString(line);
            int sizePoints=Integer.parseInt(w[0]);
                        
            int i=0;
            Point_2 point;
            System.out.print("\tReading coordinates...");
            
            while(i<sizePoints) {
                line=Jcg.io.IO.readLine();
                w = Jcg.io.IO.wordsFromString(line);
                if(w!=null && w.length>0 && w[0].charAt(0)!='#') { // skip empty lines an comments
                	x=(new Double(w[0])).doubleValue();
                	y=(new Double(w[1])).doubleValue();
                
                	point=new Point_2(x,y);
                	this.add(point);
                	i++;
                }
            }
            System.out.println("done "+sizePoints+" points");
            
           Jcg.io.IO.readStandardInput();
	}

	/**
	 * Return an array containing all points in the point cloud
	 */
	public Point_2[] toArray() {
		Point_2[] tab = new Point_2[points.size()];
		
		int i=0;
		for(Point_2 p : this.points) {
			tab[i] = p;
			i++;
		}
		
		return tab;
	}
	
	/**
	 * Export the point cloud to a text file
	 */
	public void toFile(String filename) {
    	System.out.print("Exporting point set to OFF file...");
    	Jcg.io.IO.writeNewTextFile(filename);
    	
    	int nVertices=this.size();
    	int nFaces=0;
    	
    	// file header
    	Jcg.io.IO.println("OFF");
    	Jcg.io.IO.println(nVertices+" "+nFaces+" "+0);
    	    	
    	// writing point coordinates
    	for(Point_2 p: this.listOfPoints()) { // iterate over all points
    		double x=p.getX().doubleValue();
    		double y=p.getY().doubleValue();
    		Jcg.io.IO.println(""+x+" "+y);
    	}
    	
    	Jcg.io.IO.writeStandardOutput();
    	System.out.println("done");
	}

	/**
	 * Return the centroid of the point cloud
	 */
	/*public Point_2 getBarycenter() {
		
		Point_2 res = new Point_2(0, 0);
		
		for(Point_2 p : this.listOfPoints())
		{
			res.x += p.x;
			res.y += p.y;
		}
		
		((Object) res).multiplyByScalar(1/(double)this.size());
		
		return res;
	}*/

	/**
	 * Return a new point set, obtained by applying a rotation and a translation
	 */
	public PointSet transformPoints(Rotation_2 r, Translation_2 t) {
		System.out.print("Applying rigid transformation to point set...");
		PointSet result=new PointSet();
		
		for(Point_2 p: this.listOfPoints()) {
			Point_2 q=r.transform(p);
			Point_2 z=t.transform(q);
			result.add(z);
		}
		System.out.println("done");
		return result;
	}

	/**
	 * Permute input points
	 */
	public void permute() {
		System.out.print("Permuting the point set...");
		
		int n=this.size();
		int i=(int)(Math.random()*n);
		int j=(int)(Math.random()*n);

		if(i!=j) {
			List<Point_2> list=(List<Point_2>)this.listOfPoints();
			Point_2 p=list.get(i);
			Point_2 q=list.get(j);
			
			list.set(i, q);
			list.set(j, p);
		}
		
		System.out.println("done");
	}

	/**
	 * Return the closest points to q, at distance at most d <p>
	 * <p>
	 * Warning: naive method, based on a linear scan
	 * 
	 * @param sqRad  square of the distance (sqRad=d*d)
	 * @param q  point query
	 */
	public List<Point_2> getClosestPoints(Point_2 q, double sqRad) {
		List<Point_2> closest = new LinkedList<Point_2>() ;
		for(Point_2 p : this.points){
			double dist = p.distanceFrom(q).doubleValue() ;
			if( p.squareDistance(q).doubleValue() < sqRad){
				closest.add(p) ;
			}
		}
		return closest ;
	}

	/**
	 * Return the closest point to q, among the points of the point cloud
	 * <p>
	 * Warning: naive method, based on a linear scan
	 * 
	 * @param q  point query
	 */
	public Point_2 getClosestPoint(Point_2 q) {
		double mindist = Double.POSITIVE_INFINITY ;
		Point_2 closest = new Point_2() ;
		for(Point_2 p : this.points){
			double dist = p.distanceFrom(q).doubleValue() ;
			if( dist < mindist){
				mindist = dist ;
				closest = p ;
			}
		}
		return closest ;
		
	}
	
	/**
	 * Return the k-nearest neighbors to point p in the point cloud
	 * <p>
	 * Warning: naive method (complexity n*k), based on a linear scan
	 * 
	 * @param q  point query
	 * @param k  number of points to return
	 */
	public Point_2[] getKNearestNeighbors(Point_2 q, int k) {
		
		Point_2[] result = new Point_2[k] ;
		//the distances matrix is always ordered and distances[i] contains distance between q and result[i]
		double[] distances = new double[k] ;
		//initialize result and distances
		for(int i = 0 ; i<k ; i++){
			result[i] = q ;
			distances[i] = Double.POSITIVE_INFINITY;
		}
		
		for(Point_2 p : this.points){
			double d = p.distanceFrom(q).doubleValue() ;
			int i = k ;
			while(i>0&&d<distances[i-1])
				i-- ;
			if(i<k){
				for(int j = k-1 ; j>i ; j--){
					distances[j] = distances[j-1] ;
					result[j] = result[j-1] ;
				}
				distances[i] = d ;
				result[i]  = p ;
			}
		}
		return result ;
	}
	
	/**
	 * Compute and return the maximal distance from origin
	 */
	public double getMaxDistanceFromOrigin() {
		double maxDistance=0.;
		Point_2 origin=new Point_2(0., 0.);
		for(Point_2 p: this.points) {
			double distance=Math.sqrt(p.squareDistance(origin).doubleValue());
			maxDistance=Math.max(maxDistance, distance);
		}
		return maxDistance;
	}

}
