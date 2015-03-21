package py.com.global.educador.engine.utils;

import java.util.regex.Pattern;

public class UtilsTest {
	static final String LISTA_REGX="^\\s*LISTA";
	static final String ALTA_REGEX="^\\s*ALTA";
	static final String BAJA_REGEX="^\\s*BAJA";
	static final String SUSCRIPTION_REQUEST_REGEX=LISTA_REGX+"|"+ALTA_REGEX+"|"+BAJA_REGEX;
	static final Pattern LISTA_PATTERN= Pattern.compile(LISTA_REGX,Pattern.CASE_INSENSITIVE);
	static final Pattern ALTA_PATTERN= Pattern.compile(ALTA_REGEX,Pattern.CASE_INSENSITIVE);
	static final Pattern BAJA_PATTERN= Pattern.compile(BAJA_REGEX,Pattern.CASE_INSENSITIVE);
	static final Pattern SUSCRIPTION_REQUEST_PATTERN=Pattern.compile(SUSCRIPTION_REQUEST_REGEX,Pattern.CASE_INSENSITIVE);
	public static void main(String[] args) {
		System.out.println(String.valueOf(null));
	}
}
