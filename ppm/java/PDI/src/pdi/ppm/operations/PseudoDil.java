package pdi.ppm.operations;

import pdi.ppm.model.Extremos;
import pdi.ppm.model.Pixel;

public class PseudoDil extends BaseOperation{
	@Override
	public Pixel processExtremos(Extremos extremos) {
		Pixel p= new Pixel(extremos.getExMax()[0]);
		return p;
	}
}
