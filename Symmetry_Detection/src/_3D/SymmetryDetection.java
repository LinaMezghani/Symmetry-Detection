package _3D;

import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;

import java.lang.Math;
import java.util.LinkedList;

import processing.core.PApplet;

public class SymmetryDetection {

	Point_3 p;
	Point_3 q;

	public SymmetryDetection(Point_3 p, Point_3 q) {
		p = this.p;
		q = this.q;
	}

	// Compute reflective plane (r, theta, phi spheric coordinates of the plane,
	// ie nearest point from the origin (0,0,0))
	// for two points p and q

	public static Point_3 computeReflectivePlane(Point_3 p, Point_3 q) {
		// p and q must be different
		// middle point

		Point_3 m = new Point_3((p.x + q.x) / 2, (p.y + q.y) / 2,
				(p.z + q.z) / 2);

		// normal vector to the plane
		Vector_3 v = new Vector_3(q.x - p.x, q.y - p.y, q.z - p.z);
		Number l = Math.sqrt((double) v.squaredLength());
		Vector_3 n = v.divisionByScalar(l);

		// r, theta, phi
		double r = n.x * m.x + n.y * m.y + n.z * m.z;
		Point_3 ref = new Point_3(n.x * r, n.y * r, n.z * r);
		double phi = Math.acos(n.z);
		double theta = Math.atan2(ref.y, ref.x);

		return new Point_3(r, theta, phi);
	}

	public static PointCloud computeAllPoints(PointCloud PC, double ratio) {
		PointCloud res = null;

		// Construct list of all points in PC
		LinkedList<Point_3> listPC = new LinkedList<Point_3>();
		for (PointCloud n = PC; n != null; n = n.next) {
			if (Math.random() < ratio) {
				listPC.add(n.p);
			}
		}
		// loop

		for (int i = 0; i < listPC.size(); i++) {
			for (int j = i + 1; j < listPC.size(); j++) {

				Point_3 a = listPC.get(i);
				Point_3 b = listPC.get(j);

				// In case there are duplicates in the pointCloud
				if (!a.equals(b)) {
					Point_3 c = computeReflectivePlane(a, b);
					res = new PointCloud(new Point_3(c), res, true);
				}

			}
		}
		return res;
	}

	public static DrawPointSet computeSymmetry(PApplet pA, String filename,
			double bandWidth, boolean display_N, double ratio) {

		PointCloud N = IO.pointsFromDataFile(filename);
		System.out.println("point cloud of size: " + PointCloud.size(N));

		// translate to the origin
		Point_3 centroid = PointCloud.mean(N);
		Point_3 zero = new Point_3(0, 0, 0);
		Translation_3 translation = new Translation_3(new Vector_3(centroid,
				zero));
		N = PointCloud.transformPointCloud(N, translation);

		PointSet N_ps_translated = N.toPointSet();

		System.out.print("Compute Transformation space..");
		PointCloud S = computeAllPoints(N, ratio);

		PointSet points;
		if (display_N)
			points = N_ps_translated;
		else
			points = S.toPointSet();
		;
		System.out.println("done");

		// MeanShiftClustering
		System.out.print("MeanShift Clustering...");
		long startTime = System.currentTimeMillis();

		MeanShiftClustering msc = new MeanShiftClustering(S, bandWidth);
		LinkedList<PointSet> planes = new LinkedList<PointSet>();
		Point_3[] clusterCenters = msc.computeClusters();

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("done");
		System.out.println("MeanShift execution time : " + elapsedTime / 1000
				+ " seconds");

		if (display_N) {
			int nDetectedClusters = 0;
			for (int i = 0; i < clusterCenters.length; i++) {
				PointSet plane = new PointSet();
				if (clusterCenters[i] != null) {
					nDetectedClusters++ ;
					
					double r = (double) clusterCenters[i].getX();
					double theta = (double) clusterCenters[i].getY();
					double phi = (double) clusterCenters[i].getZ();

					// this.drawPointSet.drawPlane(r,theta,phi) ;

					double a = r * Math.sin(phi) * Math.cos(theta);
					double b = r * Math.sin(phi) * Math.sin(theta);
					double c = r * Math.cos(phi);

					Point_3 n = new Point_3(a, b, c);
					Point_3 p1 = new Point_3(-b, a, 0);
					Point_3 p2 = new Point_3(-c, 0, a);
					Point_3[] t1 = { n, p1 };
					Point_3[] t2 = { n, p2 };
					Number[] sum = { 1, 20 };
					Number[] minus = { 1, -20 };
					Point_3 point1 = Point_3.linearCombination(t1, sum);
					Point_3 point2 = Point_3.linearCombination(t1, minus);
					Point_3 point3 = Point_3.linearCombination(t2, sum);
					Point_3 point4 = Point_3.linearCombination(t2, minus);
					sum[1] = 30 ; minus[1]= -30 ;
					Point_3 point5 = Point_3.linearCombination(t1, sum);
					Point_3 point6 = Point_3.linearCombination(t1, minus);
					Point_3 point7 = Point_3.linearCombination(t2, sum);
					Point_3 point8 = Point_3.linearCombination(t2, minus);

					plane.add(n);
					plane.add(point1);
					plane.add(point2);
					plane.add(point3);
					plane.add(point4);
					plane.add(point5);
					plane.add(point6);
					plane.add(point7);
					plane.add(point8);
					planes.add(plane);

				}
			}
			System.out.println("Number of clusters detected: "
					+ nDetectedClusters);
			return new DrawPointSet(pA, points, planes, null);
		}

		else
			return new DrawPointSet(pA, points, null, msc.clusters);

	}

}
