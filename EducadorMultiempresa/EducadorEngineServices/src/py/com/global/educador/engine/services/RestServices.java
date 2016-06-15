package py.com.global.educador.engine.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	@Context HttpServletRequest request;

	@EJB
	AppServices appServices;


	@GET()
	@OPTIONS
	@Produces("text/plain")
	public String homeMessages() {
		return "Request no valido,por favor arme correctamente su peticion";
	}

	@GET()
	@OPTIONS
	@Path("project/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response proyectos(@QueryParam("idEmpresa") Long idEmpresa) {

		if (isOptionRequest()) {
			return getACKResponse();
		}
		List<ProyectoDto> l = new ArrayList<ProyectoDto>();
		try {
			if (idEmpresa==null) {
				return getEntityResponse(l);
			}
			List<Proyecto> proyectos = appServices.getProyectos(idEmpresa);
			for (Proyecto _p : proyectos) {
				l.add(new ProyectoDto(_p.getIdProyecto(), _p.getNombre(), _p
						.getDescripcion()));
			}

		} catch (Exception e) {
			System.out.println("Services.process(): " + e);
			e.printStackTrace();
		}
		return getEntityResponse(l);
	}

	@GET()
	@OPTIONS
	@Path("module/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response modulos(@QueryParam("idEmpresa") Long idEmpresa,
			@QueryParam("idProyecto") Long idProyecto) {
		if (isOptionRequest()) {
			return getACKResponse();
		}

		List<ModuloDto> l = new ArrayList<ModuloDto>();
		try {
			List<Modulo> modulos = appServices
					.getModulos(idEmpresa, idProyecto);
			for (Modulo _m : modulos) {
				l.add(new ModuloDto(_m.getIdModulo(), _m.getNombre(), appServices.getCantidadPreguntasModulo(_m.getIdModulo())));
			}

		} catch (Exception e) {
			System.out.println("Services.process(): " + e);
			e.printStackTrace();
		}
		return getEntityResponse(l);
	}

	@GET()
	@OPTIONS
	@Path("module/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createNew(@QueryParam("idModulo") Long idModulo,
			@QueryParam("idSuscriptor") Long idSuscriptor) {

		if (isOptionRequest()) {
			return getACKResponse();
		}
		
		Properties data = appServices.createNew(idModulo, idSuscriptor);
		Properties p = new Properties();
		p.put("formulario", data);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(),
				ServiceStatus.OK.getDescripcion(), p);

		return getEntityResponse(r);
	}

	@GET()
	@OPTIONS
	@Path("module/ejec/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listEvaluaciones(@QueryParam("idModulo") Long idModulo,
			@QueryParam("idSuscriptor") Long idSuscriptor) {
		if (isOptionRequest()) {
			return getACKResponse();
		}
		List<Properties> l= appServices.getEjecuciones(idModulo, idSuscriptor);
		Properties p= new Properties();
		p.put("ejecuciones", l);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), p);

		return getEntityResponse(r);
	}

	@GET()
	@OPTIONS
	@Path("module/ejec/{idEjecucion}/{idDetalle}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ejecucionStatus(
			@PathParam("idEjecucion") Long idEjecucion,
			@PathParam("idDetalle") Long idDetalle) {

		if (isOptionRequest()) {
			return getACKResponse();
		}
		Properties formDto= appServices.status(idEjecucion, idDetalle);
		Properties p= new Properties();
		p.put("formulario", formDto);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), p);

		return getEntityResponse(r);
	}

	@GET()
	@OPTIONS
	@Path("module/ejec/{idEjecucion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ejecucionResume(
			@PathParam("idEjecucion") Long idEjecucion,
			@QueryParam("orden")String order
			) {
		if (isOptionRequest()) {
			return getACKResponse();
		}
		Properties formDto= appServices.resume(idEjecucion, order);
		Properties p= new Properties();
		p.put("formulario", formDto);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), p);

		return getEntityResponse(r);
	}

	@POST
	@OPTIONS
	@Path("module/ejec/{idEjecucion}/{idDetalle}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response ejecucionResponse(
			@PathParam("idEjecucion") Long idEjecucion,
			@PathParam("idDetalle") Long idDetalle,
			@FormParam("idEvaluacion") Long idEvaluacion,
			@FormParam("idPregunta") Long idPregunta,
			@FormParam("idRespuesta") Long idRespuesta,
			@FormParam("respuesta") String respuesta) {

		if (isOptionRequest()) {
			return getACKResponse();
		}
		Properties data = appServices.putResponse(idEjecucion, idDetalle,
				idEvaluacion, idPregunta, idRespuesta, respuesta);
		Properties p=new Properties();
		p.put("formulario", data);
		ResponseDto r = new ResponseDto(ServiceStatus.OK.getCode(),
				ServiceStatus.OK.getDescripcion(), p);

		return getEntityResponse(r);
	}

	@POST
	@OPTIONS
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@FormParam("user") String user, @FormParam("pass") String pass){
		//		System.out.println("HTTP Method-->"+request.getMethod());

		if (isOptionRequest()) {
			return getACKResponse();
		}

		Properties loginInfo=appServices.login(user, pass);
		ResponseDto d= new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), loginInfo);
		return getEntityResponse(d);


	}

	@POST
	@OPTIONS
	@Path("subscribe")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response subscribe(@FormParam("idSuscriptor") Long idSuscriptor, @FormParam("idProyecto") Long idProyecto){

		if (isOptionRequest()) {
			return getACKResponse();
		}
		
		Properties suscriptionInfo=appServices.subscribe(idSuscriptor, idProyecto);
		ResponseDto d= new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), suscriptionInfo);

		return getEntityResponse(d);
	}

	private Response getACKResponse(){
		System.out.println("Just OPTION request, ACK will be send");
		return Response	
				.status(200)
				//				.header("access-control-allow-origin", "http://127.0.0.1:55888")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, access-control-allow-origin")
				// .header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.build();

	}

	private Response getEntityResponse(Object d){
		return Response	
				.status(200)
				//				.header("access-control-allow-origin", "http://127.0.0.1:55888")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, access-control-allow-origin")
				// .header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.entity(d)
				.build();

	}

	private boolean isOptionRequest(){
		return "OPTIONS".equalsIgnoreCase(request.getMethod());
	}

}
