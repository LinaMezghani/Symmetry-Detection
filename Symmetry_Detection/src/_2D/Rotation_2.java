package _2D ;

import Jcg.geometry.*;

import Jama.Matrix;

/**
 * Define a 2D Rotation (using Euler angles)
 *
 * @author Luca Castelli Aleardi (INF555, 2014)
 */
public class Rotation_2 {

	Matrix m;
	
	/**
	 * The identity transformation
	 */
	public Rotation_2(Matrix m) {
		this.m=m;
	}

	/**
	 * The identity rotation
	 */
	public Rotation_2() {
		double[][] array = {{1.,0.},
							{0.,1.}}; 
		this.m=new Matrix(array);
	}

	/**
	 * Return a rotation of an angle theta, around X axis
	 */
	public static Rotation_2 rotationAxisX(double theta) {
		double[][] array = {
				{1.,0.,0.},
				{0.,Math.cos(theta),-Math.sin(theta)},
				{0.,Math.sin(theta),Math.cos(theta)}}; 
		
		return new Rotation_2(new Matrix(array));
	}

	/**
	 * Return a rotation of an angle theta, around Y axis
	 */
	public static Rotation_2 rotationAxisY(double theta) {
		throw new Error("to be completed: TD1");
	}

	/**
	 * Return a rotation of an angle theta, around Z axis
	 */
	public static Rotation_2 rotationAxisZ(double theta) {
		throw new Error("to be completed: TD1");
	}

	/**
	 * Rotate point p
	 */
	public Point_2 transform(Point_2 p) {
		double x=p.getCartesian(0).doubleValue();
		double y=p.getCartesian(1).doubleValue();
		double[][] array = {{x}, {y}}; 
		Matrix v=new Matrix(array); // the vector
		
		Matrix result=this.m.times(v);
		double[] coord={result.get(0, 0), result.get(1, 0)};
		return new Point_2(coord[0], coord[1]);
	}

	/**
	 * Compose two rotations
	 */
	public Rotation_2 compose(Rotation_2 t) {
		Matrix M=t.m; 
		
		Matrix composition=this.m.times(M);
		return new Rotation_2(composition);
	}
	
}
