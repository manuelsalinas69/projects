package pdi.image.util;

public class LogicalUtil {
	
	public static boolean exclusiveOR(boolean h1, boolean h2){
		return (h1|| h2)&&!(h1&&h2);
	}

}
