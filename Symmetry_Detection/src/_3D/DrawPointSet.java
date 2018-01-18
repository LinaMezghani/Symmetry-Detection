package _3D;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import Jcg.geometry.*;
import processing.core.PApplet;

/**
 * Class for rendering a surface triangle mesh (using Processing)
 * 
 * @author Luca Castelli Aleardi (INF555, 2012)
 *
 */
public class DrawPointSet {
	
	public PApplet view; // Processing 3d frame (where meshes are rendered)
	public PointSet points ;
	public LinkedList<PointSet> planes; // input point clouds
	public Cluster<Point_3> clusters ;
	public double[][] normals=null; // normals are not defined at the beginning (they must be computed)
	public boolean[] outliers=null; // outliers have to be computed
	
	// rendering parameters
	double scaleFactor=60; // scaling factor: useful for 3d rendering
	public double zoom=1.; // for scaling the drawing
	public double normalScale=1.; // for scaling the normal vectors
	
	/**
	 * Create a surface mesh from an OFF file
	 */	
	public DrawPointSet(PApplet view, PointSet points, LinkedList<PointSet> planes,Cluster<Point_3> clusters ) {
		this.view=view;
   	    this.points=points;
   	    this.planes=planes;
    	this.scaleFactor=this.computeScaleFactor();
    	this.clusters = clusters ;
	}
	
	/**
	 * Draw a point (as a small sphere)
	 */	
	public void drawPoint(Point_3 p) {
		float s=(float)this.scaleFactor*(float)this.zoom;
		float x1=(float)p.getX().doubleValue()*s;
		float y1=(float)p.getY().doubleValue()*s;
		float z1=(float)p.getZ().doubleValue()*s;
		
		view.translate(x1, y1, z1);
		view.sphere(s/100f);
		view.translate(-x1, -y1, -z1);
	}
	
	/**
	 * Draw the vertex normal
	 */	
	public void drawNormal(Point_3 p, double[] n) {
		double norm=Math.sqrt(n[0]*n[0]+n[1]*n[1]+n[2]*n[2]);
		double factor=normalScale*(this.scaleFactor/2000f)/norm;
		
		//System.out.println("vertex normal "+p);
		float x1=(float)p.getX().doubleValue();
		float y1=(float)p.getY().doubleValue();
		float z1=(float)p.getZ().doubleValue();
		float x2=(float)(n[0]*factor);
		float y2=(float)(n[1]*factor);
		float z2=(float)(n[2]*factor);
		
		this.drawSegment(p, new Point_3(x1+x2, y1+y2, z1+z2), new float[]{0f, 255f, 0f, 255f});
		this.drawSegment(p, new Point_3(x1-x2, y1-y2, z1-z2), new float[]{0f, 255f, 0f, 255f});
	}

	/**
	 * Draw a (colored) segment between two points
	 */	
	public void drawSegment(Point_3 p, Point_3 q, float[] color) {
		float s=(float)this.scaleFactor*(float)this.zoom;
		float x1=(float)p.getX().doubleValue()*s;
		float y1=(float)p.getY().doubleValue()*s;
		float z1=(float)p.getZ().doubleValue()*s;
		float x2=(float)q.getX().doubleValue()*s;
		float y2=(float)q.getY().doubleValue()*s;
		float z2=(float)q.getZ().doubleValue()*s;
		
		this.view.stroke(color[0], color[1], color[2], color[3]);
		this.view.line(	x1, y1, z1, x2, y2, z2 );		
	}

	/**
	 * Draw the entire point cloud
	 */
	public void draw(int type) {
		//this.drawAxis();
		
		view.noStroke();
		
		if(points!=null) {
			int i=0;
			for(Point_3 p: this.points.listOfPoints()) {
				
				view.fill(0f, 0f, 250f); // points in the first point cloud are drawn in blue by default
				this.drawPoint(p);
				i++;
			}

		}
		
		if(this.planes!=null) {
			int red = 250 ;
			
			int num_planes = this.planes.size();
			for(int i = 0 ; i<num_planes ; i++){
				view.fill(red, 0f, 0f); //red
				PointSet ps = this.planes.get(i);
				for(Point_3 p: ps.listOfPoints()) {
					this.drawPoint(p);
				}
				red = 250 - 250*(i+1)/num_planes ;
			}
			
		}
		
		if(this.clusters!=null){
			float[][] colors = {{0f,0f,250f},{0f, 250f, 0f},{250f, 0f, 0f}} ;
			for(Point_3 p : this.points.listOfPoints()){
				int i = clusters.getClusterIndex(p) ;
				view.fill(colors[i+1][0],colors[i+1][1],colors[i+1][2]) ;
				this.drawPoint(p);
			}
		}
		view.strokeWeight(1);
	}
	
	/**
	 * Compute the scale factor (depending on the maximal distance from the origin)
	 */
	public double computeScaleFactor() {
		if(this.points==null || this.points.size()<1)
			return 1;
		double maxDistance=0.;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Point_3 p: this.points.listOfPoints()) {
			double distance=Math.sqrt(p.squareDistance(origin).doubleValue());
			maxDistance=Math.max(maxDistance, distance);
		}
		return Math.sqrt(3)/maxDistance*150;
	}
	
	/**
	 * Update the scale factor
	 */
	public void updateScaleFactor() {
		this.scaleFactor=this.computeScaleFactor();
	}
	
}
