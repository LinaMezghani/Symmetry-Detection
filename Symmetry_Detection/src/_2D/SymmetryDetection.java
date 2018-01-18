package _2D ;


import Jcg.geometry.Point_2;
import Jcg.geometry.Vector_2;

import java.lang.Math;
import java.util.LinkedList;

import processing.core.PApplet;

public class SymmetryDetection {

	// Compute reflective plane (r, theta, phi spheric coordinates of the plane,
	// ie nearest point from the origin (0,0,0))
	// for two points p and q

	public static Point_2 computeReflectivePlane(Point_2 p, Point_2 q) {
		// p and q must be different
		// middle point

		Point_2 m = new Point_2((p.x + q.x) / 2, (p.y + q.y) / 2);

		// normal vector to the plane
		Vector_2 v = new Vector_2(q.x - p.x, q.y - p.y);
		Number l = Math.sqrt((double) v.squaredLength());
		Vector_2 n = v.divisionByScalar(l);

		// r, theta, phi
		double r = n.x * m.x + n.y * m.y;
		double theta = Math.atan2(r * n.y, r * n.x);
		return new Point_2(r, theta);
	}

	public static PointCloud computeAllPoints(PointCloud PC, double ratio) {
		PointCloud res = null;

		// Construct list of all points in PC
		LinkedList<Point_2> listPC = new LinkedList<Point_2>();
		for (PointCloud n = PC; n != null; n = n.next) {
			if (Math.random() < ratio) {
				listPC.add(n.p);
			}
		}

		for (int i = 0; i < listPC.size(); i++) {
			for (int j = i + 1; j < listPC.size(); j++) {
				Point_2 a = listPC.get(i);
				Point_2 b = listPC.get(j);

				// In case there are duplicates in the PointCloud
				if (!a.equals(b)) {
					Point_2 c = computeReflectivePlane(a, b);
					res = new PointCloud(new Point_2(c), res, true);
				}
			}
		}
		return res;
	}
	


	public static DrawPointSet computeSymmetry(PApplet pA ,String filename, double bandWidth, boolean display_N, double ratio) {
		
		PointCloud N = IO.pointsFromDataFile(filename);
		System.out.println("point cloud of size: " + PointCloud.size(N));
		
		// translate to the origin
		Point_2 centroid = PointCloud.mean(N);
		Point_2 zero = new Point_2(0, 0);
		Translation_2 translation = new Translation_2(new Vector_2(centroid,
				zero));
		PointCloud M = PointCloud.transformPointCloud(N, translation);

		PointSet N_ps_translated = M.toPointSet();

		System.out.print("Compute Transformation space..");
		PointCloud S = computeAllPoints(N, ratio);
		
		PointSet points ;
		if (display_N)
			points = N_ps_translated;
		else
			points = S.toPointSet() ;;
		System.out.println("done");
		
		System.out.print("MeanShift Clustering...");
		long startTime = System.currentTimeMillis();
		MeanShiftClustering msc = new MeanShiftClustering(S, bandWidth);
		LinkedList<Point_2[]> planes = new LinkedList<Point_2[]>();
		Point_2[] clusterCenters = msc.computeClusters();
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
		System.out.println("done") ;
		System.out.println("MeanShift execution time : "+elapsedTime/1000+" seconds");
		
		if (display_N){
			int nDetectedClusters = 0;
			for (int i = 0; i < clusterCenters.length; i++) {
				Point_2[] plane = new Point_2[2];
				if (clusterCenters[i] != null) {
					nDetectedClusters++;
					double r = (double) clusterCenters[i].getX();
					double theta = (double) clusterCenters[i].getY();

					double a = r * Math.cos(theta);
					double b = r * Math.sin(theta);

					Point_2 n = new Point_2(a, b);
					Point_2 p = new Point_2(-b, a);
					/*Point_2[] tab = {n,p};
					Number[] coeff = {1,0} ;*/
					
					/*for(int j =0 ; j< 10 ; j++){
						coeff[1] = j ;
						plane.add(Point_2.linearCombination(tab,coeff)) ;
						coeff[1] = -j ;
						plane.add(Point_2.linearCombination(tab,coeff)) ;
					}*/
					plane[0] = n; plane[1] = p;
					planes.add(plane);

				}
			}
			System.out.println("Number of clusters detected: " + nDetectedClusters);
			return new DrawPointSet(pA, points, planes, msc.clusters);
		}
		
		else return new DrawPointSet(pA, points, null, msc.clusters);
		
	}

}
