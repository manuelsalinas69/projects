package py.com.global.educador.engine.dto;

import java.io.Serializable;

public class ItemDto implements Serializable{

	
	public String itemName;
	public String itemValue;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5038791513147607871L;
	public ItemDto(String itemName, String itemValue) {
		this.itemName = itemName;
		this.itemValue = itemValue;
	}
}
