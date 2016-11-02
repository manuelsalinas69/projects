package pdi.ppm.operations;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import pdi.ppm.model.FeatureMatrix;
import pdi.ppm.model.FeatureVector;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.StructuringElement;
import pdi.ppm.util.StructuringElementFactory;
import pdi.ppm.util.Utils;

public class VolumeFeature {
	
	public static long getVolumeFeature(ImageMatrix m, StructuringElement se){
		BaseOperation e=new PseudoErode();
		BaseOperation d=new PseudoDil();
		ImageMatrix out=Utils.getInstance().minus(d.process(m, se), e.process(m, se));
		long v=Utils.getInstance().getVolume(out);
		return v;
		
	}
	
	public static FeatureMatrix featureFor(ImageMatrix im, StructuringElement se, int winH,
			int winW,int slide,FeatureMatrix f, int pos){
		
		int i=0,j=0;
		
		int h=im.getHeight();
		int w=im.getWidth();
		//FeatureMatrix f= new FeatureMatrix(h, w);
		//long []data=new long[h*w];
		//System.out.println("data length: "+data.length);
		int rangeF=0;
		int rangeC=0;
		long vf=0;
		
		//Matrix out=new Matrix(data); 
		//int row=0;
		//int col=0;
		int iter=0;
		while (i<h) {
			while (j<w) {
				rangeF=Math.min((h-1),i+winH-1);
				rangeC=Math.min((w-1), j+winW-1);
				ImageMatrix sub=im.subMatrix(i, rangeF, j, rangeC);
				vf=getVolumeFeature(sub, se);
				//data[pos]=vf;
				
				f.setSubMatrix(i, rangeF, j, rangeC, vf,pos);
				j+=slide;
				iter++;
			}
			j=0;
			i+=slide;
		}
		//System.out.println("Salida iter: "+iter);
		return f;
	}
	
	
	public static FeatureMatrix buildFeatureVector(ImageMatrix im, int winH, int winW, int seMin, int seMax, String strelFigure, int slide) throws Exception{
		int iter=(seMax-seMin)+1;
		
		int h=im.getHeight();
		int w=im.getWidth();
		
		//long[][] out=new long[h*w][iter];//+2 para guardar las pos fila col del pixel de la matrix
		StructuringElement se=null;
		int seSize=seMin;
		FeatureMatrix outF=new FeatureMatrix(h, w);
		outF=initialize(outF, iter);
		for (int i = 0; i < iter; i++) {
			
			se=StructuringElementFactory.getInstance().buildStrel(strelFigure, seSize+i);
			outF=featureFor(im, se, winH, winW, slide,outF,i);
			
		}
		
		
		
		return outF;
		
	}

	private static FeatureMatrix initialize(FeatureMatrix outF, int feautreSize) {
		for (int i = 0; i < outF.getHeight(); i++) {
			for (int j = 0; j < outF.getWidth();j++) {
				outF.set(i, j, new FeatureVector(i, j, feautreSize));
			}
		}
		return outF;
	}

}
