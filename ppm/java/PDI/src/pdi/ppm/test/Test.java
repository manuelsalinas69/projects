package pdi.ppm.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import ij.process.ColorProcessor;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.PCAResult;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.model.StructuringElement;
import pdi.ppm.operations.BaseOperation;
import pdi.ppm.operations.PseudoDil;
import pdi.ppm.operations.PseudoErode;
import pdi.ppm.operations.ReferencesFactory;
import pdi.ppm.util.PCAUtils;
import pdi.ppm.util.StructuringElementFactory;
import pdi.ppm.util.Utils;

public class Test {

	
	public static void main(String[] args) throws Exception {
		long t1=System.currentTimeMillis();
		File f= new File("/Users/Manuel/Documents/Tesis/miro.jpg");
		Utils.getInstance().showImage("/Users/Manuel/Documents/Tesis/miro.jpg");
		ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
		ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
		List<ReferenceVector> l= ReferencesFactory.getReferences(m);
		
		//PCAResult r=PCAUtils.PCA(m);
		
		PPMConstanst.referenceVectors=l;
		BaseOperation erode=new PseudoErode();
		BaseOperation dil=new PseudoDil();
		StructuringElement se = StructuringElementFactory.getInstance().buildStrel("square", 11);
//		ImageMatrix out = erode.process(m, se);
		ImageMatrix out = dil.process(m, se);
//		ImageMatrix out = m;
		
		BufferedImage image = new BufferedImage(out.getWidth(), out.getHeight(), BufferedImage.TYPE_INT_RGB); 

		  for (int y = 0; y < out.getHeight(); y++) {
		     for (int x = 0; x < out.getWidth(); x++) {
		        int rgb = out.R[y][x];
		        rgb = (rgb << 8) + out.G[y][x];
		        rgb = (rgb << 8) + out.B[y][x];
		        image.setRGB(x, y, rgb);
		     }
		  }

		  File outputFile = new File("/Users/Manuel/Documents/Tesis/output.jpg");
		  ImageIO.write(image, "jpg", outputFile);
		  Utils.getInstance().showImage("/Users/Manuel/Documents/Tesis/output.jpg");
		  
		
		long t2=System.currentTimeMillis();
		System.out.println("Elapsed Time: "+(t2-t1)+"ms.");
	}
}
