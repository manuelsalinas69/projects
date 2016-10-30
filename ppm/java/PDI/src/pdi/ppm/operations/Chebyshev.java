package pdi.ppm.operations;

import Jama.Matrix;
import pdi.ppm.model.ChebyshevResult;
import pdi.ppm.util.Statistic;

public class Chebyshev {

	
	
	
	public static ChebyshevResult proccess(Matrix X, int col, double K, boolean print){
		//X.print(2, 2);
		int h=X.getRowDimension();
		double[] data=new double[h];
		
		for (int i = 0; i < data.length; i++) {
			data[i]=X.getArray()[i][col];
			if (print) {
				System.out.println(data[i]);
			}
		}
		Statistic st=new Statistic(data);
		
		double mean=st.getMean();//Utils.getInstance().getMean(X,col);//media de la primera columna
		double stdv=st.getStdDev();
		double min=mean-K*stdv;
		double max=mean+K*stdv;
		if (print) {
			System.out.println("mean: "+mean);
			System.out.println("stdv: "+stdv);
			System.out.println("min:"+min);
			System.out.println("max:"+max);
		}
		
		return new ChebyshevResult(min, max);
	}


	
}
