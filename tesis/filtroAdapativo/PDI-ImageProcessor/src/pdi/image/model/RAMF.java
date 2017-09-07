package pdi.image.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pdi.image.util.LogicalUtil;

public class RAMF extends WeigthedFilter{

	public RAMF(int[][] filter) {
		super(filter);

	}

	@Override
	public int processFilter(int[][] pixels, int pivotPixel) {
		int filterResult= super.processFilter(pixels, pivotPixel);
		try {
			int[] sortedPixels=getSortedPixels(pixels);
			int xMin=getXMin(sortedPixels);
			int xMax=getXMax(sortedPixels);
			int tMin=tMinus(filterResult, xMin);
			int tMax=tPlus(filterResult, xMax);
			int eMin=tMinus(pivotPixel, xMin);
			int eMax=tPlus(pivotPixel, xMax);

			if (!truthTable1Resul(filterResult,xMin,xMax,tMin,tMax, sortedPixels)) {//si no es ruido
				if(!truthTable2Resul(pivotPixel,xMin,xMax,eMin,eMax, sortedPixels)){//si no es ruido
					System.out.println("Devuelve el pixel original!: (pixel: "+pivotPixel+",filterPixel: "+filterResult+", vecindad: "+Arrays.toString(sortedPixels)+")");
					return pivotPixel;
				}
				else{
					System.out.println("Devuelve el pixel filtrado en tabla2!: (pixel: "+pivotPixel+",filterPixel: "+filterResult+", vecindad: "+Arrays.toString(sortedPixels)+")");
					return filterResult;
				}
			}
			{
				System.out.println("Devuelve el pixel filtrado en tabla1!: (pixel: "+pivotPixel+",filterPixel: "+filterResult+", vecindad: "+Arrays.toString(sortedPixels)+")");
				return  filterResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			 
		}
		return filterResult;
	}

	protected boolean truthTable1Resul(int xMed, int xMin, int xMax, int tMin, int tMax, int[] sortedPixels) {
		boolean h1=h1(xMed, xMin);
		boolean h2=h2(xMed,xMax);
		boolean h3=h3(xMed,sortedPixels);
		boolean a=testA(tMin,tMax,h1,h2,h3);
		boolean b=testB(tMin, tMax, h1, h2, h3);
		boolean c=testC(tMin, tMax, h1, h2, h3);
		boolean d=testD(tMin, tMax, h1, h2, h3);
		return(a || b || c || d);
	}
	
	protected boolean truthTable2Resul(int xIJ, int xMin, int xMax, int uMin, int uMax, int[] sortedPixels) throws Exception {
		boolean e1=e1(xIJ, xMin);
		boolean e2=e2(xIJ,xMax);
		boolean e3=e3(xIJ,sortedPixels);
		boolean a=test2A(uMin,uMax,e1,e2,e3);
		boolean b=test2B(uMin, uMax, e1, e2, e3);
		boolean c=test2C(uMin, uMax, e1, e2, e3);
		boolean d=test2D(uMin, uMax, e1, e2, e3);
		return (a || b || c || d) ;
	}

	protected boolean test2D(int uMin, int uMax, boolean e1, boolean e2, boolean e3) {
		if (uMin>0 && uMax<0) {
			return e3;
		}
		return false;
	}

	protected boolean test2C(int uMin, int uMax, boolean e1, boolean e2, boolean e3) {
		if (uMin>0 && uMax==0) {
			return LogicalUtil.exclusiveOR(e2, e3);
		}
		return false;
	}

	protected boolean test2B(int uMin, int uMax, boolean e1, boolean e2, boolean e3) {
		if (uMin==0 && uMax<0) {
			return LogicalUtil.exclusiveOR(e1, e3);
		}
		return false;
	}

	protected boolean test2A(int uMin, int uMax, boolean e1, boolean e2, boolean e3) throws Exception {
		if (uMin==0 && uMax==0) {
			throw new Exception("No se puede dar este resultado");
		}
		return false;
	}

	protected boolean testA(int tMin, int tMax, boolean h1, boolean h2, boolean h3) {
		if (tMin==0 && tMax==0) {
			return (h1&&!h2&&!h3)||(!h1&&h2&&!h3) || (!h1&&!h2&&h3);
		}

		return false;
	}
	protected boolean testB(int tMin, int tMax, boolean h1, boolean h2, boolean h3) {
		if (tMin==0 && tMax<0) {
			LogicalUtil.exclusiveOR(h1, h3);
		}
		return false;
	}
	protected boolean testC(int tMin, int tMax, boolean h1, boolean h2, boolean h3) {
		if (tMin>0 && tMax==0) {
			LogicalUtil.exclusiveOR(h2, h3);
		}
		return false;
	}
	protected boolean testD(int tMin, int tMax, boolean h1, boolean h2, boolean h3) {
		if (tMin>0 && tMax<0) {
			return h3;
		}
		return false;
	}

	protected int[] getSortedPixels(int[][] pixels) {
		List<Integer> px= new ArrayList<Integer>();
		for (int[] is : pixels) {
			for (int i : is) {
				px.add(i);
			}
		}

		int[] primitiveArray=new int[px.size()]; 
		for (int i = 0; i < primitiveArray.length; i++) {
			primitiveArray[i]=px.get(i);
		}
		Arrays.sort(primitiveArray);
		return primitiveArray;
	}

	protected int getXMax(int[] sortedPixels) {
		return sortedPixels[sortedPixels.length-1];
	}

	protected int getXMin(int[] sortedPixels) {
		return sortedPixels[0];
	}

	@Override
	protected int getBorderValue() {
		return super.getBorderValue();
	}


	public boolean h1(int xMed, int sMin){
		return xMed==sMin;
	}

	public boolean h2(int xMed, int sMax){
		return xMed==sMax;
	}
	public boolean h3(int xMed,int[] sortedPixels){
		for (int px : sortedPixels) {
			if (px==xMed) {
				return true;
			}
		}
		return false;
	}

	public int tMinus(int xMed,int xMin){
		return xMed-xMin;
	}
	public int tPlus(int xMed, int xMax){
		return xMed-xMax;
	}

	/////////////////second level
	public boolean e1(int xIJ, int xMin){
		return xIJ==xMin;
	}

	public boolean e2(int xIJ, int xMax){
		return xIJ==xMax;
	}
	public boolean e3(int xIJ, int [] sortedPixels){
		for (int px : sortedPixels) {
			if (px==xIJ) {
				return true;
			}
		}
		return false;
	}
	
	public int uMinus(int xIJ, int xMin){
		return xIJ-xMin;
	}
	
	public int uPlus(int xIJ, int xMax){
		return xIJ-xMax;
	}


}
