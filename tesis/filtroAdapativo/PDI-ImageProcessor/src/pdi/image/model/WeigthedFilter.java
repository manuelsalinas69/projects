package pdi.image.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeigthedFilter extends BaseFilter {

	public WeigthedFilter(int[][] filter) {
		super(filter);
		
	}

	@Override
	public int processFilter(int[][] pixels, int pixel) {
		List<Integer> values= new ArrayList<Integer>();
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[0].length; j++) {
				for (int j2 = 0; j2 < filter[i][j]; j2++) {
					values.add(pixels[i][j]);
				}
			}
		}
		Integer[] a=new Integer[values.size()];
		values.toArray(a);
		Arrays.sort(a);
		return a[a.length/2];
	}

	@Override
	protected int getBorderValue() {
		return 0;
	}

}
