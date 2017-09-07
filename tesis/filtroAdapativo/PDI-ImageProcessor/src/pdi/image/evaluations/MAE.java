package pdi.image.evaluations;

public class MAE {

	public static double evaluate(int[][] filteredImage,int[][] baseImage){
		double sum=0;
		double MAE_MAX=255;
		double NxM=baseImage.length*baseImage[0].length;
		for (int i = 0; i < baseImage.length; i++) {
			for (int j = 0; j < baseImage[0].length; j++) {
				sum+=Math.abs(filteredImage[i][j]-baseImage[i][j]);
				
			}
		}
		//System.out.println("N: "+N);
		//System.out.println("Sum: "+sum);
		double MAE=sum/NxM;
		return MAE;//(MAE/MAE_MAX);
	}
}
