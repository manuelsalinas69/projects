package py.com.global.educador.gui.utils;

import java.io.Serializable;
import java.util.Comparator;

import javax.faces.model.SelectItem;

public class SelectItemsComparator implements Comparator<SelectItem>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1802466625814403814L;
	boolean asc=true;
	
	

	public SelectItemsComparator(boolean asc) {
		this.asc = asc;
	}



	public int compare(SelectItem o1, SelectItem o2) {
		if (o1==null || o2==null || o1.getValue()==null || o2.getValue()==null) {
			return 0;
		}
		
		
		int result= o1.getLabel().compareTo(o2.getLabel());
		return asc?result:result*-1;
	}
	
	
	
	
}
