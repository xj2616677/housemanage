package com.example.admin.housemanage;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import adapter.BucketListAdapter;
import shareutil.AlbumHelper;
import shareutil.ImageBucket;
import util.ActivityManage;


/**
 * 相册文件夹的界面
 */
public class BucketActivity extends BaseActivity {
	private AlbumHelper helper;
	private  List<ImageBucket> contentList;
	private ListView list_bucket;
	private Gson gson;

	@Override
	protected void initView() {
		setTitle("相册");
		hideRightView();
		ActivityManage.getInstance().addActivity(this);
		ActivityManage.getInstance().addOneActivity(this);
		setContentLayout(R.layout.bucket_activity);
		list_bucket = (ListView) findViewById(R.id.list_bucket);


	}

	@Override
	protected void initData() {
		gson = new Gson();
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		contentList = helper.getImagesBucketList(false);
		BucketListAdapter bucketListAdapter = new BucketListAdapter(this,contentList);
		list_bucket.setAdapter(bucketListAdapter);

		list_bucket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(BucketActivity.this,AlbumActivity.class);
				intent.putExtra("position",position);
				String contentStr = gson.toJson(contentList);
				intent.putExtra("content",contentStr);
				BucketActivity.this.startActivity(intent);
			}
		});
	}




	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			BucketActivity.this.finish();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.title_lefttext:
				finish();
				break;
		}
	}
}
