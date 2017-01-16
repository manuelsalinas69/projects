package pdi.ppm.operations;

import java.io.IOException;
import java.util.List;

import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.ImageStatisticData;
import pdi.ppm.model.Pixel;
import pdi.ppm.model.PreProccesorResult;
import pdi.ppm.util.Utils;

public class PreProccessor {

	 public ImageMatrix optimalSubMatrix(ImageMatrix im, int i, int rangeF, int j, int rangeC) throws Exception{
		 int range=5;
			//obtenemos la varianza de los extremos
			ImageMatrix inf=im.subMatrix(i, rangeF-range, j, rangeC-range);
			ImageMatrix piv=im.subMatrix(i, rangeF, j, rangeC);
			ImageMatrix sup=im.subMatrix(i, rangeF+range, j, rangeC+range);
			
			//ImageStatisticData infData=ImageStatistics.proccess(inf);
			ImageStatisticData pivData=ImageStatistics.proccess(piv);
			//ImageStatisticData supData=ImageStatistics.proccess(sup);
			
			if (isUmbralMinor(pivData)) 
			{
				Utils.getInstance().showImage(piv);
			}
//			else{
//				
//			}
			
//			System.out.println("INF");
//			for (Pixel pxi : inf.toList()) {
//				System.out.println(pxi);
//			}
//			System.out.println("PIV");
//			for (Pixel pxi : piv.toList()) {
//				System.out.println(pxi);
//			}
//			System.out.println("SUP");
//			for (Pixel pxi : sup.toList()) {
//				System.out.println(pxi);
//			}
//			
//			System.out.println("INF DATA: "+infData);
//			System.out.println("PIV DATA: "+pivData);
//			System.out.println("SUP DATA: "+supData);
//			
//			Utils.getInstance().showImage(inf);
//			Utils.getInstance().showImage(piv);
//			Utils.getInstance().showImage(sup);
			return null;
	 }

	private boolean isUmbralMinor(ImageStatisticData imStData){
		//evaluacion porcentual
//		return (imStData.getRData().getStDvMeanPercent()<=PPMConstanst.UMBRAL_STDV_PERCENT
//				&& imStData.getGData().getStDvMeanPercent()<=PPMConstanst.UMBRAL_STDV_PERCENT
//				&& imStData.getGData().getStDvMeanPercent()<=PPMConstanst.UMBRAL_STDV_PERCENT);
		//evaluacion numerica
		return (imStData.getRData().getStDv()<=PPMConstanst.UMBRAL_STDV
				&& imStData.getGData().getStDv()<=PPMConstanst.UMBRAL_STDV
				&& imStData.getGData().getStDv()<=PPMConstanst.UMBRAL_STDV);
	}
	 
	
	public PreProccesorResult preProccess(ImageMatrix im, int i, int rangeF, int j, int rangeC) throws Exception{
		ImageMatrix slideWindow=im.subMatrix(i, rangeF, j, rangeC);
		ImageStatisticData pivData=ImageStatistics.proccess(slideWindow);
		int skipCols=0;
		int skipRows=0;
		if (isUmbralMinor(pivData)) 
		{
			//skipRows=Math.max(1, slideWindow.getHeight());
			skipRows=0;
			skipCols=Math.max(1, slideWindow.getWidth());
			//System.out.println("Grupo de pixels similares encontrado i0:"+i+", i1:"+rangeF+" - j0:"+j+", j1:"+rangeC);
			//Utils.getInstance().showImage(piv);
		}
		else{
			skipCols=1;
			skipRows=1;
		}
		
		return new PreProccesorResult(slideWindow, skipCols, skipRows, pivData);
	}
}
