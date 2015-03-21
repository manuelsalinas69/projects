package py.com.global.educador.gui.utils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Reader {
	public static List<String> readXlsx(File inputFile,int cellType){
		List<String> values= new ArrayList<String>();
		
		try 
		{
			int countNumbers=0;
			// Get the workbook instance for XLSX file
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(inputFile));

			// Get first sheet from the workbook
			XSSFSheet sheet = wb.getSheetAt(0);

			Row row;
			Cell cell;

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) 
			{
				row = rowIterator.next();

				// For each row, iterate through each columns
				//Iterator<Cell> cellIterator = row.cellIterator();
				cell= row.getCell(0);

				//		                while (cellIterator.hasNext()) 
				//		                {
				//		                cell = cellIterator.next();

				switch (cell.getCellType()) 
				{

				case Cell.CELL_TYPE_BOOLEAN:
					//System.out.println("CELL_TYPE_BOOLEAN: "+cell.getBooleanCellValue());
					break;

				case Cell.CELL_TYPE_NUMERIC:
					countNumbers++;
					if (Cell.CELL_TYPE_NUMERIC==cellType) {
						values.add((long)cell.getNumericCellValue()+"");
						//System.out.println("Number: "+(long)cell.getNumericCellValue());
					}
					//System.out.println("CELL_TYPE_NUMERIC: "+cell.getNumericCellValue());
					break;

				case Cell.CELL_TYPE_STRING:
					System.out.println("CELL_TYPE_STRING: "+cell.getStringCellValue());
					break;

				case Cell.CELL_TYPE_BLANK:
					//System.out.println("CELL_TYPE_BLANK:[empty cell]");
					break;

				default:
					//System.out.println("DEFAULT: "+cell);

				}
				//}
			}
			System.out.println("Numbers--> "+countNumbers);
		}
		catch (Exception e) 
		{
			System.err.println("Exception :" + e.getMessage());
		}
		return values;
	}
	
	public static List<String> readXlsx(byte[] data ,int cellType){
		List<String> values= new ArrayList<String>();
		
		try 
		{
			int countNumbers=0;
			// Get the workbook instance for XLSX file
			XSSFWorkbook wb = new XSSFWorkbook(new ByteArrayInputStream(data));

			// Get first sheet from the workbook
			XSSFSheet sheet = wb.getSheetAt(0);

			Row row;
			Cell cell;

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) 
			{
				row = rowIterator.next();

				// For each row, iterate through each columns
				//Iterator<Cell> cellIterator = row.cellIterator();
				cell= row.getCell(0);

				//		                while (cellIterator.hasNext()) 
				//		                {
				//		                cell = cellIterator.next();

				switch (cell.getCellType()) 
				{

				case Cell.CELL_TYPE_BOOLEAN:
					//System.out.println("CELL_TYPE_BOOLEAN: "+cell.getBooleanCellValue());
					break;

				case Cell.CELL_TYPE_NUMERIC:
					countNumbers++;
					if (Cell.CELL_TYPE_NUMERIC==cellType) {
						values.add((long)cell.getNumericCellValue()+"");
						//System.out.println("Number: "+(long)cell.getNumericCellValue());
					}
					//System.out.println("CELL_TYPE_NUMERIC: "+cell.getNumericCellValue());
					break;

				case Cell.CELL_TYPE_STRING:
					System.out.println("CELL_TYPE_STRING: "+cell.getStringCellValue());
					break;

				case Cell.CELL_TYPE_BLANK:
					//System.out.println("CELL_TYPE_BLANK:[empty cell]");
					break;

				default:
					//System.out.println("DEFAULT: "+cell);

				}
				//}
			}
			System.out.println("Numbers--> "+countNumbers);
		}
		catch (Exception e) 
		{
			System.err.println("Exception :" + e.getMessage());
		}
		return values;
	}
	
	
	public static List<String> readAndGetNumericValues(File inputFile, int cellType){
		return inputFile.getName().endsWith("xlsx")?readXlsx(inputFile, cellType):readXls(inputFile, cellType);
	}

	public static List<String> readXls(File inputFile, int cellType) 
	{
		List<String> values=new ArrayList<String>();
		try 
		{
			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(inputFile));
			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			Cell cell;
			Row row;

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) 
			{
				row = rowIterator.next();

				// For each row, iterate through each columns
				//Iterator<Cell> cellIterator = row.cellIterator();
				cell= row.getCell(0);
				//				while (cellIterator.hasNext()) 
				//				{
				//					cell = cellIterator.next();

				switch (cell.getCellType()) 
				{

				case Cell.CELL_TYPE_BOOLEAN:
					//System.out.println(cell.getBooleanCellValue());
					break;

				case Cell.CELL_TYPE_NUMERIC:
					if (Cell.CELL_TYPE_NUMERIC==cellType) {
						values.add((long)cell.getNumericCellValue()+"");
						System.out.println("Number: "+(long)cell.getNumericCellValue());
					}
					break;

				case Cell.CELL_TYPE_STRING:
					//System.out.println(cell.getStringCellValue());
					break;

				case Cell.CELL_TYPE_BLANK:
					//System.out.println(" ");
					break;

				default:
					//System.out.println(cell);
				}
				//}
			}

		} 

		catch (FileNotFoundException e) 
		{
			System.err.println("Exception" + e.getMessage());
		}
		catch (IOException e) 
		{
			System.err.println("Exception" + e.getMessage());
		}
		return values;
	}
	
	public static List<String> readXls(byte[] data, int cellType) 
	{
		List<String> values=new ArrayList<String>();
		try 
		{
			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(new ByteArrayInputStream(data));
			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			Cell cell;
			Row row;

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) 
			{
				row = rowIterator.next();

				// For each row, iterate through each columns
				//Iterator<Cell> cellIterator = row.cellIterator();
				cell= row.getCell(0);
				//				while (cellIterator.hasNext()) 
				//				{
				//					cell = cellIterator.next();

				switch (cell.getCellType()) 
				{

				case Cell.CELL_TYPE_BOOLEAN:
					//System.out.println(cell.getBooleanCellValue());
					break;

				case Cell.CELL_TYPE_NUMERIC:
					if (Cell.CELL_TYPE_NUMERIC==cellType) {
						values.add((long)cell.getNumericCellValue()+"");
						//System.out.println("Number: "+(long)cell.getNumericCellValue());
					}
					break;

				case Cell.CELL_TYPE_STRING:
					//System.out.println(cell.getStringCellValue());
					break;

				case Cell.CELL_TYPE_BLANK:
					//System.out.println(" ");
					break;

				default:
					//System.out.println(cell);
				}
				//}
			}

		} 

		catch (FileNotFoundException e) 
		{
			System.err.println("Exception" + e.getMessage());
		}
		catch (IOException e) 
		{
			System.err.println("Exception" + e.getMessage());
		}
		return values;
	}

	public static void main(String[] args) {
//		File f=new File(args[0]);
//		String operacion=args[1];
//		String numeroCorto=args[2];
//		String host=args[3];
//		String port=args[4];
//		List<String>l=Reader.readAndGetNumericValues(f, Cell.CELL_TYPE_NUMERIC);
//		//System.out.println(l.size());
//		AppServicesClient appServicesClient= new AppServicesClient();
//		System.out.println("Start new suscription process...");
//		for (String number : l) {
//			if (!number.startsWith("0")) {
//				number="0"+number;
//				//System.out.println("Number--> "+number);
//			}
//			if(appServicesClient.suscriptonProccess(operacion, numeroCorto, number, host, port)!=HttpURLConnection.HTTP_OK){
//				System.out.println("Number Error---> "+number);
//			}
//		}
		
//		String filter="IN_STATE:FINALIZADO";
//		String states=filter.substring(filter.indexOf(":")+1);
//		String[] stat= states.split(",");
//		List<String> l= Arrays.asList(stat);
//		for (String sta : l) {
//			System.out.println(sta);
//		}
		
		String m="Bienvenido al Programa de Educacion Financiera de Vision Banco y Tigo. Te invitamos a participar sin costo y podras ganar importantes premios";
		
		System.out.println("length--> "+m.length());
				
		
		
	}
}
