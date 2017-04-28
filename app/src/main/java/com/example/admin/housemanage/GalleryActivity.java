package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.ImageItem;
import shareutil.Bimp;
import shareutil.Res;
import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;
import zoom.PhotoView;
import zoom.ViewPagerFixed;


/**
 * 这个是用于进行图片浏览时的界面
 *
 */
public class GalleryActivity extends BaseActivity {
	private Intent intent;
	// 返回按钮
	private Button back_bt;
	// 发送按钮
//	private Button send_bt;
	//删除按钮
	private Button del_bt;
	//顶部显示预览图片位置的textview
	private TextView positionTextView;
	//获取前一个activity传过来的position
	//当前的位置
	private int location = 0;

	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;
	public static ArrayList<ImageItem> imageItems;
	private String action = "";
	private ProgressDialog progressDialog;
	private Handler mHandler;
	private String checkGuid = "";
	private int selectItem;
//	private RecycleBitmapListener  recycleBitmapListener;



	@Override
	protected void initView() {
		setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
//		PublicWay.activityList.add(this);
		ActivityManage.getInstance().addActivity(this);
		Res.init(GalleryActivity.this);
		back_bt = (Button) findViewById(R.id.gallery_back);
//		send_bt = (Button) findViewById(Res.getWidgetID("send_button"));
		del_bt = (Button)findViewById(R.id.gallery_del);
		back_bt.setOnClickListener(new BackListener());
//		send_bt.setOnClickListener(new GallerySendListener());
		del_bt.setOnClickListener(new DelListener());

		intent = getIntent();
		int id = intent.getIntExtra("ID", 0);
		action = intent.getStringExtra("action");
		checkGuid = intent.getStringExtra("inspectGuid");
		selectItem = intent.getIntExtra("selectItem",-1);

		if("check".equals(action)||"objectAddress".equals(action)||"checkRecordLocal".equals(action)){
			del_bt.setVisibility(View.VISIBLE);
		}else{
			del_bt.setVisibility(View.GONE);
		}



//		isShowOkBt();
		// 为发送按钮设置文字
		pager = (ViewPagerFixed) findViewById(R.id.gallery01);
		pager.setOnPageChangeListener(pageChangeListener);

		if (listViews == null) {
			listViews = new ArrayList<View>();
		}
//		recycleBitmapListener = new RecycleBitmapListener();
		for (int i = 0; i < imageItems.size(); i++) {
			initListViews( imageItems.get(i) );
		}

		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
		pager.setCurrentItem(id);
	}


	@Override
	protected void onPause() {
		super.onPause();
		Bimp.tempSelectBitmap0.clear();
		Bimp.tempSelectBitmap0.addAll(imageItems);
		CommitActivity.selectItem = selectItem;
//		recycleBitmapListener.clearBitmap();
		listViews.clear();
		System.gc();
	}

	@Override
	protected void initData() {

		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				progressDialog.dismiss();
			}
		};


	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private void initListViews(ImageItem imageItem) {

		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(false)
				.cacheOnDisk(false)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		String url = ImageDownloader.Scheme.FILE.wrap(imageItem.getImagePath());

//		ImageLoader.getInstance().displayImage(url, img, options,recycleBitmapListener);
		ImageLoader.getInstance().displayImage(url, img, options);
//		img.setImageBitmap(bm);


	}

	class RecycleBitmapListener implements ImageLoadingListener {

//		private ImageView imageView;
		private List<Bitmap> bitmaps;

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




	// 返回按钮添加的监听器
	private class BackListener implements OnClickListener {

		public void onClick(View v) {
//			intent.setClass(GalleryActivity.this, ImageFile.class);
//			startActivity(intent);
			finish();
		}
	}

	// 删除按钮添加的监听器
	private class DelListener implements OnClickListener {

		public void onClick(View v) {

			if("check".equals(action)||"objectAddress".equals(action)){
				deleteImage();
			}else{
				String path = imageItems.get(location).getImagePath();
				if(path.contains(checkGuid)){
					String imageGuid = getImageGuid(path);
					initDeleteDialog(imageGuid);
				}else{
					deleteImage();
				}
			}
		}
	}

	private void deleteImage(){
		if(listViews!=null&&imageItems!=null){
			if (listViews.size() == 1) {
				imageItems.clear();
				Bimp.max = 0;
//				send_bt.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
//				Intent intent = new Intent("data.broadcast.action");
//                sendBroadcast(intent);
				finish();
			} else {
				imageItems.remove(location);
				Bimp.max--;
				pager.removeAllViews();
				listViews.remove(location);
				adapter.setListViews(listViews);
//				send_bt.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
				adapter.notifyDataSetChanged();
			}
		}
	}




	private void initDeleteDialog(final String imageGuid){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确定要删除该照片吗？删除后将删除服务器上该表单的该照片。");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {


				requestDeleteImg(imageGuid);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}


	private void requestDeleteImg(final String imgGuid){

		if(NetUtil.isNetworkAvailable(this)){
			if(progressDialog==null){
				progressDialog = showProgressDialog();
			}else{
				progressDialog.show();
			}

			new Thread(new Runnable() {
				@Override
				public void run() {
					Map<String,Object> params = new HashMap<String, Object>();
					params.put("guid", imgGuid);
					String result = RequestUtil.postob(RequestUtil.DeletAttachFile, params);
					Message msg = Message.obtain();
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}).start();
			deleteImage();
		}else{
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			ShowToast(noNetText);
		}

	}

	private String getImageGuid(String path){
		String[] strs = path.split("\\/");
		String name = strs[strs.length-1];
		if(!name.contains("=guid=")){
			return "";
		}else{
			String imageGuid = name.split("=guid=")[0];
			return imageGuid;
		}
	}







	/**
	 * 监听返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return true;
	}


	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			if(listViews!=null&&listViews.size()!=0&&listViews.size()>arg1){
				((ViewPagerFixed) arg0).removeView(listViews.get(arg1));
			}
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
