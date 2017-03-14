package pdi.jpa.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
	
	public static byte[] extractBytes(String fileName) throws IOException {
//		 File imgPath = new File(fileName);
//		 BufferedImage bufferedImage = ImageIO.read(imgPath);
//
//		 // get DataBufferBytes from Raster
//		 WritableRaster raster = bufferedImage .getRaster();
//		
//		 DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
//		 
//		 return data.getData();

		Path path = Paths.get(fileName);
	    byte[] data = Files.readAllBytes(path);
	    return data;
		
	}

}
