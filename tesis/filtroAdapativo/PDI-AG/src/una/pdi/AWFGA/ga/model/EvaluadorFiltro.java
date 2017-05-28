package una.pdi.AWFGA.ga.model;

import org.uma.jmetal.solution.IntegerSolution;

import pdi.image.evaluations.MAE;
import pdi.image.evaluations.NoiseDensity;
import pdi.image.model.WeigthedFilter;
import pdi.image.util.DataProvider;

public class EvaluadorFiltro {
	
	public static double evaluarIndividuo(IntegerSolution solution){
		int[][] filtro=parseToFilter(solution);
		WeigthedFilter wf=new WeigthedFilter(filtro);
		int[][] filterResult=wf.applyFilter(DataProvider.getInstance().getBaseImageArray());
		double maeValue=MAE.evaluate(filterResult, DataProvider.getInstance().getBaseImageArray());
		double noiseDensityValue=NoiseDensity.evaluate(filterResult, DataProvider.getInstance().getBaseImageArray());
	
		return DataProvider.getInstance().getMAEFitnessPercent()*maeValue
				+DataProvider.getInstance().getNoiseDensityFitnessPercent()*noiseDensityValue;
	}

	private static int[][] parseToFilter(IntegerSolution solution) {
		int rows=solution.getNumberOfVariables()/DataProvider.getInstance().getFilterColumnSize();
		int columns=DataProvider.getInstance().getFilterColumnSize();
		int r=0,c=0,pos=0;
		int[][]filter=new int[rows][columns];
		while (r<rows) {
			while (c<columns) {
				filter[r][c]=solution.getVariableValue(pos);
				pos++;
				c++;
			}
			c=0;
			r++;
		}
		return filter;
	}

}
