package pdi.ppm.util;

import java.util.Arrays;
import java.util.List;

import Jama.Matrix;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.PCAResult;
import pdi.ppm.model.Pixel;
import smile.projection.PCA;

public class PCAUtils {
	
	public static PCAResult PCA(ImageMatrix im){
		return PCA(im.toList());
	}
	
	public static PCAResult PCA(List<Pixel> pixels){

		
		double[][] data=toArray(pixels);
		smile.projection.PCA pca= new PCA(data);

		Matrix mxData= new Matrix(data);
		
		Matrix eiVec= new Matrix(pca.getLoadings());
		Matrix sc=mxData.times(eiVec);
		
		return new PCAResult(mxData, eiVec, sc,pca.getCenter());
	}

	
	public static PCAResult PCA2(ImageMatrix im){
		return PCA2(im.toList());
	}
	
	public static PCAResult PCA2(List<Pixel> pixels){
		double[][] data=toArray(pixels);
		
		int cant=pixels.size();
		
		boolean b=cant<3;
		
		PrincipalComponentAnalysis pca= new PrincipalComponentAnalysis();
		pca.setup(data.length, data[0].length);
		double[][] pcaData=new double[data.length][data[0].length];
		for (int i = 0; i < data.length; i++) {
			pca.addSample(data[i]);
		}
		if (b) {
			pca.computeBasis(cant);
		}
		else{
			pca.computeBasis(3);
		}
		
		for (int i = 0; i < data.length; i++) {
			
				pcaData[i]=pca.sampleToEigenSpace(data[i]);
			
		}
		
		
		return new PCAResult(new Matrix(data), null, new Matrix(pcaData),null,pca);
	}
	
	
	
	private static double[][] toArray(List<Pixel> pixels){
		double[][] data= new double[pixels.size()][3];
		//System.out.println("---------------");
		for (int i = 0; i < data.length; i++) {
			data[i]=pixels.get(i).toVector();
			
			//System.out.println(Arrays.toString(data[i]));
		}
		return data;

	}
}
