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
import pdi.ppm.model.SlideWindowMap;
import pdi.ppm.operations.KmeansProccess;
import pdi.ppm.operations.ReferencesFactory;
import pdi.ppm.operations.VolumeFeature;
import pdi.ppm.operations.VolumeFeature2D;
import pdi.ppm.util.Utils;

public class VolumenFeatureTest2D {

	
	
	public static void main(String[] args) throws Exception {
		
		List<Pixel> px= new ArrayList<Pixel>();
		px.add(new Pixel(0,0,0));
		px.add(new Pixel(255, 255, 255));
		px.add(new Pixel(100, 100, 100));
//		File f= new File("/Users/Manuel/Documents/Tesis/input/134008.jpg");
		File f= new File("/Users/Manuel/Documents/Tesis/tiger.jpg");
//		File f= new File("/Users/Manuel/Documents/Tesis/tiger_var1.jpg");
//		File f= new File("/Users/Manuel/Documents/Tesis/tiger_var2.jpg");

		ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
		long t1=System.currentTimeMillis();
		ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
		List<ReferenceVector> l= ReferencesFactory.getReferences(m);
		PPMConstanst.referenceVectors=l;
	
		SlideWindowMap swm=VolumeFeature2D.generateSlideWindowMap(m, 20, 20);
		long t2=System.currentTimeMillis();
		System.out.println("Elapsed Time [SlideWindowMap]: "+(t2-t1)+"ms.");
		
		FeatureMatrix fMatrix=VolumeFeature2D.buildFeatureVector(m, swm, 5, 11, "square");
		long t3=System.currentTimeMillis();
		
		
		System.out.println("Elapsed Time [FeatureMatrix]: "+(t3-t2)+"ms.");
		
		System.out.println("Elapsed Time [ALL]: "+(t3-t1)+"ms.");
//		System.out.print("X:");
//		int i=0;
//		for ( i=0; i < swm.getxVector().length; i++) {
//			
//			System.out.print(swm.getxVector()[i]+" ");
//			if (i%30==0) {
//				System.out.println("");
//			}
//			if (swm.getxVector()[i]<0) {
//				
//				break;
//			}
//		}
		ImageMatrix kmeansOut=KmeansProccess.proccess(fMatrix, 2);
//		ImageMatrix sub1=m.subMatrix(0, 30, 0, 120);
//		ImageMatrix sub=m.subMatrix(0, 30, 0, 146);
//		Utils.getInstance().showImage(sub);
		Utils.getInstance().showImage(kmeansOut);
		

		
	}

}
