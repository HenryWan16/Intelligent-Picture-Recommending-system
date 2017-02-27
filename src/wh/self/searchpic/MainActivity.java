package wh.self.searchpic;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Set;

import team.background.network.NetWork;
import team.background.network.WebViewActivity;
import team.service.CEDD.CEDD;
import team.ui.captureImage.CaptureImgActivity;
import team.ui.captureImage.FileUtil;
import team.ui.captureImage.SettingActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Advanceable;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView test_show = null;
	private Button choose = null;
	private Button set = null;
	private Button photo = null; 
	private Button adv = null;
	
	class chooseListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			todo(R.string.choose);
		}
	}
	
	class setListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			todo(R.string.setting);
			Intent intent = new Intent();
			intent = intent.setClass(MainActivity.this, SettingActivity.class);
			intent.putExtra("test_intent_setting", "setting_begin");
			MainActivity.this.startActivity(intent);
		}
	}
	
	class photoListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			todo(R.string.photo);
			Intent intent = new Intent();
			intent = intent.setClass(MainActivity.this, CaptureImgActivity.class);
			intent.putExtra("test_intent_capture", "capture_begin");
			MainActivity.this.startActivity(intent);
		}
	}
	
	class advListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// get R.drawable resources, and test CEDD
			CEDD cedd=new CEDD();
			Resources res = getResources();
//			Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.java2);
			FileUtil fileUtil = new FileUtil();
			
			Log.d("Adv", "Beginning to convert to bitmap!");
			
			Bitmap bitmap = fileUtil.convertToBitmap();
			if (bitmap != null)
			{
				Log.d("Adv", "Get bitmap successfully and recycled!");
			}
			bitmap = fileUtil.compressImg(bitmap);
//			Bitmap bitmap = BitmapFactory.decodeByteArray(fileUtil.decodeBitmap(), 0, fileUtil.decodeBitmap().length);  
//            imageCache.put(imagePath, new SoftReference<Bitmap>(bitmap));
			
			Log.d("Adv", "Converting to bitmap successed!");
			
			cedd.extract(bitmap);
			
			Log.d("Adv", "CEDD test successed!");
			
			// test NetWork
//			String str = "";
//			NetWork net = new NetWork();
//			net.setCEDD_data(cedd.data_byte);
//			str = net.sendMessage();
			
			// Just for testing!
//			Log.d("CEDD", str);
			
			// test WebView
//			Intent intent2WebView = new Intent();
//			intent2WebView = intent2WebView.setClass(MainActivity.this, WebViewActivity.class);
//			intent2WebView.putExtra("test_intent_WebView", str);
//			MainActivity.this.startActivity(intent2WebView);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Debug
		Intent intent = getIntent();
		String value = intent.getStringExtra("start_logo");
		System.out.println(value);
		
		test_show = (TextView)findViewById(R.id.test_show);
		choose = (Button)findViewById(R.id.choose);
		set = (Button)findViewById(R.id.set);
		photo = (Button)findViewById(R.id.photo);
		adv = (Button)findViewById(R.id.adv);
		choose.setOnClickListener(new chooseListener());
		set.setOnClickListener(new setListener());
		photo.setOnClickListener(new photoListener());
		adv.setOnClickListener(new advListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean todo(int text)
	{
		test_show.setText(text);
		return true;
	}
}
