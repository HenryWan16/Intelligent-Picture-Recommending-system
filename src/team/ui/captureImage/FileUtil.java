package team.ui.captureImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

public class FileUtil implements Serializable{
	private String path = null;
	private String fileName = null;
	private static String filePath = null;
	
	public FileUtil()
	{
		this.path = Environment.getExternalStorageDirectory().toString() 
				+ "/Pictures/SearchPic";
		this.fileName = "loveYou.jpg";
		FileUtil.filePath = this.path + "/" + this.fileName;
	}
	
	// ����ͼƬ����, ������ .jpg��β
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
		FileUtil.filePath = this.path + "/" + this.fileName;
	}
	
	// ��ñ������Ƭ�����ƣ����������·��
	public String getFileName()
	{
		return this.fileName;
	}
	
	// ��ñ������Ƭ�ľ���·��
	public String getPhotoPath()
	{
		return FileUtil.filePath;
	}
	
	/**
	 * ���OOM���⣬�������ľ���·���µ�ͼƬ�������ŵ���������Bitmap
	 * ����ʵ��˼·ʧ����
	 * This method has not been used.
	 * @param w
	 * 		width���ű���
	 * @param h
	 * 		height���ű���
	 * @return
	 * 		�������ɵ�Bitmap
	 */
//	public Bitmap convertToBitmap(int w, int h)
//	{
//		String path = this.path + this.fileName;
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//		// ����Ϊtureֻ��ȡͼƬ��С
//		opts.inJustDecodeBounds = true;
//		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//		// ����Ϊ��
//		BitmapFactory.decodeFile(path, opts);
//		int width = opts.outWidth;
//		int height = opts.outHeight;
//		float scaleWidth = 0.f, scaleHeight = 0.f;
//		
//		Log.d("FileUtil", "Beginning to convert to bitmap!");
//		
//		if (width > w || height > h) 
//		{
//			// ����
//			scaleWidth = ((float) width) / w;
//			scaleHeight = ((float) height) / h;
//		}
//		opts.inJustDecodeBounds = false;
//		float scale = Math.max(scaleWidth, scaleHeight);
//		opts.inSampleSize = (int)scale;
//		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
//		Log.d("FileUtil", "Beginning to create bitmap!");
//		Bitmap bm = Bitmap.createScaledBitmap(weak.get(), w, h, true); 
//		if (bm == null)
//		{
//			Log.d("FileUtil", "Failed to create bitmap!");
//		}
//		return bm;
//	}
	
	// ���ݴ������������Bitmap��
	public Bitmap data2Bitmap(byte[] data)
	{
		Bitmap bm = null;
		bm = BitmapFactory.decodeByteArray(data, 0, data.length);
		Log.d("FileUtil", "Using data to set Bitmap successfully!");
		return bm;
	}
	
	// ��ָ����Bitmap���浽ָ����·��
	public void save(Bitmap bm)
	{
		File file = new File( path, fileName);
		FileOutputStream outStream = null;
		
		// broadCasting the position of the jpg file
		String position = this.filePath;
		Log.d("CaptureImgActivity", "The photo is " + position);
		
		try
		{
			// ��ָ���ļ���Ӧ�������
			outStream = new FileOutputStream(file);
			// ��λͼ�����ָ���ļ���
			bm.compress(CompressFormat.JPEG, 100,
				outStream);
			outStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// ���õ���byte[] data���浽ָ����·��
	public void save(byte[] data)
	{
		File file = new File( path, fileName);
		FileOutputStream outStream = null;
		
		// broadCasting the position of the jpg file
		String position = FileUtil.filePath;
		Log.d("CaptureImgActivity", "The photo is " + fileName);
		
		try
		{
			// ��ָ���ļ���Ӧ�������
			outStream = new FileOutputStream(file);
			// ��byte[]�����ָ���ļ���
			outStream.write(data);
			outStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
//    public byte[] decodeBitmap() {  
//    	String path = this.path + this.fileName;
//        BitmapFactory.Options opts = new BitmapFactory.Options();  
//        opts.inJustDecodeBounds = true;// ���ó���true,��ռ���ڴ棬ֻ��ȡbitmap���  
//        BitmapFactory.decodeFile(path, opts);  
//        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);  
//        opts.inJustDecodeBounds = false;// ����һ��Ҫ�������û�false����Ϊ֮ǰ���ǽ������ó���true  
//        opts.inPurgeable = true;  
//        opts.inInputShareable = true;  
//        opts.inDither = false;  
//        opts.inPurgeable = true;  
//        opts.inTempStorage = new byte[16 * 1024];  
//        FileInputStream is = null;  
//        Bitmap bmp = null;  
//        ByteArrayOutputStream baos = null;  
//        try {  
//            is = new FileInputStream(path);  
//            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);  
//            double scale = getScaling(opts.outWidth * opts.outHeight,  
//                    1024 * 600);  
//            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,  
//                    (int) (opts.outWidth * scale),  
//                    (int) (opts.outHeight * scale), true);  
//            bmp.recycle();  
//            baos = new ByteArrayOutputStream();  
//            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
//            bmp2.recycle();  
//            return baos.toByteArray();  
//        } catch (FileNotFoundException e) {  
//            e.printStackTrace();  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//        } finally {  
//            try {  
//                is.close();  
//                baos.close();  
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            }  
//            System.gc();  
//        }  
//        return baos.toByteArray();  
//    }  
//  
//    private double getScaling(int src, int des) {  
//        /** 
//         * 48 Ŀ��ߴ��ԭ�ߴ� sqrt�������ó���߰ٷֱ� 49 
//         */  
//        double scale = Math.sqrt((double) des / (double) src);  
//        return scale;  
//    }  
//  
	
	/**
	 * ��̬�ػ�����ű�����д��options��
	 * This method has not been used in the project!
	 * @param options
	 * 			BitFactory������ѡ��
	 * @param minSideLength
	 * 			
	 * @param maxNumOfPixels
	 * 			
	 * @return
	 */
//	public static int computeSampleSize(BitmapFactory.Options options,
//			int minSideLength,int maxNumOfPixels) 
//	{
//		int initialSize = computeInitialSampleSize(options, minSideLength,
//            maxNumOfPixels);
// 
//		int roundedSize;
//		if (initialSize <= 8) 
//		{
//			roundedSize = 1;
//			while (roundedSize < initialSize) 
//			{
//				roundedSize <<= 1;
//			}
//		}
//		else 
//		{
//			roundedSize = (initialSize + 7) / 8 *8;
//		}
//		return roundedSize;
//	}
// 
//	private static int computeInitialSampleSize(BitmapFactory.Options options,
//			int minSideLength,int maxNumOfPixels) 
//	{
//		double w = options.outWidth;
//		double h = options.outHeight;
// 
//		int lowerBound = (maxNumOfPixels == -1) ? 1 : 
//			(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
//		int upperBound = (minSideLength == -1) ? 128 :
//			(int) Math.min(Math.floor(w / minSideLength),
//			Math.floor(h / minSideLength));
// 
//		if (upperBound < lowerBound) 
//		{
//			return lowerBound;
//		}
// 
//		if ((maxNumOfPixels == -1) &&
//				(minSideLength == -1)) 
//		{
//			return 1;
//		}
//		else if (minSideLength == -1) 
//		{
//			return lowerBound;
//		}
//		else 
//		{
//			return upperBound;
//		}
//	}  

	
	
	/**
	 * ����һ��12KB����ʱ�ռ�
	 * ��һ���ļ��������õ�һ��Bitmap���ɹ������ͼƬת��ΪBitmapʱ���ֵ�Out Of Memory����
	 * �������ʹ�ù̶��ı�������ͼƬ��ʹ��ͼƬ�ĳ��ȺͿ���ܱ�֤�ѿռ�Ĵ�С
	 * @return bmp
	 * 		�����Ժ�������CEDD������ȡ��Bitmap (����ѿռ��С24M�����ܷ���double[H][W])
	 */
	public Bitmap convertToBitmap()
	{
		BitmapFactory.Options bfOptions=new BitmapFactory.Options();
        bfOptions.inDither=false;                   
        bfOptions.inPurgeable=true;             
        bfOptions.inTempStorage=new byte[12 *1024];
        // bfOptions.inJustDecodeBounds = true;
        File file = new File(this.getPhotoPath());
        FileInputStream fs=null;
        try 
        {
            fs = new FileInputStream(file);
        }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        if(fs != null)
        {
            try 
            {
            	bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
            finally
            {
                if(fs!=null) 
                {
                    try 
                    {
                        fs.close();
                    }
                    catch (IOException e) 
                    {
                        e.printStackTrace();
                    }
                 }
             }
         }
        return bmp;
	}
	
	public Bitmap convertToBitmap(byte[] data)
	{
		BitmapFactory.Options bfOptions=new BitmapFactory.Options();
        bfOptions.inDither=false;                   
        bfOptions.inPurgeable=true;             
        bfOptions.inTempStorage=new byte[12 *1024];
        // bfOptions.inJustDecodeBounds = true;
        File file = new File(this.getPhotoPath());
        FileInputStream fs=null;
        try 
        {
            fs = new FileInputStream(file);
        }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        if(fs != null)
        {
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length, bfOptions);
        	Log.d("FileUtil", "Using data to set Bitmap successfully!");
            if(fs!=null) 
            {
                try 
                {
                    fs.close();
                }
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
             }
         }
        return bmp;
	}
	
	/**
	 * ʹ��һ����������������ˣ��Ӿɵ�Bitmap�����µ�Bitmap
	 * ʹ��Maxtrix�������������������ű���
	 * ����ȡCEDD֮ǰ����ʹ�ù̶��ı�������ͼƬ��ʹ��ͼƬ�ĳ��ȺͿ�ȱ仯������double[][]���
	 * @param bitmap
	 * @return
	 */
	public Bitmap compressImg(Bitmap bitmap)
	{
		Matrix matrix = new Matrix();
		//���ó��Ϳ�Ŵ���С�ı���
		matrix.postScale(0.25f,0.5f); 
		Bitmap bmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true); 
		return bmp;
	}
}
