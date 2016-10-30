package pdi.ppm.model;

import Jama.Matrix;

public class PCAResult {

	Matrix mxData;
	
	Matrix eiVec;
	Matrix sc;
	double[] centers;
	public PCAResult(Matrix mxData, Matrix eiVec, Matrix sc) {
		super();
		this.mxData = mxData;
		this.eiVec = eiVec;
		this.sc = sc;
	}
	
	public PCAResult(Matrix mxData, Matrix eiVec, Matrix sc, double[] centers) {
		super();
		this.mxData = mxData;
		this.eiVec = eiVec;
		this.sc = sc;
		this.centers=centers;
	}
	public Matrix getMxData() {
		return mxData;
	}
	public Matrix getEiVec() {
		return eiVec;
	}
	public Matrix getSc() {
		return sc;
	}
	
	public double[] getCenters() {
		return centers;
	}
	
	
}
