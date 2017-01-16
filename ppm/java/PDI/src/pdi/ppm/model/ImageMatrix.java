package pdi.ppm.model;

import java.util.ArrayList;
import java.util.List;

public class ImageMatrix {

	
	 Pixel[][] pixel=null;
	 List<Pixel> lPixels=null;
	 public double [][] R,G,B;
	 int [] rCol, bCol, gCol;
	 int width;
	 int height;
	 
	
	
	
	public ImageMatrix(Pixel[][] pixel, int height , int width) throws Exception {
		super();
		this.pixel = pixel;
		this.width = width;
		this.height = height;
		preprocessIm();
	}

	private void preprocessIm() throws Exception{
		try {
			lPixels= new ArrayList<Pixel>();
			R=new double[height][width];
			G=new double[height][width];
			B=new double[height][width];
			int mxn=width*height;
			rCol=new int[mxn];
			gCol=new int[mxn];
			bCol=new int[mxn];
			int posCol=0;
			for (int i = 0; i < pixel.length; i++) {
				for (int j = 0; j < pixel[i].length; j++) {
					//System.out.println("i:"+i+",j:"+j);
					lPixels.add(pixel[i][j]);
					R[i][j]=pixel[i][j].R;
					G[i][j]=pixel[i][j].G;
					B[i][j]=pixel[i][j].B;
					rCol[posCol]=pixel[i][j].R;
					gCol[posCol]=pixel[i][j].G;
					bCol[posCol]=pixel[i][j].B;
				}
			}
		} catch (Exception e) {
//			System.out.println("Puto ERROR: "+e);
//			e.printStackTrace();
			throw e;
		}
		
	}

	public List<Pixel> toList(){
		if (lPixels!=null) {
			return lPixels;
		}
		lPixels= new ArrayList<Pixel>();
		for (int i = 0; i < pixel.length; i++) {
			for (int j = 0; j < pixel[i].length; j++) {
				lPixels.add(pixel[i][j]);
			}
		}
		return lPixels;
		
	} 
	
	public ImageMatrix subMatrix(int f0,int f1, int c0, int c1) throws Exception{
		int row=0;
		int col=0;
		f1=Math.min(Math.max(0,f1), (height-1));
		c1=Math.min(Math.max(0,c1), (width-1));
		Pixel[][] pixels= new Pixel[Math.max(1, (f1-f0)+1)][Math.max(1,(c1-c0)+1)];
		for (int i = f0; i <= f1; i++) {
			for (int j = c0; j <= c1; j++) {
				pixels[row][col]=this.getPixel(i, j);
				col++;
			}
			col=0;
			row++;
		}
		
		
		try {
			return new ImageMatrix(pixels, pixels.length, pixels[0].length);
		} catch (Exception e) {
			System.out.println("Error generando submatriz f0:"+f0+", f1:"+f1+" - c0:"+c0+", c1: "+c1+": pixels");
			e.printStackTrace();
			throw e;
		}
	}

	
	public Pixel getPixel(int row, int col){
		return pixel[row][col];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
