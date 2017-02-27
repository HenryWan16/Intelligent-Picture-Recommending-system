/*
 * This file is part of the LIRE project: http://www.SemanticMetadata.net/lire.
 *
 * Lire is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Lire is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Lire; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Copyright statement:
 * --------------------
 * (c) 2008 by Savvas A. Chatzichristofis, savvash@gmail.com
 * 
 * some further adoptions to Lire made by
 *     Mathias Lux, mathias@juggle.at
 */

package team.service.CEDD;

import java.util.StringTokenizer;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * The CEDD feature was created, implemented and provided by Savvas A. Chatzichristofis<br/>
 * More information can be found in: Savvas A. Chatzichristofis and Yiannis S. Boutalis,
 * <i>CEDD: Color and Edge Directivity Descriptor. A Compact
 * Descriptor for Image Indexing and Retrieval</i>, A. Gasteratos, M. Vincze, and J.K.
 * Tsotsos (Eds.): ICVS 2008, LNCS 5008, pp. 312-322, 2008.
 *
 * @author: Savvas A. Chatzichristofis, savvash@gmail.com
 */
public class CEDD {
    public double T0;
    public double T1;
    public double T2;
    public double T3;
    public boolean Compact = false;
    protected double[] data = null;
    public byte[] data_byte = null;
    
    // 通过Compact来表示使用10bins还是24bins，compact = false则表示使用24bins
    public CEDD(double Th0, double Th1, double Th2, double Th3, boolean CompactDescriptor) {
        this.T0 = Th0;
        this.T1 = Th1;
        this.T2 = Th2;
        this.T3 = Th3;
        this.Compact = CompactDescriptor;
    }
    
    // T0：检测该小区是否会含有边缘信息
    // T1：判断该小区是否含有无方向信息
    // T2 = T3：判断该小区是否含有其他四个方向的信息
    public CEDD() {
        this.T0 = 14;
        this.T1 = 0.68;
        this.T2 = 0.98;
        this.T3 = 0.98;
    }

    // Apply filter
    // signature changed by mlux
    public void extract(Bitmap image) {
        Fuzzy10Bin Fuzzy10 = new Fuzzy10Bin(false);
        Fuzzy24Bin Fuzzy24 = new Fuzzy24Bin(false);
        RGB2HSV HSVConverter = new RGB2HSV();
        int[] HSV = new int[3];

        double[] Fuzzy10BinResultTable = new double[10];
        double[] Fuzzy24BinResultTable = new double[24];
        double[] CEDD = new double[144];

        int width = image.getWidth();
        int height = image.getHeight();
        
        Log.d("CEDD", width + "");
        Log.d("CEDD", height + "");

        double[][] ImageGrid = new double[width][height];		// Y值
        double[][] PixelCount = new double[2][2];
        int[][] ImageGridRed = new int[width][height];
        int[][] ImageGridGreen = new int[width][height];
        int[][] ImageGridBlue = new int[width][height];
        
        // 把图片分成1600块，每块长度值X为Step_X，Y值为Step_Y
        int NumberOfBlocks = 1600;                       
        int Step_X = (int) Math.floor(width / Math.sqrt(NumberOfBlocks));
        int Step_Y = (int) Math.floor(height / Math.sqrt(NumberOfBlocks));

        if ((Step_X % 2) != 0) {                          
            Step_X = Step_X - 1;
        }
        if ((Step_Y % 2) != 0) {
            Step_Y = Step_Y - 1;
        }

        if (Step_Y < 2) Step_Y = 2;
        if (Step_X < 2) Step_X = 2;
        
        Log.d("testCEDD", "init Successfully!");
        
        /**
         *  某个区块纹理信息的提取结果
         * (直观理解)各维的信息含义分别为：
         * Edges[0]无边缘信息，Edges[1]无方向的边缘信息
         * Edges[2]水平方向的边缘信息，Edges[3]垂直方向的边缘信息
         * Edges[4]45度方向的边缘信息，Edges[5]135度方向的边缘信息
         * 但这里不是这样实现的，而是把有哪几种边缘信息记录在Edges数组中
         * 比如Edges = {1, 4, -1, -1, -1, -1}表示有无方向的边缘信息和45度方向的边缘信息，发现-1即停止扫描
         */
        int[] Edges = new int[6];

        MaskResults MaskValues = new MaskResults();
        Neighborhood PixelsNeighborhood = new Neighborhood();

        // 初始化CEDD特征值为0，以后每一区域都修改了这个CEDD特征值数组
        for (int i = 0; i < 144; i++) {
            CEDD[i] = 0;
        }

        //获取每一点的像素点的Y值(图像的灰度信息)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
            	// 输入像素点的X坐标和Y坐标，得到RGB颜色模型，每个颜色分量只有8位精度
                int pixel = image.getPixel(x, y);
                
                // 默认RGB颜色模型8*8*8bit的数组
                ImageGridRed[x][y] = (pixel >> 16) & 0xff;             
                ImageGridGreen[x][y] = (pixel >> 8) & 0xff;
                ImageGridBlue[x][y] = (pixel) & 0xff;
                
                //Test 
                //System.out.println("x="+x+" y="+y+"{"+ImageGridRed[x][y]+" "+ImageGridGreen[x][y]+" "+ImageGridBlue[x][y]+"}");
                
                // YIQ中 Y=0.587G+0.114B+0.29R Y是颜色的明视度 即灰度
                // IQ一块儿描述了色调信息
                int mean = (int) (0.114 * ImageGridBlue[x][y] + 0.587 * ImageGridGreen[x][y] + 0.299 * ImageGridRed[x][y]);
                ImageGrid[x][y] = mean;
                
             
            }
        }
          
        Log.d("testCEDD", "Getting Y Successfully!");

        int[] CororRed = new int[Step_Y * Step_X];
        int[] CororGreen = new int[Step_Y * Step_X];
        int[] CororBlue = new int[Step_Y * Step_X];

        int[] CororRedTemp = new int[Step_Y * Step_X];
        int[] CororGreenTemp = new int[Step_Y * Step_X];
        int[] CororBlueTemp = new int[Step_Y * Step_X];

        int MeanRed, MeanGreen, MeanBlue;
        
        // 按区域扫描整张图片，(x,y)为某一区域的开始点
        // 在某一个区域中
        // 从下到上，从左到右依次扫描图片中的所有区域
        for (int y = 0; y < height - Step_Y; y += Step_Y) {
            for (int x = 0; x < width - Step_X; x += Step_X) {


                MeanRed = 0;
                MeanGreen = 0;
                MeanBlue = 0;
                PixelsNeighborhood.Area1 = 0;
                PixelsNeighborhood.Area2 = 0;
                PixelsNeighborhood.Area3 = 0;
                PixelsNeighborhood.Area4 = 0;
                Edges[0] = -1;
                Edges[1] = -1;
                Edges[2] = -1;
                Edges[3] = -1;
                Edges[4] = -1;
                Edges[5] = -1;

                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        PixelCount[i][j] = 0;
                    }
                }

                int TempSum = 0;
                
                // 从下到上，从左到右扫描一个区域里面的所有像素点
                // 此时(x, y)为区域的起点
                // CororRed为属于该区域的所有点的R值
                for (int i = y; i < y + Step_Y; i++) {
                    for (int j = x; j < x + Step_X; j++) {

                        CororRed[TempSum] = ImageGridRed[j][i];
                        CororGreen[TempSum] = ImageGridGreen[j][i];
                        CororBlue[TempSum] = ImageGridBlue[j][i];

                        CororRedTemp[TempSum] = ImageGridRed[j][i];
                        CororGreenTemp[TempSum] = ImageGridGreen[j][i];
                        CororBlueTemp[TempSum] = ImageGridBlue[j][i];

                        TempSum++;
                        
                        // 求出各个区域的4个子小区域的平均灰度值g(i,j)
                        // 即把每个子小区域的所有像素点的灰度求和，再除以区域的面积
                        // 求到的结果保存在每一个子小区块的相应位置
                        if (j < (x + Step_X / 2) && i < (y + Step_Y / 2))
                            PixelsNeighborhood.Area1 += 4 * ImageGrid[j][i] / (Step_X * Step_Y);
                        if (j >= (x + Step_X / 2) && i < (y + Step_Y / 2))
                            PixelsNeighborhood.Area2 += 4 * ImageGrid[j][i] / (Step_X * Step_Y);
                        if (j < (x + Step_X / 2) && i >= (y + Step_Y / 2))
                            PixelsNeighborhood.Area3 += 4 * ImageGrid[j][i] / (Step_X * Step_Y);
                        if (j >= (x + Step_X / 2) && i >= (y + Step_Y / 2))
                            PixelsNeighborhood.Area4 += 4 * ImageGrid[j][i] / (Step_X * Step_Y);
                    }
                }
                
                // 使用5个边缘直方图数字滤波器求出每个区域内的边缘信息值
                // 输入的参数是子小区域的平均灰度值g和每个过滤器在相应子小区域的参数a
                MaskValues.Mask1 = Math.abs(PixelsNeighborhood.Area1 * 2 + PixelsNeighborhood.Area2 * -2 + PixelsNeighborhood.Area3 * -2 + PixelsNeighborhood.Area4 * 2);
                MaskValues.Mask2 = Math.abs(PixelsNeighborhood.Area1 * 1 + PixelsNeighborhood.Area2 * 1 + PixelsNeighborhood.Area3 * -1 + PixelsNeighborhood.Area4 * -1);
                MaskValues.Mask3 = Math.abs(PixelsNeighborhood.Area1 * 1 + PixelsNeighborhood.Area2 * -1 + PixelsNeighborhood.Area3 * 1 + PixelsNeighborhood.Area4 * -1);
                MaskValues.Mask4 = Math.abs(PixelsNeighborhood.Area1 * Math.sqrt(2) + PixelsNeighborhood.Area2 * 0 + PixelsNeighborhood.Area3 * 0 + PixelsNeighborhood.Area4 * -Math.sqrt(2));
                MaskValues.Mask5 = Math.abs(PixelsNeighborhood.Area1 * 0 + PixelsNeighborhood.Area2 * Math.sqrt(2) + PixelsNeighborhood.Area3 * -Math.sqrt(2) + PixelsNeighborhood.Area4 * 0);

                double Max = Math.max(MaskValues.Mask1, Math.max(MaskValues.Mask2, Math.max(MaskValues.Mask3, Math.max(MaskValues.Mask4, MaskValues.Mask5))));
                
                // 对5个边缘值n进行规范化
                MaskValues.Mask1 = MaskValues.Mask1 / Max;
                MaskValues.Mask2 = MaskValues.Mask2 / Max;
                MaskValues.Mask3 = MaskValues.Mask3 / Max;
                MaskValues.Mask4 = MaskValues.Mask4 / Max;
                MaskValues.Mask5 = MaskValues.Mask5 / Max;
                
                // 根据边缘方向的阀值，提取出某区域上的6维的纹理特征直方图Edges[ ] 
                int T = -1;
                
                // T表示某类别的边缘信息的区域值，最多为5
                // Edges[ ]的值表示属于哪一类别 比如Edges[X] = 4表示45度方向的边缘信息区
                if (Max < T0) {
                    Edges[0] = 0;
                    T = 0;
                } else {
                    T = -1;
                    
                    // 区域内有边缘信息，则判断各个方向的信息值
                    // 一个区域可以同时属于几个类别，故允许重复
                    if (MaskValues.Mask1 > T1) {
                        T++;
                        Edges[T] = 1;
                    }
                    if (MaskValues.Mask2 > T2) {
                        T++;
                        Edges[T] = 2;
                    }
                    if (MaskValues.Mask3 > T2) {
                        T++;
                        Edges[T] = 3;
                    }
                    if (MaskValues.Mask4 > T3) {
                        T++;
                        Edges[T] = 4;
                    }
                    if (MaskValues.Mask5 > T3) {
                        T++;
                        Edges[T] = 5;
                    }
                }
                
                // 求得划分出来的每个区域的平均RGB值
                for (int i = 0; i < (Step_Y * Step_X); i++) {
                    MeanRed += CororRed[i];
                    MeanGreen += CororGreen[i];
                    MeanBlue += CororBlue[i];
                }

                MeanRed = (int) (MeanRed / (Step_Y * Step_X));
                MeanGreen = (int) (MeanGreen / (Step_Y * Step_X));
                MeanBlue = (int) (MeanBlue / (Step_Y * Step_X));

                HSV = HSVConverter.ApplyFilter(MeanRed, MeanGreen, MeanBlue);

                if (this.Compact == false) {
                    Fuzzy10BinResultTable = Fuzzy10.ApplyFilter(HSV[0], HSV[1], HSV[2], 2);
                    Fuzzy24BinResultTable = Fuzzy24.ApplyFilter(HSV[0], HSV[1], HSV[2], Fuzzy10BinResultTable, 2);
                    
                    /**
                     * 合并颜色和纹理信息
                     * 每扫描一个区域修改一遍CEDD[6*24]，即在以前的值上累加
                     * 即在含有纹理信息的对应区块（0-7）累加该区域的24bin值
                     */
                    for (int i = 0; i <= T; i++) {
                        for (int j = 0; j < 24; j++) {
                            if (Fuzzy24BinResultTable[j] > 0)
                            	// 纹理信息的每一维中加入了颜色模块，变成6*24维
                            	// Edges[i]表示有哪种方向的边缘信息，不同方向的边缘信息写在CEDD[6*24]的不同维度里
                            	// 仅仅在有边缘信息的地方加入了颜色模块
                            	CEDD[24 * Edges[i] + j] += Fuzzy24BinResultTable[j];
                        }
                    }
                } else {
                    Fuzzy10BinResultTable = Fuzzy10.ApplyFilter(HSV[0], HSV[1], HSV[2], 2);
                    for (int i = 0; i <= T; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (Fuzzy10BinResultTable[j] > 0) CEDD[10 * Edges[i] + j] += Fuzzy10BinResultTable[j];
                        }
                    }
                }
            }
        }
        
        Log.d("testCEDD", "All the areas have been extracted Successfully!");
        
        /**
         * 每一维在整个和中所占的比率有多大
         */
        double Sum = 0;
        for (int i = 0; i < 144; i++) {
            Sum += CEDD[i];
        }

        for (int i = 0; i < 144; i++) {
            CEDD[i] = CEDD[i] / Sum;
        }

        double qCEDD[];
        byte qCEDD_byte[] = null;

        // 量化，比如把double CEDD[144]变成里的每个double转换成0~7的整型数
        // 每个整型数可以用3bit来唯一表示
        // 故整个double CEDD[144]最终可以变成 CEDD[144]每个元素仅仅3bit 
        // 所以总长度从(144*8) = 1152byte 为(144*3)/8 = 54byte
//        if (Compact == false) 
//        {
//            qCEDD = new double[144];
//            CEDDQuant quants = new CEDDQuant();
//            qCEDD = quants.Apply(CEDD);
//        } 
        if (Compact == false) 
        {
            qCEDD_byte = new byte[54];
            CEDDQuant quants = new CEDDQuant();
            qCEDD_byte = quants.Apply(CEDD);
        } 
        else 
        {
            qCEDD = new double[60];
            CompactCEDDQuant quants = new CompactCEDDQuant();
            qCEDD = quants.Apply(CEDD);
        }

//        for (int i = 0; i < qCEDD.length; i++)
//            System.out.println(qCEDD[i]);

//        data = qCEDD;  // changed by mlux
        
        Log.d("testCEDD", "量化并转换为byte[]成功");
        
        this.data_byte = qCEDD_byte;
        
        for(int i = 0; i < qCEDD_byte.length; i++)
        {
        	this.data_byte[i] = qCEDD_byte[i];
        }
//        for(int j=0;j<data.length;j++){
//        	   System.out.println(j+": "+data[j]);
//        }
        for (int j = 0; j < this.data_byte.length; j++)
        {
//        	System.out.println(j+": "+this.data_byte[j]);
        	String str = "";
        	str += j + ": "+this.data_byte[j];
        	Log.d("CEDD", str);
        }
    }
    
//    public float getDistance(VisualDescriptor vd) { // added by mlux
//        // Check if instance of the right class ...
//        if (!(vd instanceof CEDD))
//            throw new UnsupportedOperationException("Wrong descriptor.");
//
//        // casting ...
//        CEDD ch = (CEDD) vd;
//
//        // check if parameters are fitting ...
//        if ((ch.data.length != data.length))
//            throw new UnsupportedOperationException("Histogram lengths or color spaces do not match");
//
//        // Tanimoto coefficient
//        double Result = 0;
//        double Temp1 = 0;
//        double Temp2 = 0;
//
//        double TempCount1 = 0, TempCount2 = 0, TempCount3 = 0;
//
//        for (int i = 0; i < ch.data.length; i++) {
//            Temp1 += ch.data[i];
//            Temp2 += data[i];
//        }
//
//        if (Temp1 == 0 || Temp2 == 0) Result = 100;
//        if (Temp1 == 0 && Temp2 == 0) Result = 0;
//
//        if (Temp1 > 0 && Temp2 > 0) {
//            for (int i = 0; i < ch.data.length; i++) {
//                TempCount1 += (ch.data[i] / Temp1) * (data[i] / Temp2);
//                TempCount2 += (data[i] / Temp2) * (data[i] / Temp2);
//                TempCount3 += (ch.data[i] / Temp1) * (ch.data[i] / Temp1);
//
//            }
//
//            Result = (100 - 100 * (TempCount1 / (TempCount2 + TempCount3
//                    - TempCount1))); //Tanimoto
//        }
//        return (float) Result;
//
//    }
//
//    private double scalarMult(double[] a, double[] b) {
//        double sum = 0.0;
//        for (int i = 0; i < a.length; i++) {
//            sum += a[i] * b[i];
//        }
//        return sum;
//    }
//
//    public String getStringRepresentation() { // added by mlux
//        StringBuilder sb = new StringBuilder(data.length * 2 + 25);
//        sb.append("cedd");
//        sb.append(' ');
//        sb.append(data.length);
//        sb.append(' ');
//        for (double aData : data) {
//            sb.append((int) aData);
//            sb.append(' ');
//        }
//        return sb.toString().trim();
//    }
//
//    public void setStringRepresentation(String s) { // added by mlux
//        StringTokenizer st = new StringTokenizer(s);
//        if (!st.nextToken().equals("cedd"))
//            throw new UnsupportedOperationException("This is not a CEDD descriptor.");
//        data = new double[Integer.parseInt(st.nextToken())];
//        for (int i = 0; i < data.length; i++) {
//            if (!st.hasMoreTokens())
//                throw new IndexOutOfBoundsException("Too few numbers in string representation.");
//            data[i] = Integer.parseInt(st.nextToken());
//        }
//    }
}
