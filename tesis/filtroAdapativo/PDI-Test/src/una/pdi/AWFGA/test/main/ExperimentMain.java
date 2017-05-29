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
		System.out.println("ExperimentMain.	EXPERIMENTO INICIADO!");
		long t1=System.currentTimeMillis();
		Properties p=new Properties();
		p.load(new FileInputStream(args[0]));
		System.out.println("Loaded Properties: "+p);
		ImageDataLoader idl= new ImageDataLoader();
		idl.loadBaseImage(p.getProperty("IMG_URL"),Boolean.valueOf(p.getProperty("IMG_ADD_NOISE")));
		DataProvider.getInstance().setParam(DataProvider.DataParams.FILTER_COLUMN_SIZE, Integer.parseInt(p.getProperty("FILTER_COLUMN_SIZE")));
		DataProvider.getInstance().setParam(DataProvider.DataParams.MAE_FITNESS_PERCENT, Double.parseDouble(p.getProperty("MAE_FITNESS_PERCENT")));
		DataProvider.getInstance().setParam(DataProvider.DataParams.NOISE_DENSITY_FITNESS_PERCENT, Double.parseDouble(p.getProperty("NOISE_DENSITY_FITNESS_PERCENT")));
		DataProvider.getInstance().print();
		ExperimentRunner.run(p);
		long t2=System.currentTimeMillis();
		System.out.println("ExperimentMain.	EXPERIMENTO TERMINADO! Tiempo Total (milisegundos): "+(t2-t1)+" ms.");

	}
}
