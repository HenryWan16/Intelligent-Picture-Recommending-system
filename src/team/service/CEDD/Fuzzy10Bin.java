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

    // Hֵ(ɫ��)�ľ����Ե�ֲ�����������ɫ�ʿռ�
    protected static double[] HueMembershipValues = {
    	    0, 0, 5, 10,
            5, 10, 35, 50,
            35, 50, 70, 85,
            70, 85, 150, 165,
            150, 165, 195, 205,
            195, 205, 265, 280,
            265, 280, 315, 330,
            315, 330, 360, 360}; // Table Dimensions= Number Of Triangles X 4 (Start - Stop)
    
    // Sֵ(���Ͷ�)�ľ����Ե�ֲ��������������Ͷȿռ�
    protected static double[] SaturationMembershipValues = {
    	     0, 0, 10, 75,
            10, 75, 255, 255};
    
    // Vֵ(����)�ľ����Ե�ֲ��������������ȿռ�
    protected static double[] ValueMembershipValues = {
    	    0, 0, 10, 75,
            10, 75, 180, 220,
            180, 220, 255, 255};

    //Vector fuzzy10BinRules = new Vector();
    public static FuzzyRules[] Fuzzy10BinRules = new FuzzyRules[48];
    
    // ��ʾHSV����CLF����������֮��Ľ��
    public double[] Fuzzy10BinHisto = new double[10];
    public static double[] HueActivation = new double[8];
    public static double[] SaturationActivation = new double[2];
    public static double[] ValueActivation = new double[3];
    
    /**
     *  20-TSK-like �������ɫ�ж�����
     *  {H, S, V, ��ɫֱ��ͼ��Ϣ���(0~9)}
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

    // ���캯������ʼ��������ɫ�ж�����
    // ֱ�Ӹ�������������ݵ�һ��48ά��������
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
     * ��������HSVֵ(input)������(ɫ�ʣ����Ͷȣ�����)�ռ��ϵķֲ����
     * �ֱ��ó�һ��HSVֵ�ڶ�Ӧ�ռ��ϵ�8ά��3ά��2ά����
     * @param Input
     * 				�����HSVֵ
     * @param Triangles
     * 				�����Ե�ֲ���
     * @param MembershipFunctionToSave
     * 				�����binֵ
     * For example: H = 40
     * ��ɨ�辶���Ե�ֲ���
     * MembershipFunctionToSave[1] =  (Input - Triangles[i + 2]) / (Triangles[i + 2] - Triangles[i + 3]) + 1
     * MembershipFunctionToSave[2] =  (Input - Triangles[i]) / (Triangles[i + 1] - Triangles[i])
     * ����MembershipFunctionToSave[i]Ϊ0��
     */
    private void FindMembershipValueForTriangles(double Input, double[] Triangles, double[] MembershipFunctionToSave) {
        int Temp = 0;
        
        // ����ͨ�������Ե�ֲ�ͼ�����ÿ������
        for (int i = 0; i <= Triangles.length - 1; i += 4) {

            MembershipFunctionToSave[Temp] = 0;

            // H��ֵ���м䣬�����ʱbinֵ
            if (Input >= Triangles[i + 1] && Input <= +Triangles[i + 2]) {
                MembershipFunctionToSave[Temp] = 1;
            }

            // H��ֵ�����ģ�����������ʱbinֵ
            if (Input >= Triangles[i] && Input < Triangles[i + 1]) {
                MembershipFunctionToSave[Temp] = (Input - Triangles[i]) / (Triangles[i + 1] - Triangles[i]);
            }

            // H��ֵ���Ҳ�ģ�����������ʱ��binֵ
            if (Input > Triangles[i + 2] && Input <= Triangles[i + 3]) {
                MembershipFunctionToSave[Temp] = (Input - Triangles[i + 2]) / (Triangles[i + 2] - Triangles[i + 3]) + 1;
            }

            Temp += 1;
        }

    }

    /**
     * ʹ��LOM_Defazzificator��ʽ���20-TSK-like�Ĺ�����
     * @param Rules
     * 							20-TSK-like������һ��int[48][4]����
     * @param Input1
     * 							����CLF���������˺�ĳһ�����ص��Hֵ 8ά����Hֵ��8������ռ��еķֲ����
     * @param Input2
     * 							����CLF���������˺�ĳһ�����ص��Sֵ 2ά����Sֵ��2������ռ��еķֲ����
     * @param Input3
     * 							����CLF���������˺�ĳһ�����ص��Vֵ 3ά����Vֵ��3������ռ��еķֲ����
     * @param ResultTable
     * 							���˽�� double[10]
     */
    private void LOM_Defazzificator(FuzzyRules[] Rules, double[] Input1, double[] Input2, double[] Input3, double[] ResultTable) {
        int RuleActivation = -1;
        double LOM_MAXofMIN = 0;
        
        /**
         * ѭ��48�Σ��������������б�Ѱ��HSV���ڵ���������
         * �������ڽ��紦��{5,1,1,8} {6,1,1,9}�������������
         */
        for (int i = 0; i < Rules.length; i++) {
        	
        	/**
        	 *  Ѱ�������8-bins��2-bins��3-bins����Ϊ���ֵ
        	 *  Rules[i].input1��H�������
        	 *  Rules[i].input2��S�������
        	 *  Rules[i].input3��V�������
        	 *  For example: �����������{6, 1, 1, 9}ʱ��
        	 *  ����ζ��HueActivation[6] SaturationActivation[1] ValueActivation[1]������0�����
        	 *  Input1[6] = HueActivation[6]����ζ��Hֵ������(195, 205, 265, 280)��
        	 *  Ҳ�п��ܻ����������������棬��Input1[5] Input1[6]����ֵ
        	 *  ResultTable[ ] ͳ������Ӧ��ɫ�ĳ��ֵĴ�����һ��10����ɫ�������Ϊ0-9
        	 */
            if ((Input1[Rules[i].Input1] > 0) && (Input2[Rules[i].Input2] > 0) && (Input3[Rules[i].Input3] > 0)) {
            	
            	// ȡ��{5,1,1,8}��Сbinֵ
            	// ȡ��{6,1,1,9}��Сbinֵ
            	// ȡ���min����¼���Ӧoutput��ResultTable[output]++
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
     * ʹ��10-binsģ�������������Ժ�Ľ��
     * @param Hue
     * 						һ�����ص��Hֵ
     * @param Saturation
     * 						һ�����ص��Sֵ
     * @param Value
     * 						һ�����ص��Vֵ
     * @param Method
     * 						ʹ�ü����double[10]�Ĺ��˷�ʽ
     * @return double[10]
     * 						���˽�� 10ά
     */
    public double[] ApplyFilter(double Hue, double Saturation, double Value, int Method) {
        // Method   0 = LOM
        //          1 = Multi Equal Participate
        //          2 = Multi Participate
    	
    	// ���㣬���ٱ��ִ�ǰ�Ľ��
        if (KeepPreviuesValues == false) {
            for (int i = 0; i < 10; i++) {
                Fuzzy10BinHisto[i] = 0;
            }

        }
        
        // CLF�����������˺�õ���Hֵ��H�ռ��еķֲ������8-bins��double[8]
        // ����������һ��������
        FindMembershipValueForTriangles(Hue, HueMembershipValues, HueActivation);
        FindMembershipValueForTriangles(Saturation, SaturationMembershipValues, SaturationActivation);
        FindMembershipValueForTriangles(Value, ValueMembershipValues, ValueActivation);

        // �������Ժ�õ�������ʹ��20-TSK-like����õ�10-binsֱ��ͼ���
        // ����������һ��������
        if (Method == 0)
            LOM_Defazzificator(Fuzzy10BinRules, HueActivation, SaturationActivation, ValueActivation, Fuzzy10BinHisto);
        if (Method == 1)
            MultiParticipate_Equal_Defazzificator(Fuzzy10BinRules, HueActivation, SaturationActivation, ValueActivation, Fuzzy10BinHisto);
        if (Method == 2)
            MultiParticipate_Defazzificator(Fuzzy10BinRules, HueActivation, SaturationActivation, ValueActivation, Fuzzy10BinHisto);

        return (Fuzzy10BinHisto);

    }
}
