/**
 * һ��ר�ŷ���byte[]���ݵ���
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
		// ��������ַ
		this.sendURL = "http://192.168.1.100:8080/YLWS/ServletForPOSTMethod";
		// ���������ص���ҳ��ַ
	}
	
	/**
	 * ����Ҫ���͵��������˵�CEDD_data��Byte����
	 * ʹ�õ���ǳ����
	 * @param CEDD_data
	 */
	public void setCEDD_data(byte[] CEDD_data)
	{
		this.CEDD_data = CEDD_data;
	}
	
	/**
	 * ����ָ������Ϣ��������
	 * ʹ��HttpPost��ʽ���ͼ�ֵ�Ը�ָ���ķ�����
	 * �������յ�����󷵻�һ��ָ����URL���ͻ���
	 * @return
	 * 			���ط��������صĽ��
	 */
	public String sendMessage()
	{
		InputStream inputStream = null;
		String result = "";
		String line = "";
		// ������ֵ��
//		NameValuePair nameValuePair1 = new BasicNameValuePair("info", this.info);
//		NameValuePair nameValuePair2 = new BasicNameValuePair("CEDD", CEDD_data);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("CEDD", new String(Base64.encode(this.CEDD_data))));
//		nameValuePairs.add(nameValuePair1);
//		nameValuePairs.add(nameValuePair2);
		try {
			// ���������壬HttpEntity�ȿ���������ʱ���͵�����Ҳ��������Ӧʱ���ܵ�����
//			ByteArrayEntity httpEntity = new ByteArrayEntity(this.CEDD_data);
//			httpEntity.setContentType("CEDD");
			// �����URL��ԭʼ��URL����Ϊ��Ϣ���Ѿ�д����httpEntity����
			HttpPost httpPost = new HttpPost(this.sendURL);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
//			httpPost.setEntity(httpEntity);
			
			Log.d("NetWork", "Send to " + this.sendURL);
			this.showCEDD_Byte(this.CEDD_data);
			
			// ͨ��httpClientִ������
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			// ����ɹ�
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				Log.d("NetWork", "Connected successfully");
			}
			
			// ����յ���Ӧ������
			HttpEntity receiveHttpEntity = httpResponse.getEntity();
			inputStream = receiveHttpEntity.getContent();
			
			// ���������ж������ݲ�����ʾ���յ��Ľ��
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			while ((line = bufferedReader.readLine()) != null)
			{
				// �Ѷ������������ݶ��ŵ�result�ַ�����
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
