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
 */

package team.service.CEDD;

public class Fuzzy10Bin {
    public boolean KeepPreviuesValues = false;

    // H值(色度)的径向边缘分布表，用于描述色彩空间
    protected static double[] HueMembershipValues = {
    	    0, 0, 5, 10,
            5, 10, 35, 50,
            35, 50, 70, 85,
            70, 85, 150, 165,
            150, 165, 195, 205,
            195, 205, 265, 280,
            265, 280, 315, 330,
            315, 330, 360, 360}; // Table Dimensions= Number Of Triangles X 4 (Start - Stop)
    
    // S值(饱和度)的径向边缘分布表，用于描述饱和度空间
    protected static double[] SaturationMembershipValues = {
    	     0, 0, 10, 75,
            10, 75, 255, 255};
    
    // V值(亮度)的径向边缘分布表，用于描述亮度空间
    protected static double[] ValueMembershipValues = {
    	    0, 0, 10, 75,
            10, 75, 180, 220,
            180, 220, 255, 255};

    //Vector fuzzy10BinRules = new Vector();
    public static FuzzyRules[] Fuzzy10BinRules = new FuzzyRules[48];
    
    // 表示HSV经过CLF过滤器过滤之后的结果
    public double[] Fuzzy10BinHisto = new double[10];
    public static double[] HueActivation = new double[8];
    public static double[] SaturationActivation = new double[2];
    public static double[] ValueActivation = new double[3];
    
    /**
     *  20-TSK-like 规则的颜色判定方法
     *  {H, S, V, 颜色直方图信息结果(0~9)}
     *  0 black; 1 gray; 2 white; 3 red; 4 orange; 5 yellow
     *  6 green; 7 cyan; 8 blue; 9 magenta
     */
    public static int[][] Fuzzy10BinRulesDefinition = {
            {0, 0, 0, 2},
            {0, 1, 0, 2},
            {0, 0, 2, 0},
            {0, 0, 1, 1},
            {1, 0, 0, 2},
            {1, 1, 0, 2},
            {1, 0, 2, 0},
            {1, 0, 1, 1},
            {2, 0, 0, 2},
            {2, 1, 0, 2},
            {2, 0, 2, 0},
            {2, 0, 1, 1},
            {3, 0, 0, 2},
            {3, 1, 0, 2},
            {3, 0, 2, 0},
            {3, 0, 1, 1},
            {4, 0, 0, 2},
            {4, 1, 0, 2},
            {4, 0, 2, 0},
            {4, 0, 1, 1},
            {5, 0, 0, 2},
            {5, 1, 0, 2},
            {5, 0, 2, 0},
            {5, 0, 1, 1},
            {6, 0, 0, 2},
            {6, 1, 0, 2},
            {6, 0, 2, 0},
            {6, 0, 1, 1},
            {7, 0, 0, 2},
            {7, 1, 0, 2},
            {7, 0, 2, 0},
            {7, 0, 1, 1},
            {0, 1, 1, 3},
            {0, 1, 2, 3},
            {1, 1, 1, 4},
            {1, 1, 2, 4},
            {2, 1, 1, 5},
            {2, 1, 2, 5},
            {3, 1, 1, 6},
            {3, 1, 2, 6},
            {4, 1, 1, 7},
            {4, 1, 2, 7},
            {5, 1, 1, 8},
            {5, 1, 2, 8},
            {6, 1, 1, 9},
            {6, 1, 2, 9},
            {7, 1, 1, 3},
            {7, 1, 2, 3}
    };  // 48 0 

    // 构造函数，初始化像素颜色判定规则
    // 直接复制了上面的数据到一个48维的数组中
    public Fuzzy10Bin(boolean KeepPreviuesValues) {

        for (int R = 0; R < 48; R++) {
            //fuzzy10BinRules.addElement(new FuzzyRules());
            //FuzzyRules Fuzzy10BinRules = (FuzzyRules) fuzzy10BinRules.elementAt(R);
            Fuzzy10BinRules[R] = new FuzzyRules();
            Fuzzy10BinRules[R].Input1 = Fuzzy10BinRulesDefinition[R][0];
            Fuzzy10BinRules[R].Input2 = Fuzzy10BinRulesDefinition[R][1];
            Fuzzy10BinRules[R].Input3 = Fuzzy10BinRulesDefinition[R][2];
            Fuzzy10BinRules[R].Output = Fuzzy10BinRulesDefinition[R][3];

        }

        this.KeepPreviuesValues = KeepPreviuesValues;


    }

    /**
     * 求出输入的HSV值(input)在整个(色彩，饱和度，亮度)空间上的分布情况
     * 分别会得出一个HSV值在对应空间上的8维、3维、2维数组
     * @param Input
     * 				输入的HSV值
     * @param Triangles
     * 				径向边缘分布表
     * @param MembershipFunctionToSave
     * 				输出的bin值
     * For example: H = 40
     * 则扫描径向边缘分布表
     * MembershipFunctionToSave[1] =  (Input - Triangles[i + 2]) / (Triangles[i + 2] - Triangles[i + 3]) + 1
     * MembershipFunctionToSave[2] =  (Input - Triangles[i]) / (Triangles[i + 1] - Triangles[i])
     * 其余MembershipFunctionToSave[i]为0；
     */
    private void FindMembershipValueForTriangles(double Input, double[] Triangles, double[] MembershipFunctionToSave) {
        int Temp = 0;
        
        // 遍历通道径向边缘分布图里面的每个区域
        for (int i = 0; i <= Triangles.length - 1; i += 4) {

            MembershipFunctionToSave[Temp] = 0;

            // H的值在中间，求出此时bin值
            if (Input >= Triangles[i + 1] && Input <= +Triangles[i + 2]) {
                MembershipFunctionToSave[Temp] = 1;
            }

            // H的值在左侧模糊区，求出此时bin值
            if (Input >= Triangles[i] && Input < Triangles[i + 1]) {
                MembershipFunctionToSave[Temp] = (Input - Triangles[i]) / (Triangles[i + 1] - Triangles[i]);
            }

            // H的值在右侧模糊区，求出此时的bin值
            if (Input > Triangles[i + 2] && Input <= Triangles[i + 3]) {
                MembershipFunctionToSave[Temp] = (Input - Triangles[i + 2]) / (Triangles[i + 2] - Triangles[i + 3]) + 1;
            }

            Temp += 1;
        }

    }

    /**
     * 使用LOM_Defazzificator方式求得20-TSK-like的规则结果
     * @param Rules
     * 							20-TSK-like规则，是一个int[48][4]数组
     * @param Input1
     * 							经过CLF过滤器过滤后某一个像素点的H值 8维，即H值在8个区域空间中的分布情况
     * @param Input2
     * 							经过CLF过滤器过滤后某一个像素点的S值 2维，即S值在2个区域空间中的分布情况
     * @param Input3
     * 							经过CLF过滤器过滤后某一个像素点的V值 3维，即V值在3个区域空间中的分布情况
     * @param ResultTable
     * 							过滤结果 double[10]
     */
    private void LOM_Defazzificator(FuzzyRules[] Rules, double[] Input1, double[] Input2, double[] Input3, double[] ResultTable) {
        int RuleActivation = -1;
        double LOM_MAXofMIN = 0;
        
        /**
         * 循环48次，遍历整个规则列表，寻找HSV落在的区间的组合
         * 比如落在交界处，{5,1,1,8} {6,1,1,9}都可能满足情况
         */
        for (int i = 0; i < Rules.length; i++) {
        	
        	/**
        	 *  寻找输入的8-bins，2-bins，3-bins都不为零的值
        	 *  Rules[i].input1是H的区域号
        	 *  Rules[i].input2是S的区域号
        	 *  Rules[i].input3是V的区域号
        	 *  For example: 比如满足规则{6, 1, 1, 9}时；
        	 *  将意味着HueActivation[6] SaturationActivation[1] ValueActivation[1]都大于0的情况
        	 *  Input1[6] = HueActivation[6]将意味着H值在区间(195, 205, 265, 280)里
        	 *  也有可能会落在两个区间里面，即Input1[5] Input1[6]都有值
        	 *  ResultTable[ ] 统计了相应颜色的出现的次数，一共10种颜色，被标号为0-9
        	 */
            if ((Input1[Rules[i].Input1] > 0) && (Input2[Rules[i].Input2] > 0) && (Input3[Rules[i].Input3] > 0)) {
            	
            	// 取出{5,1,1,8}最小bin值
            	// 取出{6,1,1,9}最小bin值
            	// 取大的min，记录其对应output，ResultTable[output]++
                double Min = 0;
                Min = Math.min(Input1[Rules[i].Input1], Math.min(Input2[Rules[i].Input2], Input3[Rules[i].Input3]));

                if (Min > LOM_MAXofMIN) {
                    LOM_MAXofMIN = Min;
                    RuleActivation = Rules[i].Output;
                }

            }

        }


        ResultTable[RuleActivation]++;


    }


    private void MultiParticipate_Equal_Defazzificator(FuzzyRules[] Rules, double[] Input1, double[] Input2, double[] Input3, double[] ResultTable) {

        int RuleActivation = -1;

        for (int i = 0; i < Rules.length; i++) {
            if ((Input1[Rules[i].Input1] > 0) && (Input2[Rules[i].Input2] > 0) && (Input3[Rules[i].Input3] > 0)) {
                RuleActivation = Rules[i].Output;
                ResultTable[RuleActivation]++;

            }

        }
    }

    private void MultiParticipate_Defazzificator(FuzzyRules[] Rules, double[] Input1, double[] Input2, double[] Input3, double[] ResultTable) {

        int RuleActivation = -1;
        for (int i = 0; i < Rules.length; i++) {
            if ((Input1[Rules[i].Input1] > 0) && (Input2[Rules[i].Input2] > 0) && (Input3[Rules[i].Input3] > 0)) {
                RuleActivation = Rules[i].Output;
                double Min = 0;
                Min = Math.min(Input1[Rules[i].Input1], Math.min(Input2[Rules[i].Input2], Input3[Rules[i].Input3]));
                
//             System.out.println(Min);
                
                ResultTable[RuleActivation] += Min;

            }

        }
    }
    
    /**
     * 使用10-bins模糊过滤器过滤以后的结果
     * @param Hue
     * 						一个像素点的H值
     * @param Saturation
     * 						一个像素点的S值
     * @param Value
     * 						一个像素点的V值
     * @param Method
     * 						使用计算出double[10]的过滤方式
     * @return double[10]
     * 						过滤结果 10维
     */
    public double[] ApplyFilter(double Hue, double Saturation, double Value, int Method) {
        // Method   0 = LOM
        //          1 = Multi Equal Participate
        //          2 = Multi Participate
    	
    	// 清零，不再保持从前的结果
        if (KeepPreviuesValues == false) {
            for (int i = 0; i < 10; i++) {
                Fuzzy10BinHisto[i] = 0;
            }

        }
        
        // CLF过滤器，过滤后得到该H值在H空间中的分布情况，8-bins，double[8]
        // 结果放在最后一个参数中
        FindMembershipValueForTriangles(Hue, HueMembershipValues, HueActivation);
        FindMembershipValueForTriangles(Saturation, SaturationMembershipValues, SaturationActivation);
        FindMembershipValueForTriangles(Value, ValueMembershipValues, ValueActivation);

        // 将过滤以后得到的向量使用20-TSK-like规则得到10-bins直方图结果
        // 结果放在最后一个参数中
        if (Method == 0)
            LOM_Defazzificator(Fuzzy10BinRules, HueActivation, SaturationActivation, ValueActivation, Fuzzy10BinHisto);
        if (Method == 1)
            MultiParticipate_Equal_Defazzificator(Fuzzy10BinRules, HueActivation, SaturationActivation, ValueActivation, Fuzzy10BinHisto);
        if (Method == 2)
            MultiParticipate_Defazzificator(Fuzzy10BinRules, HueActivation, SaturationActivation, ValueActivation, Fuzzy10BinHisto);

        return (Fuzzy10BinHisto);

    }
}
