package pdi.ppm.operations;

import Jama.Matrix;
import pdi.ppm.model.ChebyshevResult;
import pdi.ppm.util.Utils;

public class Chebyshev {

	
	
	
	public static ChebyshevResult proccess(Matrix X, int col, double K){
		double mean=Utils.getInstance().getMean(X,col);//media de la primera columna
		double stdv=Utils.getInstance().getStDv(X,col, mean);
		double min=mean-K*stdv;
		double max=mean+K*stdv;
		return new ChebyshevResult(min, max);
	}


	
}
