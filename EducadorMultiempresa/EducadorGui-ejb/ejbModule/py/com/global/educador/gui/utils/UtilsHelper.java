package py.com.global.educador.gui.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UtilsHelper {

	
	public static int getMonthFromDate(Date d, boolean sumOne){
		return getFieldFromDate(d, Calendar.MONTH)+(sumOne?1:0);
		
	} 
	
	public static int getYearFromDate(Date d){
		return getFieldFromDate(d, Calendar.YEAR);
		
	} 
	
	public static SimpleDateFormat getSimpleDateFormat(String pattern){
		return new SimpleDateFormat(pattern);
	}
	/**
	 * El valor por defecto es dd/MM/yyyy
	 * */
	public static SimpleDateFormat getDefaultSimpleDateFormat(){
		return getSimpleDateFormat("dd/MM/yyyy");
	}
	
	public static int getFieldFromDate(Date d, int field){
		if (d==null) {
			return -1;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		return	c.get(field);
	}
	
	
	public static <T > List<T> extractFieldFromList(Class<T> c,List<?> l, String methodName){
		List<T> toReturn= new ArrayList<T>();
		if (l==null || l.isEmpty()) {
			return toReturn;
		}
		
		try {
			for (Object obj : l) {
				T value=extracFromObject(c,obj, methodName);
				toReturn.add(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T extracFromObject(Class<T> c,Object obj, String methodName) {
		T value;
		try {
			value = (T)obj.getClass().getMethod(methodName).invoke(obj);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BigDecimal getMontoImpuestOf(BigDecimal monto, Float percent){
		return new BigDecimal(monto.floatValue()/((100/percent)+1));
	}
}
