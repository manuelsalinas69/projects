package pdi.ppm.operations;

import java.util.List;

import pdi.ppm.model.ChannelStatisticData;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.ImageStatisticData;
import pdi.ppm.model.Pixel;
import pdi.ppm.util.Statistic;
import pdi.ppm.util.Utils;

public class ImageStatistics {
	
	
	public static ImageStatisticData proccess(ImageMatrix m){
		List<Pixel> l=m.toList();
		double data[][]=Utils.getInstance().toArray(l);
		
		ChannelStatisticData chR= proccessChannel(data,0);
		ChannelStatisticData chB= proccessChannel(data,1);
		ChannelStatisticData chG= proccessChannel(data,2);
		
		return new ImageStatisticData(chR, chG, chB);
	}

	private static ChannelStatisticData proccessChannel(double[][] data, int i) {
		double[] channelValues= new double[data.length];
		for (int j = 0; j < data.length; j++) {
			channelValues[j]= data[j][i];
					
		}
		
		Statistic st= new Statistic(channelValues);
		return new ChannelStatisticData(st.getMean(), st.getVariance(), st.getStdDev());
	}

	
}
