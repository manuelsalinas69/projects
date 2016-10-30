package pdi.ppm.operations;

import pdi.ppm.model.Extremos;
import pdi.ppm.model.Pixel;

public class PseudoErode extends BaseOperation{

	@Override
	public Pixel processExtremos(Extremos extremos) {
		Pixel p= new Pixel(extremos.getExMin()[0]);
		return p;
	}

}
