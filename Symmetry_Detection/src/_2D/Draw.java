package _2D ;

import processing.core.*;

import java.util.*;

import Jcg.geometry.Point_2;

/**
 * A simple 2D editor for handling curves and mouse events
 *
 * @author Luca Castelli Aleardi (INF555, 2014)
 */
public class Draw extends PApplet {
	
	final static double epsilon=10.;
	public PointCloud points ;
	public LinkedList<PointSet> planes; // input point clouds
	public Cluster<Point_2> clusters ;
	
	public Point_2 selectedPoint=null; // point selected with mouse events

	  public void setup() {
		  size(400,400); // size of the window
	      this.points=null; // input points
	      //setInterpolationMethod();
	  }
	  
	  /*public void setInterpolationMethod() {		  
	      if(curveMode%curveTypes==0) {
	    	  this.scheme=new NoInterpolation(this);
	    	  System.out.println("No interpolation");
	      }
	      else if(curveMode%curveTypes==1) {
	    	  this.scheme=new LinearInterpolation(this);
	    	  System.out.println("Linear interpolation");
	      }
	      else if(curveMode%curveTypes==2) {
	    	  this.scheme=new LagrangeInterpolation(this);
	    	  System.out.println("Lagrange interpolation");
	      }
	      else if(curveMode%curveTypes==3) {
	    	  this.scheme=new CubicSplineInterpolation(this);
	    	  System.out.println("Cubic spline interpolation");
	      }
	      else if(curveMode%curveTypes==4) {
	    	  this.scheme=new HermiteSplineInterpolation(this);
	    	  System.out.println("Hermite Spline interpolation");
	      }
	      else {
	    	  this.scheme=new NoInterpolation(this);
	    	  System.out.println("No interpolation");
	      }
	      this.stroke(0, 0, 0);
	  }*/

	  /*public void applyTransformation(int i) {		  
	      if(i==0) {
	    	  this.transformation=new Transformation_2(new Vector_2(2., 0.));
	    	  System.out.println("translation");
	      }
	      else if(i==1) {
	    	  double angle=Math.PI/4;
			  Point_2 barycenter=Point_2.barycenter(points);
			  this.transformation=new Transformation_2(angle, barycenter);
			  //this.transformation=new Transformation_2(angle);
			  System.out.println("rotation");
	      }
	      else if(i==2) {
	    	  this.transformation=new Transformation_2(1.2, 1.2);
	    	  System.out.println("scaling in");
	      }
	      else if(i==3) {
	    	  this.transformation=new Transformation_2(0.8, 0.8);
	    	  System.out.println("scaling in");
	      }
	      else {
	    	  this.transformation=new Transformation_2();
	      }
	      
	      for(Point_2 p: this.points) { // iterate over all points
	    	  Point_2 q; // the transformed point
	    	  q=this.transformation.transform(p);
	    	  p.setX(q.getX());
	    	  p.setY(q.getY());
	      }
	  }*/

	  public void draw() {
	    background(200);
	    
	    if(points==null) return; // no points to interpolate
	    //scheme.interpolate();
	  }

	  /*public void removePoint(int x, int y) {
		  int index=findPoint(x, y);
		  if(index>=0 && index<this.points.size())
			  this.points.remove(index);
	  }*/
	  
	 /*public int findPoint(int x, int y) {
		  Point_2 p=new Point_2(x, y);
		  
		  int index=0;
		  boolean found=false;
		  for(Point_2 q:this.points) {
			  if(q.squareDistance(p)<epsilon) {
				  found=true;
				  break;
			  }
			  index++;
		  }
		  if(found==true)
			  return index;
		  else
			  return -1;
	  }*/

	  public Point_2 selectPoint(int x, int y) {
		  Point_2 p=new Point_2(x, y);
		  
		  for(PointCloud n = this.points ; n!=null;n = n.next) {
			  if(n.p.squareDistance(p).doubleValue()<epsilon) {
				  return n.p ;
			  }
		  }
		  return null ;
	  }

	  public void mouseClicked() {
		  Point_2 p=new Point_2(mouseX, mouseY);
		  
		  if(mouseButton==LEFT && this.selectedPoint==null){
			  this.points = new PointCloud(p,this.points, true);
			  this.ellipse(mouseX, mouseY, 5, 5);
		  }
		  /*else if(mouseButton==RIGHT)
			  removePoint(mouseX, mouseY);*/
	  }

	  public void mousePressed() {
		  this.selectedPoint=selectPoint(mouseX, mouseY);
	  }
	  
	  public void mouseReleased() {
		  
		  if(this.selectedPoint!=null) {
			  this.selectedPoint.setX((double) mouseX);
			  this.selectedPoint.setY((double) mouseY);
		  }
	  }
	  
	  public void mouseDragged() {
	  }
	  
	  public void keyPressed(){
		  switch(key) {
		  	case('s'):case('S'): {  } break;
		  }
	  }

	  /*public void removeLastPoint() {
		  if(this.points==null || this.points.size()==0)
			  return;
		  this.points.removeLast();
		  this.setInterpolationMethod();
	  }*/

}