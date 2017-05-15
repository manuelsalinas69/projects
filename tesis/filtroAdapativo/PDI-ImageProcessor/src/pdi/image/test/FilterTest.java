package pdi.image.test;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import pdi.image.model.WeigthedFilter;

public class FilterTest {

	public static void main(String[] args) {
		//ImagePlus img0=IJ.openImage("/Users/Manuel/Documents/Tesis/input/B2DBy.jpg");
		ImagePlus img=IJ.openImage("/Users/Manuel/Documents/Tesis/input/c.png");
		//img.show();
		ImageProcessor imp=img.getProcessor();
		//4 2 6 2 1 9 1 5 3 
		//10 5 2 2 2 2 6 9 5 
		//2 9 10 9 6 2 4 4 5
		int[][] filter={{2, 9, 10},{ 9, 6, 2},{ 4, 4, 5} };
		WeigthedFilter mf=new WeigthedFilter(filter);
		int[][] imFiltered=mf.applyFilter(imp.getIntArray());//mf.getPixels(imp.getIntArray(), 0, 299);
		imp.setIntArray(imFiltered);
		img.show();

	}

}
