package bean;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;

import shareutil.Bimp;


public class ImageItem implements Serializable {
	private  String imageId;
	private String thumbnailPath;
	private String imagePath;
	private String Type;

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	//	public Bitmap bitmap;
	private boolean isSelected = false;
//	public Bitmap bitmapSelf;

	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
//	public Bitmap getBitmap(Context context) {
//		if(bitmap == null){
//			try {
//				bitmap = Bimp.getSelfImage(imagePath,10240);
//				Bimp.bitmaps.add(bitmap);
//			}catch(OutOfMemoryError e){
//				bitmap = Bimp.getDefultBitmap(context);
//			}
//		}
//		return bitmap;
//	}
//
//
//	public Bitmap getBitmapSelf(Context context) {
//		if(bitmapSelf==null){
//			try {
//				bitmapSelf = Bimp.getSelfImage(imagePath,1048576);
//				Bimp.bitmaps.add(bitmapSelf);
//			}catch(OutOfMemoryError e){
//				bitmapSelf = Bimp.getDefultBitmap(context);
//			}
//		}
//
//		return bitmapSelf;
//	}
//
//	public void setBitmapSelf(Bitmap bitmapSelf) {
//		this.bitmapSelf = bitmapSelf;
//	}
//
//	public void setBitmap(Bitmap bitmap) {
//		this.bitmap = bitmap;
//	}



}
