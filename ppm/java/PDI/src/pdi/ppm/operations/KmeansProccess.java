package pdi.ppm.operations;

import java.util.ArrayList;
import java.util.List;

import pdi.kmeans.Cluster;
import pdi.kmeans.KMeans;
import pdi.kmeans.KMeansResultado;
import pdi.kmeans.Punto;
import pdi.ppm.model.FeatureMatrix;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.Pixel;

public class KmeansProccess {
	
	public static ImageMatrix proccess(FeatureMatrix fMatrix, int k) throws Exception{
		List<Pixel> px= new ArrayList<Pixel>();
		px.add(new Pixel(0,0,0));
		px.add(new Pixel(255, 255, 255));
		px.add(new Pixel(100, 100, 100));
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
				Pixel [][] imKmeans=new Pixel[fMatrix.getHeight()][fMatrix.getWidth()];
				int clusterTag=0;
				for (Cluster cluster : kMeansResultado.getClusters()) {
					for (Punto p : cluster.getPuntos()) {
						Pixel pix=px.get(clusterTag);
						imKmeans[p.getY()][p.getX()]=pix;
					}
					clusterTag++;
				}
				ImageMatrix outKmeans= new ImageMatrix(imKmeans, fMatrix.getHeight(), fMatrix.getWidth());
				
				return outKmeans;
		
	}

}
