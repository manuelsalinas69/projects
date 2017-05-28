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
		
	}

}
