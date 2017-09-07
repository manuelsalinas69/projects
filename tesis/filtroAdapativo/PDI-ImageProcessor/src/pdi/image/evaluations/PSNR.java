package pdi.image.evaluations;

public class PSNR {
	public static double evaluate(int[][] noisyImage,int[][] originalImage){
		double sum=0;
		double NxM=originalImage.length*originalImage[0].length;
		for (int i = 0; i < originalImage.length; i++) {
			for (int j = 0; j < originalImage[0].length; j++) {
				sum+=Math.pow(noisyImage[i][j]-originalImage[i][j],2);
			}
		}
		//System.out.println("N: "+N);
		//System.out.println("Sum: "+sum);
		return 20*Math.log(255d/Math.sqrt((1/NxM)*sum));
	}
}
