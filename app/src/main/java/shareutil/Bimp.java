package shareutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.admin.housemanage.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.ImageItem;

public class Bimp {
	public static int max = 0;
	public  static int bimposition = 0;
	public static  List<Map<String, Object>> recordFiles = null;
	public static boolean  isCarame  = false;
	public static  List<Bitmap> bitmaps = new ArrayList<>();

	private static Bitmap defultBitmap ;


	public static Map<String,ArrayList<ImageItem>> selectBitmaps = new HashMap<>();
	public static ArrayList<ImageItem> tempSelectBitmap0 = new ArrayList<ImageItem>();   //选择的图片的临时列表


	public static Bitmap getDefultBitmap(Context context){
		if(defultBitmap==null){
			defultBitmap = BitmapFactory.decodeResource(
					context.getResources(), R.mipmap.plugin_camera_no_pictures);
		}
		return defultBitmap;
	}
	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	private static int getNum(int length,int goalNum){
		int num = 1;
		while(true){
			if(length>goalNum){
				length = length/num;
				num+=1;
			}else{
				break;
			}
		}
		return num;
	}

	/*
	path为图片路径

	 */
	public static Bitmap getSelfImage(String path){
		Bitmap bitmap = null;
		try {
			File file = new File(path);
			FileInputStream fileInputStream = new FileInputStream(file);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inSampleSize=2;//压缩为原来的一般
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			bitmap = BitmapFactory.decodeStream(fileInputStream,null,opt);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static Bitmap getSelfImage(String path,int length){
		Bitmap bitmap = null;
		try {
			File file = new File(path);
			FileInputStream fileInputStream = new FileInputStream(file);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inSampleSize=2;//压缩为原来的一般
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			bitmap = BitmapFactory.decodeStream(fileInputStream,null,opt);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

}
