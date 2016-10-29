package pdi.ppm.util;

import pdi.ppm.model.StructuringElement;

public class StructuringElementFactory {
	
	private static StructuringElementFactory instance= new StructuringElementFactory();
	
	public static StructuringElementFactory getInstance(){
		return instance;
	}
	
	public StructuringElement buildStrel(String figure, int dim) throws Exception{
		if (dim<=0) {
			throw new Exception("La dimension para el elemento estructurante no es valida, debe ser mayor a 0");
		}
		
		
		return build0(figure, dim);
	}

	private StructuringElement build0(String figure, int dim) throws Exception {
		switch (figure) {
		case "square":
			return buildSquareStrel(dim);
			

		default:
			throw new Exception("Figura no implementada: "+figure);
			//return buildSquareStrel(dim);
			
		}
		
	}

	private StructuringElement buildSquareStrel(int dim) {
		int[] row= new int[dim];
		int[][] nhood= new int[dim][dim];
		for (int i = 0; i < row.length; i++) {
			row[i]=1;
		}
		
		for (int j=0;j<dim;j++)
			nhood[j]=row;
		
		return new StructuringElement(nhood);
	}
	
	

}
