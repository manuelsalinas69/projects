package pdi.ppm.model;

public class ChebyshevResult {

	double alfa;//chebyshev valor izquierda
	double beta;//chesyshev valor derecha
	
	
	
	public ChebyshevResult(double alfa, double beta) {
		super();
		this.alfa = alfa;
		this.beta = beta;
	}
	public double getAlfa() {
		return alfa;
	}
	public double getBeta() {
		return beta;
	}
	@Override
	public String toString() {
		return "ChebyshevResult [alfa=" + alfa + ", beta=" + beta + "]";
	}
	
	
	
	
	
	
	
}
