package pdi.ppm.model;

public class SlideWindowMap {
	

	int[] xVector;
	int[] yVector;
	int[] xSlideWindow;
	int[] ySlideWindow;
	public SlideWindowMap(int[] xVector, int[] yVector, int[] xSlideWindow, int[] ySlideWindow) {
		super();
		this.xVector = xVector;
		this.yVector = yVector;
		this.xSlideWindow = xSlideWindow;
		this.ySlideWindow = ySlideWindow;
	}
	public int[] getxVector() {
		return xVector;
	}
	public void setxVector(int[] xVector) {
		this.xVector = xVector;
	}
	public int[] getyVector() {
		return yVector;
	}
	public void setyVector(int[] yVector) {
		this.yVector = yVector;
	}
	public int[] getxSlideWindow() {
		return xSlideWindow;
	}
	public void setxSlideWindow(int[] xSlideWindow) {
		this.xSlideWindow = xSlideWindow;
	}
	public int[] getySlideWindow() {
		return ySlideWindow;
	}
	public void setySlideWindow(int[] ySlideWindow) {
		this.ySlideWindow = ySlideWindow;
	}
	
	

}
