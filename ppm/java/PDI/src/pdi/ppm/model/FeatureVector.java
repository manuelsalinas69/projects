package pdi.ppm.model;

public class FeatureVector {
	
	int row;
	int col;
	long[] features;
	public FeatureVector(int row, int col, int feautreSize) {
		super();
		this.row = row;
		this.col = col;
		features= new long[feautreSize];
	}
	
	public void setFeature(long value, int pos){
		features[pos]=value;
	}
	
	public long[] getFeatures() {
		return features;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	

}
