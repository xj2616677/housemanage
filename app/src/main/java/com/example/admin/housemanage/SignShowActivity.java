package com.example.admin.housemanage;

import android.content.Intent;
import android.view.View;


import constants.Contant;
import shareutil.Bimp;
import zoom.PhotoView;

/**
 * Created by admin on 2016/8/24.
 */
public class SignShowActivity extends BaseActivity {

    private int index = 1;

    private PhotoView photoView;

    private String action =  "";
    private Intent intent;



    @Override
    protected void initView() {
        setContentLayout(R.layout.signshow_activity);
        intent = getIntent();
        index = intent.getIntExtra("index",1);
        if(index==1){
            setTitle("检查人签名");
        }else if(index==2){
            setTitle("受检单位签名");
        }

        hideRightView();

        photoView = (PhotoView) findViewById(R.id.photoview_signshow);

    }

    @Override
    protected void initData() {

        Bimp.isCarame = true;

        action = intent.getAction();

        if("commit".equals(action)){
            if(index==1){
                photoView.setImageBitmap(Contant.personBitmap);
            }else if(index==2){
                photoView.setImageBitmap(Contant.objectBitmap);
            }
        }else if("CheckPreview".equals(action)){
            if(index==1){
                photoView.setImageBitmap(Contant.personBitmap);
            }else if(index==2){
                photoView.setImageBitmap(Contant.objectBitmap);
            }
//        }else if("preview".equals(action)){
//
//            String url= intent.getStringExtra("url");
//
//            DisplayImageOptions options = new DisplayImageOptions.Builder()
//                    .cacheInMemory(false)
//                    .cacheOnDisk(false)
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .build();
//            if(!"".equals(url)){
//                ImageLoader.getInstance().displayImage(url,photoView,options);
//            }


        }

    }

    @Override
    protected void onPause() {
        super.onPause();
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
