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

// �����service���½���һ���߳�����ר����ȡCEDD����ֵ���������硢���½���������ʾ���ص���ҳ
public class ExtractCEDDService extends Service{
	private static Context context;
	
	// ʹ��bindService��������serviceֻ������onBind������������onStartCommand����
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		IBinder binder = new CEDD_Binder();
		String fileName = intent.getStringExtra("file_name");
		FileUtil fileUtil = new FileUtil();
		fileUtil.setFileName(fileName);
		Log.d("ExtractCEDDService", "setFileName successfully");
		Bitmap bitmap = fileUtil.convertToBitmap();
		
		// ������ȡCEDD����ֵ��Bitmap����������ʾ��Bitmap����ͬһ����
		Bitmap bitmap_cedd = fileUtil.compressImg(bitmap);
		Log.d("ExtractCEDDService", "Get the bitmap_cedd successfully");
		
		// ����service����ʱ����һ���µ��߳���ִ��CEDD����ֵ����ȡ
		// ������ֱ��ͨ����Դ�ķ�ʽ������ͼƬR. ...
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
//		// ����service����ʱ����һ���µ��߳���ִ��CEDD����ֵ����ȡ
//		// ������ֱ��ͨ����Դ�ķ�ʽ������ͼƬR. ...
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
