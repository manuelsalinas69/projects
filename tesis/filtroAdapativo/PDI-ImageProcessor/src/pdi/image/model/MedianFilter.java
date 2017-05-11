package pdi.image.model;

public class MedianFilter extends WeigthedFilter{

//	static int[][] filter={{1,1,1},{1,1,1},{1,1,1}};
	
	static int[][] filter={{1,1,1},{1,4,1},{1,1,1}};
//	1 1 1 1 4 1 1 1 1 
	//1 2 2 1 9 1 1 1 1
	public MedianFilter() {
		super(filter);
				
		
	}

//	@Override
//	public int processFilter(int[][] pixels) {
//		int medio=(pixels.length*pixels[0].length)/2;
//		int cantPixels=pixels.length*pixels[0].length;
//		int pos=0;
//		int[] a=new int[cantPixels];
//		for (int i = 0; i < pixels.length; i++) {
//			for (int j = 0; j < pixels[0].length; j++) {
//				a[pos]=pixels[i][j];
//				pos++;
//			}
//		}
//		Arrays.sort(a);
//		return a[medio];
//	}

	
	

}
