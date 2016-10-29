package pdi.ppm.util;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mkobos.pca_transform.PCA;
import com.mkobos.pca_transform.PCA.TransformationType;

import Jama.Matrix;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.Pixel;

public class Utils {
	
	private static Utils instance= new Utils();
	
	public static Utils getInstance(){
		return instance;
	}

	private PCA p;
	
	public void showImage(String filePath) throws IOException{
		File f = new File(filePath);
		Image im=ImageIO.read(f);
		showImage(im);
	}
	
	
	public void showImage(Image im){
		
		JFrame frame = new JFrame();

		JLabel lblimage = new JLabel(new ImageIcon(im));
		frame.getContentPane().add(lblimage, BorderLayout.CENTER);
		frame.setSize(300, 400);
		frame.setVisible(true);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(lblimage);
		// add more components here
		frame.add(mainPanel);
		frame.setVisible(true);
	}
	
	
	public ImageMatrix parseToImageMatrix(ColorProcessor cp){
		ByteProcessor[] channel;
		//cp=cp.convertToRGB();
        channel = new ByteProcessor[cp.getNChannels()];

        for (int i = 0; i < channel.length; i++) {
            channel[i] = cp.getChannel(i + 1, channel[i]);
        }
        
        Pixel[][] pixels= new Pixel[cp.getHeight()][cp.getWidth()];
          
           for (int u = 0; u < cp.getHeight(); u++) {
                for (int v = 0; v < cp.getWidth(); v++) {
                	pixels[u][v]= new Pixel(channel[0].get(u, v), channel[1].get(u, v), channel[2].get(u, v));
                	//System.out.print(pixels[u][v]);
                }
           }
           
           return new ImageMatrix(pixels,cp.getWidth(),cp.getHeight());
	}
	
	public double getMean(Matrix x, int col) {
		int rows=x.getRowDimension();
		double sum=0;
		for (int i = 0; i < rows; i++) {
			sum=sum+x.get(i, col);
		}
		return sum/rows;
	}
	
	public double getStDv(Matrix x, int col) {
		double mean=getMean(x, col);
		return getStDv(x, col, mean);
	}
	
	public double getStDv(Matrix x, int col, double mean) {
		return Math.sqrt(getVariance(x, col, mean));
	}
	
	public double getVariance(Matrix x, int col, double mean) {
		int rows=x.getRowDimension();
		
		double sum=0;
		for (int i = 0; i < rows; i++) {
			sum=sum + Math.pow((x.get(i, col)-mean),2);
		}
		return (sum/rows);
	}
	
	public PCA getPCA(){
		if (p!=null) {
			return p;
		}
		
		Matrix trainindData= new Matrix(new double[][] {
            {1, 2, 3},
            {3, 2, 1}});
		p=new PCA(trainindData);
		
		return p;
		
	}
	
	public Matrix getScores(List<Pixel> pixels, PCA p){
		double[][] data= new double[pixels.size()][3];
		for (int i = 0; i < data.length; i++) {
			data[i]=pixels.get(i).toVector();
		}
		Matrix m= new Matrix(data);
		return p.transform(m, TransformationType.ROTATION);
		
	}
	
	
}
