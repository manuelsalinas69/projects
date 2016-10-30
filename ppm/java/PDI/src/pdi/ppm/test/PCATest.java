package pdi.ppm.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import Jama.Matrix;
import ij.process.ColorProcessor;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.PCAResult;
import pdi.ppm.model.Pixel;
import pdi.ppm.model.StructuringElement;
import pdi.ppm.util.PCAUtils;
import pdi.ppm.util.Utils;

public class PCATest {

	public static void main(String[] args) throws IOException {
		//long t1=System.currentTimeMillis();
		File f= new File("/Users/Manuel/Documents/Tesis/miro.jpg");
		ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
		ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
		ImageMatrix out=process(m, null);
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
		

	}
	
	
public static ImageMatrix process(ImageMatrix im,StructuringElement se){
		
		
		Pixel[][] pixels=new Pixel[im.getHeight()][im.getWidth()];
		List<Pixel> l=im.toList();
		PCAResult r=PCAUtils.PCA(im);
		
		Matrix c= r.getSc();
		
		Matrix orig=c.times(r.getEiVec().inverse());
		int pos=0;
		for (int i = 0; i < im.getHeight(); i++) {
			for (int j = 0; j < im.getWidth(); j++) {
				
				double R=orig.get(pos, 0);
				double G=orig.get(pos, 1);
				double B=orig.get(pos, 2);
				
				pixels[i][j]=new Pixel((int)R, (int)G, (int)B);//c.get(arg0, arg1);
				pos++;
			}
		}
		return new ImageMatrix(pixels, pixels.length, pixels[0].length);
	}

}
