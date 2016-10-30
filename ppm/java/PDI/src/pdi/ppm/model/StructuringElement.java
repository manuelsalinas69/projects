package pdi.ppm.model;

public class StructuringElement {
	
	private int [][] nhood;
	private StreMetaData metaData;
	public StructuringElement(int[][] nhood) {
		super();
		this.nhood = nhood;
		this.metaData= new StreMetaData(this);
	}
	public int getSize(){
		return nhood.length;
	}
	
	public int get(int row, int col){
		return nhood[row][col];
	}
	
	public void printNhood(){
		System.out.println("nhood: "+getSize()+"X"+getSize());
		for (int i = 0; i < nhood.length; i++) {
			for (int j = 0; j < nhood[i].length; j++) {
				System.out.print(nhood[i][j]+"\t");
			}
			System.out.println("");
		}
	}
	
	public class StreMetaData{
		
		StructuringElement se;
		int centerRow;
		int centerCol;
		int distOrigenFilas;
		int distOrigenColumnas;
		int distFinFilas;
		int distFinColumnas;
		public StreMetaData(StructuringElement se) {
			super();
			this.se = se;
			preproccess(se);
		}
		private void preproccess(StructuringElement se) {
			
			int mod=se.getSize()%2;
			
			if (mod==1) {
				centerRow=(se.getSize()/2)+1;
				centerCol=(se.getSize()/2)+1;
			}
			else{
				centerRow=se.getSize()/2;
				centerCol=se.getSize()/2;	
			}
			
			//TODO: ver de modificar para otros tipos de SE
			distOrigenFilas=centerRow-1;
			distOrigenColumnas=centerCol-1;
			distFinFilas=se.getSize()-centerRow;
			distFinColumnas=se.getSize()-centerCol;
			
		}
		public int getCenterRow() {
			return centerRow;
		}
		public int getCenterCol() {
			return centerCol;
		}
		public int getDistOrigenFilas() {
			return distOrigenFilas;
		}
		public int getDistOrigenColumnas() {
			return distOrigenColumnas;
		}
		public int getDistFinFilas() {
			return distFinFilas;
		}
		public int getDistFinColumnas() {
			return distFinColumnas;
		}
		
		
		
		
	}

	public int[][] getNhood() {
		return nhood;
	}
	
	public StreMetaData getMetaData() {
		return metaData;
	}
}
