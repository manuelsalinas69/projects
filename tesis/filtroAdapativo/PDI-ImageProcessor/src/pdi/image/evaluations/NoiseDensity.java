package pdi.image.evaluations;

public class NoiseDensity {
	
	public static double evaluate(int[][] filterImage,int[][] originalImage){
		double sum=0;
		double iSum=0;
		int N=originalImage.length*originalImage[0].length;
		for (int i = 0; i < originalImage.length; i++) {
			for (int j = 0; j < originalImage[0].length; j++) {
				sum+=Math.pow(filterImage[i][j]-originalImage[i][j],2);
				iSum+=Math.pow(originalImage[i][j], 2);
			}
		}
		//System.out.println("N: "+N);
		//System.out.println("Sum: "+sum);
		return new Double((((double)sum)/(double)N)/iSum);
	}

}
