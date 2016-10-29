package pdi.ppm.model;

import java.util.ArrayList;
import java.util.List;

public class ImageMatrix {

	
	 Pixel[][] pixel=null;
	 List<Pixel> lPixels=null;
	 int [][] R,G,B;
	 int [] rCol, bCol, gCol;
	 int width;
	 int height;
	 
	
	
	
	public ImageMatrix(Pixel[][] pixel, int width, int height) {
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
