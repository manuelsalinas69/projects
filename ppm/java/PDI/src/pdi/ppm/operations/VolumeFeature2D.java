package pdi.ppm.operations;

import pdi.ppm.model.FeatureMatrix;
import pdi.ppm.model.FeatureVector;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.PreProccesorResult;
import pdi.ppm.model.SlideWindowMap;
import pdi.ppm.model.StructuringElement;
import pdi.ppm.util.StructuringElementFactory;
import pdi.ppm.util.Utils;

public class VolumeFeature2D {
	
	public static long getVolumeFeature(ImageMatrix m, StructuringElement se) throws Exception{
		BaseOperation e=new PseudoErode();
		BaseOperation d=new PseudoDil();
		ImageMatrix out=Utils.getInstance().minus(d.process(m, se), e.process(m, se));
		long v=Utils.getInstance().getVolume(out);
		return v;
		
	}
	
	public static FeatureMatrix featureFor(ImageMatrix im, StructuringElement se,SlideWindowMap swm,
			FeatureMatrix f, int zIndex) throws Exception{
		

		
		int rangeF=0;
		int rangeC=0;
		long vf=0;
		int i=0,j=0;
		int h=im.getHeight();
		int w=im.getWidth();
		int vectorPos=0;
		while (swm.getxVector()[vectorPos]>=0) {
			i=swm.getyVector()[vectorPos];
			j=swm.getxVector()[vectorPos];
			int winH=swm.getySlideWindow()[vectorPos];
			int winW=swm.getxSlideWindow()[vectorPos];
			rangeF=Math.min((h-1),i+winH-1);
			rangeC=Math.min((w-1), j+winW-1);
			
			//ImageMatrix sub1=preproccessMx(im,i,rangeF,j,rangeC);
			ImageMatrix sub=im.subMatrix(i, rangeF, j, rangeC);
			
			
			vf=getVolumeFeature(sub, se);
			//data[pos]=vf;
			
			f.setSubMatrix(i, rangeF, j, rangeC, vf,zIndex);
			vectorPos++;

			
		}
		
		return f;
	}
	
	
	

	public static SlideWindowMap generateSlideWindowMap(ImageMatrix im, int winH, int winW) throws Exception {
		int j=0;
		int i=0;
		int h=im.getHeight();
		int w=im.getWidth();
		
		int[] xVector=new int[h*w];
		int[] yVector=new int[h*w];
		int[] xSlideWindow= new int[h*w];
		int[] ySlideWindow= new int[h*w];
		int[] yFlagVector=new int[w];
		int vectorPos=0;
		int rangeF=0;
		int rangeC=0;
		
		for (int k = 0; k < xVector.length; k++) {
			xVector[k]=-1;
			yVector[k]=-1;
		}
//		xVector[0]=0;
//		yVector[0]=0;
//		xSlideWindow[0]=winW;
//		ySlideWindow[0]=winH;
//		vectorPos++;
		
		while (i<h) {
//			xVector[vectorPos]=0;
//			yVector[vectorPos]=i;
//			xSlideWindow[vectorPos]=winW;
//			ySlideWindow[vectorPos]=winH;
//			vectorPos++;
			while (j<w) {
				
				if ((yFlagVector[j]-i)>5) {//rango de solapamiento
					//System.out.println("yFlagVector[j]: "+yFlagVector[j]+",i:"+i);
					j++;
					continue;
				}
				
				xVector[vectorPos]=j;
				yVector[vectorPos]=i;
				

				rangeF=Math.min((h-1),i+winH-1);
				rangeC=Math.min((w-1), j+winW-1);
				
				PreProccessor p= new PreProccessor();
				PreProccesorResult ppr=p.preProccess(im, i, rangeF, j, rangeC);
				
//				xVector[vectorPos]=Math.min((w-1),j+ppr.getSkipCols());
//				yVector[vectorPos]=i;//ppr.getSkipRows();
				for (int k = j; k < j+ppr.getSkipCols(); k++) {
					yFlagVector[k]=i+ppr.getSkipRows();
				}
				xSlideWindow[vectorPos]=ppr.getOptimal().getWidth();
				ySlideWindow[vectorPos]=ppr.getOptimal().getHeight();
//				vectorPos++;
				
				j+=ppr.getSkipCols();
				vectorPos++;
			}
			j=0;
			i++;
			
		}
		return new SlideWindowMap(xVector, yVector, xSlideWindow, ySlideWindow);
	}

	public static FeatureMatrix buildFeatureVector(ImageMatrix im, SlideWindowMap swm, int seMin
			, int seMax, String strelFigure) throws Exception{
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
			outF=featureFor(im, se, swm,outF,i);
			
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
