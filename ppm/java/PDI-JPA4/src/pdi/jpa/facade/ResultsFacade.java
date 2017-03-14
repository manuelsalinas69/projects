package pdi.jpa.facade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.persistence.EntityManager;

import pdi.jpa.conf.ParamsConf;
import pdi.jpa.model.ExecutionResults;
import pdi.jpa.model.Image;
import pdi.jpa.model.Result;
import pdi.jpa.util.DBUtil;
import pdi.jpa.util.FileUtil;

public class ResultsFacade {
	
	public void saveResults(ExecutionResults results) throws IOException{
		EntityManager em=DBUtil.getInstance().getEntityManager();
		
		Result r=new Result();
		r.setImage(em.find(Image.class, Long.parseLong(results.getParam(ParamsConf.IMG_INPUT_ID).toString())));
		r.setRunnerId(Long.parseLong(results.getParam(ParamsConf.RUNNER_ID).toString()));
		r.setEndTime(new Date(Long.parseLong(results.getResult(ParamsConf.PROCESS_END).toString())));
		r.setStartTime(new Date(Long.parseLong(results.getResult(ParamsConf.PROCESS_START).toString())));
		r.setOutputImg(FileUtil.extractBytes(results.getResult(ParamsConf.IMG_OUTPUT_FILE).toString()));
		r.setParams(results.getParams().toString());
		em.getTransaction().begin();
		em.persist(r);
		em.getTransaction().commit();
		em.close();
	}
	
	


}
