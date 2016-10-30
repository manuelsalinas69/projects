package pdi.ppm.model;

import java.util.Arrays;

public class Extremos {
	
	double[][] exMin;
	double[][] exMax;
	public Extremos(double[][] exMin, double[][] exMax) {
		super();
		this.exMin = exMin;
		this.exMax = exMax;
	}
	public double[][] getExMin() {
		return exMin;
	}
	public double[][] getExMax() {
		return exMax;
	}
	@Override
	public String toString() {
		return "Extremos [exMin=" + Arrays.toString(exMin[0]) + ", exMax=" + Arrays.toString(exMax[0]) + "]";
	}
	
	

}
