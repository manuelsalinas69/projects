package pdi.image.test;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import pdi.image.model.BaseFilter;
import pdi.image.model.MedianFilter;
import pdi.image.model.WeigthedFilter;

public class FilterTest {

	public static void main(String[] args) {
		//ImagePlus img0=IJ.openImage("/Users/Manuel/Documents/Tesis/input/B2DBy.jpg");
		ImagePlus img=IJ.openImage("/Users/Manuel/Documents/Tesis/input/test.jpg");
		
		//img.show();
		ImageProcessor imp=img.getProcessor();
		imp.noise(10);
		//4 2 6 2 1 9 1 5 3 
		//10 5 2 2 2 2 6 9 5 
		//2 9 10 9 6 2 4 4 5
		//6 7 7 4 7 9 3 4 6
		//1 1 1 1 10 1 1 3 1 
		int[][] filter={{1, 1, 1},{ 1, 10, 1},{ 1, 3, 1} };
//		BaseFilter mf=new WeigthedFilter(filter);
//		BaseFilter mf=new MedianFilter();
//
//		int[][] imFiltered=mf.applyFilter(imp.getIntArray());//mf.getPixels(imp.getIntArray(), 0, 299);
//		imp.setIntArray(imFiltered);
		img.show();

	}

}
