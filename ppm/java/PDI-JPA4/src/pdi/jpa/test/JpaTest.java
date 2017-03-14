package pdi.jpa.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import pdi.jpa.model.Result;

public class JpaTest {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("pdi_jpa");
        EntityManager em = factory.createEntityManager();
        // read the existing entries and write to console
        Result r=em.find(Result.class, 1L);
		System.out.println("Result: "+r);
        //em.persist(todo);
		 em.getTransaction().begin();
		em.getTransaction().commit();

        em.close();

	}

}
