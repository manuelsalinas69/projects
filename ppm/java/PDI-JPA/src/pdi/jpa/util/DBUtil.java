package pdi.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBUtil {
	
	static DBUtil instance;
	
	public static final String PERSISTENCE_UNIT_NAME="pdi_jpa";
	
	EntityManagerFactory emf;
	
	public static DBUtil getInstance(){
		if (instance==null) {
			instance=new DBUtil();
		}
		return instance;
	}
	
	public EntityManager getEntityManager(){
		if (emf==null) {
			emf = Persistence.createEntityManagerFactory(DBUtil.PERSISTENCE_UNIT_NAME);
	      
		}
		return emf.createEntityManager();
	}
	

}
