package _2D ;

import java.awt.Color;
import java.awt.image.*;

import Jcg.geometry.Point_2;
import Jcg.geometry.Point_d;

/**
 * Class providing methods for conversion between point clouds and pixels
 * 
 * @author Luca Castelli Aleardi and Steve Oudot(Ecole Polytechnique, INF562)
 * @version jan 2014
 */
class ImageManipulation {

	public static PointCloud rasterToPointCloud (Raster r, int dimx, int dimy, int dim) {
		PointCloud res = null;
		for (int j=0; j<dimy; j++)
			for (int i=0; i<dimx; i++) {
				int[] pix = new int [3];
				r.getPixel(i, j, pix);
				//System.out.println("pix = [" + pix[0] + " " + pix[1] + " " 
				//+ pix[2] + "]");
				// convert pixel to L*u*v*
				float[] pixluv = new float [3];
				ImageManipulation.rgb2luv(pix[0], pix[1], pix[2], pixluv);
				double[] coord;
				if (dim == 3) {
					coord = new double [3];
					coord[0] = pixluv[0];
					coord[1] = pixluv[1];
					coord[2] = pixluv[2];
				}
				else if (dim == 5) {
					coord = new double [5];
					coord[0] = (double)i;
					coord[1] = (double)j;
					coord[2] = pixluv[0];
					coord[3] = pixluv[1];
					coord[4] = pixluv[2];
				}		   
				else
					throw new RuntimeException ("Bad dimension!");
				res = new PointCloud (new Point_2(new Point_d (coord)), res, true);
			}
		return res;
	}

	public static void PointCloudToRaster 
	(PointCloud n, Cluster<Point_2> cluster, Color[] cols, WritableRaster r, int dimx, int dimy, int dim) {

		// points should be taken out in reverse order
		for (int j=dimy-1; j>=0; j--)
			for (int i=dimx-1; i>=0; i--) {
				int[] pix;// n should not be null
				if (n == null)
					throw new RuntimeException ("Empty point cloud!");
				if (cluster.getClusterIndex(n.p) < 0) {
					//throw new RuntimeException ("Unclustered point!");
					System.out.println("Unclustered point");
					pix = new int[]{
							0,
							0,
							0	};
				}
				else {
					pix = new int[]{
							cols[cluster.getClusterIndex(n.p)].getRed(),
							cols[cluster.getClusterIndex(n.p)].getGreen(),
							cols[cluster.getClusterIndex(n.p)].getBlue()	};
				}	
				r.setPixel(i, j, pix);

				n = n.next;  // move to next point in cloud
			}
	}

	static void assignColors (int n, Point_d[] clusterCenters, Color[] cols) {
		// In this version, assign the center's color to each cluster
		for (int i=0; i<n && clusterCenters[i] != null; i++) {
			int dim = clusterCenters[i].dimension();
			float[] pixluv = {
					(float)clusterCenters[i].getCartesian(dim-3).doubleValue(),
					(float)clusterCenters[i].getCartesian(dim-2).doubleValue(),
					(float)clusterCenters[i].getCartesian(dim-1).doubleValue()};
			int[] pix = ImageManipulation.luv2rgb (
					pixluv[0], pixluv[1], pixluv[2]);
					cols[i] = new Color (pix[0], pix[1], pix[2]);
		}
	}

	static final float Xr = 0.964221f;  // reference white D50
	static final float Yr = 1.0f;
	static final float Zr = 0.825211f;

	private static void rgb2luv(int R, int G, int B, float[] luv) {

		//float rf, gf, bf;
		float r, g, b;
		//float X_, Y_, Z_;
		float X, Y, Z;
		//float fx, fy, fz, xr, zr;
		float yr;
		float L;
		float eps = 216.f/24389.f;
		float k = 24389.f/27.f;


		// RGB to XYZ

		r = R/255.f; //R 0..1
		g = G/255.f; //G 0..1
		b = B/255.f; //B 0..1

		// assuming sRGB (D65)
		if (r <= 0.04045)
			r = r/12;
		else
			r = (float) Math.pow((r+0.055)/1.055,2.4);

		if (g <= 0.04045)
			g = g/12;
		else
			g = (float) Math.pow((g+0.055)/1.055,2.4);

		if (b <= 0.04045)
			b = b/12;
		else
			b = (float) Math.pow((b+0.055)/1.055,2.4);


		X =  0.436052025f*r     + 0.385081593f*g + 0.143087414f *b;
		Y =  0.222491598f*r     + 0.71688606f *g + 0.060621486f *b;
		Z =  0.013929122f*r     + 0.097097002f*g + 0.71418547f  *b;

		// XYZ to Luv

		float u, v, u_, v_, ur_, vr_;

		u_ = 4*X / (X + 15*Y + 3*Z);
		v_ = 9*Y / (X + 15*Y + 3*Z);

		ur_ = 4*Xr / (Xr + 15*Yr + 3*Zr);
		vr_ = 9*Yr / (Xr + 15*Yr + 3*Zr);

		yr = Y/Yr;

		if ( yr > eps )
			L =  (float) (116*Math.pow(yr, 1/3.) - 16);
		else
			L = k * yr;

		u = 13*L*(u_ -ur_);
		v = 13*L*(v_ -vr_);

		luv[0] = L;
		luv[1] = u; 
		luv[2] = v;        


		// check result
		// 	int[] back = luv2rgb (luv[0], luv[1], luv[2]);
		// 	System.out.println ("R = " + R + " = " + back[0]);
		// 	System.out.println ("G = " + G + " = " + back[1]);
		// 	System.out.println ("B = " + B + " = " + back[2]);

	} 


	private static int[] luv2rgb (float L, float u, float v) {

		// First convert from Luv to XYZ

		float kappa = 24389f / 27f;
		float eps = 216f / 24389f;

		float u0 = 4*Xr / (Xr + 15*Yr + 3*Zr);
		float v0 = 9*Yr / (Xr + 15*Yr + 3*Zr);

		float Y = L / kappa;
		if (L > kappa*eps)
			Y = (float)Math.pow ((L + 16)/116, 3);

		float a = 1f/3f * (52*L / (u + 13*L*u0) -1);
		float bb = -5 * Y;
		float c = - 1f / 3f;
		float d = Y * (39*L / (v + 13*L*v0) - 5);

		float X = (d - bb) / (a - c);
		float Z = a*X + bb;

		// Now convert from XYZ to RGB -- assuming sRGB (D65)

		float r = 3.1340513f*X - 1.6170277f*Y - 0.49065221f*Z;
		float g = -0.97876273f*X + 1.9161422f*Y + 0.033449629f*Z; 
		float b = 0.071942577f*X - 0.22897118f*Y + 1.4052183f*Z;  

		float R;
		if (r <= 0.0031308f)
			R = 12.92f*r;
		else
			R = (float)Math.pow(1.055f*r, 1f/2.4f) - 0.055f;
		float G;
		if (g <= 0.0031308f)
			G = 12.92f*g;
		else
			G = (float)Math.pow(1.055f*g, 1f/2.4f) - 0.055f;
		float B;
		if (b <= 0.0031308f)
			B = 12.92f*b;
		else
			B = (float)Math.pow(1.055f*b, 1f/2.4f) - 0.055f;

		int[] res = {Math.min(255, (int)Math.ceil(R*263)), 
				Math.min(255, (int)Math.ceil(G*263)), 
				Math.min(255, (int)Math.ceil(B*263))};
		// 	int[] res = {(int)Math.ceil(R*255), 
		// 		     (int)Math.ceil(G*255), 
		// 		     (int)Math.ceil(B*255)};
		return res;
	}

}
