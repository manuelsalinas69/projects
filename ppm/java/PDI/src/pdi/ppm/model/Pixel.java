package pdi.ppm.model;

import pdi.ppm.conf.PPMConstanst;

public class Pixel {

	public int R;
	public int G;
	public int B;
	double [] v=new double[3];
	public Pixel(int r, int g, int b) {
		super();
		R = r;
		G = g;
		B = b;
		initVector(r, g, b);
		
	}
	
	private void initVector(int r, int g, int b){
		v[0]=r;
		v[1]=g;
		v[2]=b;
	}
	
	public Pixel(double[] vector) {
		super();
		R = (int) vector[0];
		R= Math.min(255, Math.max(0, R));
		G = (int) vector[1];
		G= Math.min(255, Math.max(0, G));
		B = (int) vector[2];
		B= Math.min(255, Math.max(0, B));
		initVector(R, G, B);
		
		
	}
	
	public double[] toVector(){
		return v;
	}
	
	public void printPixelValues(){
		System.out.println("[R=" + R + ", G=" + G + ", B=" + B + "]");
	}

	@Override
	public String toString() {
		return "Pixel [R=" + R + ", G=" + G + ", B=" + B + "]";
	}
	
	public long volume(){
		return G*R*B;
	}
	
	public Pixel minus(Pixel other){
		int R=(this.R+PPMConstanst.FACTOR_REALCE)-(other.R-PPMConstanst.FACTOR_REALCE);
		int G=(this.G+PPMConstanst.FACTOR_REALCE)-(other.G-PPMConstanst.FACTOR_REALCE);
		int B=(this.B+PPMConstanst.FACTOR_REALCE)-(other.B-PPMConstanst.FACTOR_REALCE);
		
		return new Pixel(R, G, B);
	}
	
	
}
