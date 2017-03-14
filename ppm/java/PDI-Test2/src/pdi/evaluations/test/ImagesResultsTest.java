package pdi.evaluations.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import pdi.jpa.model.Result;
import pdi.ppm.util.Utils;

public class ImagesResultsTest {
	
	public static void main(String[] args) throws IOException {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("pdi_jpa");
        EntityManager em = factory.createEntityManager();
        // read the existing entries and write to console
        Result r=em.find(Result.class, (long)11);
		System.out.println("Result: "+r);
		BufferedImage bf= ImageIO.read(new ByteArrayInputStream(r.getOutputImg()));
		Utils.getInstance().showImage(bf);
        //em.persist(todo);
		 //em.getTransaction().begin();
		//em.getTransaction().commit();

        //em.close();
	}

}
