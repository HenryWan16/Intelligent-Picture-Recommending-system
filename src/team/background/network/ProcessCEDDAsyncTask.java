package team.background.network;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import team.service.CEDD.CEDD;
import team.service.CEDD.ExtractCEDDService;
import team.ui.captureImage.CaptureImgActivity;
import team.ui.captureImage.FileUtil;
import wh.self.searchpic.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProcessCEDDAsyncTask extends AsyncTask<Integer, Integer, String>{
	private Resources res = null;
	private String showURL = null;
	private TextView textView = null;
	private ProgressBar progressBar = null;
	private WebView webView = null;
	private Context context = null;
	private Bitmap bm =null;
	
	/**
	 * 
	 * @param res
	 * 			Ϊ�˵õ�R.id.face��Ҫ������Դ
	 * @param context
	 * 			
	 * @param progressBar
	 * 			��Ҫ���µĽ�����
	 */
	public ProcessCEDDAsyncTask(Resources res, Context context, TextView textView, 
			ProgressBar progressBar, WebView webView, Bitmap bm)
	{
		this.res = res;
		this.showURL = "";
		this.textView = textView;
		this.progressBar = progressBar;
		this.webView = webView;
		this.context = context;
		this.bm = bm;
		Log.d("ProcessCESSAsy", "constructor begin");
	}
	
	public void setShowURL(String URL)
	{
		this.showURL = URL;
	}
	
	public String getShowURL()
	{
		return this.showURL;
	}
	
	/**
	 * ��÷�����������N����ַ
	 * ��ֹ�������紫������ֵ���ַ���󣬽�������ַ
	 * @param webSites
	 * 		�ӷ�������õ�String�����ں���ҳ��ַ
	 * @param N
	 * 		��ʾN����ҳ
	 * @return
	 * 		����һ��������ҳ��ַ������
	 */
	public ArrayList<String> chooseWeb(String webSites)
	{
		String temp = "";
		ArrayList<String> str = new ArrayList<String>();
		String pattern = "http://img.alicdn.com/bao/uploaded/(\\w|/)*\\.jpg";
		Pattern p = Pattern.compile(pattern);
		Matcher m =p.matcher(webSites);
		while(m.find())
		{
			temp = m.group();
			str.add(temp);
			Log.d("ProcessCESSAsy", "The answer is as follow:");
			Log.d("ProcessCESSAsy", m.group() + " ");
		}
		
		return str;
	}
	
	// ִ��doInBackground����֮ǰ��Ԥ����
	protected void onPreExcute() {
		;
	}
	
	@Override
	protected String doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		Log.d("ProcessCESSAsy", "�µ��߳̿�ʼ����");
		String str = "";
		ArrayList<String> webSites = new ArrayList<String>();
		CEDD cedd=new CEDD();
//		Bitmap bitmap = BitmapFactory.decodeResource(this.res, R.drawable.java2);
		Bitmap bitmap = this.bm;
		cedd.extract(bitmap);
		
		// ����̨�̻߳��ڼ�������ʱ����һ�ݸ��½�����
		publishProgress(40);
		
		NetWork net = new NetWork();
		net.setCEDD_data(cedd.data_byte);
		str = net.sendMessage();
		webSites = this.chooseWeb(str);
		// Just for testing!
		Log.d("ProcessCESSAsy", str);
		
		// ����̨�̻߳��ڼ�������ʱ����һ�ݸ��½�����
		publishProgress(100);
		
		// ��ʾһ����ҳ
		// ʵ����һ��WebView����
		// ����WebView���ԣ��ܹ�ִ��JavaScript�ű�
		webView.getSettings().setJavaScriptEnabled(true);
		// ������Ҫ��ʾ����ҳ
		webView.loadUrl(webSites.get(webSites.size() - 1));
		Log.d("ProcessCESSAsy", "LoadUrl successfully");
		return str;
	}
	
	// onPostExcute������doInBackground�ķ���ֵ
	protected void onPostExcute(String result) {
		Log.d("CEDD", result);
	}
	
	// ÿ�ε���publishProgress(int)����ʱ���ᴥ���÷�������
	// �������������UI�߳���
	// ���������û������״̬
	protected void onProgressUpdate(Integer... values) 
	{
		int value = values[0];
		if (value == 20)
		{
			this.textView.setText(R.string.testProcess3);
		}
		else if (value == 40)
		{
			this.textView.setText(R.string.testProcess4);
		}
		else if (value == 100)
		{
			this.textView.setText(R.string.testProcess6);
		}
		else 
		{
			;
		}
		progressBar.setProgress(value);
	};
}
