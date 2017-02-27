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
	
	// 更改图片名称, 必须用 .jpg结尾
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
		FileUtil.filePath = this.path + "/" + this.fileName;
	}
	
	// 获得保存的照片的名称，仅仅有相对路径
	public String getFileName()
	{
		return this.fileName;
	}
	
	// 获得保存的照片的绝对路径
	public String getPhotoPath()
	{
		return FileUtil.filePath;
	}
	
	/**
	 * 解决OOM问题，将给定的绝对路径下的图片经过缩放调整后做成Bitmap
	 * 此种实现思路失败了
	 * This method has not been used.
	 * @param w
	 * 		width缩放比例
	 * @param h
	 * 		height缩放比例
	 * @return
	 * 		返回生成的Bitmap
	 */
//	public Bitmap convertToBitmap(int w, int h)
//	{
//		String path = this.path + this.fileName;
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//		// 设置为ture只获取图片大小
//		opts.inJustDecodeBounds = true;
//		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//		// 返回为空
//		BitmapFactory.decodeFile(path, opts);
//		int width = opts.outWidth;
//		int height = opts.outHeight;
//		float scaleWidth = 0.f, scaleHeight = 0.f;
//		
//		Log.d("FileUtil", "Beginning to convert to bitmap!");
//		
//		if (width > w || height > h) 
//		{
//			// 缩放
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
	
	// 根据传入的数据生成Bitmap；
	public Bitmap data2Bitmap(byte[] data)
	{
		Bitmap bm = null;
		bm = BitmapFactory.decodeByteArray(data, 0, data.length);
		Log.d("FileUtil", "Using data to set Bitmap successfully!");
		return bm;
	}
	
	// 将指定的Bitmap保存到指定的路径
	public void save(Bitmap bm)
	{
		File file = new File( path, fileName);
		FileOutputStream outStream = null;
		
		// broadCasting the position of the jpg file
		String position = this.filePath;
		Log.d("CaptureImgActivity", "The photo is " + position);
		
		try
		{
			// 打开指定文件对应的输出流
			outStream = new FileOutputStream(file);
			// 把位图输出到指定文件中
			bm.compress(CompressFormat.JPEG, 100,
				outStream);
			outStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	// 将得到的byte[] data保存到指定的路径
	public void save(byte[] data)
	{
		File file = new File( path, fileName);
		FileOutputStream outStream = null;
		
		// broadCasting the position of the jpg file
		String position = FileUtil.filePath;
		Log.d("CaptureImgActivity", "The photo is " + fileName);
		
		try
		{
			// 打开指定文件对应的输出流
			outStream = new FileOutputStream(file);
			// 把byte[]输出到指定文件中
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
//        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高  
//        BitmapFactory.decodeFile(path, opts);  
//        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);  
//        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true  
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
//         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49 
//         */  
//        double scale = Math.sqrt((double) des / (double) src);  
//        return scale;  
//    }  
//  
	
	/**
	 * 动态地获得缩放比例，写入options中
	 * This method has not been used in the project!
	 * @param options
	 * 			BitFactory的设置选项
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
	 * 开辟一个12KB的临时空间
	 * 从一个文件描述符得到一个Bitmap，成功解决由图片转换为Bitmap时出现的Out Of Memory问题
	 * 后面必须使用固定的比率缩放图片，使得图片的长度和宽度能保证堆空间的大小
	 * @return bmp
	 * 		处理以后能用于CEDD特征提取的Bitmap (满足堆空间大小24M，且能分配double[H][W])
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
	 * 使用一个矩阵过滤器来过滤，从旧的Bitmap生成新的Bitmap
	 * 使用Maxtrix当作过滤器来控制缩放比率
	 * 在提取CEDD之前必须使用固定的比率缩放图片，使得图片的长度和宽度变化，以免double[][]溢出
	 * @param bitmap
	 * @return
	 */
	public Bitmap compressImg(Bitmap bitmap)
	{
		Matrix matrix = new Matrix();
		//设置长和宽放大缩小的比例
		matrix.postScale(0.25f,0.5f); 
		Bitmap bmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true); 
		return bmp;
	}
}
