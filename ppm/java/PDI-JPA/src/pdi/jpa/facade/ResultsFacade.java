package pdi.jpa.facade;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;

import pdi.jpa.conf.ParamsConf;
import pdi.jpa.model.ExecutionResults;
import pdi.jpa.model.Image;
import pdi.jpa.model.Result;
import pdi.jpa.util.DBUtil;

public class ResultsFacade {
	
	public void saveResults(ExecutionResults results) throws IOException{
		EntityManager em=DBUtil.getInstance().getEntityManager();
		
		Result r=new Result();
		r.setImage(em.find(Image.class, Long.parseLong(results.getParam(ParamsConf.IMG_INPUT_ID).toString())));
		r.setRunnerId(Long.parseLong(results.getParam(ParamsConf.RUNNER_ID).toString()));
		r.setEndTime(new Date(Long.parseLong(results.getResult(ParamsConf.PROCESS_END).toString())));
		r.setStartTime(new Date(Long.parseLong(results.getResult(ParamsConf.PROCESS_START).toString())));
		r.setOutputImg(extractBytes(results.getResult(ParamsConf.IMG_OUTPUT_FILE).toString()));
		r.setParams(results.getParams().toString());
		em.getTransaction().begin();
		em.persist(r);
		em.getTransaction().commit();
		em.close();
	}
	
	
	private byte[] extractBytes(String fileName) throws IOException {
		 File imgPath = new File(fileName);
		 BufferedImage bufferedImage = ImageIO.read(imgPath);

		 // get DataBufferBytes from Raster
		 WritableRaster raster = bufferedImage .getRaster();
		
		 DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
		 
		 return data.getData();

		
	}

}
