package pdi.ppm.model;

public class ChannelStatisticData{
	
	double mean;
	double var;
	double stDv;
	double stDvMeanPercent;
	public ChannelStatisticData(double channelMean, double channelVar) {
		super();
		this.mean = channelMean;
		this.var = channelVar;
		
		
	}
	
	
	
	public ChannelStatisticData(double mean, double var, double stDv) {
		super();
		this.mean = mean;
		this.var = var;
		this.stDv = stDv;
		this.stDvMeanPercent=(stDv/mean)*100;
	}



	public ChannelStatisticData() {
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	
	public double getMean() {
		return mean;
	}



	public void setMean(double mean) {
		this.mean = mean;
	}



	public double getVar() {
		return var;
	}



	public void setVar(double var) {
		this.var = var;
	}



	public double getStDv() {
		return stDv;
	}



	public void setStDv(double stDv) {
		this.stDv = stDv;
	}
	
	



	public double getStDvMeanPercent() {
		return stDvMeanPercent;
	}



	@Override
	public String toString() {
		return "ChannelStatisticData [mean=" + mean + ", var=" + var + ", stDv=" + stDv + ", stDvMeanPercent="
				+ stDvMeanPercent + "]";
	}



	


	
	
	
	
	
}