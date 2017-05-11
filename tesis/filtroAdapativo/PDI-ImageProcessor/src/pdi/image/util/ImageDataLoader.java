package pdi.image.util;

import ij.IJ;
import ij.ImagePlus;

public class ImageDataLoader {
	
	public void loadBaseImage(String filePath, boolean addNoise){
		ImagePlus img=IJ.openImage(filePath);
		if (addNoise) {
			img.getProcessor().noise(10);
		}
		DataProvider.getInstance().setBaseImage(img);
		DataProvider.getInstance().setParam(DataProvider.DataParams.FILTER_COLUMN_SIZE, 3);
		DataProvider.getInstance().setParam(DataProvider.DataParams.MAE_FITNESS_PERCENT, 0.6);
		DataProvider.getInstance().setParam(DataProvider.DataParams.NOISE_DENSITY_FITNESS_PERCENT, 0.4);
	}

}
