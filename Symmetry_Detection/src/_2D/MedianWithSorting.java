package _2D ;

import java.util.Arrays;

import Jcg.geometry.Point_2;
import Jcg.geometry.Point_2;

/**
 * Computation of the median (via array sorting)
 * 
 * @author Luca Castelli Aleardi (Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public class MedianWithSorting implements Median {

	private final PointCloud N; // input point cloud (original points should not be sorted)
	
	public MedianWithSorting(PointCloud points) {
		this.N=points;
	}
	
	/**
	 * Sort the input array and select the i-th element
	 * Remark: it takes O(nlog n) time
	 * 
	 * @param buffer array of input points
	 * @param index rank of the element to return
	 * @param cutDim indicates which are the coordinates to compare
	 * @return Point_2 the i-th smallest element
	 */    
	private Point_2 selectWithSorting (Point_2[] buffer, int index, int cutDim){
		//On trie suivant la direction de la coupe donn� par cutDim
		Arrays.sort(buffer,new CoordinateComparator<Point_2>(cutDim));
		return buffer[index];
	}

	/**
	 * Compute the median of a set of point cloud (after sorting)
	 * cutDim = {0,1,2} => {x,y,z}
	 * 
	 * Warning: input points should not be sorted (perform a copy of input points)
	 */    
	public Point_2 findMedian(int cutDim){
		int size = PointCloud.size(N);
		if(size==0) return null;
		if(size==1) return N.p;
		//On convertit le PointCloud en tableau (cf classe PointCloud modifi�e)
		Point_2[] buffer = PointCloud.copy(N, size);
		return selectWithSorting (buffer, size/2, cutDim);
	}

}
