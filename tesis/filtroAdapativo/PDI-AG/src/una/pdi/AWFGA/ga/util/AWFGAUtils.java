package una.pdi.AWFGA.ga.util;

import org.uma.jmetal.solution.IntegerSolution;

import pdi.image.model.WeigthedFilter;
import pdi.image.util.DataProvider;

public class AWFGAUtils {
	
	public static int[][] applySolution(IntegerSolution solution){
		int[][] filtro=AWFGAUtils.parseToFilter(solution);
		WeigthedFilter wf=new WeigthedFilter(filtro);
		int[][] filterResult=wf.applyFilter(DataProvider.getInstance().getBaseImageArray());
		return filterResult;
	}

	public static int[][] parseToFilter(IntegerSolution solution) {
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
