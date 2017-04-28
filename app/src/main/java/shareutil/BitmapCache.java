package shareutil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.ActivityManage;


public class BitmapCache {

	public Handler h = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Toast.makeText(context,"内存不足",Toast.LENGTH_SHORT).show();
			ActivityManage.getInstance().exit();

		}
	};
	public final String TAG = getClass().getSimpleName();
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	private Context context;

	public BitmapCache(Context context) {
		this.context = context;
	}

	public void put(String path, Bitmap bmp) {
		if (!TextUtils.isEmpty(path) && bmp != null) {
			imageCache.put(path, new SoftReference<Bitmap>(bmp));
		}
	}

//	public void displayBmp(final ImageView iv, final String thumbPath,
//						   final String sourcePath, final ImageCallback callback) {
//		if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
//			Log.e(TAG, "no paths pass in");
//			return;
//		}
//
//		final String path;
//		final boolean isThumbPath;
//		if (!TextUtils.isEmpty(thumbPath)) {
//			path = thumbPath;
//			isThumbPath = true;
//		} else if (!TextUtils.isEmpty(sourcePath)) {
//			path = sourcePath;
//			isThumbPath = false;
//		} else {
//			// iv.setImageBitmap(null);
//			return;
//		}
//
//		if (imageCache.containsKey(path)) {
//			SoftReference<Bitmap> reference = imageCache.get(path);
//			Bitmap bmp = reference.get();
//			Bimp.bitmaps.add(bmp);
//			if (bmp != null) {
//				if (callback != null) {
//					callback.imageLoad(iv, bmp, sourcePath);
//				}
//				iv.setImageBitmap(bmp);
//				Log.d(TAG, "hit cache");
//				return;
//			}
//		}
//		iv.setImageBitmap(null);
//
//		new Thread() {
//			Bitmap thumb;
//
//			public void run() {
//
////				try {
//					if (isThumbPath) {
//						thumb = Bimp.getSelfImage(thumbPath, 5120);
//						if (thumb == null) {
//							thumb = Bimp.getSelfImage(sourcePath, 5120);
//						}
//					} else {
//						thumb = Bimp.getSelfImage(sourcePath,5120);
//					}
////				} catch (Exception e) {
////
////				}
////			catch(OutOfMemoryError e){
////					Message message = Message.obtain();
////					h.sendMessage(message);
////
////				}
//				if (thumb == null) {
////					thumb = MainActivity.bimap;
//				}
//				Log.e(TAG, "-------thumb------"+thumb);
//				Bimp.bitmaps.add(thumb);
//				put(path, thumb);
//
//				if (callback != null) {
//					h.post(new Runnable() {
//						@Override
//						public void run() {
//							callback.imageLoad(iv, thumb, sourcePath);
//						}
//					});
//				}
//			}
//		}.start();
//
//	}

	public Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 256)
					&& (options.outHeight >> i <= 256)) {
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



	public interface ImageCallback {
		public void imageLoad(ImageView imageView, Bitmap bitmap,
							  Object... params);
	}
}
