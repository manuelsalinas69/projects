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

import Jama.Matrix;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import pdi.ppm.model.ImageMatrix;
import pdi.ppm.model.Pixel;

public class Utils {
	
	private static Utils instance= new Utils();
	
	boolean preserveImageFile=true;
	
	public static Utils getInstance(){
		return instance;
	}

	
	public void showImage(String filePath) throws IOException{
		File f = new File(filePath);
		Image im=ImageIO.read(f);
		showImage(im);
	}
	
	
	public void showImage(Image im){
		
		JFrame frame = new JFrame();

		JLabel lblimage = new JLabel(new ImageIcon(im));
		frame.getContentPane().add(lblimage, BorderLayout.CENTER);
		frame.setSize(im.getWidth(lblimage), im.getHeight(lblimage));
		frame.setVisible(true);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(lblimage);
		// add more components here
		frame.add(mainPanel);
		frame.setVisible(true);
	}
	
	public void showImage(BufferedImage im){

		JFrame frame = new JFrame();

		JLabel lblimage = new JLabel(new ImageIcon(im));
		frame.getContentPane().add(lblimage, BorderLayout.CENTER);
		frame.setSize(im.getWidth(lblimage), im.getHeight(lblimage));
		frame.setVisible(true);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(lblimage);
		// add more components here
		frame.add(mainPanel);
		frame.setVisible(true);
		
	}
	
	public void showImage(ImageMatrix im) throws IOException{

		BufferedImage image = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB); 

		  for (int y = 0; y < im.getHeight(); y++) {
		     for (int x = 0; x < im.getWidth(); x++) {
		        int rgb = (int)im.R[y][x];
		        rgb = (rgb << 8) + (int)im.G[y][x];
		        rgb = (rgb << 8) + (int)im.B[y][x];
		        image.setRGB(x, y, rgb);
		     }
		  }
		  
		  String fileName="output";
		  String fileExt=".jpg";
		  long t=System.currentTimeMillis();
		  if (preserveImageFile) {
			fileName+=t;
		  }
		  fileName+=fileExt;

		  File outputFile = new File("/Users/Manuel/Documents/Tesis/output/"+fileName);
		  ImageIO.write(image, "jpg", outputFile);
		  Utils.getInstance().showImage("/Users/Manuel/Documents/Tesis/output/"+fileName);
	}
	
	public File saveImage(ImageMatrix im, String outputDir){
		try {
			BufferedImage image = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB); 

			  for (int y = 0; y < im.getHeight(); y++) {
			     for (int x = 0; x < im.getWidth(); x++) {
			        int rgb = (int)im.R[y][x];
			        rgb = (rgb << 8) + (int)im.G[y][x];
			        rgb = (rgb << 8) + (int)im.B[y][x];
			        image.setRGB(x, y, rgb);
			     }
			  }
			  
			  String fileName="output";
			  String fileExt=".jpg";
			  long t=System.currentTimeMillis();
			  fileName+=t;
			  fileName+=fileExt;

			  File outputFile = new File(outputDir+"/"+fileName);
			  ImageIO.write(image, "jpg", outputFile);
			  return outputFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ImageMatrix parseToImageMatrix(ColorProcessor cp) throws Exception{
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
			        int rgb = (int)out.R[y][x];
			        rgb = (rgb << 8) + (int)out.G[y][x];
			        rgb = (rgb << 8) + (int)out.B[y][x];
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
	
	public long getVolume(ImageMatrix im){
		long v=0;
		  for (int y = 0; y < im.getHeight(); y++) {
			     for (int x = 0; x < im.getWidth(); x++) {
			        v+=im.getPixel(y, x).volume();
			     }
			  }
		return v;
		
	}
	
	public ImageMatrix minus(ImageMatrix A, ImageMatrix B) throws Exception{
		Pixel [][] out=new Pixel[A.getHeight()][A.getWidth()];
		
		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[0].length; j++) {
				out[i][j]=A.getPixel(i, j).minus(B.getPixel(i, j));
			}
		}
		return new ImageMatrix(out, A.getHeight(), A.getWidth());
	}
	
	
	public double[][] toArray(List<Pixel> pixels){
		double[][] data= new double[pixels.size()][3];
		//System.out.println("---------------");
		for (int i = 0; i < data.length; i++) {
			data[i]=pixels.get(i).toVector();
			
			//System.out.println(Arrays.toString(data[i]));
		}
		return data;

	}
	
}
