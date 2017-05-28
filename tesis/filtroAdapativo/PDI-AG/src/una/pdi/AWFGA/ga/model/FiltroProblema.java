package una.pdi.AWFGA.ga.model;

import java.util.ArrayList;

import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

public class FiltroProblema extends AbstractIntegerProblem{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void evaluate(IntegerSolution solution) {
		double evaluation=EvaluadorFiltro.evaluarIndividuo(solution);
		solution.setObjective(0, evaluation);
		
		
	}

/**
 * @param tamVentana la medida del tamanho de la ventana del filtro, por defecto es 3
 * @param numObjectivos cantidad de objetivos a cumplir, por defecto es uno
 * 
 * **/
	public FiltroProblema(Integer tamanoVentana, Integer numObjectivos) {
		if (tamanoVentana==null || tamanoVentana<=0) {
			tamanoVentana=3;
		}
		
		if (numObjectivos==null || numObjectivos<=0) {
			numObjectivos=1;
		}
		setNumberOfVariables((int)Math.pow(tamanoVentana, 2));
		setNumberOfObjectives(numObjectivos);
		setLimits();
		
	}

private void setLimits() {
	 java.util.List<Integer> lowerLimit=new ArrayList<>() ;
	  java.util.List<Integer> upperLimit=new ArrayList<>() ;

	  for (int i = 0; i < getNumberOfVariables(); i++) {
		lowerLimit.add(1);
		upperLimit.add(10);
	}
	  setUpperLimit(upperLimit);
	  setLowerLimit(lowerLimit);
}

	
	
}
