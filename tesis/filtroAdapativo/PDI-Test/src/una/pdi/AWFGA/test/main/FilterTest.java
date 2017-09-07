package una.pdi.AWFGA.test.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import pdi.image.model.BaseFilter;
import pdi.image.model.MedianFilter;
import pdi.image.model.RAMF;
import pdi.image.model.WeigthedFilter;
import una.pdi.AWFGA.test.utils.TestUtils;

public class FilterTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Properties p=new Properties();
		p.load(new FileInputStream(args[0]));
		System.out.println("Loaded Properties: "+p);
		ImagePlus img=IJ.openImage(p.getProperty("IMG_URL"));
		//img.show();
		ImageConverter ic = new ImageConverter(img);
		ic.convertToGray8();
		img.updateAndDraw();
		ImageProcessor imp=img.getProcessor();
		
		//4 2 6 2 1 9 1 5 3 
		//10 5 2 2 2 2 6 9 5 
		//2 9 10 9 6 2 4 4 5
		//6 7 7 4 7 9 3 4 6 
		//6 1 2 1 9 2 7 5 7
		//3 5 8 8 6 7 3 2 5
		//2 2 8 4 1 3 1 4 1 
		//2 7 5 7 7 9 4 2 9 
		//9 1 7 5 4 1 5 8 6 
		//8 5 9 9 2 10 2 7 4
		//7 1 1 8 1 1 6 1 2
		//8 5 2 2 1 1 4 2 1
//		1 1 3 1 9 4 1 6 4
//		1 1 1 5 9 2 2 3 1
//		1 1 2 1 9 2 2 3 1
//		1 2 1 1 9 2 1 4 1
//		1 1 1 1 8 1 1 1 1
//		1 1 1 1 9 1 1 1 1
//		1 1 1 1 1 1 1 1 1
//		1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
//		5 1 8 6 9 5 6 8 6 9 9 7 9 6 5 6 7 9 1 1 2 8 9 7 10
//		4 8 9 4 6 8 6 7 5 3 8 7 6 6 8 8 6 10 6 7 6 9 5 8 2
//		2 6 8 4 4 8 8 8 9 7 6 7 7 9 5 9 7 6 2 4 2 6 5 5 4
		
//		6 5 8 4 4 6 8 8 7 7 8 6 6 8 3 4 8 8 5 8 4 6 9 5 6 Objectives: 5387
		String filerText=new String(Files.readAllBytes(Paths.get("VAR.tsv")));
		//String f1="15 18 21 24 27 24 21 18 15";
		//System.out.println("OutPutFilter: "+filerText);
//		int[][] filter={{2, 7, 5},{ 7, 7, 9},{ 4, 2, 9} };
		String f1="6 5 8 4 4 6 8 8 7 7 8 6 6 8 3 4 8 8 5 8 4 6 9 5 6";
		int[][] filter=TestUtils.parseToArray(f1, 5, 5,true);
//		int[][] filter=TestUtils.parseToArray(filerText, 3, 3,true);

//		BaseFilter mf=new RAMF(filter);
		BaseFilter mf=new WeigthedFilter(filter);

		int[][] imFiltered=mf.applyFilter(imp.getIntArray());
		//imFiltered=mf.applyFilter(imFiltered);
		imp.setIntArray(imFiltered);
		img.setTitle(mf.getClass().getName()+": "+f1);
		img.show();

	}

}
