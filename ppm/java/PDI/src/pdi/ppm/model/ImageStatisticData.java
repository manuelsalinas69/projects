package pdi.ppm.model;

public class ImageStatisticData {
	
	ChannelStatisticData RData;
	ChannelStatisticData GData;
	ChannelStatisticData BData;
	

	public ImageStatisticData(ChannelStatisticData rData, ChannelStatisticData gData, ChannelStatisticData bData) {
		super();
		RData = rData;
		GData = gData;
		BData = bData;
	}

	

	public ChannelStatisticData getRData() {
		return RData;
	}



	public ChannelStatisticData getGData() {
		return GData;
	}



	public ChannelStatisticData getBData() {
		return BData;
	}



	@Override
	public String toString() {
		return "ImageStatisticData [RData=" + RData + ", GData=" + GData + ", BData=" + BData + "]";
	}



	




	
}
