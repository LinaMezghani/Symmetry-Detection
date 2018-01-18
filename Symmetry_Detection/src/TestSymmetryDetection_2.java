
import processing.core.*;

import _2D.*;


/**
 * A simple 3d viewer for visualizing point clouds (based on Processing). It
 * deals with methods for
 * 
 * @author Luca Castelli Aleardi (INF555, 2016)
 *
 */
public class TestSymmetryDetection_2 extends PApplet {

	PointSet points; // input point cloud
	int renderType = 0; // choice of type of rendering
	int renderModes = 3; // number of rendering modes
	DrawPointSet drawPointSet;

	public void setup() {
		ArcBall arcball = new ArcBall(this);
		size(1000, 800, P3D);
		
		String filename = "InputDataImages/3D_nefertiti.dat" ;
		double bandWidth = 1.5 ; //bandWidth for MeanShiftClustering
		boolean display_N = true ; //true to display the original point cloud, false for the transformative space
		double ratio = 0.5 ; //ratio of point cloud
		
		this.drawPointSet = SymmetryDetection.computeSymmetry(this,filename,bandWidth,display_N,ratio) ;
		
	}

	public void draw() {
		background(10);
		directionalLight(101, 204, 255, -1, 0, 0);
		directionalLight(51, 102, 126, 0, -1, 0);
		directionalLight(51, 102, 126, 0, 0, -1);
		directionalLight(102, 50, 126, 1, 0, 0);
		directionalLight(51, 50, 102, 0, 1, 0);
		directionalLight(51, 50, 102, 0, 0, 1);

		translate(width / 2.f, height / 2.f, -1 * height / 2.f);
		this.strokeWeight(1);
		stroke(150, 150, 150);

		this.drawPointSet.draw(renderType);
		this.drawOptions();
	}

	public void keyPressed() {
		switch (key) {
		case ('z'):
		case ('Z'):
			this.zoomIn();
			break;
		case ('x'):
		case ('X'):
			this.zoomOut();
			break;
		case ('s'):
		case ('S'):
			this.savePointCloud();
			break;
		}
	}

	public void drawOptions() {
		int hF = 12;
		fill(255);
		this.textMode(TestSymmetryDetection_2.SCREEN);
		this.text("'z' or 'x' for zooming", 10, hF * 3);
	}

	public void zoomIn() {
		this.drawPointSet.zoom = this.drawPointSet.zoom * 1.5;
	}

	public void zoomOut() {
		this.drawPointSet.zoom = this.drawPointSet.zoom * 0.75;
	}

	public void savePointCloud() {
		if (points != null)
			points.toFile("output.off");
	}
	
	/**
	 * For running the PApplet as Java application
	 */
	public static void main(String args[]) {
		PApplet.main(new String[] { "PointCloudViewer" });
	}

}
