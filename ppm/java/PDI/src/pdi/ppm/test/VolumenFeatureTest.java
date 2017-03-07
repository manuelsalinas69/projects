package pdi.ppm.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ij.process.ColorProcessor;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.FeatureMatrix;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.Pixel;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.operations.KmeansProccess;
import pdi.ppm.operations.ReferencesFactory;
import pdi.ppm.operations.VolumeFeature;
import pdi.ppm.operations.VolumeFeatureV2;
import pdi.ppm.util.Utils;

public class VolumenFeatureTest {

	
	
	public static void main(String[] args) throws Exception {
		
		
		
//		File f= new File("/Users/Manuel/Documents/Tesis/tiger.jpg");
//		File f= new File("/Users/Manuel/Documents/Tesis/input/persona.jpg");
		File f= new File("/Users/Manuel/Documents/Tesis/input/134008.jpg");
//		File f= new File("/Users/Manuel/Documents/Tesis/tiger_var2.jpg");

		ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
		long t1=System.currentTimeMillis();
		ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
		List<ReferenceVector> l= ReferencesFactory.getReferences(m);
		PPMConstanst.referenceVectors=l;
	
		FeatureMatrix fMatrix=VolumeFeature.buildFeatureVector(m, 32, 32, 5, 11, "square", 1);
//		FeatureMatrix fMatrix=VolumeFeatureV2.buildFeatureVector(m, 32, 32, 5, 11, "square", 10);
		
		long t2=System.currentTimeMillis();
		System.out.println("Elapsed Time: "+(t2-t1)+"ms.");
		
		ImageMatrix kmeansOut=KmeansProccess.proccess(fMatrix, 2);
		

		Utils.getInstance().showImage(kmeansOut);
		

		
	}

}
