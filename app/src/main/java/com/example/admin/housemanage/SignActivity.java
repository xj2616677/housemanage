package com.example.admin.housemanage;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import constants.Contant;
import shareutil.Bimp;
import util.DensityUtil;
import util.ImageUtils;
import widget.PaintView;

/**
 * Created by admin on 2016/8/23.
 */
public class SignActivity extends BaseActivity{


    //    private RadioGroup rg_sign;
//    private RadioButton rb_checkPerson;
//    private RadioButton rb_checkedObject;
    private RelativeLayout rel_checkPerson;
    private RelativeLayout rel_checkedObject;
    private Button bt_clear;
    private Button bt_sure;

    private PaintView personPaintView;
    private PaintView objectPaintView;
    private int index = 1;//检查人是1，被检查单位是2




    @Override
    protected void initView() {

        setContentLayout(R.layout.sign_activity);

        Intent intent = getIntent();
        index = intent.getIntExtra("index",1);
        if(index==1){
            setTitle("检查人签名");
        }else if(index==2){
            setTitle("受检单位签名");
        }

        hideRightView();



        rel_checkPerson = (RelativeLayout) findViewById(R.id.rel_sign_checkperson);
        rel_checkedObject = (RelativeLayout) findViewById(R.id.rel_sign_checkedobject);
//        int width = DensityUtil.getScreenWidth(this);
        personPaintView = new PaintView(this,800,500);
        objectPaintView = new PaintView(this,800,500);
        if(index==1){
            rel_checkPerson.addView(personPaintView);
            rel_checkedObject.setVisibility(View.GONE);
        }else if(index ==2){
            rel_checkedObject.addView(objectPaintView);
            rel_checkPerson.setVisibility(View.GONE);
        }
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_clear.setOnClickListener(this);
        bt_sure = (Button) findViewById(R.id.bt_sign_sure);
        bt_sure.setOnClickListener(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
//        rel_checkPerson = null;
//        personPaintView = null;
//        bt_sure = null;
//        bt_clear = null;
//        System.gc();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_clear:
                if(index == 1){
                personPaintView.clear();
                }else if(index==2){
                    objectPaintView.clear();
                }

                break;
            case R.id.title_lefttext:

                finish();

                break;
            case R.id.bt_sign_sure:
                if(index==1){
                    Contant.personBitmapList.add(ImageUtils.compressImage(personPaintView.getCachebBitmap()));
                }else if(index==2){
                    Contant.objectBitmapList.add(ImageUtils.compressImage(objectPaintView.getCachebBitmap()));
                }
//                Contant.objectBitmap = objectPaintView.getCachebBitmap();
                Bimp.isCarame = true;
                Contant.isSign = true;
                Contant.signIndex = index;
                finish();
                break;
        }

    }

}
