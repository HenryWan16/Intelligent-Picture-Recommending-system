package team.ui.captureImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import team.background.network.WebViewActivity;
import team.service.CEDD.ExtractCEDDService;
import team.service.CEDD.ExtractCEDDService.CEDD_Binder;
import wh.self.searchpic.MainActivity;
import wh.self.searchpic.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.EditText;
import android.widget.ImageView;

public class CaptureImgActivity extends Activity{
	SurfaceView sView;
	SurfaceHolder surfaceHolder;
	int screenWidth, screenHeight;
	// ����ϵͳ���õ������
	Camera camera;
	// �Ƿ���Ԥ����
	boolean isPreview = false;
	
	FileUtil fileUtil = new FileUtil();
	Bitmap bm = null;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// ��ȡ��������Intent��Ϣ���ڵ���
		Intent intent = getIntent();
		String value = intent.getStringExtra("test_intent_capture");
		System.out.println(value);
		// ����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.capture_img_activity);
		// ��ȡ���ڹ�����
		WindowManager wm = getWindowManager();
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		// ��ȡ��Ļ�Ŀ��͸�
		display.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// ��ȡ������SurfaceView���
		sView = (SurfaceView) findViewById(R.id.sView);
		// ���ø�Surface����Ҫ�Լ�ά��������
		sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// ���SurfaceView��SurfaceHolder
		surfaceHolder = sView.getHolder();
		// ΪsurfaceHolder����һ���ص�������
		surfaceHolder.addCallback(new Callback()
		{
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
				int width, int height)
			{
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder)
			{
				// ������ͷ
				initCamera();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				// ���camera��Ϊnull ,�ͷ�����ͷ
				if (camera != null)
				{
					if (isPreview) camera.stopPreview();
					camera.release();
					camera = null;
				}
			}
		});
	}

	private void initCamera()
	{
		if (!isPreview)
		{
			// �˴�Ĭ�ϴ򿪺�������ͷ��
			// ͨ������������Դ�ǰ������ͷ
			camera = Camera.open(0);  
			camera.setDisplayOrientation(90);
		}
		if (camera != null && !isPreview)
		{
			try
			{
				Camera.Parameters parameters = camera.getParameters();
				// ����Ԥ����Ƭ�Ĵ�С
				parameters.setPreviewSize(screenWidth, screenHeight);
				// ����Ԥ����Ƭʱÿ����ʾ����֡����Сֵ�����ֵ
				parameters.setPreviewFpsRange(4, 10);
				// ����ͼƬ��ʽ
				parameters.setPictureFormat(ImageFormat.JPEG);
				// ����JPG��Ƭ������
				parameters.set("jpeg-quality", 85);
				// ������Ƭ�Ĵ�С
				parameters.setPictureSize(screenWidth, screenHeight);
				// ͨ��SurfaceView��ʾȡ������
				camera.setPreviewDisplay(surfaceHolder); 
				// ��ʼԤ��
				camera.startPreview();  
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			isPreview = true;
		}
	}

	public void capture(View source)
	{
		if (camera != null)
		{
			// ��������ͷ�Զ��Խ��������
			camera.autoFocus(autoFocusCallback); 
		}
	}

	AutoFocusCallback autoFocusCallback = new AutoFocusCallback()
	{
		// ���Զ��Խ�ʱ�����÷���
		@Override
		public void onAutoFocus(boolean success, Camera camera)
		{
			if (success)
			{
				// takePicture()������Ҫ����3������������
				// ��1�������������û����¿���ʱ�����ü�����
				// ��2�����������������ȡԭʼ��Ƭʱ�����ü�����
				// ��3�����������������ȡJPG��Ƭʱ�����ü�����
				camera.takePicture(new ShutterCallback()
				{
					public void onShutter()
					{
						// ���¿���˲���ִ�д˴�����
					}
				}, new PictureCallback()
				{
					public void onPictureTaken(byte[] data, Camera c)
					{
						// �˴�������Ծ����Ƿ���Ҫ����ԭʼ��Ƭ��Ϣ
					}
				}, myJpegCallback);  
			}
		}
	};

	PictureCallback myJpegCallback = new PictureCallback()
	{
		@Override
		public void onPictureTaken(byte[] data, Camera camera)
		{
			// ����/layout/saver.xml�ļ���Ӧ�Ĳ�����Դ
			View saveDialog = getLayoutInflater().inflate(R.layout.saver,
				null);
			final EditText photoName = (EditText) saveDialog
				.findViewById(R.id.phone_name);
			// ��ȡsaveDialog�Ի����ϵ�ImageView���
			ImageView show = (ImageView) saveDialog
				.findViewById(R.id.show);
			
			bm = fileUtil.data2Bitmap(data);
			Log.d("CaptureImgActivity", "Change to Bitmap from byte[] successfully!");
			// ��ʾ�ո��ĵõ���Ƭ
			show.setImageBitmap(bm);
			// ʹ�öԻ�����ʾsaveDialog���
			new AlertDialog.Builder(CaptureImgActivity.this).setView(saveDialog)
				.setPositiveButton("����", new OnClickListener()
				{
					// �����ͼƬ���水ť�Ժ�Ĳ���
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// ����һ��λ��SD���ϵ��ļ�
						String fileName = photoName.getText().toString() + ".jpg";
						String dstPicture = photoName.getText().toString() + "z.jpg";
						fileUtil.setFileName(fileName);
						fileUtil.save(bm);
//						bm.recycle();
						Log.d("CaptureImgActivity", "Click save button");
						
						// ������ȡCEDD����ֵ��Bitmap����������ʾ��Bitmap����ͬһ����
						// ��ת��һ���µ�Activity������ר����ʾ���Ⱥ������ҳ
						Intent intent = new Intent();
						intent = intent.setClass(CaptureImgActivity.this, WebViewActivity.class);
						intent.putExtra("file_name", fileName);
//						intent.putExtra("FileUtil_Bitmap", bitmap_cedd);
						Log.d("CaptureImgActivity", "putExtr bitmap to file successfully");
						CaptureImgActivity.this.startActivity(intent);
						
//						Intent intent2WebView = new Intent();
//						intent2WebView = intent2WebView.setClass(CaptureImgActivity.this, WebViewActivity.class);
//						intent2WebView.putExtra("test_intent_WebView", "WebView_begin");
//						CaptureImgActivity.this.startActivity(intent2WebView);
					}
				}).setNegativeButton("ȡ��", null).show();
			// �������
			camera.stopPreview();
			camera.startPreview();
			isPreview = true;
		}
	};
}