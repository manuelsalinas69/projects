package pdi.image.util;

import java.util.Arrays;

public class MaskCreator {
	
	public static int[][] buildIntMask(int row, int col, int value){
		int[][] mask= new int[row][col];
		
		for (int i = 0; i < mask.length; i++) {
			Arrays.setAll(mask[i], k->value);
		}
		
		return mask;
	}

}
