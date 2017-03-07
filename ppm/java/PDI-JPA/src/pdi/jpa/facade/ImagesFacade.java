package pdi.jpa.facade;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import pdi.jpa.conf.ParamsConf;
import pdi.jpa.model.Image;
import pdi.jpa.util.DBUtil;
import pdi.jpa.util.GenericExtFilter;

public class ImagesFacade {

	public void loadImages() throws IOException {
		QueryFacade qf= new QueryFacade();
		String inputDir=qf.getParam(ParamsConf.IMG_INPUT_DIR);
		String extension=qf.getParam(ParamsConf.IMG_INPUT_FILE_EXT);
		GenericExtFilter filter = new GenericExtFilter(extension);
		
		File dir=new File(inputDir);
		
		if (!dir.isDirectory()) {
			System.err.println("La ubicacion no es un directorio: "+inputDir);
			return;
		}
		
		String[] list = dir.list(filter);
		if (list.length == 0) {
			System.out.println("No hay archivos en el directorio ["+inputDir+"] con la extesion : " + extension);
			return;
		}
		
		for (String fileName : list) {
			System.out.println("Archivo encontrado:"+fileName);
			//File f= new File(inputDir+"/"+fileName);
			
			insertToDB(inputDir+"/"+fileName);
			
			
			
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getLoadedFilesName(){
		EntityManager em=DBUtil.getInstance().getEntityManager();
		String hql="SELECT im.name FROM Image im";
		Query q= em.createQuery(hql);
		return q.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Image> getLoadedFiles(){
		EntityManager em=DBUtil.getInstance().getEntityManager();
		Query q= em.createNamedQuery("Image.findAll");
		return q.getResultList();
		
	}
	
	private void insertToDB(String fileName) throws IOException {
		 File imgPath = new File(fileName);
		 BufferedImage bufferedImage = ImageIO.read(imgPath);

		 // get DataBufferBytes from Raster
		 WritableRaster raster = bufferedImage .getRaster();
		
		 DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
		 
		 byte[] content=data.getData();
		 int w=bufferedImage.getWidth();
		 int h=bufferedImage.getHeight();
		 String mimeType=MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fileName);
		 
		 EntityManager em=DBUtil.getInstance().getEntityManager();
		 Image im=new Image();
		 im.setName(fileName);
		 im.setContent(content);
		 im.setHeight((long)h);
		 im.setWidth((long)w);
		 im.setMimeType(mimeType);
		 
		 em.getTransaction().begin();
		 em.persist(im);
		 em.getTransaction().commit();
		 em.close();
		 
		
	}

	
}
