package py.com.global.educador.engine.config;

public class EducadorEngineServices {

	public static class AppPath{
		
		public static final String rootTree="";
		public static final String servicesRootTree=rootTree+"/services";
		public static final String surveyRootTree=servicesRootTree+"/survey";
		public static final String authenticationTree=servicesRootTree+"/authentication";
		public static final String loginRootTree=authenticationTree+"/login";
		public static final String sessionRootTree=authenticationTree+"/session";
		public static final String sessionCheckRootTree=sessionRootTree+"/check"; 
		
	}
	
}
