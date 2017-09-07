package pdi.image.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ij.ImagePlus;

public class DataProvider {

	static DataProvider instance;
	ImagePlus imp;
	Map<String, Object> params= new HashMap<String,Object>();
	
	public static DataProvider getInstance(){
		if (instance==null) {
			instance= new DataProvider();
		}
		return instance;
	}

	public ImagePlus getBaseImage() {
		return imp;
	}

	public void setBaseImage(ImagePlus baseImage) {
		this.imp = baseImage;
		
	}
	
	public int[][] getBaseImageArray(){
		return imp!=null?imp.getProcessor().getIntArray():null;
	}
	
	
	public double getNoiseDensityFitnessPercent(){
		return (double) getParam(DataParams.NOISE_DENSITY_FITNESS_PERCENT);
	}
	
	public double getMAEFitnessPercent(){
		return (double) getParam(DataParams.MAE_FITNESS_PERCENT);
	}
	
	public int getFilterColumnSize(){
		return (int) getParam(DataParams.FILTER_COLUMN_SIZE);
	}
	
	public int[] getHistrogram(){
		return (int[])getParam(DataParams.HISTOGRAM);
	}
	
	public double getNoiseProbability(){
		return (double) getParam(DataParams.NOISE_PROBABILITY);
	}
	
	public int getGA_MinLimit(){
		return (int) getParam(DataParams.ALGORITM_GA_MIN_LIMIT);
	}
	
	public int getGA_MaxLimit(){
		return (int) getParam(DataParams.ALGORITM_GA_MAX_LIMIT);
	}
	
	public int getNoiseEstimationFilter(){
		return (int) getParam(DataParams.ALGORITM_NOISE_ESTIMATION_FILTER);
	}
	
	
	public Object getParam(String key){
		return params.get(key);
	}
	
	public void setParam(String key, Object value){
		params.put(key, value);
	}
	
	public void updateBaseImageArray(int [][] imageArray){
		imp.getProcessor().setIntArray(imageArray);
	}
	
	public static class DataParams{
		public static String NOISE_DENSITY_FITNESS_PERCENT="NOISE_DENSITY_FITNESS_PERCENT";
		public static String MAE_FITNESS_PERCENT="MAE_FITNESS_PERCENT";
		public static String FILTER_COLUMN_SIZE="FILTER_COLUMN_SIZE";
		public static String HISTOGRAM="HISTOGRAM";
		public static String NOISE_PROBABILITY="NOISE_PROBABILITY";
		public static String NBINS="NBINS";
		public static String IMG_URL="IMG_URL";
		public static String ALGORITM_GA_MIN_LIMIT="ALGORITM_GA_MIN_LIMIT";
		public static String ALGORITM_GA_MAX_LIMIT="ALGORITM_GA_MAX_LIMIT";
		public static String ALGORITM_NOISE_ESTIMATION_FILTER="ALGORITM_NOISE_ESTIMATION_FILTER";
	}
	
	public void print(){
		for (Entry<String, Object> _p : params.entrySet()) {
			System.out.println(_p.getKey()+": "+_p.getValue());
		}
	}
	
	
}
