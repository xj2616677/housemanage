package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.admin.housemanage.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import bean.ImageItem;
import shareutil.BitmapCache;
import shareutil.ImageBitmapListener;
import shareutil.Res;
import util.DensityUtil;
import util.ImageUtils;


/**
 * 这个是显示一个文件夹里面的所有图片时用的适配器
 *
 * @author xj
 * @version 2014年10月18日  下午11:49:35
 */
public class AlbumGridViewAdapter extends BaseAdapter{
	final String TAG = getClass().getSimpleName();
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private ArrayList<ImageItem> selectedDataList;
	private DisplayMetrics dm;
	private List<Bitmap> bitmaps;

//	public RecycleBitmapListener recycleBitmapListener;

//	private  List<RecycleBitmapListener> recycleBitmapListeners;

	BitmapCache cache;
	//	private ImageBitmapListener imageBitmapListener;
	public AlbumGridViewAdapter(Context c, ArrayList<ImageItem> dataList,
								ArrayList<ImageItem> selectedDataList) {
		mContext = c;
		cache = new BitmapCache(mContext);
		this.dataList = dataList;
		this.selectedDataList = selectedDataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

//		imageBitmapListener  =new ImageBitmapListener();
	}

	public int getCount() {
		return dataList.size();
	}

	public Object getItem(int position) {
		return dataList.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}


	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public ToggleButton toggleButton;
		public Button choosetoggle;
		public TextView textView;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					Res.getLayoutID("plugin_camera_select_imageview"), parent, false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(Res.getWidgetID("image_view"));
			viewHolder.toggleButton = (ToggleButton) convertView
					.findViewById(Res.getWidgetID("toggle_button"));
			viewHolder.choosetoggle = (Button) convertView
					.findViewById(Res.getWidgetID("choosedbt"));

			;
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.getScreenWidth((Activity) mContext)/4,DensityUtil.getScreenWidth((Activity) mContext)/8);
//			lp.setMargins(10, 10,0 ,0);
			viewHolder.imageView.setLayoutParams(lp);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path;
		if (dataList != null && dataList.size() > position)
			path = dataList.get(position).getImagePath();
		else
			path = "camera_default";
		if (path.contains("camera_default")) {
			viewHolder.imageView.setImageResource(Res.getDrawableID("plugin_camera_no_pictures"));
		} else {
//			ImageManager2.from(mContext).displayImage(viewHolder.imageView,
//					path, Res.getDrawableID("plugin_camera_camera_default"), 100, 100);
//			final ImageItem item = dataList.get(position);
//			viewHolder.imageView.setTag(item.getImagePath());
//			cache.displayBmp(viewHolder.imageView, item.getThumbnailPath(), item.getImagePath(),
//					callback);


//			if(recycleBitmapListeners==null){
//				recycleBitmapListeners = new ArrayList<>();
//			}

//			if(recycleBitmapListener==null){
//			recycleBitmapListener = new RecycleBitmapListener();
//			}
//			recycleBitmapListeners.add(recycleBitmapListener);

//			ImageSize imageSize = new ImageSize(DensityUtil.getScreenWidth((Activity) mContext)/4,DensityUtil.getScreenWidth((Activity) mContext)/8);


			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.mipmap.plugin_camera_no_pictures)
					.showImageOnFail(R.mipmap.plugin_camera_no_pictures)
					.cacheInMemory(false)
					.cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.EXACTLY)
					.build();
			String url = ImageDownloader.Scheme.FILE.wrap(path);

//			ImageLoader.getInstance().displayImage(url, viewHolder.imageView, options,recycleBitmapListener);
			ImageLoader.getInstance().displayImage(url, viewHolder.imageView, options);

//			ImageLoader.getInstance().loadImage(url,imageSize,options,recycleBitmapListener);

		}

		viewHolder.toggleButton.setTag(position);
		viewHolder.choosetoggle.setTag(position);
		viewHolder.toggleButton.setOnClickListener(new ToggleClickListener(viewHolder.choosetoggle));
		if (selectedDataList.contains(dataList.get(position))) {
			viewHolder.toggleButton.setChecked(true);
			viewHolder.choosetoggle.setVisibility(View.VISIBLE);
		} else {
			viewHolder.toggleButton.setChecked(false);
			viewHolder.choosetoggle.setVisibility(View.GONE);
		}
		return convertView;
	}


	public void clear(){
//		if(recycleBitmapListener!=null){
//			recycleBitmapListener.clearBitmap();
//		}
//		if(recycleBitmapListeners!=null&&recycleBitmapListeners.size()>0){
//			for(RecycleBitmapListener recycleBitmapListener:recycleBitmapListeners){
//				recycleBitmapListener = null;
//			}
//			recycleBitmapListeners.clear();
//			recycleBitmapListeners = null;
//		}

	}

	class RecycleBitmapListener implements ImageLoadingListener{

//		private ImageView imageView;

		public RecycleBitmapListener() {
			if(bitmaps==null){
				bitmaps = new ArrayList<>();
			}
//			this.imageView = imageView;
		}

		public void clearBitmap(){
			if(bitmaps.size()>0){
				for(Bitmap bitmap:bitmaps){
					if(bitmap!=null&&!bitmap.isRecycled()){
						bitmap.recycle();
					}
				}
				bitmaps = null;
			}
		}



		@Override
		public void onLoadingStarted(String s, View view) {

		}

		@Override
		public void onLoadingFailed(String s, View view, FailReason failReason) {

		}

		@Override
		public void onLoadingComplete(String s, View view, Bitmap bitmap) {

//			imageView.setImageBitmap(bitmap);
			bitmaps.add(bitmap);


		}

		@Override
		public void onLoadingCancelled(String s, View view) {


		}
	}





	private class ToggleClickListener implements OnClickListener{
		Button chooseBt;
		public ToggleClickListener(Button choosebt){
			this.chooseBt = choosebt;
		}

		@Override
		public void onClick(View view) {
			if (view instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) view;
				int position = (Integer) toggleButton.getTag();
				if (dataList != null && mOnItemClickListener != null
						&& position < dataList.size()) {
					mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(),chooseBt);
				}
			}
		}
	}


	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(ToggleButton view, int position,
								boolean isChecked, Button chooseBt);
	}

//	public void clearBitmap(){
//		imageBitmapListener.clearBitmapList();
//	}

}
