package pdi.ppm.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Jama.Matrix;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.ChebyshevResult;
import pdi.ppm.model.Extremos;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.PCAResult;
import pdi.ppm.model.Pixel;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.model.StructuringElement;
import pdi.ppm.util.PCAUtils;
import pdi.ppm.util.Utils;

public abstract class BaseOperation {
	
	public ImageMatrix process(ImageMatrix im,StructuringElement se){
		
		List<Pixel> intersect;
		Pixel[][] pixels=new Pixel[im.getHeight()][im.getWidth()];
		
		for (int i = 0; i < im.getHeight(); i++) {
			for (int j = 0; j < im.getWidth(); j++) {
				
				Pixel p=im.getPixel(i, j);
//				boolean print=p.R==61 
//						&& p.G==60
//						&& p.B==58;
				boolean print=false;
				if (print) {
					System.out.println("i:"+i+",j:"+j+" - "+p);
				}
//				
//				
//				if (i==10 && j==50) {
//					System.out.println("----");
//			  }
				intersect= getIntersection(im,se,i,j,print);
				if (print) {
					System.out.println("----");
					for (Pixel pxi : intersect) {
						System.out.println(pxi);
					}
				}
//				//Math.sqrt(intersect.size());
//				//Utils.getInstance().printPixels(intersect,6,6);
				Pixel out= baseOper0(intersect,print);
				pixels[i][j]=out;
			}
		}
		return new ImageMatrix(pixels, pixels.length, pixels[0].length);
	}

	private Pixel baseOper0(List<Pixel> intersect, boolean print)  {
		
		
		PCAResult r=PCAUtils.PCA2(intersect);
		//Matrix inverse=r.getEiVec().inverse();
		
		
		ChebyshevResult cbr=Chebyshev.proccess(r.getSc(), 0, PPMConstanst.K,print);
		double[][] alfa_pca={{cbr.getAlfa(),0d,0d}};
		double[][] beta_pca={{cbr.getBeta(),0d,0d}};
		double[][] alfa=null;
		double[][] beta=null;
//		alfa=parseToInitialCoordinates(alfa_pca,inverse,r.getCenters());
//		beta=parseToInitialCoordinates(beta_pca,inverse,r.getCenters());
		
		alfa=parseToInitialCoordinates(alfa_pca, r);
		beta=parseToInitialCoordinates(beta_pca, r);
		Extremos extremos=ordenarExtremos(alfa,beta);
		
		if (print) {
			r.getSc().print(1, 1);
			r.getEiVec().print(1, 1);
			//inverse.print(1, 1);
			System.out.println("alfa_pca:"+Arrays.toString(alfa_pca[0]));
			System.out.println("alfa:"+Arrays.toString(alfa[0]));
			System.out.println("beta_pca:"+Arrays.toString(beta_pca[0]));
			System.out.println("beta:"+Arrays.toString(beta[0]));
			System.out.println(extremos);
		}
		
		
		return processExtremos(extremos);
	}

	public abstract Pixel processExtremos(Extremos extremos);

	private double[][] parseToInitialCoordinates(double[][] pcaCoorData, Matrix inverse, double[] ds) {
		Matrix m= new Matrix(pcaCoorData);
		//m.print(2, 2);
		Matrix initCoorDataMx= m.times(inverse);
		//initCoorDataMx.print(2, 2);
		double[][] r= initCoorDataMx.getArray();
		//double [][] r1={{r[0][0]+ds[0],r[0][1]+ds[1],r[0][2]+ds[2]}};
		//return r1;
		return r;
	}
	
	private double[][] parseToInitialCoordinates(double[][] pcaCoorData, PCAResult r) {
		double[] d= r.getPca().eigenToSampleSpace(pcaCoorData[0]);
		double [][]dd= {d};
		return dd;
	}

	private Extremos ordenarExtremos(double[][] alfa, double[][] beta) {
		double[][] exMin=null;
		double[][] exMax=null;
		double[][] vecBetaAlfa={{beta[0][0]-alfa[0][0],
								beta[0][1]-alfa[0][1],
								beta[0][2]-alfa[0][2]}};
		double[][] vBetaR;
		double[][] vAlfaR;
		
		double dotBeta;
		double dotAlfa;
		
		for (ReferenceVector v : PPMConstanst.referenceVectors) {
			try {
				if (Utils.getInstance().dot(vecBetaAlfa, v.vectorMaxMinD())==0) {
					continue;
				}
				vBetaR=Utils.getInstance().toVector(v.getrMinD(), beta);
				vAlfaR=Utils.getInstance().toVector(v.getrMinD(), alfa);
				
				dotBeta=Utils.getInstance().dot(vBetaR, v.vectorMaxMinD());
				dotAlfa=Utils.getInstance().dot(vAlfaR, v.vectorMaxMinD());
				
				if (dotBeta>dotAlfa) {
					exMax=beta;
					exMin=alfa;
					return new Extremos(exMin, exMax);
				}
				else{
					exMax=alfa;
					exMin=beta;
					return new Extremos(exMin, exMax);
				}
				
			} catch (Exception e) {
				exMin=alfa;
				exMax=beta;
				e.printStackTrace();
			}
		}
		if (exMax==null || exMin==null) {
			exMin=alfa;
			exMax=beta;
		}
		return new Extremos(exMin, exMax);
	}

	private List<Pixel> getIntersection(ImageMatrix im, StructuringElement se, int f, int c , boolean print) {
		int h=im.getHeight();
		int w=im.getWidth();
		int startF=Math.max(f-se.getMetaData().getDistOrigenFilas(), 0);
		int startC=Math.max(c-se.getMetaData().getDistOrigenFilas(), 0);
		int endF=Math.min(f+se.getMetaData().getDistFinFilas(), (h-1));
		int endC=Math.min(c+se.getMetaData().getDistFinColumnas(), (w-1));
		
		int row;
		int col;
		
		List<Pixel> l= new ArrayList<Pixel>();
		
		
		for (int j = startC; j <= endC; j++) {
			for (int i = startF; i <= endF; i++) {
				row=se.getMetaData().getCenterRow()-1-(f-i);
				col=se.getMetaData().getCenterCol()-1-(c-j);
				if (se.get(row, col)==1) {
					if (print) {
						System.out.println("i:"+i+",j:"+j);
					}
					l.add(im.getPixel(i, j));
				}
			}
		}
		
		return l;
		
	}
	
	
	
}
