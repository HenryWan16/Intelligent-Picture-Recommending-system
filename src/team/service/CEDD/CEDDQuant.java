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
/*
 * ������
 */
package team.service.CEDD;

import java.util.BitSet;

import android.R.integer;

public class CEDDQuant {
    private static double[] QuantTable =

            {180.19686541079636, 23730.024499150866, 61457.152912541605, 113918.55437576842, 179122.46400035513, 260980.3325940354, 341795.93301552488, 554729.98648386425};


    double[] QuantTable2 =

            {209.25176965926232, 22490.5872862417345, 60250.8935141849988, 120705.788057580583, 181128.08709063051, 234132.081356900555, 325660.617733105708, 520702.175858657472};


    double[] QuantTable3 =

            {405.4642173212585, 4877.9763319071481, 10882.170090625908, 18167.239081219657, 27043.385568785292, 38129.413201299016, 52675.221316293857, 79555.402607004813};


    double[] QuantTable4 =

            {405.4642173212585, 4877.9763319071481, 10882.170090625908, 18167.239081219657, 27043.385568785292, 38129.413201299016, 52675.221316293857, 79555.402607004813};


    double[] QuantTable5 =

            {968.88475977695578, 10725.159033657819, 24161.205360376698, 41555.917344385321, 62895.628446402261, 93066.271379694881, 136976.13317822068, 262897.86056221306};


    double[] QuantTable6 =

            {968.88475977695578, 10725.159033657819, 24161.205360376698, 41555.917344385321, 62895.628446402261, 93066.271379694881, 136976.13317822068, 262897.86056221306};
    
    // ��BitSetת����Byte[],android��û��toByteArray��������
    // �봫����8λ��BitSet����Ȼ��������
    // This method is just for fun, and it will not be used in this project!
    public byte[] bitSet2ByteArray(BitSet bitSet) {
        byte[] bytes = new byte[bitSet.size() / 8];
        System.out.println("bitSet2ByteArray length is " + bytes.length);
        for (int i = 0; i < bitSet.size(); i++) {
            int index = i / 8;
            // �����һ��byte�����λ��
            int offset = 7 - i % 8;
            // byte��offsetλҪ��Ϊ1
            bytes[index] |= (bitSet.get(i) ? 1 : 0) << offset;
        }
        return bytes;
    }
    
    /**
     * ����double CEDD[144] ת����� answer[54] (3bit * 144)/8
     * @param CEDD
     * @return byte[54]
     */
    public byte[] doubleTobyte(double[] CEDD)
    {
    	byte[] answer = new byte[CEDD.length * 3/8];
    	String str = "";
    	// ����С����ת��������bitSet�ĵ�8λ��byte����ǰ��
    	// �������bitSet.set(7);��ʹ��toByteArray�����õ�����-128��������Ϊ10000000
    	for (int i = 0; i < CEDD.length; i++)
    	{
	    	switch((int)CEDD[i])
	    	{
	    	case 0: str += "000"; break;
	    	case 1: str += "001"; break;
	    	case 2: str += "010"; break;
	    	case 3: str += "011"; break;
	    	case 4: str += "100"; break;
	    	case 5: str += "101"; break;
	    	case 6: str += "110"; break;
	    	case 7: str += "111"; break;
	    	default: System.out.println("Double is out of range!");break;
	    	}
    	}
    	// ����ɨ�赽��str����01���������byte[54]
    	// index��ʾ��byte[index];offset��ʾӦ�����ƶ���λ���޸ĵ�ǰbyte����Ӧλ
    	for (int i = 0; i < str.length(); i++) {
    		char ch = str.charAt(i);
            int index = i / 8;
            // �����һ��byte�����λ��
            int offset = 7 - i % 8;
            // byte��offsetλҪ��Ϊ1
            answer[index] |= (ch == '1' ? 1 : 0) << offset;
        }
    	return answer;
    }
    
    // ��һ���������紫�����CEDD[6*24]
//    public double[] Apply(double[] Local_Edge_Histogram)
    public byte[] Apply(double[] Local_Edge_Histogram)
    {
    	int len = 3 * Local_Edge_Histogram.length/8;
    	byte[] answer = new byte[len];
        double[] Edge_HistogramElement = new double[Local_Edge_Histogram.length];
        double[] ElementsDistance = new double[8];
        double Max = 1;
        
        // CEDD[0~23]
        for (int i = 0; i < 24; i++)
        {
            Edge_HistogramElement[i] = 0;
            
            /**
             * �����ж�CEDD[i]��ֵ������8����������ĸ�����
             * ���������CEDD[i]��������ֹλ�õľ���
             * ȡ������С��
             * ������������в�ƫ����߻ᱻ�ж�Ϊ��һ��������ǵڶ�����
             * ������ʹ��switch-case��䲻�Ϸ�֧�ж�
             */
            for (int j = 0; j < 8; j++)
            {
                ElementsDistance[j] = Math.abs(Local_Edge_Histogram[i] - QuantTable[j] / 1000000);
            }
            Max = 1;
            for (int j = 0; j < 8; j++)
            {
            	// ��˳��ɨ�裬ȡ�������Сֵ
                if (ElementsDistance[j] < Max)
                {
                    Max = ElementsDistance[j];
                    Edge_HistogramElement[i] = j;
                }
            }
        }

        // CEDD[24~47]
        for (int i = 24; i < 48; i++)
        {
            Edge_HistogramElement[i] = 0;
            for (int j = 0; j < 8; j++)
            {
                ElementsDistance[j] = Math.abs(Local_Edge_Histogram[i] - QuantTable2[j] / 1000000);
            }
            Max = 1;
            for (int j = 0; j < 8; j++)
            {
                if (ElementsDistance[j] < Max)
                {
                    Max = ElementsDistance[j];
                    Edge_HistogramElement[i] = j;
                }
            }
        }

        // CEDD[48~95] ע�⵽QuantTable3��QuantTable4��ȫһ�������Կ��Ը�Ϊ[48~95]
        for (int i = 48; i < 72; i++)
        {
            Edge_HistogramElement[i] = 0;
            for (int j = 0; j < 8; j++)
            {
                ElementsDistance[j] = Math.abs(Local_Edge_Histogram[i] - QuantTable3[j] / 1000000);
            }
            Max = 1;
            for (int j = 0; j < 8; j++)
            {
                if (ElementsDistance[j] < Max)
                {
                    Max = ElementsDistance[j];
                    Edge_HistogramElement[i] = j;
                }
            }
        }

        // CEDD[96~143]
        for (int i = 72; i < 96; i++)
        {
            Edge_HistogramElement[i] = 0;
            for (int j = 0; j < 8; j++)
            {
                ElementsDistance[j] = Math.abs(Local_Edge_Histogram[i] - QuantTable4[j] / 1000000);
            }
            Max = 1;
            for (int j = 0; j < 8; j++)
            {
                if (ElementsDistance[j] < Max)
                {
                    Max = ElementsDistance[j];
                    Edge_HistogramElement[i] = j;
                }
            }
        }

        // CEDD[0~23]
        for (int i = 96; i < 120; i++)
        {
            Edge_HistogramElement[i] = 0;
            for (int j = 0; j < 8; j++)
            {
                ElementsDistance[j] = Math.abs(Local_Edge_Histogram[i] - QuantTable5[j] / 1000000);
            }
            Max = 1;
            for (int j = 0; j < 8; j++)
            {
                if (ElementsDistance[j] < Max)
                {
                    Max = ElementsDistance[j];
                    Edge_HistogramElement[i] = j;
                }
            }
        }

        for (int i = 120; i < 144; i++)
        {
            Edge_HistogramElement[i] = 0;
            for (int j = 0; j < 8; j++)
            {
                ElementsDistance[j] = Math.abs(Local_Edge_Histogram[i] - QuantTable6[j] / 1000000);
            }
            Max = 1;
            for (int j = 0; j < 8; j++)
            {
                if (ElementsDistance[j] < Max)
                {
                    Max = ElementsDistance[j];
                    Edge_HistogramElement[i] = j;
                }
            }
        }
        answer = doubleTobyte(Edge_HistogramElement);
        return answer;
    }
}
