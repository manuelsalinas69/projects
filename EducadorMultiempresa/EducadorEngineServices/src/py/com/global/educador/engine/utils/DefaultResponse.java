package py.com.global.educador.engine.utils;

import javax.ws.rs.core.Response;

public class DefaultResponse {
	
	public static Response getACKResponse(){
		return Response	
				.status(200)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "sessionId, origin, content-type, accept, authorization, access-control-allow-origin")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.build();

	}

	public static Response getEntityResponse(Object d){
		return Response	
				.status(200)
				//				.header("access-control-allow-origin", "http://127.0.0.1:55888")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "sessionId, origin, content-type, accept, authorization, access-control-allow-origin")
				// .header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.entity(d)
				.build();

	}

	

}
