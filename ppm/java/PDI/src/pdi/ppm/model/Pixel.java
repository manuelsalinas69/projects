package pdi.ppm.model;

public class Pixel {

	int R;
	int G;
	int B;
	double [] v=new double[3];
	public Pixel(int r, int g, int b) {
		super();
		R = r;
		G = g;
		B = b;
		v[0]=r;
		v[1]=g;
		v[2]=b;
		
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
	
	
}
