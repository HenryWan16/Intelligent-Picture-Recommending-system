package team.unit.test;

import java.util.Scanner;
import team.service.CEDD.RGB2HSV;

public class Test_RGB2HSV {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Input RGB Value:");
		int rgb[]=new int[3];
		int hsv[];
		
        Scanner in=new Scanner(System.in); 		
		for(int i=0;i<3;i++){
			rgb[i]=in.nextInt();

		}
			
		RGB2HSV rgb2hsv=new RGB2HSV();    
	
		hsv=rgb2hsv.ApplyFilter(rgb[0], rgb[1], rgb[2]);
	    
	   for(int element_rgb:rgb)
		   System.out.print(element_rgb+" ");
	   System.out.println("");
	   for(int element_hsv:hsv)
		   System.out.print(element_hsv+" ");
	}

}
