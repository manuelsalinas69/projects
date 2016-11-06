package pdi.ppm.conf;

import java.util.List;

import Jama.Matrix;
import pdi.ppm.model.ReferenceVector;

public class PPMConstanst {

	public static final double K=1.5;
	public static final double[][] bwValues={{1d,1d,1d}};
	public static Matrix BW_AXIS=new Matrix(bwValues);
	public static List<ReferenceVector> referenceVectors;
//	public static ReferenceVector r0;
//	public static ReferenceVector r1;
//	public static ReferenceVector r2;
	
}
