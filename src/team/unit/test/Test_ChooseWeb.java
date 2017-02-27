package team.unit.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;
import android.util.Log;

public class Test_ChooseWeb {
	private String[] str = null;
	private String input = "nullhttp://img.alicdn.com/bao/uploaded/i4/TB1E7aEHpXXXXbXXVXX.jpghttp://img.alicdn.com/bao/uploaded/i4/TB1E7aEHpVVVVSubXXVXX.jpgpub";
	
	/**
	 * 防止由于网络传输而出现的网址错误，解析出网址
	 * @param webSites
	 * 		从服务器获得的String串，内含网页地址
	 * @param N
	 * 		显示N张网页
	 * @return
	 * 		返回一个含有网页地址的数组
	 */
	public String[] chooseWeb(String webSites, int N)
	{
		String[] str = new String[N];
		String pattern = "http://img.alicdn.com/bao/uploaded/*";
		Pattern p = Pattern.compile(pattern);
		Matcher m =p.matcher(webSites);
		for (int i = 0; i < N; i++)
		{
			str[i] = "";
		}
		for (int i = 0; i < N && m.find(); i++)
		{
			str[i] = m.group();
			Log.d("ProcessCESSAsy", "The answer is as follow:");
			Log.d("ProcessCESSAsy", str[i] + " ");
		}
		
		return str;
	}
	
	public static void main()
	{
		int N = 3;
 		String[] test_answer = new String[N];
		for (int i = 0; i < N; i++)
		{
			test_answer[i] = "";
		}
		Test_ChooseWeb test = new Test_ChooseWeb();
		test_answer = test.chooseWeb(test.input, N);
		
		System.out.println("The webSite is :");
		for (int i = 0; i < N; i++)
		{
			System.out.println(test_answer[i]);
		}
	}
}
