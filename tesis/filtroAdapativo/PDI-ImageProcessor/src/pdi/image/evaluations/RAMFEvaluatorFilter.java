package pdi.image.evaluations;

import java.util.Arrays;

import pdi.image.model.RAMF;

public class RAMFEvaluatorFilter extends RAMF {

	public RAMFEvaluatorFilter(int[][] filter) {
		super(filter);
		// TODO Auto-generated constructor stub
	}


	@Override
	public int processFilter(int[][] pixels, int pivotPixel) {
		try {
			int[] sortedPixels=getSortedPixels(pixels);
			int xMin=getXMin(sortedPixels);
			int xMax=getXMax(sortedPixels);
			int eMin=tMinus(pivotPixel, xMin);
			int eMax=tPlus(pivotPixel, xMax);


			if(!truthTable2Resul(pivotPixel,xMin,xMax,eMin,eMax, sortedPixels)){//si no es ruido
				return 0;
			}
			else{
				return 1;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return 0;
	}
}
