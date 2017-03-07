package pdi.jpa.facade;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import pdi.jpa.model.Param;
import pdi.jpa.model.Runner;
import pdi.jpa.model.RunnerParam;
import pdi.jpa.util.DBUtil;

public class QueryFacade {
	
	@SuppressWarnings("unchecked")
	List<Param> getMainParamList(){
		EntityManager em= DBUtil.getInstance().getEntityManager();
		String hql="SELECT p FROM Param p";
		Query q=em.createQuery(hql);
		return q.getResultList(); 
		
	}
	
	public String getParam(String key){
		EntityManager em= DBUtil.getInstance().getEntityManager();
		Param p=em.find(Param.class, key);
		if (p==null) {
			return null;
		}
		
		return p.getValue();
	}
	
	@SuppressWarnings("unchecked")
	List<RunnerParam> getRunnerParamList(Long runnerId){
		EntityManager em= DBUtil.getInstance().getEntityManager();
		String hql="SELECT p FROM RunnerParam p WHERE p.runner.id= :id";
		Query q=em.createQuery(hql);
		q.setParameter("id", runnerId);
		return q.getResultList(); 
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Runner> getRunners(){
		EntityManager em= DBUtil.getInstance().getEntityManager();
		String hql="SELECT r FROM Runner r";
		Query q= em.createQuery(hql);
		return q.getResultList();
		
	}
	
	

}
