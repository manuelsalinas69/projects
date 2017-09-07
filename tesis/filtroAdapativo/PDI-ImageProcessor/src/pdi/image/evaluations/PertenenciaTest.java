package pdi.image.evaluations;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class PertenenciaTest {

	
	
	public static double process(double[] pixels, int pixel){
		DescriptiveStatistics s= new DescriptiveStatistics(pixels);
		NormalDistribution nd= new NormalDistribution(s.getMean(), s.getStandardDeviation());
		
		nd.cumulativeProbability(pixel);
		System.out.println("Mean: "+s.getMean());
		System.out.println("Variance: "+s.getVariance());
		System.out.println("STDDV: "+s.getStandardDeviation());
		s.getPercentile(pixel);
		double a=1
				/(
						Math.pow(2*Math.PI, 1/s.getStandardDeviation())
				);
		double b=s.getMean();
		double c2=s.getVariance();
		for (double x : pixels) {
			double gauss=GaussDensity.evaluate(a, b, c2, (int)x);
			System.out.println("Pixel: "+x+", Gauss: "+(gauss*100)+ ", INT: "+(int)(gauss*100)
					+", Cumulative Probability: "+(nd.cumulativeProbability(x)*100)
					+", Density:"+(nd.density(x)*100));
		}
//		
		return GaussDensity.evaluate(a, b, c2, pixel)*100;
	}
	
	public static void main(String[] args) {
		double [] vecindario_gris={55, 255, 53,57, 57, 58,57, 57, 56, 56.6};
		process(vecindario_gris,57);
//		double [] b={57, 57, 57,57, 57, 56,56, 57, 56};
//		System.out.println(process(b,57));
	}
}
