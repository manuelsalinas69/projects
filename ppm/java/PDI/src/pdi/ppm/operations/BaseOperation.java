package pdi.ppm.operations;

import java.util.ArrayList;
import java.util.List;

import com.mkobos.pca_transform.PCA;
import com.mkobos.pca_transform.PCA.TransformationType;

import Jama.Matrix;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.ChebyshevResult;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.Pixel;
import pdi.ppm.model.StructuringElement;

public class BaseOperation {
	
	public ImageMatrix process(ImageMatrix im,StructuringElement se){
		
		List<Pixel> intersect;
		for (int i = 0; i < im.getHeight(); i++) {
			for (int j = 0; j < im.getWidth(); j++) {
				intersect= getIntersection(im,se,i,j);
				Pixel p= baseOper0(intersect);
				break;
			}
			break;
		}
		return null;
	}

	private Pixel baseOper0(List<Pixel> intersect) {
		
		
		
		double[][] data= new double[intersect.size()][3];
		for (int i = 0; i < data.length; i++) {
			data[i]=intersect.get(i).toVector();
		}
		Matrix m= new Matrix(data);
		Matrix trainindData= new Matrix(new double[][] {
            {1, 2, 3},
            {3, 2, 1}});
		PCA p=new PCA(trainindData);
		
		Matrix transData=p.transform(m, TransformationType.WHITENING);
		
		
		Matrix inverse=p.getEigenvectorsMatrix().inverse();
		ChebyshevResult cr=Chebyshev.proccess(transData, 0, PPMConstanst.K);
		
//		p.enterScoresAsRowPerPerson(data);
//		p.analysis("pca.txt");
		
		return null;
	}

	private List<Pixel> getIntersection(ImageMatrix im, StructuringElement se, int f, int c ) {
		int h=im.getHeight();
		int w=im.getWidth();
		int startF=Math.max(f-se.getMetaData().getDistOrigenFilas(), 0);
		int startC=Math.max(c-se.getMetaData().getDistOrigenFilas(), 0);
		int endF=Math.min(f+se.getMetaData().getDistFinFilas(), h);
		int endC=Math.min(c+se.getMetaData().getDistFinColumnas(), w);
		
		int row;
		int col;
		
		List<Pixel> l= new ArrayList<Pixel>();
		
		for (int i = startF; i < endF; i++) {
			for (int j = startC; j < endC; j++) {
				row=se.getMetaData().getCenterRow()-(f-i);
				col=se.getMetaData().getCenterCol()-(c-j);
				if (se.get(row, col)==1) {
					l.add(im.getPixel(i, j));
				}
			}
		}
		
		return l;
		
	}
	
	
	
}
