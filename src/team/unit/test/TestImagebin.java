//package com.yc.test;
//
//import java.awt.image.BufferedImage; 
//import javax.imageio.ImageIO; 
//import java.io.*;
//
//public class TestImagebin{ 
//public static void main(String args[]) { 
//int[] rgb = new int[3];
//
//File file = new File("./images/Lena.jpg"); 
//BufferedImage bi=null; 
//try{ 
//bi = ImageIO.read(file); 
//}catch(Exception e){ 
//e.printStackTrace(); 
//}
//
//int width=bi.getWidth(); 
//int height=bi.getHeight(); 
//int minx=bi.getMinX(); 
//int miny=bi.getMinY(); 
//System.out.println("width="+width+",height="+height+"."); 
//System.out.println("minx="+minx+",miniy="+miny+".");
//
//for(int i=minx;i<width;i++){ 
//for(int j=miny;j<height;j++){ 
////System.out.print(bi.getRGB(jw, ih)); 
//int pixel=bi.getRGB(i, j); 
//rgb[0] = (pixel & 0xff0000 ) >> 16 ; 
//rgb[1] = (pixel & 0xff00 ) >> 8 ; 
//rgb[2] = (pixel & 0xff ); 
//System.out.println("i="+i+",j="+j+":("+rgb[0]+","+rgb[1]+","+rgb[2]+")");
//
//} 
//}
//
//}
//
//}
