package pdi.ppm.model;

import java.util.ArrayList;
import java.util.List;

public class ImageMatrix {

	
	 Pixel[][] pixel=null;
	 List<Pixel> lPixels=null;
	 public int [][] R,G,B;
	 int [] rCol, bCol, gCol;
	 int width;
	 int height;
	 
	
	
	
	public ImageMatrix(Pixel[][] pixel, int height , int width) {
		super();
		this.pixel = pixel;
		this.width = width;
		this.height = height;
		preprocessIm();
	}

	private void preprocessIm() {
		lPixels= new ArrayList<Pixel>();
		R=new int[height][width];
		G=new int[height][width];
		B=new int[height][width];
		int mxn=width*height;
		rCol=new int[mxn];
		gCol=new int[mxn];
		bCol=new int[mxn];
		int posCol=0;
		for (int i = 0; i < pixel.length; i++) {
			for (int j = 0; j < pixel[i].length; j++) {
				lPixels.add(pixel[i][j]);
				R[i][j]=pixel[i][j].R;
				G[i][j]=pixel[i][j].G;
				B[i][j]=pixel[i][j].B;
				rCol[posCol]=pixel[i][j].R;
				gCol[posCol]=pixel[i][j].G;
				bCol[posCol]=pixel[i][j].B;
			}
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
	
	public ImageMatrix subMatrix(int f0,int f1, int c0, int c1){
		Pixel[][] pixels= new Pixel[(f1-f0)+1][(c1-c0)+1];
		int row=0;
		int col=0;
		for (int i = f0; i <= f1; i++) {
			
			for (int j = c0; j <= c1; j++) {
				pixels[row][col]=this.getPixel(i, j);
				col++;
			}
			col=0;
			row++;
		}
		return new ImageMatrix(pixels, pixels.length, pixels[0].length);
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
