package pdi.image.evaluations;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Pertenencia {

	public static final double MIN_STDV=5;
	
	public static double process(double[] pixels, int pixel){
		DescriptiveStatistics s= new DescriptiveStatistics(pixels);
		//System.out.println("Mean: "+s.getMean());
		//System.out.println("Variance: "+s.getVariance());
		if (s.getStandardDeviation()<MIN_STDV) {
			return 100;
		}
//		if (s.getStandardDeviation()<5) {
//			//return 90;
//			System.out.println("....");
//		}
//		if (s.getStandardDeviation()<10) {
//			//return 90;
//			System.out.println("....");
//		}
		
		double a=1/(Math.pow(2*Math.PI, 1/Math.sqrt(s.getVariance())));
		double b=s.getMean();
		double c2=s.getVariance();
	
		return GaussDensity.evaluate(a, b, c2, pixel)*100;
	}
	
	public static void main(String[] args) {
		double [] a={100,100,100,120,255,90, 80, 81, 120};
		double [] b={57, 57, 57,57, 57, 56,56, 57, 56};
		System.out.println(process(b,57));
	}
}
