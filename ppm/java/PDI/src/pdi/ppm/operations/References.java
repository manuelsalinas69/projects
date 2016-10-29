package pdi.ppm.operations;

import java.util.List;

import Jama.Matrix;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.ChebyshevResult;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.Pixel;
import pdi.ppm.model.ReferenceVector;
import smile.projection.PCA;

public class References {
	
	
	public static List<ReferenceVector> getReferences(ImageMatrix im){
		List<Pixel> pixels=im.toList();
		
		double[][] data= new double[pixels.size()][3];
		for (int i = 0; i < data.length; i++) {
			data[i]=pixels.get(i).toVector();
		}
		smile.projection.PCA pca= new PCA(data);
//		double[][] projection = pca.getProjection();
//		double[][] pj= new double [3][3];
//		if (projection.length<3) {
//			double[] zeroRow={1d,1d,1d};
//			for (int i = 0; i < 3; i++) {
//				if ((i+1)<=projection.length) {
//					pj[i]=projection[i];
//				}
//				else{
//					
//					pj[i]=zeroRow;
//				}
//			}
//			
//		}
//		Matrix m= new Matrix(pj);
//		
//		Matrix inverse=m.inverse();//.times(new Matrix(data));
//		inverse.print(10, 2);
//		Matrix scores=inverse.times(new Matrix(data));
//		scores.print(10, 2);
		//double[] projec = pca.project(data[0]);
		Matrix mxData= new Matrix(data);
		
		Matrix eiVec= new Matrix(pca.getLoadings());
		//eiVec.print(10, 2);
		
		//mxData.getMatrix(arg0, arg1, arg2)
		
		Matrix sc=mxData.times(eiVec);
		
		ReferenceVector r0=getReferenceVector(sc, 0, PPMConstanst.K);
		ReferenceVector r1=getReferenceVector(sc, 1, PPMConstanst.K);
		ReferenceVector r2=getReferenceVector(sc, 2, PPMConstanst.K);
		
		//pasar a los coordenadas iniciales
		//  pca.getCenter();
		Matrix inverse=eiVec.inverse();
		r0=passToInitialCoordinates(r0,inverse);
		r1=passToInitialCoordinates(r1,inverse);
		r2=passToInitialCoordinates(r2,inverse);
		
		
		
//		for (int i = 0; i < scData.length; i++) {
//			System.out.println(scData[i][0]);
//		}
		//PCA p=Utils.getInstance().getPCA();
		//Matrix transfData=Utils.getInstance().getScores(pixels, p);
		return null;
	}

	private static ReferenceVector passToInitialCoordinates(ReferenceVector r, Matrix inverse) {
		Matrix xMin=new Matrix(r.getrMin());
		Matrix xMax=new Matrix(r.getrMax());
		Matrix mMin=xMin.times(inverse);
		Matrix mMax=xMax.times(inverse);
		r.setrMin(mMin.getArray());
		r.setrMax(mMax.getArray());
		return r;
	}

	private static ReferenceVector getReferenceVector(Matrix sc, int i, double k) {
		
		ChebyshevResult cbsR1 = Chebyshev.proccess(sc, 0, PPMConstanst.K);
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
