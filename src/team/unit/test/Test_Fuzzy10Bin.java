package team.unit.test;

import java.util.Scanner;
import team.service.CEDD.*;

public class Test_Fuzzy10Bin {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        RGB2HSV  rgb2hsv=null;
        Fuzzy10Bin fuzzy10bin=null;
        int rgb[]=new int[3];
        double intHst[]=new double[10]; 
        Scanner in=new Scanner(System.in); 		
		for(int i=0;i<3;i++){
			rgb[i]=in.nextInt();
		}
		rgb2hsv=new RGB2HSV();
		
		int hsv[]=rgb2hsv.ApplyFilter(rgb[0], rgb[1], rgb[2]);
		
		fuzzy10bin=new Fuzzy10Bin(false); 
		intHst=fuzzy10bin.ApplyFilter(hsv[0],hsv[1],hsv[2],2);
		
		for(double element_intHst:intHst) 
		 System.out.println(" "+element_intHst);
	}

}
