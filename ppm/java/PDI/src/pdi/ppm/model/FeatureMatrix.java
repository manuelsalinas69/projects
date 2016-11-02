package pdi.ppm.model;

public class FeatureMatrix {

	int height;
	int width;
	FeatureVector[][]values;
	
	
	
	
	
	public FeatureMatrix(FeatureVector[][] values) {
		super();
		this.values = values;
		height=values.length;
		width=values[0].length;
	}
	
	


	public FeatureMatrix(int height, int width) {
		
		this.height = height;
		this.width = width;
		values=new FeatureVector[height][width];
	}
	
	public FeatureVector getFeatureAtPos(int h, int w){
		return values[h][w];
	}




	public FeatureMatrix subMatrix(int f0,int f1, int c0, int c1){
		FeatureVector[][] values= new FeatureVector[(f1-f0)+1][(c1-c0)+1];
		int row=0;
		int col=0;
		for (int i = f0; i <= f1; i++) {
			
			for (int j = c0; j <= c1; j++) {
				values[row][col]=this.values[i][j];
				col++;
			}
			col=0;
			row++;
		}
		return new FeatureMatrix(values);
	}
	
	
	public void setSubMatrix(int f0,int f1, int c0, int c1, long value, int pos){
		
		for (int i = f0; i <= f1; i++) {
			
			for (int j = c0; j <= c1; j++) {
				values[i][j].setFeature(value, pos);
				
			}
		
		}
		
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public FeatureVector[][] getValues() {
		return values;
	}
	
	public void set(int h, int w, FeatureVector fv){
		this.values[h][w]=fv;
	}
	
	
	
}
