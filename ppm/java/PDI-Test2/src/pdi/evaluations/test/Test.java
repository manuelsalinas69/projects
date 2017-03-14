package pdi.evaluations.test;

import javax.persistence.EntityManager;

import pdi.jpa.model.Result;
import pdi.jpa.util.DBUtil;

public class Test {

	
	public static void main(String[] args) {
		EntityManager em = DBUtil.getInstance().getEntityManager();
		Result r=em.find(Result.class, 1L);
		System.out.println("Result: "+r);
	}
}
