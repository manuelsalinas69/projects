package pdi.ppm.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ij.process.ColorProcessor;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.Pixel;
import pdi.ppm.model.PreProccesorResult;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.operations.PreProccessor;
import pdi.ppm.operations.ReferencesFactory;
import pdi.ppm.util.Utils;

public class PreproccessorTest {

	public static void main(String[] args) throws Exception {
		List<Pixel> px= new ArrayList<Pixel>();
		px.add(new Pixel(0,0,0));
		px.add(new Pixel(255, 255, 255));
		px.add(new Pixel(100, 100, 100));
		
		//File f= new File("/Users/Manuel/Documents/Tesis/tiger.jpg");
		File f= new File("/Users/Manuel/Documents/Tesis/tiger_var1.jpg");
//		File f= new File("/Users/Manuel/Documents/Tesis/tiger_var2.jpg");
//		File f= new File("/Users/Manuel/Documents/Tesis/tiger_v3.jpg");

		ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
		//long t1=System.currentTimeMillis();
		ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
		List<ReferenceVector> l= ReferencesFactory.getReferences(m);
		PPMConstanst.referenceVectors=l;
		
		PreProccessor p=new PreProccessor();
		PreProccesorResult result = p.preProccess(m, 0, m.getHeight(), 0, m.getWidth());
		System.out.println("StData: "+result.getStData());
		Utils.getInstance().showImage(m);
	
	}

}
