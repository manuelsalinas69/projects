package pdi.image.util;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import pdi.image.util.DataProvider.DataParams;

public class ImageDataLoader {
	
	public void setAndPreProccessBaseImage(String filePath, boolean addNoise){
		ImagePlus imp=IJ.openImage(filePath);
		if (addNoise) {
			imp.getProcessor().noise(10);;
		}
		//img.gray
		ImageConverter ic = new ImageConverter(imp);
		ic.convertToGray8();
		imp.updateAndDraw();
		DataProvider.getInstance().setBaseImage(imp);
		
		int nbins=calculateNbis(imp.getProcessor().getHistogram());
		DataProvider.getInstance().setParam(DataParams.HISTOGRAM, imp.getProcessor().getHistogram(nbins));
		DataProvider.getInstance().setParam(DataParams.NBINS, nbins);
		double noiseP=calculateNoiseProbality(DataProvider.getInstance().getHistrogram());
		DataProvider.getInstance().setParam(DataParams.NOISE_PROBABILITY, noiseP*100);
	}
	
	private int calculateNbis(int[] histogram) {
//		long totalValues=getTotalValues(histogram);
//		int nbins=(int) (1+Math.log10(totalValues)/Math.log10(2));
		//System.out.println("Nbins: "+nbins);
		
//		DescriptiveStatistics st= new DescriptiveStatistics();
//		for (int i : histogram) {
//			st.addValue(i);
//		}
//		 double h = 3.5*st.getStandardDeviation()*Math.pow(histogram.length,(-1/3));
//	     Arrays.sort(histogram); 
//		 
//		 if (h > 0)  
//	         nbins = (int) ((histogram[histogram.length-1]-histogram[0])/h);
//	      else 
//	         nbins = 1;
//		 System.out.println("Nbins: "+nbins);
		return 25;
	}

	private double calculateNoiseProbality(int[] h) {
//		int[] h=DataProvider.getInstance().getHistrogram();
		long totalValues=getTotalValues(h);
		//int nbins=(int) (1+Math.log10(totalValues)/Math.log(2));
//		System.out.println("Nbins: "+nbins);
		int a=h.length;
		long pepper=h[0];
		long salt=h[a-1];
		long sumS_P=salt+pepper;
//		System.out.println("Salt: "+salt);
//		System.out.println("Pepper: "+pepper);
//		System.out.println("SUM (S&P): "+sumS_P);
//		System.out.println("Total Pixel Values:"+totalValues);
		double p=(double) sumS_P/(double)totalValues;
//		System.out.println("Probability: "+p);
//		System.out.println(Arrays.toString(h));
		return p;
	}

	private long getTotalValues(int[] histogram) {
		long sum=0;
		for (int i = 0; i < histogram.length; i++) {
			sum+=histogram[i];
		}
		return sum;
	}

	public static void main(String[] args) {
//		System.out.println("Lena 5 noise:");
//		String file="/Users/Manuel/Documents/Tesis/input/lenanoise.jpg";
//		ImageDataLoader imgDataLoader= new ImageDataLoader();
//		imgDataLoader.setAndPreProccessBaseImage(file, false);
//		System.out.println("Lena 4 noise: ");
//		String file1="/Users/Manuel/Documents/Tesis/input/lena4noise.jpg";
//		ImageDataLoader imgDataLoader1= new ImageDataLoader();
//		imgDataLoader1.setAndPreProccessBaseImage(file1, false);
		
		String[] files={"/Users/Manuel/Documents/Tesis/input/lenanoise.jpg",
				"/Users/Manuel/Documents/Tesis/input/lena4noise.jpg",
				"/Users/Manuel/Documents/Tesis/input/b.jpg"
		};	
		for (String _f : files) {
			System.out.println("File: "+_f);
			ImageDataLoader idl= new ImageDataLoader();
			idl.setAndPreProccessBaseImage(_f, false);
		}
	}
	
	

}
