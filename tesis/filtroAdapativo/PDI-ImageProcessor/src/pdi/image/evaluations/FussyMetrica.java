package pdi.image.evaluations;

import pdi.image.model.FussyFilter;
import pdi.image.util.DataProvider;
import pdi.image.util.MaskCreator;

public class FussyMetrica {
	
	public static int UMBRAL=40;
	
	public static int[][] filter=MaskCreator.buildIntMask(DataProvider.getInstance().getNoiseEstimationFilter()
			, DataProvider.getInstance().getNoiseEstimationFilter(), 1);//{{1,1,1},{1,1,1},{1,1,1}};
	
	public static double evaluate(int[][] filteredImage){
		double sum=0;
		FussyFilter f= new FussyFilter(filter);
		int[][] fussyResult=f.applyFilter(filteredImage);
		//String v=null;
		for (int i = 0; i < filteredImage.length; i++) {
			for (int j = 0; j < filteredImage[0].length; j++) {
//				if (filteredImage[i][j]==255 || filteredImage[i][j]==0) {
//					System.out.println("Validando si es ruido...d");
//				}
				if (fussyResult[i][j]<DataProvider.getInstance().getNoiseProbability()) {
					sum++;
				}
				
				//sum+=fussyResult[i][j]>UMBRAL?1:0;
				//System.out.println("pixel: "+filteredImage[i][j]+", Pertencia: "+fussyResult[i][j]+", suma:"+v);
			}
		}
		return sum;
	}

}
