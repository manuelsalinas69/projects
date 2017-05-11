package pdi.image.evaluations;

public class MAE {

	public static double evaluate(int[][] noisyImage,int[][] originalImage){
		int sum=0;
		int N=originalImage.length*originalImage[0].length;
		for (int i = 0; i < originalImage.length; i++) {
			for (int j = 0; j < originalImage[0].length; j++) {
				sum+=Math.abs(noisyImage[i][j]-originalImage[i][j]);
			}
		}
		//System.out.println("N: "+N);
		//System.out.println("Sum: "+sum);
		return new Double((1d/(3d*(double)N)*(double)sum));
	}
}
