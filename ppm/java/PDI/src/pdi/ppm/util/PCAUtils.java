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

		
		double[][] data= new double[pixels.size()][3];
		//System.out.println("---------------");
		for (int i = 0; i < data.length; i++) {
			data[i]=pixels.get(i).toVector();
			
			//System.out.println(Arrays.toString(data[i]));
		}
		smile.projection.PCA pca= new PCA(data);

		Matrix mxData= new Matrix(data);
		
		Matrix eiVec= new Matrix(pca.getLoadings());
		Matrix sc=mxData.times(eiVec);
		
		return new PCAResult(mxData, eiVec, sc,pca.getCenter());
	}

}
