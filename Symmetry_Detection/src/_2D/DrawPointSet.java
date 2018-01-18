package _2D ;

import java.awt.Color;
import java.util.LinkedList;

import Jcg.geometry.*;
import processing.core.PApplet;

/**
 * Class for rendering a surface triangle mesh (using Processing)
 * 
 * @author Luca Castelli Aleardi (INF555, 2012)
 *
 */
public class DrawPointSet {
	
	PApplet view; // Processing 3d frame (where meshes are rendered)
	public PointSet points ;
	public LinkedList<Point_2[]> planes ; // input point clouds
	public Cluster<Point_2> clusters ;
	
	// rendering parameters
	double scaleFactor=60; // scaling factor: useful for 3d rendering
	public double zoom=1.; // for scaling the drawing
	public double normalScale=1.; // for scaling the normal vectors
	
	/**
	 * Create a surface mesh from an OFF file
	 */	
	public DrawPointSet(PApplet view, PointSet points, LinkedList<Point_2[]> planes,Cluster<Point_2> clusters ) {
		this.view=view;
   	    this.points=points;
   	    this.planes=planes;
    	this.scaleFactor=this.computeScaleFactor();
    	this.clusters = clusters ;
	}
	
	/**
	 * Draw a point (as a small sphere)
	 */	
	public void drawPoint(Point_2 p) {
		float s=(float)this.scaleFactor*(float)this.zoom;
		float x1=(float)p.getX().doubleValue()*s;
		float y1=(float)p.getY().doubleValue()*s;
		
		view.translate(x1, y1);
		view.sphere(s/100f);
		view.translate(-x1, -y1);
	}

	/**
	 * Draw a (colored) segment between two points
	 */	
	public void drawSegment(Point_2 p, Point_2 q) {
		float s=(float)this.scaleFactor*(float)this.zoom;
		float x1=(float)p.getX().doubleValue()*s;
		float y1=(float)p.getY().doubleValue()*s;
		
		float x2=(float)q.getX().doubleValue()*s;
		float y2=(float)q.getY().doubleValue()*s;
		
		this.view.stroke(125f,125f,125f);
		this.view.line(	x1, y1, x2, y2);		
	}
	
	public float[] computeRandomColor(){
		return new float[]{(float)Math.random()*250,(float)Math.random()*250,(float)Math.random()*250} ;
	}

	/**
	 * Draw the entire point cloud
	 */
	public void draw(int type) {
		
		view.noStroke();
		
		if(points!=null) {
			for(Point_2 p: this.points.listOfPoints()) {
				view.fill(0f, 0f, 250f); // points in the point cloud are drawn in blue by default
				this.drawPoint(p);
			}

		}
		
		if (this.planes!= null){
			Number[] coeff = {1,100} ;
			Number[] coeff_2 = {1,-100} ;
			int num_planes = this.planes.size();
			for(int i = 0 ; i<num_planes ; i++){
				drawSegment(Point_2.linearCombination(this.planes.get(i), coeff),Point_2.linearCombination(this.planes.get(i), coeff_2)) ;
			}
			
		}
		
		else if(this.clusters!=null){
			float[][] colors = {{0f,0f,250f},{0f, 250f, 0f},{250f, 0f, 0f},{125f,125f,125f},{0f,0f,250f}} ;
			for(Point_2 p : this.points.listOfPoints()){
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
		Point_2 origin=new Point_2(0., 0.);
		for(Point_2 p: this.points.listOfPoints()) {
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
