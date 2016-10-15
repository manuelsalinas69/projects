package py.com.global.educador.engine.services;

import java.util.Properties;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.com.global.educador.engine.app.services.AppServices;
import py.com.global.educador.engine.dto.ResponseDto;
import py.com.global.educador.engine.enums.ServiceStatus;
import py.com.global.educador.engine.utils.DefaultResponse;

@Path("/services/authentication/")
public class LoginServices {
	
	@Context HttpServletRequest request;

	@EJB
	AppServices appServices;
	
	@POST
	@OPTIONS
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@FormParam("user") String user, @FormParam("pass") String pass){
		
		
		Properties loginInfo=appServices.login(user, pass);
		ResponseDto d= new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), loginInfo);
		return DefaultResponse.getEntityResponse(d);


	}
	
	@GET()
	@OPTIONS
	@Path("session/check")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checksession(@QueryParam("sid") String sid){
		
		
		Properties checkSessionInfo=appServices.checkSessionId(sid);
		ResponseDto d= new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), checkSessionInfo);
		return DefaultResponse.getEntityResponse(d);


	}
	
	@GET()
	@OPTIONS
	@Path("session/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@QueryParam("sid") String sid){
		
		
		Properties checkSessionInfo=appServices.checkSessionId(sid);
		ResponseDto d= new ResponseDto(ServiceStatus.OK.getCode(), ServiceStatus.OK.getDescripcion(), checkSessionInfo);
		return DefaultResponse.getEntityResponse(d);


	}

	
	
	


}
