package pdi.ppm.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ij.process.ColorProcessor;
import pdi.kmeans.Cluster;
import pdi.kmeans.KMeans;
import pdi.kmeans.KMeansResultado;
import pdi.kmeans.Punto;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.FeatureMatrix;
import pdi.ppm.model.FeatureVector;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.Pixel;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.model.StructuringElement;
import pdi.ppm.operations.ReferencesFactory;
import pdi.ppm.operations.VolumeFeature;
import pdi.ppm.util.StructuringElementFactory;
import pdi.ppm.util.Utils;

public class VolumenFeatureTest {

	
	
	public static void main(String[] args) throws Exception {
		
		List<Pixel> px= new ArrayList<Pixel>();
		px.add(new Pixel(0,0,0));
		px.add(new Pixel(255, 255, 255));
		px.add(new Pixel(100, 100, 100));
		
		File f= new File("/Users/Manuel/Documents/Tesis/tiger.jpg");
		//Utils.getInstance().showImage("/Users/Manuel/Documents/Tesis/miro.jpg");
		ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
		long t1=System.currentTimeMillis();
		ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
		List<ReferenceVector> l= ReferencesFactory.getReferences(m);
		PPMConstanst.referenceVectors=l;
		//StructuringElement se = StructuringElementFactory.getInstance().buildStrel("square", 11);

		//long v= VolumeFeature.getVolumeFeature(m, se);
		
		FeatureMatrix fMatrix=VolumeFeature.buildFeatureVector(m, 32, 32, 5, 11, "square", 1);
		long t2=System.currentTimeMillis();
		System.out.println("Elapsed Time: "+(t2-t1)+"ms.");
		//System.out.println("Feacture Vector: "+kout);
		List<Punto> puntos= new ArrayList<Punto>();
		//List<long[]> lFeatures=new ArrayList<long[]>();
		Punto punto=null;
		for (int i = 0; i < fMatrix.getHeight(); i++) {
			for (int j = 0; j < fMatrix.getWidth();j++) {
				punto=new Punto(fMatrix.getFeatureAtPos(i, j).getFeatures(), i, j);
				puntos.add(punto);
			}
		}
		
		KMeans kMeans= new KMeans();
		KMeansResultado kMeansResultado = kMeans.calcular(puntos, 2);
		Pixel [][] imKmeans=new Pixel[m.getHeight()][m.getWidth()];
		int clusterTag=0;
		for (Cluster cluster : kMeansResultado.getClusters()) {
			for (Punto p : cluster.getPuntos()) {
				Pixel pix=px.get(clusterTag);
				imKmeans[p.getY()][p.getX()]=pix;
			}
			clusterTag++;
		}
		ImageMatrix outKmeans= new ImageMatrix(imKmeans, m.getHeight(), m.getWidth());
		BufferedImage image = new BufferedImage(outKmeans.getWidth(), outKmeans.getHeight(), BufferedImage.TYPE_INT_RGB); 

		  for (int y = 0; y < outKmeans.getHeight(); y++) {
		     for (int x = 0; x < outKmeans.getWidth(); x++) {
		        int rgb = outKmeans.R[y][x];
		        rgb = (rgb << 8) + outKmeans.G[y][x];
		        rgb = (rgb << 8) + outKmeans.B[y][x];
		        image.setRGB(x, y, rgb);
		     }
		  }

		  File outputFile = new File("/Users/Manuel/Documents/Tesis/output.jpg");
		  ImageIO.write(image, "jpg", outputFile);
		  Utils.getInstance().showImage("/Users/Manuel/Documents/Tesis/output.jpg");
		//List<Cluster> clusters = kMeansResultado.getClusters();
		
	}

}
