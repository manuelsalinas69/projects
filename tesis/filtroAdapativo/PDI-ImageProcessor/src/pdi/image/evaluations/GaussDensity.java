package pdi.image.evaluations;

public class GaussDensity {
	
	public static double evaluate(double a, double b, double c2, int x){
		return a*Math.pow(Math.E, 
						-1*(
								Math.pow(x-b, 2)
								/
								(2*c2)
							)
					);
	}

}
