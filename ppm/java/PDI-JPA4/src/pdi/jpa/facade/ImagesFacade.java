package pdi.jpa.facade;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.rowset.serial.SerialException;

import pdi.jpa.conf.ParamsConf;
import pdi.jpa.model.Image;
import pdi.jpa.util.DBUtil;
import pdi.jpa.util.FileUtil;
import pdi.jpa.util.GenericExtFilter;

public class ImagesFacade {

	public void loadImages() throws IOException, SerialException, SQLException {
		QueryFacade qf= new QueryFacade();
		boolean loadImages=Boolean.valueOf(qf.getParam(ParamsConf.IMG_INPUT_DIR_LOAD));
		if (!loadImages) {
			System.out.println("NO se cargaran las imagenes");
			return;
		}
		System.out.println("Cargando imagenes la Base de Datos...");
		
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
			String filePath=inputDir+"/"+fileName;
			if (contentExistInDB(filePath)) {
				System.out.println("Archivo ya encontrado en la base de datos: "+filePath);
				continue;
			}
			insertToDB(inputDir+"/"+fileName);
			
			
			
		}
		
		
	}
	
	private boolean contentExistInDB(String filePath) throws IOException, SerialException, SQLException {
		String jql="SELECT count(i) FROM Image i where i.name= :name";
		Query q=DBUtil.getInstance().getEntityManager().createQuery(jql);
		q.setParameter("name", filePath);
		Number r=(Number) q.getSingleResult();
		return r.intValue()>0;
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
		 
		 byte[] content=FileUtil.extractBytes(fileName);
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
