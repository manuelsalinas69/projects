package pdi.image.evaluations;

import pdi.image.model.BaseFilter;
import pdi.image.util.DataProvider;
import pdi.image.util.MaskCreator;

public class RAMFEvaluator {

	public static int[][] filter=MaskCreator.buildIntMask(DataProvider.getInstance().getNoiseEstimationFilter()
			, DataProvider.getInstance().getNoiseEstimationFilter(), 1);//{{1,1,1},{1,1,1},{1,1,1}};
	
	public static double evaluate(int[][] filteredImage){
		double sum=0;
		BaseFilter f= new RAMFEvaluatorFilter(filter);
		int[][] result=f.applyFilter(filteredImage);
		//String v=null;
		for (int i = 0; i < filteredImage.length; i++) {
			for (int j = 0; j < filteredImage[0].length; j++) {
				sum=+result[i][j];
				
				//sum+=fussyResult[i][j]>UMBRAL?1:0;
				//System.out.println("pixel: "+filteredImage[i][j]+", Pertencia: "+fussyResult[i][j]+", suma:"+v);
			}
		}
		return sum;
	}
}
