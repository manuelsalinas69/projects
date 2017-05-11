package pdi.image.test;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import pdi.image.evaluations.MAE;
import pdi.image.evaluations.NoiseDensity;
import pdi.image.model.MedianFilter;

public class ImageTest {
	
	
	public static void main(String[] args) {
		ImagePlus img0=IJ.openImage("/Users/Manuel/Documents/Tesis/input/B2DBy.jpg");
		ImagePlus img=IJ.openImage("/Users/Manuel/Documents/Tesis/input/a.jpg");
		//img.show();
		ImageProcessor imp=img.getProcessor();
		
//		int[][] original=imp.getIntArray();
		//imp.noise(10);
//		
//		//img.show();
//		int[][] noiseArray = imp.getIntArray();
//		
//		imp.noise(20);
//		
//		//img.show();
//		int[][] noiseArray2 = imp.getIntArray();
//		
		imp.medianFilter();
//		
//		int[][] filterArray = imp.getIntArray();
//		
//		double mae1= MAE.evaluate(noiseArray, original);
//		double mae2= MAE.evaluate(filterArray, original);
//		double noiseD1= NoiseDensity.evaluate(noiseArray, noiseArray2);
//		double noiseD2= NoiseDensity.evaluate(filterArray, noiseArray2);
//		System.out.println("Mae1 noiseArray: "+mae1);
//		System.out.println("Mae2 filterArray: "+mae2);
//		System.out.println("noiseD1 noiseArray: "+noiseD1);
//		System.out.println("noiseD2 filterArray: "+noiseD2);
//		//img.show();
//	
//		MedianFilter mf=new MedianFilter();
//		int[][] imFiltered=mf.applyFilter(imp.getIntArray());//mf.getPixels(imp.getIntArray(), 0, 299);
//		imp.setIntArray(imFiltered);
		img.show();
	}

}
