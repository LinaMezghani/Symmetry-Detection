import _2D.ArcBall;
import _3D.* ;
import processing.core.*;
import Jcg.geometry.*;

/**
 * A simple 3d viewer for visualizing point clouds (based on Processing).
 * 
 * @author Luca Castelli Aleardi (INF555, 2016)
 *
 */
public class TestSymmetryDetection_3 extends PApplet {

	PointSet points; // input point cloud
	int renderType = 0; // choice of type of rendering
	int renderModes = 3; // number of rendering modes
	DrawPointSet drawPointSet;

	Rotation_3 rotation = new Rotation_3(); // identity rotation by default
	Translation_3 translation = new Translation_3(new Vector_3(0., 0., 0.)); // no
																				// translation
	public static String filename;// input point clouds

	public void setup() {
		size(1000, 800, P3D);
		ArcBall arcball = new ArcBall(this);
		
		String filename = "InputDataImages/3D_nefertiti.dat" ;
		double bandWidth = 3.0 ;
		boolean display_N = true ; //
		double ratio = 0.6 ;
		
		this.drawPointSet = SymmetryDetection.computeSymmetry(this,filename,bandWidth,display_N,ratio) ;
	}

	public void draw() {
		background(10);
		// this.lights();
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
			this.savePointCloud() ;
			break ;
		}
	}

	public void drawOptions() {
		int hF = 12;
		fill(255);
		this.textMode(TestSymmetryDetection_3.SCREEN);
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
