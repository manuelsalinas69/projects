package py.com.global.educador.engine.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Educador_Constants {

	
	public static class Queues{
		public static final String SMS_IN="queue/QUEUE_SMS_IN";
		public static final String SUBSCRIPTION_IN="queue/QUEUE_SUBSCRIPTION_IN";
		public static final String CLIENT_RESPONSE_IN="queue/QUEUE_CLIENT_RESPONSE_IN";
		public static final String CLIENT_TASK_EVENT="queue/QUEUE_CLIENT_TASK_EVENT";
		public static final String NOTIFICATION_REQUEST="queue/QUEUE_NOTIFICATION_REQUEST";
		public static final String SMS_OUT="queue/QUEUE_SMS_OUT";
		public static final String EJECUTION_UPDATER="queue/QUEUE_EJECUTION_UPDATER";
	}
	
	public static final class Sequences{
		
	}
	
	public static final class Patterns{
		public static final String LISTA_REGX="^\\s*LISTA";
		public static final String ALTA_REGEX="^\\s*(ALTA|EDUCA).*";
		public static final String BAJA_REGEX="^\\s*(BAJA|SALIR).*";
		public static final String SUSCRIPTION_REQUEST_REGEX=LISTA_REGX+"|"+ALTA_REGEX+"|"+BAJA_REGEX;
		
		public static final Pattern LISTA_PATTERN= Pattern.compile(LISTA_REGX,Pattern.CASE_INSENSITIVE);
		public static final Pattern ALTA_PATTERN= Pattern.compile(ALTA_REGEX,Pattern.CASE_INSENSITIVE);
		public static final Pattern BAJA_PATTERN= Pattern.compile(BAJA_REGEX,Pattern.CASE_INSENSITIVE);
		public static final Pattern SUSCRIPTION_REQUEST_PATTERN=Pattern.compile(SUSCRIPTION_REQUEST_REGEX,Pattern.CASE_INSENSITIVE);
	}
	
	public static void main(String[] args) {
		String test="salir la puta carajo";
		Matcher matcher = Educador_Constants.Patterns.BAJA_PATTERN.matcher(test);
		System.out.println("Match ["+test+"]--> "+matcher.matches());
	}
}
