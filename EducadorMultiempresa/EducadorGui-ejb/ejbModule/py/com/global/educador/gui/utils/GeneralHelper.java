package py.com.global.educador.gui.utils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.security.digest.DigestUtils;

@Name("generalHelper")
@Scope(ScopeType.APPLICATION)
public class GeneralHelper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String aTilde = "\u00E1";
	public static String eTilde = "\u00E9";
	public static String iTilde = "\u00ED";
	public static String oTilde = "\u00F3";
	public static String uTilde = "\u00FA";
	public static String nTilde = "\u00F1";
	static Logger logger = Logger.getLogger(GeneralHelper.class);
	String datePattern = "dd-MM-yyyy HH:mm:ss,SSS";

	public TimeZone getTimeZone() {
		return TimeZone.getDefault();

	}

	public int tiempoMen() {
		try {

			String lastMessages = FacesContext.getCurrentInstance()
					.getMessages().next().getSummary();

			int tiempo = (int) (lastMessages.trim().isEmpty() ? 1
					: (1.5 * (int) (lastMessages.length() / 40.0) + 3) * 1000);

			return tiempo;
		} catch (Exception e) {

		}

		return 0;
	}

	public static Object getObjetValue(EntityInterface entityInterface,
			String displayName) {
		String values[] = displayName.split("\\.");
		if (values.length == 1) {
			return entityInterface.getProperties().get(displayName);
		}
		String lastProperty = values[0];

		try {
			Object objValue = entityInterface.getProperties().get(lastProperty);
			if (objValue instanceof EntityInterface) {
				return getObjetValue((EntityInterface) objValue,
						displayName.substring(displayName.indexOf(".") + 1));
			}
			if (objValue != null) {
				return objValue.toString();
			}
		} catch (Exception e) {
			System.out.println("GeneralHelper.getObjetValue(): "+e);
		}
		return null;
	}

	public static boolean hourEquals(Date h1, Date h2) {
		SimpleDateFormat hourSdf = new SimpleDateFormat("HH:mm");

		if (h1 == null || h2 == null) {
			return false;
		}

		String hour1 = hourSdf.format(h1);
		String hour2 = hourSdf.format(h2);

		return hour1.equalsIgnoreCase(hour2);

	}

	public static boolean hourIsInRange(Date hourDate, Date loDate, Date hiDate) {
		try {
			SimpleDateFormat hourSdf = new SimpleDateFormat("HH:mm");
			String hour = hourSdf.format(hourDate);
			String lo = hourSdf.format(loDate);
			String hi = hourSdf.format(hiDate);

			if (hour.compareTo(lo) >= 0 && hour.compareTo(hi) <= 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("hourIsInRange", e);

		}

		return false;
	}

	public static boolean validHourRange(Date loDate, Date hiDate) {
		try {
			SimpleDateFormat hourSdf = new SimpleDateFormat("HH:mm");
			String lo = hourSdf.format(loDate);
			String hi = hourSdf.format(hiDate);

			return lo.compareTo(hi) < 0;
		} catch (Exception e) {
			logger.error("validHourRange", e);
		}
		return false;

	}

	public static String getString(String param) {
		return param == null || param.trim().isEmpty() ? null : param.trim();
	}

	public static String getUpperCase(String param) {
		return param == null || param.trim().isEmpty() ? null : param.trim()
				.toUpperCase();
	}

	public static boolean validDateRange(Date loDate, Date hiDate) {
		SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy/MM/dd");
		String lo = timeSdf.format(loDate);
		String hi = timeSdf.format(hiDate);

		return lo.compareTo(hi) <= 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean tieneReferencias(Object entityInstance) {
		try {
			for (Method metodo : entityInstance.getClass().getMethods()) {
				Type tipo = metodo.getReturnType();
				if (tipo.equals(Set.class)) {
					List<?> list = new ArrayList(
							(Set) metodo.invoke(entityInstance));
					if (list.size() > 0) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			logger.error("tieneReferencias", e);
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean tieneReferencias(Object entityInstance, Class clase) {
		try {
			for (Method metodo : entityInstance.getClass().getMethods()) {
				Type tipo = metodo.getReturnType();
				if (tipo.equals(Set.class)) {
					List<?> list = new ArrayList(
							(Set) metodo.invoke(entityInstance));
					if (list.size() > 0
							&& (list.get(0).getClass().equals(clase))) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			logger.error("tieneReferencias", e);
		}
		return false;
	}

	public static String getRealPath(String param) {
		try {
			ServletContext context = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			return context.getRealPath(param);
		} catch (Exception e) {
			logger.error("getRealPath", e);
		}
		return null;
	}

	public static Integer parseInt(Object o) {
		return o == null || o.toString().trim().isEmpty() ? null : Integer
				.parseInt(o.toString());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> toArray(Set<?> set) {
		return new ArrayList(set);
	}

	public static String goToUrl(String url) {
		return url.replace(";", "&");
	}

	public static String getA_Tilde() {
		return aTilde;
	}

	public static String getE_Tilde() {
		return eTilde;
	}

	public static String getI_Tilde() {
		return iTilde;
	}

	public static String getO_Tilde() {
		return oTilde;
	}

	public static String getU_Tilde() {
		return uTilde;
	}

	public static String getN_Tilde() {
		return nTilde;
	}

	public static <T> List<T> replaceInList(List<T> l, T o, Integer index) {
		try {
			if (l == null) {
				return null;
			}
			if (o == null) {
				return l;
			}
			if (index == null || index < 0) {
				l.add(o);
				return l;
			}

			l.remove(index.intValue());
			l.add(index.intValue(), o);
			return l;

		} catch (Exception e) {
			logger.error("replaceInList", e);
		}
		return l;
	}

	public static String getMessages(String key) {
		try {
			String msg = SeamResourceBundle.getBundle().getString(key);
			return msg;
		} catch (Exception e) {
			logger.trace("GeneralHelper.getMessages(): " + e);
		}
		return key;
	}

	public static boolean isNaN(String value) {
		try {
			Double d = Double.parseDouble(value);
			return d.isNaN();
		} catch (Exception e) {
			logger.trace("No es un numero--->" + value);
		}
		return true;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public static boolean isWsdl(String url) {
		if (getString(url) == null) {
			return false;
		}
		return url.endsWith("?wsdl");
	}

	public static boolean isEndPoint(String url) {
		if (getString(url) == null) {
			return false;
		}
		return !url.endsWith("?wsdl");
	}

	public static String getEndPoint(String url) {
		if (isEndPoint(url)) {
			return url;
		}
		if (isWsdl(url)) {
			return url.substring(0, url.lastIndexOf("?wsdl"));
		}

		return null;
	}

	public static String getWsdl(String url) {
		if (isWsdl(url)) {
			return url;
		}
		if (isEndPoint(url)) {
			return url + "?wsdl";
		}
		return null;
	}

	/***
	 * @param d
	 *            Fecha a modificar
	 * @param time
	 *            Valor de hora a modificar. Formato por defecto HH:mm:ss
	 * @param hourFormat
	 *            (Optional)Formato de hora. Por defecto HH:mm:ss
	 * @return  La misma fecha que el parametro recibido y con la hora formateada del valor pasado o null si ocurrio alguna
	 *         exepcion
	 * 
	 * */

	public static Date setTimeOfDate(Date d, String time, String hourFormat) {
		if (d==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if (hourFormat != null) {
			sdf = new SimpleDateFormat(hourFormat);
		}

		try {
			Date t = sdf.parse(time);

			Calendar c1 = Calendar.getInstance();
			c1.setTime(d);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(t);
			c1.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
			c1.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
			c1.set(Calendar.SECOND, c2.get(Calendar.SECOND));

			return c1.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static Date setToHighestHourOfDay(Date d){
		return setTimeOfDate(d, "23:59:59", null);
	}
	
	public static Date setToLowestHourOfDay(Date d){
		return setTimeOfDate(d, "00:00:00", null);
	}

	public static String getShortUrl(String url) {
		if (url == null || url.trim().isEmpty()) {
			return "";
		}
		url = url.replace("xhtml", "seam");
		if (url.contains("/")) {
			url = url.substring(url.lastIndexOf("/") + 1);
		}
		return url;
	}

	public static String MD5(String toHash) {
		return DigestUtils.md5Hex(toHash);
	}

//	public static void main(String[] args) {
////		String[] pass = { "admin", "msalinas", "aferreira", "prueba", "123456" };
////		for (String p : pass) {
////			System.out.println("MD5 for ----> " + p + " - "
////					+ GeneralHelper.MD5(p));
////		}
//		
//		Calendar c1=Calendar.getInstance();
//		Calendar c2=Calendar.getInstance();
//		
//		c1.set(Calendar.YEAR, 2012);
//		c1.set(Calendar.MONTH, 11);
//		
//		c2.set(Calendar.YEAR, 2014);
//		c2.set(Calendar.MONTH, 0);
//		Long t1=System.currentTimeMillis();
//		System.out.println("Meses--> "+GeneralHelper.getMonthsBetweenDates(c1.getTime(), c2.getTime()));
//		Long t2=System.currentTimeMillis();
//		System.out.println("Tiempo--> "+(t2-t1));
//
//	}

	public static boolean isValidatePhoneNumber(String phonenumber) {
		if (phonenumber == null || phonenumber.length() != 10) {
			return false;
		} else if (!Pattern.matches("\\d+", phonenumber)) {
			return false;
		}
		return true;
	}

	
	public static Integer getMonthsBetweenDates(Date lo, Date hi){
		try {
			if (lo==null || hi==null) {
				return null;
			}
			
			if (!hi.after(lo)) {
				return null;
			}
			
			Calendar loC=Calendar.getInstance();
			loC.setTime(lo);
			Calendar hiC=Calendar.getInstance();
			hiC.setTime(hi);
			
			int yH=hiC.get(Calendar.YEAR);
			int yL=loC.get(Calendar.YEAR);
			int mH=hiC.get(Calendar.MONTH);
			int mL=loC.get(Calendar.MONTH);
			
			int years=yH-yL;
			
			if (years==0) {
				return (mH-mL)+1;
			}
			
			return (12-mL)+(years-1)*12+(mH+1);
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	
	
	public static void main(String[] args) {
		String msg="Plantilla del mensaje para los detalles de factura, Campos Disponibles: {nroCuota},{periodoCuota},{periodoIniCuota},{periodoFinCuota}, {contrato}, {contratoDescripcion}";
		
		String[] s=msg.split("(?<=\\G.{160})");
		System.out.println("Size: "+msg.length());
		System.out.println("Cantidad de mensajes--> "+s.length);
		int n=0;
		for (String string : s) {
			n++;
			System.out.println("Msg_part "+n+": "+string);
		}
		//System.out.println(Arrays.toString(s));
	}
	
	public static boolean match(String data, String pattern){
		if (data==null) {
			return false;
		}
		Pattern p= Pattern.compile(pattern);
		Matcher m=p.matcher(data);
		return m.matches();
	}

}
