package pdi.ppm.operations;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.ChebyshevResult;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.PCAResult;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.util.PCAUtils;

public class ReferencesFactory {
	
	
	public static List<ReferenceVector> getReferences(ImageMatrix im){

		boolean print=false;
		if (print) {
			System.out.println("PRINT SET TO TRUE: References.getReferences");
		}
		PCAResult r=PCAUtils.PCA2(im);
		
		if (print) {
			r.getSc().print(1, 1);
		}
		
		
		
		ReferenceVector r0=getReferenceVector(r.getSc(), 0, PPMConstanst.K);
		ReferenceVector r1=getReferenceVector(r.getSc(), 1, PPMConstanst.K);
		ReferenceVector r2=getReferenceVector(r.getSc(), 2, PPMConstanst.K);
		
		//Matrix inverse=r.getEiVec().inverse();
//		r0=passToInitialCoordinates(r0,inverse,r.getCenters(),print);
//		r1=passToInitialCoordinates(r1,inverse,r.getCenters(),print);
//		r2=passToInitialCoordinates(r2,inverse,r.getCenters(),print);
		
		r0=passToInitialCoordinates(r0,r,print);
		r1=passToInitialCoordinates(r1,r,print);
		r2=passToInitialCoordinates(r2,r,print);
		System.out.println("R0: "+r0);
		System.out.println("R1: "+r1);
		System.out.println("R2: "+r2);
	
		List<ReferenceVector> l=new ArrayList<ReferenceVector>();
		l.add(r0);
		l.add(r1);
		l.add(r2);
		
		return l;
	}

	private static ReferenceVector passToInitialCoordinates(ReferenceVector r0, PCAResult r, boolean print) {
		double[]maxData=r.getPca().eigenToSampleSpace(r0.getrMaxD()[0]);
		double[]minData=r.getPca().eigenToSampleSpace(r0.getrMinD()[0]);
		int[][] min= new int[1][3];
		int[][] max= new int[1][3];
		
		for (int i = 0; i <3; i++) {
			min[0][i]=(int)minData[i];
			max[0][i]=(int)maxData[i];
		}
		r0.setrMin(min);
		r0.setrMax(max);
		
		return r0;
	}

	private static ReferenceVector passToInitialCoordinates(ReferenceVector r, Matrix inverse, double[] ds, boolean print) {
		Matrix xMin=new Matrix(r.getrMinD());
		Matrix xMax=new Matrix(r.getrMaxD());
		Matrix mMin=xMin.times(inverse);
		Matrix mMax=xMax.times(inverse);
		//double[][] min={{mMin.getArray()[0][0]+ds[0],mMin.getArray()[0][1]+ds[1],mMin.getArray()[0][2]+ds[2]}};
		//double[][] max={{mMax.getArray()[0][0]+ds[0],mMax.getArray()[0][1]+ds[1],mMax.getArray()[0][2]+ds[2]}};
		
		int[][] min= new int[xMin.getArray().length][xMin.getArray()[0].length];
		int[][] max= new int[xMax.getArray().length][xMax.getArray()[0].length];
		
		for (int i = 0; i < xMin.getArray()[0].length; i++) {
			min[0][i]=(int)mMin.getArray()[0][i];
			max[0][i]=(int)mMax.getArray()[0][i];
		}
		r.setrMin(min);
		r.setrMax(max);
		return r;
	}

	private static ReferenceVector getReferenceVector(Matrix sc, int i, double k) {
		
		ChebyshevResult cbsR1 = Chebyshev.proccess(sc, i, PPMConstanst.K,false);
		double[][] vecBeta= {{0d,0d,0d}};
		double[][] vecAlfa= {{0d,0d,0d}};
		
		double betaAlfa =cbsR1.getBeta()-cbsR1.getAlfa();
		
		vecAlfa[0][i]=cbsR1.getAlfa();
		vecBeta[0][i]=cbsR1.getBeta();

		if (betaAlfa>=0) {
			return new ReferenceVector(vecAlfa,vecBeta);
		}
		else{
			new ReferenceVector(vecBeta,vecAlfa);
		}
		
		return new ReferenceVector(vecAlfa,vecBeta);
	}
	
	

}
