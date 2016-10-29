package pdi.ppm.test;

import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import Jama.Matrix;
import ij.process.ColorProcessor;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.operations.BaseOperation;
import pdi.ppm.operations.References;
import pdi.ppm.util.StructuringElementFactory;
import pdi.ppm.util.Utils;

public class Test {

	
	public static void main(String[] args) throws Exception {
		//Utils.getInstance().showImage("/Users/Manuel/Documents/Tesis/tiger.jpg");
		
		//StructuringElement se=StructuringElementFactory.getInstance().buildStrel("square", 5);
		//se.printNhood();
	
//		Pixel pix= new Pixel(250, 1, 40);
//		System.out.println("Pixel "+pix);
		File f= new File("/Users/Manuel/Documents/Tesis/miro.jpg");
		ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
		ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
		List<ReferenceVector> l= References.getReferences(m);
//		BaseOperation bo= new BaseOperation();
//		bo.process(m, StructuringElementFactory.getInstance().buildStrel("square", 4));
//		 ByteProcessor[] channel;
//	        channel = new ByteProcessor[cp.getNChannels()];
//
//	        for (int i = 0; i < channel.length; i++) {
//	            channel[i] = cp.getChannel(i + 1, channel[i]);
//	        }
//	        for (int i = 0; i < 3; i++) {
//	            System.out.println("Canal " + (i + 1));
//	            for (int u = 0; u < cp.getHeight(); u++) {
//	                for (int v = 0; v < cp.getWidth(); v++) {
//	                    System.out.print("\t" + channel[i].get(v, u));
//
//	                }
//	                System.out.println();
//
//	            }
//	        }
		
		//System.out.println(Arrays.toString(intArray));
		
//		double [] rhs = { 1, 1, 1 };
//		Matrix m= new Matrix(rhs,rhs.length);
//		m.print(10, 0);
		
//		double r=Math.pow(2, 3);
//		System.out.println(r);
	}
}
