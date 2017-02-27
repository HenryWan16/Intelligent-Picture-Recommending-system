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
	 * 			为了得到R.id.face需要传入资源
	 * @param context
	 * 			
	 * @param progressBar
	 * 			需要更新的进度条
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
	 * 获得服务器传来的N个网址
	 * 防止由于网络传输而出现的网址错误，解析出网址
	 * @param webSites
	 * 		从服务器获得的String串，内含网页地址
	 * @param N
	 * 		显示N张网页
	 * @return
	 * 		返回一个含有网页地址的数组
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
	
	// 执行doInBackground方法之前的预处理
	protected void onPreExcute() {
		;
	}
	
	@Override
	protected String doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		Log.d("ProcessCESSAsy", "新的线程开始运行");
		String str = "";
		ArrayList<String> webSites = new ArrayList<String>();
		CEDD cedd=new CEDD();
//		Bitmap bitmap = BitmapFactory.decodeResource(this.res, R.drawable.java2);
		Bitmap bitmap = this.bm;
		cedd.extract(bitmap);
		
		// 当后台线程还在继续运行时发布一份更新进度条
		publishProgress(40);
		
		NetWork net = new NetWork();
		net.setCEDD_data(cedd.data_byte);
		str = net.sendMessage();
		webSites = this.chooseWeb(str);
		// Just for testing!
		Log.d("ProcessCESSAsy", str);
		
		// 当后台线程还在继续运行时发布一份更新进度条
		publishProgress(100);
		
		// 显示一个网页
		// 实例化一个WebView对象
		// 设置WebView属性，能够执行JavaScript脚本
		webView.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webView.loadUrl(webSites.get(webSites.size() - 1));
		Log.d("ProcessCESSAsy", "LoadUrl successfully");
		return str;
	}
	
	// onPostExcute参数是doInBackground的返回值
	protected void onPostExcute(String result) {
		Log.d("CEDD", result);
	}
	
	// 每次调用publishProgress(int)方法时都会触发该方法调用
	// 这个方法运行在UI线程中
	// 负责反馈给用户任务的状态
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
