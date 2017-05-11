package pdi.image.evaluations;

public class NoiseDensity {
	
	public static double evaluate(int[][] noisyImage,int[][] originalImage){
		double sum=0;
		int N=originalImage.length*originalImage[0].length;
		for (int i = 0; i < originalImage.length; i++) {
			for (int j = 0; j < originalImage[0].length; j++) {
				sum+=Math.pow(noisyImage[i][j]-originalImage[i][j],2);
			}
		}
		//System.out.println("N: "+N);
		//System.out.println("Sum: "+sum);
		return new Double(((double)sum)/(double)N);
	}

}
