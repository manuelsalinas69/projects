package pdi.evaluations.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import pdi.evaluations.model.interfaces.RunnerInterface;
import pdi.jpa.conf.ParamsConf;
import pdi.jpa.facade.ImagesFacade;
import pdi.jpa.facade.QueryFacade;
import pdi.jpa.model.Image;
import pdi.jpa.model.Runner;
import pdi.ppm.conf.PPMConstanst;

public class TestLauncher {
	
	public void launch() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SerialException, SQLException{
		startEvaluations();
	}

	
	private RunnerInterface createRunner(Runner _r) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> r= Class.forName(_r.getClass_());
		pdi.evaluations.model.interfaces.RunnerInterface i=(pdi.evaluations.model.interfaces.RunnerInterface)r.newInstance();
		return i;
	}
	
	public void startEvaluations() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SerialException, SQLException{
		ImagesFacade il= new ImagesFacade();
		QueryFacade qf= new QueryFacade();
		il.loadImages();
		List<Image> l=il.getLoadedFiles();
		HashMap<String, Object> params= new HashMap<String,Object>();
		params.put(ParamsConf.WIN_SIZE_HEIGHT, qf.getParam(ParamsConf.WIN_SIZE_HEIGHT));
		params.put(ParamsConf.WIN_SIZE_WIDTH, qf.getParam(ParamsConf.WIN_SIZE_WIDTH));
		params.put(ParamsConf.WIN_SLIDE, qf.getParam(ParamsConf.WIN_SLIDE));
		params.put(ParamsConf.STREL_SIZE_MIN, qf.getParam(ParamsConf.STREL_SIZE_MIN));
		params.put(ParamsConf.STREL_SIZE_MAX, qf.getParam(ParamsConf.STREL_SIZE_MAX));
		params.put(ParamsConf.STREL_SHAPE, qf.getParam(ParamsConf.STREL_SHAPE));
		params.put(ParamsConf.PPM_CHEBYSHEV_K, qf.getParam(ParamsConf.PPM_CHEBYSHEV_K));
		//params.put(ParamsConf.IMG_INPUT_FILE, qf.getParam(ParamsConf.IMG_INPUT_FILE));
		params.put(ParamsConf.IMG_OUTPUT_DIR, qf.getParam(ParamsConf.IMG_OUTPUT_DIR));
		params.put(ParamsConf.IMG_INPUT_FILE_EXT, qf.getParam(ParamsConf.IMG_INPUT_FILE_EXT));
		params.put(ParamsConf.OPTMIZATION_UMBRAL_STDV, qf.getParam(ParamsConf.OPTMIZATION_UMBRAL_STDV));
		params.put(ParamsConf.IMG_OUTPUT_REMOVE, qf.getParam(ParamsConf.IMG_OUTPUT_REMOVE));
		params.put(ParamsConf.RUNNERS_IMG_PROCESS, qf.getParam(ParamsConf.RUNNERS_IMG_PROCESS));
		
		setPPMParams(params);
		
		List<Runner> runners= qf.getRunners();
		List<RunnerInterface> currentsRunners= new ArrayList<RunnerInterface>();
		for (Runner _r : runners) {
			System.out.println("*****RUNNER*****");
			System.out.println("ID: "+_r.getId()+", NAME: "+_r.getName()+", CLASS: "+_r.getClass_());
			for (Image imgFile : l) {
				RunnerInterface r=createRunner(_r);
				params.put(ParamsConf.IMG_INPUT_FILE,imgFile.getName());
				params.put(ParamsConf.IMG_INPUT_ID, imgFile.getId());
				params.put(ParamsConf.RUNNER_ID, _r.getId());
				r.setParams(new HashMap<String,Object>(params));
				new Thread(r).start();
				currentsRunners.add(r);
			}
		
		}
		
		boolean bussy=false;
		
		do {
			bussy=false;
			for (pdi.evaluations.model.interfaces.RunnerInterface _r : currentsRunners) {
				if (_r.isRunnig()) {
					bussy=true;
					continue;
				}
			}
		} while (bussy);
		
		System.out.println("Finalizado");
	}

	private void setPPMParams(HashMap<String, Object> params) {
		PPMConstanst.K=Double.parseDouble(params.get(ParamsConf.PPM_CHEBYSHEV_K).toString());
		PPMConstanst.UMBRAL_STDV=Double.parseDouble(params.get(ParamsConf.OPTMIZATION_UMBRAL_STDV).toString());
		
	}
	
	
}
