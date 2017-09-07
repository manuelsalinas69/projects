package una.pdi.AWFGA.ga.model;

import org.uma.jmetal.solution.IntegerSolution;

import pdi.image.evaluations.FussyMetrica;
import pdi.image.evaluations.MAE;
import pdi.image.evaluations.NoiseDensity;
import pdi.image.util.DataProvider;
import una.pdi.AWFGA.ga.util.AWFGAUtils;

public class EvaluadorFiltro {
	
	public static double evaluarIndividuo(IntegerSolution solution){
		;
		int[][] filterResult=AWFGAUtils.applySolution(solution);
		//double maeValue=MAE.evaluate(filterResult, DataProvider.getInstance().getBaseImageArray());
//		double noiseDensityValue=NoiseDensity.evaluate(filterResult, DataProvider.getInstance().getBaseImageArray());
//	
//		return (DataProvider.getInstance().getMAEFitnessPercent()*maeValue
//				+DataProvider.getInstance().getNoiseDensityFitnessPercent()*noiseDensityValue);//*-1 intenton fallido por mejorar evaluacion;
		double fussy=FussyMetrica.evaluate(filterResult);
		return fussy;
	}

	

}
