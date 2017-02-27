/**
 * 一个专门发送byte[]数据的类
 */
package team.background.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;
import android.webkit.WebView;

public class NetWork {
	private String info = null;
	private String sendURL = null;
	private byte[] CEDD_data = null;
	
	public NetWork()
	{
		this.info = "Hello information!";
		// 服务器地址
		this.sendURL = "http://192.168.1.100:8080/YLWS/ServletForPOSTMethod";
		// 服务器返回的网页地址
	}
	
	/**
	 * 设置要发送到服务器端的CEDD_data的Byte数组
	 * 使用的是浅拷贝
	 * @param CEDD_data
	 */
	public void setCEDD_data(byte[] CEDD_data)
	{
		this.CEDD_data = CEDD_data;
	}
	
	/**
	 * 发送指定的信息给服务器
	 * 使用HttpPost方式发送键值对给指定的服务器
	 * 服务器收到请求后返回一个指定的URL给客户端
	 * @return
	 * 			返回服务器返回的结果
	 */
	public String sendMessage()
	{
		InputStream inputStream = null;
		String result = "";
		String line = "";
		// 制作键值对
//		NameValuePair nameValuePair1 = new BasicNameValuePair("info", this.info);
//		NameValuePair nameValuePair2 = new BasicNameValuePair("CEDD", CEDD_data);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("CEDD", new String(Base64.encode(this.CEDD_data))));
//		nameValuePairs.add(nameValuePair1);
//		nameValuePairs.add(nameValuePair2);
		try {
			// 生成请求体，HttpEntity既可以是请求时发送的内容也可以是响应时接受的内容
//			ByteArrayEntity httpEntity = new ByteArrayEntity(this.CEDD_data);
//			httpEntity.setContentType("CEDD");
			// 传入的URL是原始的URL，因为信息都已经写道了httpEntity里面
			HttpPost httpPost = new HttpPost(this.sendURL);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
//			httpPost.setEntity(httpEntity);
			
			Log.d("NetWork", "Send to " + this.sendURL);
			this.showCEDD_Byte(this.CEDD_data);
			
			// 通过httpClient执行请求
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				Log.d("NetWork", "Connected successfully");
			}
			
			// 获得收到响应的内容
			HttpEntity receiveHttpEntity = httpResponse.getEntity();
			inputStream = receiveHttpEntity.getContent();
			
			// 从输入流中读出内容并且显示出收到的结果
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			while ((line = bufferedReader.readLine()) != null)
			{
				// 把读到的所有内容都放到result字符串中
				result += line;
			}
			inputStream.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// Just test CEDD
//		for (int i = 0; i < 54; i++)
//		{
//			System.out.println((int)this.CEDD_data[i]);
//		}
		// Just for testing
		return result;
	}
	
	public void showCEDD_Byte(byte[] CEDD_byte)
	{
		for (int j = 0; j < CEDD_byte.length; j++)
        {
//        	System.out.println(j+": "+this.data_byte[j]);
        	String str = "";
        	str += j + ": "+CEDD_byte[j];
        	Log.d("NetWork_Send", str);
        }
	}
}
