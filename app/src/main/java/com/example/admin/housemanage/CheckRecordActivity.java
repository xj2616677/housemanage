package com.example.admin.housemanage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import constants.Contant;
import fragment.StateLocalFragment;
import fragment.StateNoSHFragment;
import fragment.StateSHFragment;
import shareutil.Bimp;
import util.ActivityManage;

/**
 * 检查记录页
 * Created by Administrator on 2016/5/13.
 */
public class CheckRecordActivity extends BaseActivity {
    private RadioButton button1,button2,button3;
    private RadioGroup rg_issh;
    private Gson gson;
    private ProgressDialog progressDialog;
    private StateNoSHFragment stateNoSHFragment;
    private StateSHFragment stateSHFragment;
    private StateLocalFragment stateLocalFragment;

    private TextView text_line1,text_line2,text_line3;

    private FragmentTransaction transaction;



    @Override
    protected void initView() {

        setTitle(getResources().getString(R.string.jcjl));
        hideRightView();
        setContentLayout(R.layout.checkrecord_activity);
        ActivityManage.getInstance().addActivity(this);
        button1=(RadioButton)findViewById(R.id.button1_check);
        button2=(RadioButton)findViewById(R.id.button2_check);
        button3=(RadioButton)findViewById(R.id.button3_check);

        text_line1 = (TextView) findViewById(R.id.text_recordline1);
        text_line2 = (TextView) findViewById(R.id.text_recordline2);
        text_line3 = (TextView) findViewById(R.id.text_recordline3);

        rg_issh = (RadioGroup) findViewById(R.id.rg_check_issh);
        rg_issh.check(R.id.button1_check);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        setDefultFragment();
         gson = new Gson();

        Contant.isAddObject = false;
        Contant.personBitmapList.clear();
        Contant.objectBitmapList.clear();
        Contant.isSign = false;

        rg_issh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                switch (checkedId){
                    case R.id.button1_check:
                        text_line1.setBackgroundColor(getResources().getColor(R.color.titleback));
                        text_line2.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        text_line3.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        if(stateNoSHFragment==null){
                            stateNoSHFragment = new StateNoSHFragment();
//                            transaction.add(R.id.frame_record,stateNoSHFragment);
                        }
                        transaction.replace(R.id.frame_record, stateNoSHFragment);
                        break;
                    case R.id.button2_check:
                        text_line1.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        text_line2.setBackgroundColor(getResources().getColor(R.color.titleback));
                        text_line3.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        if(stateSHFragment==null){
                            stateSHFragment = new StateSHFragment();
                        }
                        transaction.replace(R.id.frame_record, stateSHFragment);
                        break;
                    case R.id.button3_check:
                        text_line1.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        text_line2.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        text_line3.setBackgroundColor(getResources().getColor(R.color.titleback));
                        if(stateLocalFragment==null){
                            stateLocalFragment = new StateLocalFragment();
                        }
                        transaction.replace(R.id.frame_record, stateLocalFragment);
                        break;
                }
                transaction.commit();
            }
        });
    }


//    private void showFragment(FragmentTransaction transaction,Fragment fragment){
//        for(Fragment f:fragments){
//            if(f==fragment){
//                transaction.show(f);
//            }else{
//                transaction.hide(f);
//            }
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Bimp.isCarame = true;
        Bimp.selectBitmaps.clear();
        Contant.infoAttributeBeans.clear();
    }

    public void showProDialog(){
        if(progressDialog==null){
            progressDialog = showProgressDialog();
//            progressDialog.setCanceledOnTouchOutside(false);
        }else{
            progressDialog.show();
        }
    }

    public void dimissProgress(){
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }




    private void setDefultFragment(){
        FragmentManager fm = getFragmentManager();
        transaction = fm.beginTransaction();
        stateNoSHFragment = new StateNoSHFragment();
        transaction.replace(R.id.frame_record, stateNoSHFragment);
        transaction.commit();
    }




    @Override
    public void onClick(View v) {
        // 开启Fragment事务
        switch (v.getId()) {
            case R.id.title_lefttext:
                finish();
                break;
        }

    }
}
