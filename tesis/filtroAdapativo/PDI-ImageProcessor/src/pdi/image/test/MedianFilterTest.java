package pdi.image.test;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import pdi.image.model.WeigthedFilter;

public class MedianFilterTest {

	public static void main(String[] args) {
		//ImagePlus img0=IJ.openImage("/Users/Manuel/Documents/Tesis/input/B2DBy.jpg");
		ImagePlus img=IJ.openImage("/Users/Manuel/Documents/Tesis/input/b.jpg");
		//img.show();
		ImageProcessor imp=img.getProcessor();
		int[][] filter={{2, 2, 1},{ 3, 9, 2},{ 2, 1, 2} };
		WeigthedFilter mf=new WeigthedFilter(filter);
		int[][] imFiltered=mf.applyFilter(imp.getIntArray());//mf.getPixels(imp.getIntArray(), 0, 299);
		imp.setIntArray(imFiltered);
		img.show();

	}

}
