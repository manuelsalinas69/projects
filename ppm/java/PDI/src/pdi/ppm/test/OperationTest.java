package pdi.ppm.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ij.process.ColorProcessor;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.Pixel;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.model.StructuringElement;
import pdi.ppm.operations.PseudoDil;
import pdi.ppm.operations.PseudoErode;
import pdi.ppm.operations.ReferencesFactory;
import pdi.ppm.util.StructuringElementFactory;
import pdi.ppm.util.Utils;

public class OperationTest {
	

			
	public static void main(String[] args) throws Exception {
		List<Pixel> px= new ArrayList<Pixel>();
		px.add(new Pixel(0,0,0));
		px.add(new Pixel(255, 255, 255));
		px.add(new Pixel(100, 100, 100));
		
		File f= new File("/Users/Manuel/Documents/Tesis/tiger.jpg");
		ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
		//long t1=System.currentTimeMillis();
		ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
		List<ReferenceVector> l= ReferencesFactory.getReferences(m);
		PPMConstanst.referenceVectors=l;
		
		PseudoErode er=new PseudoErode();
		PseudoDil dil=new PseudoDil();
		StructuringElement se= StructuringElementFactory.getInstance().buildStrel("square", 11);
		ImageMatrix outErod=er.process(m, se);
		Utils.getInstance().showImage(outErod);
		ImageMatrix outDil=dil.process(m, se);
		Utils.getInstance().showImage(outDil);
		
		//ImageMatrix out3=Utils.getInstance().minus(dil.process(outDil, se), er.process(outErod, se));
		ImageMatrix out3=Utils.getInstance().minus(outDil, outErod);
		//Utils.getInstance().showImage(dil.process(out3, se));
		Utils.getInstance().showImage(out3);
	}

}
