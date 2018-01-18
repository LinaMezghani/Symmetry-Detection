package _3D;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import Jcg.geometry.*;
import Jcg.viewer.old.Fenetre;
import Jcg.viewer.j3d.*;

/**
 * Methods for visualizing (colored and/or clustered) point clouds in 2D and 3D
 * 
 * @author Luca Castelli Aleardi and Steve Oudot(Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public class Draw {

	/**
	 * Draw a point cloud in a 2D frame (using x and y coordinates)
	 * 
	 * @param N the input points
	 */
    public static void draw2D(PointCloud N, String title) {
    	/*System.out.println("Drawing point cloud in 2D... ");
    	if(N==null) {
    		System.out.println("No point to draw");
    		return;
    	}
    	if(N.p.dimension()<2) {
    		System.out.println("error: wrong point dimension "+N.p.dimension());
    		return;
    	}
    	if(N.p.dimension()!=2) {
    		System.out.println("warning: wrong point dimension "+N.p.dimension());
    	}
    	
    	List<Point_2> points2D=new ArrayList<Point_2>();
    	PointCloud t=N;
    	while(t!=null) {
    		points2D.add(new Point_2(t.p));
    		t=t.next;
    	}
    	Fenetre f=new Fenetre(title);
    	f.addPoints(points2D);
    	
    	double[] box=PointCloud.boundingBox(N);
    	int dim=N.p.dimension();
    	Point_2 p00=new Point_2(box[0], box[1]);
    	Point_2 p10=new Point_2(box[dim], box[1]);
    	Point_2 p01=new Point_2(box[0], box[dim+1]);
    	Point_2 p11=new Point_2(box[dim], box[dim+1]);
    	f.addSegment(p00, p10);
    	f.addSegment(p00, p01);
    	f.addSegment(p10, p11);
    	f.addSegment(p01, p11);
    	//System.out.println("2D drawing done");
*/    }

	/**
	 * Draw a point cloud in a 3D frame (with no colors)
	 * 
	 * @param N the input points
	 */
    public static void draw3DNColors(PointCloud N) {
    	/*System.out.print("Drawing point cloud in 3D...");
    	if(N==null) {
    		System.out.println("No point to draw");
    		return;
    	}
    	if(N.p.dimension()<3) {
    		System.out.println("warning: wrong point dimension "+N.p.dimension());
    		//return;
    	}
    	
    	List<Point_3> points3D=new ArrayList<Point_3>();
    	PointCloud t=N;
    	while(t!=null) {
    		Point_3 newPoint=null;
    		if(N.p.dimension()==3)
    			newPoint=new Point_3(t.p);
    		else
    			newPoint=new Point_3(t.p.getCartesian(0).doubleValue(), t.p.getCartesian(1).doubleValue(), 0.);
    		points3D.add(newPoint);
    		t=t.next;
    	}
    	new MeshViewer(points3D);
    	System.out.println("done");*/
    }

	/**
	 * Draw a point cloud in a 3D frame (with no colors)
	 * 
	 * @param N the input points
	 */
    public static void draw3D(PointCloud N) {
    	int sizeCloud=PointCloud.size(N);
    	Color[] colors=new Color[sizeCloud];
    	
    	PointCloud t=N;
    	int i=0;
    	while(t!=null) {
    		colors[i]=Color.red; // every point has color red
    		t=t.next;
    		i++;
    	}
    	
    	draw3D(N, colors);
    }
    
	/**
	 * Draw a point cloud in 3D with colors.
	 * Points in the same cluster have the same color (colors are assigned at random). 
	 * 
	 * @param N the input points
	 * @param cluster defines how points are partitioned into clusters
	 */
    /*public static void draw3D(PointCloud N, Cluster<Point_d> cluster) {
    	int sizeCloud=PointCloud.size(N);
    	Color[] colors=new Color[sizeCloud]; // one color for each point in the cloud
    	Color[] randomColors=new Color[sizeCloud]; // one color for each cluster
    	
    	computeRandomColors(sizeCloud, randomColors); // compute colors at random
    	
    	PointCloud t=N;
    	int i=0;
    	while(t!=null) { // assign a color to each point
    		if(cluster.getClusterIndex(t.p)==-1) // point t.p is not classified
    			colors[i]=Color.red;
    		else
    			colors[i]=randomColors[cluster.getClusterIndex(t.p)];
    		t=t.next;
    		i++;
    	}
    	
    	draw3D(N, colors);
    }*/

    public static void draw3D(PointCloud N, Color[] colors) {
    	/*//System.out.println("Drawing point cloud in 3D...");
    	if(N==null || colors==null) {
    		System.out.println("No points (or colors) to draw");
    		return;
    	}
    	if(N.p.dimension()<3) {
    		System.out.println("Warning: drawing in dimension 3 a cloud of points of dimension "+N.p.dimension());
    		//return;
    	}
    	
    	List<Point_3> points3D=new ArrayList<Point_3>();
    	PointCloud t=N;
    	while(t!=null) {
    		Point_3 newPoint=null;
    		if(N.p.dimension()==3)
    			newPoint=new Point_3(t.p);
    		else
    			newPoint=new Point_3(t.p.getCartesian(0).doubleValue(), t.p.getCartesian(1).doubleValue(), 0.);
    		points3D.add(newPoint);
    		t=t.next;
    	}
    	if(colors==null || points3D.size()!=colors.length) {
    		System.out.println("error: colors null");
    		return;
    	}
    	new MeshViewer(points3D, colors);
    	//System.out.println("3D drawing done");
*/    }
    
    private static void computeRandomColors (int n, Color[] cols) {
	// In this basic version, assign random colors
	for (int i=0; i<n; i++)
	    cols[i] = new Color (
	    			(float)Math.random(), 
	    			(float)Math.random(), 
	    			(float)Math.random());
    }

}
