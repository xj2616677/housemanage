package com.example.admin.housemanage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import fragment.BranchAnalyFragment;
import fragment.JJJGAllFragment;
import fragment.JJJGHeadFragment;

import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;

/**
 * Created by admin on 2016/11/4.
 */
public class AnalysisInfoTwoActivity extends BaseActivity {


    private LinearLayout lin_jjjg,lin_other;
    private RadioGroup rg_jjjg,rg_other;
    private RadioButton rb_jjjgAll,rb_jjjgHead,rb_jjjgBranch,rb_object,rb_branch;
    private TextView text_jjjgAll,text_jjjgHead,text_jjjgBranch,text_object,text_branch;
    public String startTime,endTime, houseManager,streetName,bType,source,analyType,branchName;
    public String inspector = "";
    public String objectName = "";
    public  ProgressDialog progressDialog;
    private JJJGAllFragment jjjgAllFragment;
    private JJJGHeadFragment jjjgHeadFragment;
    private BranchAnalyFragment branchAnalyFragment;
    private FragmentTransaction transaction;
    public String index;


    @Override
    protected void initView() {


        hideRightView();
        setContentLayout(R.layout.analyinfotwo_activity);
        ActivityManage.getInstance().addActivity(this);

        lin_jjjg = (LinearLayout) findViewById(R.id.lin_analytwo_jjjg);
        lin_other = (LinearLayout) findViewById(R.id.lin_analytwo);

        rg_jjjg = (RadioGroup) findViewById(R.id.rg_analytwo_jjjg);
        rg_other = (RadioGroup) findViewById(R.id.rg_analytwo);

        rb_jjjgAll = (RadioButton) findViewById(R.id.rb_analytwo_jjjgAll);
        rb_jjjgHead = (RadioButton) findViewById(R.id.rb_analytwo_jjjgHead);
        rb_jjjgBranch = (RadioButton) findViewById(R.id.rb_analytwo_jjjgbranch);
        rb_branch = (RadioButton) findViewById(R.id.rb_analytwo_branch);
        rb_object = (RadioButton) findViewById(R.id.rb_analytwo_object);

        text_jjjgAll  = (TextView) findViewById(R.id.text_analytwo_jjjgAll);
        text_jjjgHead = (TextView) findViewById(R.id.text_analytwo_jjjghead);
        text_jjjgBranch = (TextView) findViewById(R.id.text_analytwo_jjjgbranch);
        text_branch = (TextView) findViewById(R.id.text_analytwo_branch);
        text_object = (TextView) findViewById(R.id.text_analytwo_object);


    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        houseManager = intent.getStringExtra("houseManager");
        branchName = intent.getStringExtra("branchName");
        streetName = intent.getStringExtra("streetName");
        objectName = intent.getStringExtra("objectName");
        bType = intent.getStringExtra("bType");
        inspector = intent.getStringExtra("inspector");
        source = intent.getStringExtra("source");
        startTime =intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        index = intent.getStringExtra("index");
        setTitle("统计分析("+bType+")");


        setDefultFragment();


        if("经纪机构".equals(bType)){
            lin_jjjg.setVisibility(View.VISIBLE);
            lin_other.setVisibility(View.GONE);
            rg_jjjg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    switch (checkedId){
                        case R.id.rb_analytwo_jjjgAll:
                            text_jjjgAll.setBackgroundColor(getResources().getColor(R.color.titleback));
                            text_jjjgHead.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            text_jjjgBranch.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            if(jjjgAllFragment==null){
                                jjjgAllFragment = new JJJGAllFragment();
//                            transaction.add(R.id.frame_record,stateNoSHFragment);
                            }
                            transaction.replace(R.id.frame_analy, jjjgAllFragment);
                            break;
                        case R.id.rb_analytwo_jjjgHead:
                            text_jjjgAll.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            text_jjjgHead.setBackgroundColor(getResources().getColor(R.color.titleback));
                            text_jjjgBranch.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            if(jjjgHeadFragment==null){
                                jjjgHeadFragment = new JJJGHeadFragment();
                            }
                            transaction.replace(R.id.frame_analy, jjjgHeadFragment);
                            break;
                        case R.id.rb_analytwo_jjjgbranch:
                            text_jjjgAll.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            text_jjjgHead.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            text_jjjgBranch.setBackgroundColor(getResources().getColor(R.color.titleback));
                            if(branchAnalyFragment==null){
                                branchAnalyFragment = new BranchAnalyFragment();
                            }
                            transaction.replace(R.id.frame_analy, branchAnalyFragment);
                            break;
                    }
                    transaction.commit();
                }
            });
        }else {
            lin_jjjg.setVisibility(View.GONE);
            lin_other.setVisibility(View.VISIBLE);
            rg_other.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    switch (checkedId) {
                        case R.id.rb_analytwo_object:
                            text_object.setBackgroundColor(getResources().getColor(R.color.titleback));
                            text_branch.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            if (jjjgAllFragment == null) {
                                jjjgAllFragment = new JJJGAllFragment();
//                            transaction.add(R.id.frame_record,stateNoSHFragment);
                            }
                            transaction.replace(R.id.frame_analy, jjjgAllFragment);
                            break;
                        case R.id.rb_analytwo_branch:
                            text_object.setBackgroundColor(getResources().getColor(R.color.lightgray));
                            text_branch.setBackgroundColor(getResources().getColor(R.color.titleback));
                            if (branchAnalyFragment == null) {
                                branchAnalyFragment = new BranchAnalyFragment();
                            }
                            transaction.replace(R.id.frame_analy, branchAnalyFragment);
                            break;
                    }
                    transaction.commit();
                }
            });
        }
    }


    private void setDefultFragment(){
        FragmentManager fm = getFragmentManager();
        transaction = fm.beginTransaction();
        jjjgAllFragment = new JJJGAllFragment();
        transaction.replace(R.id.frame_analy, jjjgAllFragment);
        transaction.commit();
    }

    public void requestData(final String staticType, final Handler handler){
        if(NetUtil.isNetworkAvailable(this)) {
            if(progressDialog==null){
                progressDialog = showProgressDialog();
            }else{
                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("houseManager", houseManager);
                    params.put("BranchName", branchName);
                    params.put("streetName", streetName);
                    params.put("bType", bType);
                    params.put("inspector", inspector);
                    params.put("source", source);
                    params.put("startTime", startTime);
                    params.put("endTime", endTime);
                    params.put("staticType", staticType);
                    params.put("index",index);
                    String result = "";
                    if("检查部门".equals(staticType)){
                        result = RequestUtil.post(RequestUtil.GetInspectStatcicDataByBranch, params);
                    }else if("检查对象".equals(staticType)){
                        result = RequestUtil.post(RequestUtil.GetInspectStatcicData,params);
                    }else if("经纪机构总支".equals(staticType)){
                        result = RequestUtil.post(RequestUtil.GetInspectStatcicDataByJJJGHead,params);
                    }else{
                        result = RequestUtil.post(RequestUtil.GetInspectStatcicDataByType,params);
                    }
                    Message msg = Message.obtain();
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            }).start();
        }else{
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            ShowToast(noNetText);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_lefttext:
                this.finish();
                break;
        }

    }
}
