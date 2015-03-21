package py.com.global.educador.engine.utils;

import java.net.URL;
import java.util.Date;

import sun.net.www.protocol.http.HttpURLConnection;

public class AppServicesClient {
	private final String USER_AGENT = "Mozilla/5.0";
	
	String suscripcionBaseUrl="http://localhost:8080/EducadorEngineServices/Suscripcion/<OPERACION>/<SHOR_NUMBER>/<CLIENT_NUMBER>";
	
	
	public int suscriptonProccess(String operacion, String numeroCorto, String numeroCliente){
		String url=suscripcionBaseUrl.replace("<OPERACION>", operacion.toLowerCase());
		url=url.replace("<SHOR_NUMBER>", numeroCorto);
		url=url.replace("<CLIENT_NUMBER>", numeroCliente);
		
		return send(url);
	}
	
	private int send(String url){
		HttpURLConnection con=null;
		try {
			URL obj= new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			 
			//add reuqest header
			con.setRequestMethod("GET");
			//String urlParameters = parameters;
			con.setRequestProperty("User-Agent", USER_AGENT);
		
				 
			int responseCode = con.getResponseCode();
			//System.out.println("Response---> "+responseCode);
			return responseCode;
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			if (con!=null) {
				try {
					con.disconnect();
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		return -1;
	}
	
	
	public static void main(String[] args) {
		
		AppServicesClient servicesClient= new AppServicesClient();
		
		int baseNumber=983900009;
		int maxNumber= baseNumber+5;
		int SUCCESS=0;
		int ERROR=0;
		int result=0;
		long t1=System.currentTimeMillis();
		System.out.println("Sending request...");
		System.out.println("Current Time "+new Date());
		String number=null;
		String shortNumber="606";
		String operacion="alta";
		for (int i = baseNumber; i <= maxNumber; i++) {
			number="0"+i;
			//System.out.println("Sending to--> "+number);
			result=servicesClient.suscriptonProccess(operacion,shortNumber,number);
			if (result==HttpURLConnection.HTTP_OK) {
				SUCCESS++;
			}
			else{
				ERROR++;
			}
		}
		long t2=System.currentTimeMillis();
		System.out.println("Time delay--> "+(t2-t1)+"ms.");
		System.out.println("SUCCESS--> "+SUCCESS);
		System.out.println("ERROR--> "+ERROR);
	}
}
