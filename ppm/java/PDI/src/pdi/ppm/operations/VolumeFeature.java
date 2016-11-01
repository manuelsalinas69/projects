package pdi.ppm.operations;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
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
	
	public static long[] featureFor(ImageMatrix im, StructuringElement se, int winH,int winW,int slide){
		
		int i=0,j=0;
		
		int h=im.getHeight();
		int w=im.getWidth();
		long []data=new long[h*w];
		int rangeF=0;
		int rangeC=0;
		long vf=0;
		
		//Matrix out=new Matrix(data); 
		int pos=0;
		while (i<h) {
			while (j<w) {
				rangeF=Math.min((h-1),i+winH-1);
				rangeC=Math.min((w-1), j+winW-1);
				ImageMatrix sub=im.subMatrix(i, rangeF, j, rangeC);
				vf=getVolumeFeature(sub, se);
				data[pos]=vf;
				pos++;
				j+=j+slide;
			}
			j=0;
			i+=slide;
		}
		
		return data;
	}
	
	
	public static long[][] buildFeatureVector(ImageMatrix im, int winH, int winW, int seMin, int seMax, String strelFigure, int slide) throws Exception{
		int iter=seMax-seMin+1;
		
		int h=im.getHeight();
		int w=im.getWidth();
		
		long[][] out=new long[h*w][iter];//+2 para guardar las pos fila col del pixel de la matrix
		StructuringElement se=null;
		int seSize=seMin;
		List<long[]> features= new ArrayList<long[]>();
		long[] outF=null;
		for (int i = 0; i < iter; i++) {
			se=StructuringElementFactory.getInstance().buildStrel(strelFigure, seSize+i);
			outF=featureFor(im, se, winH, winW, slide);
			features.add(outF);
		}
		
		for (int i = 0; i < out.length; i++) {
			//out[i][1]=Math.max(0, Math.min(i%h, h));
			for (int j = 0; j < features.size(); j++) {
				out[i][j]=features.get(j)[i];
			}
		}
		
		return out;
		
	}

}
