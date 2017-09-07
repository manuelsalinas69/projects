package pdi.image.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pdi.image.evaluations.Pertenencia;

public class FussyFilter extends BaseFilter{


	
	public FussyFilter(int[][] filter) {
		super(filter);
		
	}
	
	@Override
	public int processFilter(int[][] pixels, int pixel) {
		//double [] singleArray= new double[pixels.length*pixels[0].length];
		
		List<Double> pix= new ArrayList<Double>();
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[0].length; j++) {
				if(pixels[i][j]>=0)
					pix.add((double) pixels[i][j]);
			}
		}
		double[]a=new double[pix.size()];
		for (int i = 0; i < a.length; i++) {
			a[i]=pix.get(i).doubleValue();
		}
		int result= (int)Pertenencia.process(a,pixel);
//		if (result<5) {
//			System.out.println("Validando pertenencia..: pertencia:"+result+" pixel:"+pixel+", vecindad:"+Arrays.toString(pixels[0])
//				+","+Arrays.toString(pixels[1])
//				+","+Arrays.toString(pixels[2])
//			
//			);
//			//System.out.println(Arrays.toString(pixels));
//		}
		return result;
	}

	@Override
	protected int getBorderValue() {
		return -1;
	}

	
}
