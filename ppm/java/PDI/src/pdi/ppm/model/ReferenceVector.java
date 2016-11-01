package pdi.ppm.model;

import java.util.Arrays;

public class ReferenceVector {
	
	int[][] rMin;
	int[][] rMax;
	public ReferenceVector(int[][] rMin, int[][] rMax) {
		super();
		this.rMin = rMin;
		this.rMax = rMax;
	}
	
	public ReferenceVector(double[][] vecAlfa, double[][] vecBeta) {
		rMin=new int[1][vecAlfa[0].length];
		rMax=new int[1][vecBeta[0].length];
		for (int i = 0; i < vecAlfa[0].length; i++) {
			rMin[0][i]=(int)vecAlfa[0][i];
			rMax[0][i]=(int)vecBeta[0][i];
		}
	}

	public int[][] vectorMaxMin(){
		int[][] v={
						{
							rMax[0][0]-rMin[0][0],
							rMax[0][1]-rMin[0][1],
							rMax[0][2]-rMin[0][2]
						}
				};
		return v;
	} 
	
	public double[][] vectorMaxMinD(){
		double[][] v={
						{
							(double)(rMax[0][0]-rMin[0][0]),
							(double)(rMax[0][1]-rMin[0][1]),
							(double)(rMax[0][2]-rMin[0][2])
						}
				};
		return v;
	} 

	public int[][] getrMin() {
		return rMin;
	}
	
	public double[][] getrMinD() {
		double [][] r= new double[1][rMin[0].length];
		for (int i = 0; i < rMin[0].length; i++) {
			r[0][i]=(double)rMin[0][i];
		}
		return r;
	}

	public void setrMin(int[][] rMin) {
		this.rMin = rMin;
	}

	public int[][] getrMax() {
		return rMax;
	}
	
	public double[][] getrMaxD() {
		double [][] r= new double[1][rMax[0].length];
		for (int i = 0; i < rMax[0].length; i++) {
			r[0][i]=(double)rMax[0][i];
		}
		return r;
	}

	public void setrMax(int[][] rMax) {
		this.rMax = rMax;
	}

	@Override
	public String toString() {
		return "ReferenceVector [rMin=" + Arrays.toString(rMin[0]) + ", rMax=" + Arrays.toString(rMax[0]) + "]";
	}
	
	
	
	
	
	

}
