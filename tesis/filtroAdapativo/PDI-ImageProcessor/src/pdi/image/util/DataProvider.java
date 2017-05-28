package pdi.image.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ij.ImagePlus;

public class DataProvider {

	static DataProvider instance;
	ImagePlus baseImage;
	Map<String, Object> params= new HashMap<String,Object>();
	
	public static DataProvider getInstance(){
		if (instance==null) {
			instance= new DataProvider();
		}
		return instance;
	}

	public ImagePlus getBaseImage() {
		return baseImage;
	}

	public void setBaseImage(ImagePlus baseImage) {
		this.baseImage = baseImage;
	}
	
	public int[][] getBaseImageArray(){
		return baseImage!=null?baseImage.getProcessor().getIntArray():null;
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
	
	
	
	public Object getParam(String key){
		return params.get(key);
	}
	
	public void setParam(String key, Object value){
		params.put(key, value);
	}
	
	
	
	public static class DataParams{
		public static String NOISE_DENSITY_FITNESS_PERCENT="NOISE_DENSITY_FITNESS_PERCENT";
		public static String MAE_FITNESS_PERCENT="MAE_FITNESS_PERCENT";
		public static String FILTER_COLUMN_SIZE="FILTER_COLUMN_SIZE";
	}
	
	public void print(){
		for (Entry<String, Object> _p : params.entrySet()) {
			System.out.println(_p.getKey()+": "+_p.getValue());
		}
	}
	
	
}
