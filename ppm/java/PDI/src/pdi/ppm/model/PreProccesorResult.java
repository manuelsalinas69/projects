package pdi.ppm.model;

public class PreProccesorResult {
	
	ImageMatrix optimal;
	int skipCols;
	int skipRows;
	ImageStatisticData stData;
	
	public PreProccesorResult() {
		
	}

	public PreProccesorResult(ImageMatrix optimal, int skipCols, int skipRows) {
		super();
		this.optimal = optimal;
		this.skipCols = skipCols;
		this.skipRows = skipRows;
	}
	
	

	public PreProccesorResult(ImageMatrix optimal, int skipCols, int skipRows, ImageStatisticData stData) {
		super();
		this.optimal = optimal;
		this.skipCols = skipCols;
		this.skipRows = skipRows;
		this.stData = stData;
	}

	public ImageMatrix getOptimal() {
		return optimal;
	}

	public void setOptimal(ImageMatrix optimal) {
		this.optimal = optimal;
	}

	public int getSkipCols() {
		return skipCols;
	}

	public void setSkipCols(int skipCols) {
		this.skipCols = skipCols;
	}

	public int getSkipRows() {
		return skipRows;
	}

	public void setSkipRows(int skipRows) {
		this.skipRows = skipRows;
	}
	public ImageStatisticData getStData() {
		return stData;
	}
	

}
