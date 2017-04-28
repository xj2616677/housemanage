package com.example.admin.housemanage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import adapter.AlbumGridViewAdapter;
import bean.ImageItem;
import shareutil.AlbumHelper;
import shareutil.Bimp;
import shareutil.ImageBucket;
import shareutil.PublicWay;
import shareutil.Res;
import util.ActivityManage;


/**
 * 这个是进入相册显示所有图片的界面
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日  下午11:47:15
 */
public class AlbumActivity extends BaseActivity {
	//显示手机里的所有图片的列表控件
	private GridView gridView;
	//当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	//gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	//完成按钮
	private Button okButton;
	// 返回按钮
	private Button back;
	// 取消按钮
	// 预览按钮
	private Button preview;
	private ArrayList<ImageItem> dataList;
	private Gson gson;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;

	@Override
	protected void initView() {
		setContentView(Res.getLayoutID("plugin_camera_album"));
		ActivityManage.getInstance().addActivity(this);
//		PublicWay.activityList.add(this);
		//注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
//		IntentFilter filter = new IntentFilter("data.broadcast.action");
//		registerReceiver(broadcastReceiver, filter);

	}

	@Override
	protected void initData() {
		gson = new Gson();
		init();
		initListener();
		//这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			//mContext.unregisterReceiver(this);
			// TODO Auto-generated method stub
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	// 预览按钮的监听
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.tempSelectBitmap0.size() > 0) {
				GalleryActivity.imageItems=Bimp.tempSelectBitmap0;
				Intent intent = new Intent(AlbumActivity.this, GalleryActivity.class);
				intent.putExtra("position", "1");
				intent.putExtra("action","");
				intent.putExtra("ID", 0);
				intent.putExtra("inspectGuid", "-1");
				startActivity(intent);
			}
		}
	}

	// 完成按钮的监听
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
			overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//			intent.setClass(mContext, InputActivity.class);
//			startActivity(intent);
			finish();
//			ActivityManage.getInstance().clearOneActivity();
		}

	}

	// 返回按钮监听
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
//			intent.setClass(AlbumActivity.this, ImageFile.class);
//			startActivity(intent);
			finish();
		}
	}




	// 初始化，给一些对象赋值
	private void init() {
//		dataList = new ArrayList<ImageItem>();
//		Intent intent = getIntent();
//		int position = intent.getIntExtra("position", 0);
//		String content = intent.getStringExtra("content");
//		List<ImageBucket> imageBuckets =gson.fromJson(content,new TypeToken<List<ImageBucket>>(){}.getType());
//		if(imageBuckets!=null){
//			dataList.addAll(imageBuckets.get(position).getImageList());
//		}



		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(true);
		dataList = new ArrayList<ImageItem>();
		for(int i = 0; i<contentList.size(); i++){
			dataList.addAll( contentList.get(i).getImageList() );
		}
//		dataList = new ArrayList<ImageItem>();
		back = (Button) findViewById(Res.getWidgetID("back"));
		back.setOnClickListener(new BackListener());
		preview = (Button) findViewById(Res.getWidgetID("preview"));
		preview.setVisibility(View.GONE);
//		preview.setOnClickListener(new PreviewListener());
		gridView = (GridView) findViewById(Res.getWidgetID("myGrid"));
		gridImageAdapter = new AlbumGridViewAdapter(this,dataList,
				Bimp.tempSelectBitmap0);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(Res.getWidgetID("myText"));
		gridView.setEmptyView(tv);
		okButton = (Button) findViewById(Res.getWidgetID("ok_button"));
		okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap0.size()
				+ "/" + PublicWay.num + ")");

		gridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(),true,true));
	}


	@Override
	protected void onResume() {
		super.onResume();
//		helper = AlbumHelper.getHelper();
//		helper.init(getApplicationContext());
//
//		contentList = helper.getImagesBucketList(false);
//		if(dataList==null){
//			dataList = new ArrayList<ImageItem>();
//		}else{
//			dataList.clear();
//		}
//		for(int i = 0; i<contentList.size(); i++){
//			Log.i("TAG","+++++++++"+contentList.get(i).getBucketName());
//			dataList.addAll( contentList.get(i).getImageList() );
//		}
//
//		if(gridImageAdapter!=null){
//			gridImageAdapter.notifyDataSetChanged();
//		}
	}

	private void initListener() {

		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
											int position, boolean isChecked,Button chooseBt) {
						if (Bimp.tempSelectBitmap0.size() >= PublicWay.num) {
							toggleButton.setChecked(false);
							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								Toast.makeText(AlbumActivity.this, Res.getString("only_choose_num"),
										Toast.LENGTH_SHORT).show();
							}
							return;
						}
						if (isChecked) {
							chooseBt.setVisibility(View.VISIBLE);
							Bimp.tempSelectBitmap0.add(dataList.get(position));
							okButton.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap0.size()
									+ "/"+ PublicWay.num+")");
						} else {
							Bimp.tempSelectBitmap0.remove(dataList.get(position));
							chooseBt.setVisibility(View.GONE);
							okButton.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap0.size() + "/"+ PublicWay.num+")");
						}
						isShowOkBt();
					}
				});

		okButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(ImageItem imageItem) {
		if (Bimp.tempSelectBitmap0.contains(imageItem)) {
			Bimp.tempSelectBitmap0.remove(imageItem);
			okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap0.size() + "/" + PublicWay.num + ")");
			return true;
		}
		return false;
	}

	public void isShowOkBt() {
		int count = 0;
		count = Bimp.tempSelectBitmap0.size();
		if (count> 0) {
			okButton.setText(Res.getString("finish") + "(" + count + "/" + PublicWay.num + ")");
//			preview.setPressed(true);
			okButton.setPressed(true);
//			preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
//			preview.setTextColor(Color.WHITE);
		} else {
			okButton.setText(Res.getString("finish") + "(" + count + "/" + PublicWay.num + ")");
//			preview.setPressed(false);
//			preview.setClickable(false);
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(Color.parseColor("#E1E0DE"));
//			preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlbumActivity.this.finish();
		}
		return false;

	}
	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().stop();
//		gridImageAdapter.clear();
//		dataList = null;
//		contentList = null;
//		helper.bucketList.clear();
//		helper.bucketList = null;
//		helper = null;
		System.gc();
	}
}
