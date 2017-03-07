package pdi.evaluations.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import ij.process.ColorProcessor;
import pdi.evaluations.model.interfaces.RunnerInterface;
import pdi.jpa.conf.ParamsConf;
import pdi.jpa.facade.ResultsFacade;
import pdi.jpa.model.ExecutionResults;
import pdi.ppm.conf.PPMConstanst;
import pdi.ppm.model.FeatureMatrix;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.ReferenceVector;
import pdi.ppm.operations.KmeansProccess;
import pdi.ppm.operations.ReferencesFactory;
import pdi.ppm.operations.VolumeFeature;
import pdi.ppm.util.Utils;

public class ClassicRunner implements RunnerInterface {
	
	int winH;
	int winW;
	int strelMin;
	int strelMax;
	int chebyshevK;
	int slide;
	String strelShape;
	
	/*CONF_RESULTS*/
	String inputFile;
	String outputDir;
	//String imgFileExt;
	
	HashMap<String, Object> params;
	ExecutionResults executionsResult;
	
	boolean runnig=false;
	

	@Override
	public void run() {
		execute();
		ResultsFacade rf=new ResultsFacade();
		try {
			rf.saveResults(getExecutionResults());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setParams(HashMap<String, Object> params) {
		winH=Integer.parseInt(params.get(ParamsConf.WIN_SIZE_HEIGHT).toString());
		winW=Integer.parseInt(params.get(ParamsConf.WIN_SIZE_WIDTH).toString());
		slide=Integer.parseInt(params.get(ParamsConf.WIN_SLIDE).toString());
		strelMin=Integer.parseInt(params.get(ParamsConf.STREL_SIZE_MIN).toString());
		strelMax=Integer.parseInt(params.get(ParamsConf.STREL_SIZE_MAX).toString());
		strelShape=params.get(ParamsConf.STREL_SHAPE).toString();
		chebyshevK=Integer.parseInt(params.get(ParamsConf.PPM_CHEBYSHEV_K).toString());
		inputFile=params.get(ParamsConf.IMG_INPUT_FILE).toString();
		outputDir=params.get(ParamsConf.IMG_OUTPUT_DIR).toString();
		//imgFileExt=params.get(ParamsConf.IMG_INPUT_FILE_EXT).toString();
		this.params=params;

		
		
	}

	@Override
	public ExecutionResults getExecutionResults() {
		
		return executionsResult;
	}

	@Override
	public void execute() {
		runnig=true;
		try {
			File f= new File(inputFile);
			ColorProcessor cp=new ColorProcessor(ImageIO.read(f));
			
			long t1=System.currentTimeMillis();
			ImageMatrix m= Utils.getInstance().parseToImageMatrix(cp);
			List<ReferenceVector> l= ReferencesFactory.getReferences(m);
			PPMConstanst.referenceVectors=l;

			FeatureMatrix fMatrix=VolumeFeature.buildFeatureVector(m, winH, winW, strelMin, strelMax, strelShape, slide);
			
			long t2=System.currentTimeMillis();
			//System.out.println("Elapsed Time: "+(t2-t1)+"ms.");
			
			ImageMatrix kmeansOut=KmeansProccess.proccess(fMatrix, 2);
			File outFile=Utils.getInstance().saveImage(kmeansOut,outputDir );
			saveResult(outFile,t1,t2);
			//Utils.getInstance().showImage(kmeansOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		runnig=false;
		
	}

	protected void saveResult(File outFile, long start, long end) {
		ExecutionResults r= new ExecutionResults();
		r.putResult(ParamsConf.IMG_OUTPUT_FILE, outFile);
		r.putResult(ParamsConf.PROCESS_START, start);
		r.putResult(ParamsConf.PROCESS_END, end);
		
		r.putParam(ParamsConf.RUNNER_ID, params.get(ParamsConf.RUNNER_ID));
		r.putParam(ParamsConf.WIN_SIZE_WIDTH, winW);
		r.putParam(ParamsConf.WIN_SIZE_HEIGHT, winH);
		r.putParam(ParamsConf.WIN_SLIDE, slide);
		r.putParam(ParamsConf.IMG_INPUT_ID,params.get(ParamsConf.IMG_INPUT_ID));
		r.putParam(ParamsConf.STREL_SIZE_MIN, strelMin);
		r.putParam(ParamsConf.STREL_SIZE_MAX, strelMax);
		r.putParam(ParamsConf.STREL_SHAPE, strelShape);
		r.putParam(ParamsConf.PPM_CHEBYSHEV_K, chebyshevK);
		
		
		this.executionsResult=r;
		
	}

	@Override
	public boolean isRunnig() {
		return runnig;
	}

	@Override
	public void stop() {
		
		
	}

	@Override
	public void setUp() {
		
		
	}

}
