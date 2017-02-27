package team.background.network;

import team.service.CEDD.ExtractCEDDService;
import team.service.CEDD.ExtractCEDDService.CEDD_Binder;
import wh.self.searchpic.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebViewActivity extends Activity{
	private String URL = null;
	private String showURL = null;
	public static WebView clientWebView = null;
	public static TextView clientText = null;
	public static ProgressBar clientProgressBar = null;
	
	// ʹ��setContentView��������java���Ӧxml�ļ�����ϵ
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_activity);
		clientWebView = (WebView)findViewById(R.id.clientWebView);
		clientText = (TextView)findViewById(R.id.textViewProgressBar);
		clientProgressBar = (ProgressBar)findViewById(R.id.progressBarId);
		
		// �����һ��Activity���洫������URL��ֵ
		Intent intentReceive = getIntent();
		String fileName = intentReceive.getStringExtra("file_name");
		Log.d("WebView", fileName);
		// ����һ���µ�service�����µ�service���濪��һ���̣߳��ڿ������߳�������ȡCEDD������
		Intent intent = new Intent();
		intent.putExtra("file_name", fileName);
		intent.setClass(WebViewActivity.this, ExtractCEDDService.class);
		bindService(intent, conn, BIND_AUTO_CREATE);
	}
	
	public ServiceConnection conn = new ServiceConnection() {
		
		// Activity��Service�󶨳ɹ���ʱ�򽫻����ServiceConnected����
		// SecondService���ص�Binder���󱻴�����MainActivity��
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			CEDD_Binder binder = (CEDD_Binder)service;
			Log.d("CEDD", binder.finish_message());
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	// This method will be implemented in WebViewActivity.java
	public void showURL(String URL)
	{
		Log.d("CEDD", "Received from URL " + URL);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if 	((keyCode == KeyEvent.KEYCODE_BACK) && (clientWebView.canGoBack()))
		{
			clientWebView.goBack();
			return super.onKeyDown(keyCode, event);
		}
		else 
		{
			return false;
		}
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		
		super.onLowMemory();
	}
	
//	public boolean onKeyDown(int keyCoder, KeyEvent event)
//	{
//		if 	((keyCoder == KeyEvent.KEYCODE_BACK) && (clientWebView.canGoBack()))
//		{
//			clientWebView.goBack();
//			return true;
//		}
//		return false;
//	}
	
//	ServiceConnection conn = new ServiceConnection() {
//		
//		// Activity��Service�󶨳ɹ���ʱ�򽫻����ServiceConnected����
//		// SecondService���ص�Binder���󱻴�����MainActivity��
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			// TODO Auto-generated method stub
//			CEDD_Binder binder = (CEDD_Binder)service;
//			Log.d("CEDD", binder.finish_message());
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	};
}
