package _2D ;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;

import javax.imageio.ImageIO;

import Jcg.geometry.Point_2;
import Jcg.geometry.Point_d;

/**
 * Class providing methods for reading data (point clouds, images, ...) from file
 * 
 * @author Luca Castelli Aleardi and Steve Oudot(Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public class IO {

    /**
     * Load point clouds from file
     * 
     * @param filename the input file (text file storing point coordinates)
     * @return PointCloud the points loaded from file
     */
    public static PointCloud pointsFromDataFile (String fileName) {
    	System.out.print("Loading point cloud from file "+fileName+" ...");
    	PointCloud N = null;
    	try { 
    		BufferedReader in = new BufferedReader(new FileReader(fileName));
    		String s = null;
    		
    		int dim = -1;
    		
    		// Get dimension of point cloud
    		s = in.readLine();
    		if (s == null) {
    			in.close();
    			return N;
    		}
    		
    		// pass comments in data file
    		while (s.charAt(0) == '/')
    			s = in.readLine();
    		
    		int i = 0;
    		while ( s.charAt(i) == ' ' )
    			++i;
    		// i points to first digit of number
    		dim = (int)Double.parseDouble (s.substring (i));
    		
    		// Get the points, one after the other
    		double[] coords = new double [dim]; 
    		while ( (s = in.readLine()) != null ) {
    			i=0;
    			for (int d=0; d<dim-1; d++) {
    				while ( s.charAt(i) == ' ' )
    					++i;
    				// i points to first digit of coordinate
    				int j = i+1;
    				while ( s.charAt(j) != ' ')
    					++j;
    				// j points to space after coordinate
    				coords[d] = Double.parseDouble(s.substring(i, j));
    				i = j+1;
    			}
    			// Get last coordinate
    			while ( s.charAt(i) == ' ' )
    				++i;
    			// i points to first digit of last coordinate
    			coords[dim-1] = Double.parseDouble(s.substring(i));
    			// Append new point to point cloud
    			N = new PointCloud (new Point_2(new Point_d (coords)), N, true);  // copy point
    			}
    			in.close();
    		} 	catch (Exception e) {  
    				e.printStackTrace();  
    			}
    	System.out.println(" done");
    	return N;
    }
 
    /**
     * Load point clouds from image file
     * 
     * @param filename the input image (pixels are mapped to 3D points, in LUV space)
     * @return PointCloud the points corresponding to pixels in the input image
     */
    public static PointCloud pointsFromImage(String imageFile) {
		PointCloud N; // result
		BufferedImage bimg = IO.loadImage(imageFile); // load input image
		int dimx = bimg.getWidth();
		int dimy = bimg.getHeight();
		
		WritableRaster raster = bimg.getRaster(); // get array of pixels from image, in L*u*v* space
		//System.out.println("Pixels array created from image");

		int dim = 3;
		N = ImageManipulation.rasterToPointCloud (raster, dimx, dimy, dim); // build point cloud from raster
		return N;
    }

    /**
     * Load image from file
     * 
     * @param filename the input image file
     * @return BufferedImage the loaded image
     */
	public static BufferedImage loadImage(String fileName) {  
		BufferedImage bimg = null;  
		try {  
			bimg = ImageIO.read(new File(fileName));  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return bimg;  
	}  


}
