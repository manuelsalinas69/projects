package una.pdi.AWFGA.test.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import pdi.image.model.BaseFilter;
import pdi.image.model.WeigthedFilter;
import una.pdi.AWFGA.test.utils.TestUtils;

public class FilterTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Properties p=new Properties();
		p.load(new FileInputStream(args[0]));
		System.out.println("Loaded Properties: "+p);
		ImagePlus img=IJ.openImage(p.getProperty("IMG_URL"));
		//img.show();
		ImageProcessor imp=img.getProcessor();
		//4 2 6 2 1 9 1 5 3 
		//10 5 2 2 2 2 6 9 5 
		//2 9 10 9 6 2 4 4 5
		//6 7 7 4 7 9 3 4 6 
		//6 1 2 1 9 2 7 5 7
		//3 5 8 8 6 7 3 2 5
		//2 2 8 4 1 3 1 4 1 
		//2 7 5 7 7 9 4 2 9 
		String filerText=new String(Files.readAllBytes(Paths.get("VAR.tsv")));
		System.out.println("OutPutFilter: "+filerText);
//		int[][] filter={{2, 7, 5},{ 7, 7, 9},{ 4, 2, 9} };
		int[][] filter=TestUtils.parseToArray(filerText, 3, 3,true);
		BaseFilter mf=new WeigthedFilter(filter);
//		BaseFilter mf=new MedianFilter();

		int[][] imFiltered=mf.applyFilter(imp.getIntArray());
		imp.setIntArray(imFiltered);
		img.show();

	}

}
