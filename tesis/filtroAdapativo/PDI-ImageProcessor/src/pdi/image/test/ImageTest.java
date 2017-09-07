package pdi.image.test;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import pdi.image.evaluations.MAE;
import pdi.image.evaluations.NoiseDensity;
import pdi.image.model.MedianFilter;

public class ImageTest {
	
	
	public static void main(String[] args) {
		ImagePlus img0=IJ.openImage("/Users/Manuel/Documents/Tesis/input/B2DBy.jpg");
		ImagePlus img=IJ.openImage("/Users/Manuel/Documents/Tesis/input/lenanoise.jpg");
		//img.show();
		ImageProcessor imp=img.getProcessor();
		ImageConverter ic = new ImageConverter(img);
		ic.convertToGray8();
		img.updateAndDraw();
		
		System.out.println("Histograma: "+Arrays.toString(imp.getHistogram()));
		System.out.println("Histograma MAX: "+imp.getHistogramMax());
		System.out.println("Histograma MIN: "+imp.getHistogramMin());
		
		int a=imp.getHistogram().length;
		int pepper=imp.getHistogram()[0];
		int salt=imp.getHistogram()[a-1];
		System.out.println("StDev w/ Salt & Pepper: "+imp.getStatistics().stdDev);
		
		double[] v=new double [imp.getHistogram().length-2];
		for (int i = 1; i < imp.getHistogram().length-1; i++) {
			v[i-1]=imp.getHistogram()[i];
		}
		DescriptiveStatistics st= new DescriptiveStatistics(v);
		System.out.println("Histogram.length: "+imp.getHistogram().length);
		
		System.out.println("Salt: "+salt);
		System.out.println("Pepper: "+pepper);
		System.out.println(Arrays.toString(v));
		System.out.println("StDev wo/ Salt & Pepper: "+st.getStandardDeviation());
		//imp.getStatistics();
		
		//		imp.getH
		
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
		//imp.medianFilter();
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
