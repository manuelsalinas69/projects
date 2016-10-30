package pdi.ppm.model;

import java.util.Arrays;

public class ReferenceVector {
	
	double[][] rMin;
	double[][] rMax;
	public ReferenceVector(double[][] rMin, double[][] rMax) {
		super();
		this.rMin = rMin;
		this.rMax = rMax;
	}
	
	public double[][] vectorMaxMin(){
		double[][] v={
						{
							rMax[0][0]-rMin[0][0],
							rMax[0][1]-rMin[0][1],
							rMax[0][2]-rMin[0][2]
						}
				};
		return v;
	} 

	public double[][] getrMin() {
		return rMin;
	}

	public void setrMin(double[][] rMin) {
		this.rMin = rMin;
	}

	public double[][] getrMax() {
		return rMax;
	}

	public void setrMax(double[][] rMax) {
		this.rMax = rMax;
	}

	@Override
	public String toString() {
		return "ReferenceVector [rMin=" + Arrays.toString(rMin[0]) + ", rMax=" + Arrays.toString(rMax[0]) + "]";
	}
	
	
	
	
	
	

}
