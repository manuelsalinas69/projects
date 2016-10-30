package pdi.ppm.util;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
                	if (channel[0].get(v, u)==61
                			&& channel[1].get(v, u)==60
                			&& channel[2].get(v, u)==58
                			) {
						System.out.println("u[fila]:"+u+",v[col]: "+v);
					}
                	pixels[u][v]= new Pixel(channel[0].get(v, u), channel[1].get(v, u), channel[2].get(v, u)); //channel[2].get(columna or width, fila or heigth)
                	//System.out.print(pixels[u][v]);
                }
           }
           
           return new ImageMatrix(pixels,cp.getHeight(),cp.getWidth());
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
	
	public double dot(double[] A, double[]B) throws Exception{
		if (A.length!=B.length) {
			throw new Exception("La dimensiones debe ser iguales");
		}
		double sum=0;
		for (int i = 0; i < A.length; i++) {
			sum+=A[i]*B[i];
		}
		return sum;
	}
	
	public double dot(double[][] A, double[][]B) throws Exception{
		if (A[0].length!=B[0].length) {
			throw new Exception("La dimensiones debe ser iguales");
		}
		double sum=0;
		for (int i = 0; i < A[0].length; i++) {
			sum+=A[0][i]*B[0][i];
		}
		return sum;
	}
	
	public double[][] toVector(double[][]pOrigen,double[][]pExtremo){
		double[][] v= {{
			
			
				pExtremo[0][0]-pOrigen[0][0],
				pExtremo[0][1]-pOrigen[0][1],
				pExtremo[0][2]-pOrigen[0][2]
			
		}};
		
		return v;
	}
	
	public void printPixels(List<Pixel> l, int h, int w) {
		try {
			int pos=0;
			Pixel[][] pixels=new Pixel[h][w];
			
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					pixels[i][j]= l.get(pos);
					pos++;
				}
			}
			ImageMatrix out=new ImageMatrix(pixels, h, w);
			BufferedImage image = new BufferedImage(out.getWidth(), out.getHeight(), BufferedImage.TYPE_INT_RGB); 

			  for (int y = 0; y < out.getHeight(); y++) {
			     for (int x = 0; x < out.getWidth(); x++) {
			        int rgb = out.R[y][x];
			        rgb = (rgb << 8) + out.G[y][x];
			        rgb = (rgb << 8) + out.B[y][x];
			        image.setRGB(x, y, rgb);
			     }
			  }

			  File outputFile = new File("/Users/Manuel/Documents/Tesis/output.jpg");
			  ImageIO.write(image, "jpg", outputFile);
			  Utils.getInstance().showImage("/Users/Manuel/Documents/Tesis/output.jpg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
