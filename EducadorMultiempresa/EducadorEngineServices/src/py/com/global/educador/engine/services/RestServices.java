package py.com.global.educador.engine.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.app.services.AppServices;
import py.com.global.educador.engine.dto.ModuloDto;
import py.com.global.educador.engine.dto.ProyectoDto;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.dto.ResponseDto;
import py.com.global.educador.engine.enums.ServiceStatus;
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
	public String homeMessages() {
		return "Request no valido,por favor arme correctamente su peticion";
	}

	@GET()
	@Path("project/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProyectoDto> proyectos(@QueryParam("idEmpresa") Long idEmpresa) {

		List<ProyectoDto> l = new ArrayList<ProyectoDto>();
		try {
			List<Proyecto> proyectos = appServices.getProyectos(idEmpresa);
			for (Proyecto _p : proyectos) {
				l.add(new ProyectoDto(_p.getIdProyecto(), _p.getNombre(), _p
						.getDescripcion()));
			}

		} catch (Exception e) {
			System.out.println("Services.process(): " + e);
			e.printStackTrace();
		}
		return l;
	}

	@GET()
	@Path("module/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ModuloDto> modulos(@QueryParam("idEmpresa") Long idEmpresa,
			@QueryParam("idProyecto") Long idProyecto) {

		List<ModuloDto> l = new ArrayList<ModuloDto>();
		try {
			List<Modulo> modulos = appServices
					.getModulos(idEmpresa, idProyecto);
			for (Modulo _m : modulos) {
				l.add(new ModuloDto(_m.getIdModulo(), _m.getNombre(), (long) 5));
			}

		} catch (Exception e) {
			System.out.println("Services.process(): " + e);
			e.printStackTrace();
		}
		return l;
	}

	@GET()
	@Path("module/new")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDto createNew(@QueryParam("idModulo") Long idModulo,
			@QueryParam("idSuscriptor") Long idSuscriptor) {

		Properties data = appServices.createNew(idModulo, idSuscriptor);
		Properties p = new Properties();
		p.put("formulario", data);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(),
				ServiceStatus.OK.getDescripcion(), p);

		return r;
	}

	@GET()
	@Path("module/ejec/list")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDto listEvaluaciones(@QueryParam("idModulo") Long idModulo,
			@QueryParam("idSuscriptor") Long idSuscriptor) {

		List<Properties> l= appServices.getEjecuciones(idModulo, idSuscriptor);
		Properties p= new Properties();
		p.put("ejecuciones", l);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), p);

		return r;
	}

	@GET()
	@Path("module/ejec/{idEjecucion}/{idDetalle}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDto ejecucionStatus(
			@PathParam("idEjecucion") Long idEjecucion,
			@PathParam("idDetalle") Long idDetalle) {

		Properties formDto= appServices.status(idEjecucion, idDetalle);
		Properties p= new Properties();
		p.put("formulario", formDto);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), p);

		return r;
	}
	
	@GET()
	@Path("module/ejec/{idEjecucion}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDto ejecucionResume(
			@PathParam("idEjecucion") Long idEjecucion) {

		Properties formDto= appServices.resume(idEjecucion);
		Properties p= new Properties();
		p.put("formulario", formDto);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), p);

		return r;
	}

	@POST
	@Path("module/ejec/{idEjecucion}/{idDetalle}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ResponseDto ejecucionResponse(
			@PathParam("idEjecucion") Long idEjecucion,
			@PathParam("idDetalle") Long idDetalle,
			@FormParam("idEvaluacion") Long idEvaluacion,
			@FormParam("idPregunta") Long idPregunta,
			@FormParam("idRespuesta") Long idRespuesta,
			@FormParam("respuesta") String respuesta) {

		Properties data = appServices.putResponse(idEjecucion, idDetalle,
				idEvaluacion, idPregunta, idRespuesta, respuesta);
		Properties p=new Properties();
		p.put("formulario", data);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(),
				ServiceStatus.OK.getDescripcion(), p);

		return r;
	}

	@POST
	@Path("login")
	public ResponseDto login(@FormParam("user") String user, @FormParam("pass") String pass){
		Properties loginInfo=appServices.login(user, pass);
		ResponseDto d= new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), loginInfo);
		return d;
	}
	
	@POST
	@Path("login")
	public ResponseDto subscribe(@FormParam("idSuscriptor") Long idSuscriptor, @FormParam("idProyecto") Long idProyecto){
		Properties suscriptionInfo=appServices.subscribe(idSuscriptor, idProyecto);
		ResponseDto d= new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), suscriptionInfo);
		return d;
	}

}
