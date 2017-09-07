package pdi.image.model;

public abstract class BaseFilter {

	int[][] filter;
	int [] filterCenter;
	
	public BaseFilter(int[][] filter) {
		this.filter=filter;
		this.filterCenter=getFilterCenter(filter);
	}
	
	
	public int[][] applyFilter(int[][] image){
		//int [] filterCenter=getFilterCenter(filter);
		int[][] outputImage=new int[image.length][image[0].length];
		for (int i = 0; i < image.length; i++) {
			int[] row=image[i];
			for (int j = 0; j < row.length; j++) {
				//int col=row[j];
				
				int [][] pixels=getPixels(image,i,j);
				int pixel=processFilter(pixels, image[i][j]);
				
				outputImage[i][j]=pixel;
			}
		}
		
		return outputImage;
	}

	public abstract int processFilter(int[][] pixels, int pivotPixel);
	protected abstract int getBorderValue();
	
	public int[][] getPixels(int[][] image, int i, int j) {
		int minI=Math.max(0, i-filterCenter[0]);
		int maxI=Math.min(image.length-1, i+(filter.length-1-filterCenter[0]));
		int minJ=Math.max(0, j-filterCenter[1]);
		int maxJ=Math.min(image[0].length-1, j+(filter[0].length-1-filterCenter[1]));
		int f,c;
		int[][] pixels=new int[filter.length][filter.length];
		//System.out.println("Image H: "+image.length);
		//System.out.println("Image W: "+image[0].length);
		for (int k = 0; k < pixels.length; k++) {
			for (int k2 = 0; k2 < pixels.length; k2++) {
				f=i-(filterCenter[0]-k);
				c=j-(filterCenter[1]-k2);
				if (f>=minI && f<=maxI && c>=minJ && c<=maxJ) {
					pixels[k][k2]=image[f][c];
				}
				else{
					pixels[k][k2]=getBorderValue();
				}
				//System.out.print(" "+pixels[k][k2]);
			}
			//System.out.println("");
		}
		return pixels;
	}

	private int[] getFilterCenter(int[][] filter) {
		int rowSize=filter.length;
		int colSize=filter[0].length;
		int[] center={rowSize/2,colSize/2};
		return center;
	}
	
	
	
}
