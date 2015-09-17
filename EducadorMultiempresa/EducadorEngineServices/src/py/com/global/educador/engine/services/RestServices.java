package py.com.global.educador.engine.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.app.services.AppServices;
import py.com.global.educador.engine.dto.ModuloDto;
import py.com.global.educador.engine.dto.ProyectoDto;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.dto.ResponseDto;
import py.com.global.educador.jpa.entity.Modulo;
import py.com.global.educador.jpa.entity.Proyecto;


@Path("/Services")
public class RestServices {
QueueMessage message = new QueueMessage();
	
	Logger log = Logger.getLogger(RestServices.class);
	
	@EJB
	AppServices appServices;
	
	@GET()
	@Produces("text/plain")
	public String homeMessages(){
		return "Request no valido,por favor arme correctamente su peticion";
	}
	
	@GET()
	@Path("list/projects")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProyectoDto> proyectos(@QueryParam("idEmpresa") Long idEmpresa) {
		
		List<ProyectoDto> l=new ArrayList<ProyectoDto>();
		try {
			List<Proyecto> proyectos=appServices.getProyectos(idEmpresa);
			for (Proyecto _p : proyectos) {
				l.add(new ProyectoDto(_p.getIdProyecto(),_p.getNombre(),_p.getDescripcion()));
			}
			
		} catch (Exception e) {
			System.out.println("Services.process(): "+e);
			e.printStackTrace();
		}
		return l;
	}
	
	@GET()
	@Path("list/modules")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ModuloDto> modulos(@QueryParam("idEmpresa") Long idEmpresa,@QueryParam("idProyecto")Long idProyecto) {
		
		List<ModuloDto> l=new ArrayList<ModuloDto>();
		try {
			List<Modulo> modulos=appServices.getModulos(idEmpresa, idProyecto);
			for (Modulo _m : modulos) {
				l.add(new ModuloDto(_m.getIdModulo(),_m.getNombre(),(long)5));
			}
			
		} catch (Exception e) {
			System.out.println("Services.process(): "+e);
			e.printStackTrace();
		}
		return l;
	}
	
	@GET()
	@Path("list/modules")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDto createNew(@QueryParam("idEmpresa") Long idEmpresa,
			@QueryParam("idProyecto")Long idProyecto, @QueryParam("idModulo") Long idModulo) {
		
		ResponseDto r= new ResponseDto(null, null, null);
		
		return r;
	}
	
	
	
}
