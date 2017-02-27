package team.service.CEDD;

import team.background.network.ProcessCEDDAsyncTask;
import team.background.network.WebViewActivity;
import team.ui.captureImage.FileUtil;
import wh.self.searchpic.R;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

// 在这个service中新建了一个线程用来专门提取CEDD特征值、链接网络、更新进度条、显示返回的网页
public class ExtractCEDDService extends Service{
	private static Context context;
	
	// 使用bindService方法连接service只调用了onBind方法，而不是onStartCommand方法
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		IBinder binder = new CEDD_Binder();
		String fileName = intent.getStringExtra("file_name");
		FileUtil fileUtil = new FileUtil();
		fileUtil.setFileName(fileName);
		Log.d("ExtractCEDDService", "setFileName successfully");
		Bitmap bitmap = fileUtil.convertToBitmap();
		
		// 用于提取CEDD特征值的Bitmap，和用来显示的Bitmap不是同一个，
		Bitmap bitmap_cedd = fileUtil.compressImg(bitmap);
		Log.d("ExtractCEDDService", "Get the bitmap_cedd successfully");
		
		// 建立service链接时启动一个新的线程来执行CEDD特征值的提取
		// 这里是直接通过资源的方式传送了图片R. ...
		ProcessCEDDAsyncTask async = new ProcessCEDDAsyncTask(getResources(), context, WebViewActivity.clientText, WebViewActivity.clientProgressBar, 
				WebViewActivity.clientWebView, bitmap_cedd);
		async.execute();
		
		return binder;
	}
	
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		// TODO Auto-generated method stub
//		context = this;
//		Log.d("ExtractCEDDService", "onStartCommand");
//		// 建立service链接时启动一个新的线程来执行CEDD特征值的提取
//		// 这里是直接通过资源的方式传送了图片R. ...
//		ProcessCEDDAsyncTask async = new ProcessCEDDAsyncTask(getResources(), context, WebViewActivity.clientText, WebViewActivity.clientProgressBar, WebViewActivity.clientWebView);
//		async.execute();
//		return super.onStartCommand(intent, flags, startId);
//	}

	public class CEDD_Binder extends Binder
	{
		public String finish_message()
		{
			String str = "Service finished. CEDD has been extracted!";
			return str;
		}
		
		// this method has not been used!
		public byte[] getData()
		{
			CEDD cedd=new CEDD();
			Resources res = getResources();
			Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.face);
			cedd.extract(bitmap);
			return cedd.data_byte;
		}
	}
}
