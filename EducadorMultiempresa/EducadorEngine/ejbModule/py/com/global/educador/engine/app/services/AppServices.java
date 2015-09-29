package py.com.global.educador.engine.app.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.com.global.educador.engine.enums.EstadoRegistro;
import py.com.global.educador.jpa.entity.Modulo;
import py.com.global.educador.jpa.entity.Proyecto;

@Stateless
public class AppServices {

	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	
	
	
	@SuppressWarnings("unchecked")
	public List<Proyecto> getProyectos(Long idEmpresa){
		List<Proyecto>l = new ArrayList<Proyecto>();
		try {
			String hql="SELECT _p FROM Proyecto _p WHERE _p.estadoRegistro= :estadoRegistro AND _p.estadoProyecto= :estadoProyecto " +
					" AND _p.empresa.idEmpresa= :idEmpresa AND _p.canalApp= :canalApp";
			Query q=entityManager.createQuery(hql);
			q.setParameter("estadoRegistro", EstadoRegistro.ACTIVO.name());
			q.setParameter("estadoProyecto", EstadoRegistro.ACTIVO.name());
			q.setParameter("idEmpresa", idEmpresa);
			q.setParameter("canalApp", Boolean.TRUE);
			
			l=q.getResultList();
			
		} catch (Exception e) {
			System.out.println("AppSerivces.getProyectos(): "+e);
			e.printStackTrace();
		}
		return l;
	}
	
	@SuppressWarnings("unchecked")
	public List<Modulo> getModulos(Long idEmpresa, Long idProyecto){
		List<Modulo>l = new ArrayList<Modulo>();
		try {
			String hql="SELECT _m FROM Modulo _m WHERE _m.estadoRegistro= :estadoRegistro AND _m.canalApp= :canalApp" +
					" AND _m.proyecto.idProyecto= :idProyecto AND _m.proyecto.empresa.idEmpresa= :idEmpresa";
			Query q=entityManager.createQuery(hql);
			q.setParameter("estadoRegistro", EstadoRegistro.ACTIVO.name());
			q.setParameter("idEmpresa", idEmpresa);
			q.setParameter("idProyecto", idProyecto);
			q.setParameter("canalApp", Boolean.TRUE);
			
			l=q.getResultList();
			
		} catch (Exception e) {
			System.out.println("AppSerivces.getProyectos(): "+e);
			e.printStackTrace();
		}
		return l;
	}

	

}
