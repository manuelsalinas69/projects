package una.pdi.AWFGA.test.main;

import java.io.FileInputStream;
import java.util.Properties;

import pdi.image.util.DataProvider;
import pdi.image.util.DataProvider.DataParams;
import pdi.image.util.ImageDataLoader;
import una.pdi.AWFGA.ga.main.GenerationalGeneticAlgorithmRunner;

public class Main {

	public static void main(String[] args) throws Exception {
		
		Properties p=new Properties();
		p.load(new FileInputStream(args[0]));
		System.out.println("Loaded Properties: "+p);
		ImageDataLoader idl= new ImageDataLoader();
		idl.setAndPreProccessBaseImage(p.getProperty("IMG_URL"),false);
		DataProvider.getInstance().setParam(DataProvider.DataParams.FILTER_COLUMN_SIZE, Integer.parseInt(p.getProperty("FILTER_COLUMN_SIZE")));
		//DataProvider.getInstance().setParam(DataProvider.DataParams.MAE_FITNESS_PERCENT, Double.parseDouble(p.getProperty("MAE_FITNESS_PERCENT")));
		//DataProvider.getInstance().setParam(DataProvider.DataParams.NOISE_DENSITY_FITNESS_PERCENT, Double.parseDouble(p.getProperty("NOISE_DENSITY_FITNESS_PERCENT")));
		DataProvider.getInstance().setParam(DataParams.ALGORITM_GA_MAX_LIMIT, Integer.parseInt(p.getProperty("ALGORITM_GA_MAX_LIMIT")));
		DataProvider.getInstance().setParam(DataParams.ALGORITM_GA_MIN_LIMIT, Integer.parseInt(p.getProperty("ALGORITM_GA_MIN_LIMIT")));
		DataProvider.getInstance().setParam(DataParams.ALGORITM_NOISE_ESTIMATION_FILTER, Integer.parseInt(p.getProperty("ALGORITM_NOISE_ESTIMATION_FILTER")));
		DataProvider.getInstance().print();
		System.out.println("IMG_URL: "+p.getProperty("IMG_URL"));
		GenerationalGeneticAlgorithmRunner.run(p);
		//FilterTest.main(args);

	}

}
