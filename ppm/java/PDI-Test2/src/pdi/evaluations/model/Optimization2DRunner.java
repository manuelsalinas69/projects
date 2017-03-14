package pdi.evaluations.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import ij.process.ColorProcessor;
import pdi.jpa.conf.ParamsConf;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.FeatureMatrix;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.model.SlideWindowMap;
import pdi.ppm.operations.KmeansProccess;
import pdi.ppm.operations.ReferencesFactory;
import pdi.ppm.operations.VolumeFeature2D;
import pdi.ppm.util.Utils;

public class Optimization2DRunner extends ClassicRunner{

	
	@Override
	public void setParams(HashMap<String, Object> params) {
		super.setParams(params);
		this.params.put(ParamsConf.OPTMIZATION_UMBRAL_STDV,PPMConstanst.UMBRAL_STDV );//alambre para ver los parametros
		
	}

	@Override
	public void execute() {
		File f= new File(inputFile);
		try {
			ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
			long t1=System.currentTimeMillis();
			ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
			List<ReferenceVector> l= ReferencesFactory.getReferences(m);
			PPMConstanst.referenceVectors=l;

			SlideWindowMap swm=VolumeFeature2D.generateSlideWindowMap(m, winH, winW);
			long t2=System.currentTimeMillis();
			System.out.println("Elapsed Time [SlideWindowMap]: "+(t2-t1)+"ms.");
			
			FeatureMatrix fMatrix=VolumeFeature2D.buildFeatureVector(m, swm, strelMin, strelMax, strelShape);
			long t3=System.currentTimeMillis();
			
			
			System.out.println("Elapsed Time [FeatureMatrix]: "+(t3-t2)+"ms.");
			
			System.out.println("Elapsed Time [ALL]: "+(t3-t1)+"ms.");

			ImageMatrix kmeansOut=KmeansProccess.proccess(fMatrix, 2);
			File outFile=Utils.getInstance().saveImage(kmeansOut, outputDir);
			saveResult(outFile, t2, t3);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
