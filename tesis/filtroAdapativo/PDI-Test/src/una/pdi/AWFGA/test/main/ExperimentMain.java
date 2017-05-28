package una.pdi.AWFGA.test.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import pdi.image.util.DataProvider;
import pdi.image.util.ImageDataLoader;
import una.pdi.AWFGA.ga.main.ExperimentRunner;

public class ExperimentMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Properties p=new Properties();
		p.load(new FileInputStream(args[0]));
		System.out.println("Loaded Properties: "+p);
		ImageDataLoader idl= new ImageDataLoader();
		idl.loadBaseImage(p.getProperty("IMG_URL"),false);
		DataProvider.getInstance().setParam(DataProvider.DataParams.FILTER_COLUMN_SIZE, Integer.parseInt(p.getProperty("FILTER_COLUMN_SIZE")));
		DataProvider.getInstance().setParam(DataProvider.DataParams.MAE_FITNESS_PERCENT, Double.parseDouble(p.getProperty("MAE_FITNESS_PERCENT")));
		DataProvider.getInstance().setParam(DataProvider.DataParams.NOISE_DENSITY_FITNESS_PERCENT, Double.parseDouble(p.getProperty("NOISE_DENSITY_FITNESS_PERCENT")));
		DataProvider.getInstance().print();
		ExperimentRunner.run(p);
		System.out.println("ExperimentMain.Experiment Finished!");

	}
}
