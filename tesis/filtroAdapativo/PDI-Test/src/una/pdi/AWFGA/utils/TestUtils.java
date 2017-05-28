package una.pdi.AWFGA.utils;

import java.util.Arrays;

public class TestUtils {

	public static int[][] parseToArray(String text, int sizeF, int sizeC,boolean printArray){
		String [] data=text.trim().split(" ");
		int[][] result=new int[sizeF][sizeC];
		int pos=0;
		for (int i = 0; i < sizeF; i++) {
			for (int j = 0; j < sizeC; j++) {
				result[i][j]=Integer.parseInt(data[pos]);
				pos++;
			}
			if (printArray) {
				System.out.println(Arrays.toString(result[i]));
			}
			
		}
		return result;
	} 
}
