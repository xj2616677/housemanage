package com.example.admin.housemanage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import adapter.InfoListAdapter;
import bean.HouseSafeBean;
import bean.InfoAttributeBean;
import bean.InspectHistoryBean;
import bean.PropertyBean;
import constants.Contant;
import fragment.InfoListFragment;
import fragment.StateNoSHFragment;
import util.ActivityManage;

/**
 * Created by admin on 2016/11/24.
 */
public class NewCommitActivity extends BaseActivity {


    public Gson gson;
    public Intent intent;
    public String action;
    public List<String> bussinessType;
    public String isEnd;
    public InspectHistoryBean historyBean;
    public String brokerId;
    public ProgressDialog progressDialog;


    @Override
    protected void initView() {

        setTitle(getResources().getString(R.string.checkrecord));
        setContentLayout(R.layout.newcommit_activity);
        setRightText("");
        setRightBackGround(R.mipmap.main_logo);
        ActivityManage.getInstance().addActivity(this);

    }

        @Override
        protected void initData() {
            gson = new Gson();
            intent = getIntent();
            action = intent.getAction();
            bussinessType = new ArrayList<>();
            if(action.equals("check")||action.equals("objectAddress")){
                bussinessType.clear();
                bussinessType.addAll(Contant.enforceList);
                historyBean = new InspectHistoryBean();
            }else{
                bussinessType.clear();
                String history = intent.getStringExtra("history");

                isEnd = intent.getStringExtra("isEnd");
                historyBean = gson.fromJson(history, new TypeToken<InspectHistoryBean>() {
                }.getType());
                String bussiness = historyBean.getBussinesstype();
                String[] buss = bussiness.split(",");
                if(buss!=null&&buss.length!=0){
                    for(String str:buss){
                        bussinessType.add(str);
                    }
                }else{
                    bussinessType.add(bussiness);
                }
            }





            if(bussinessType.size()==1&&bussinessType.contains(getResources().getString(R.string.primebroker))){

                historyBean.setCheckBaseInfoBean(Contant.checkBaseInfoBean);
                historyBean.setName(Contant.primeObjectList.get(0).getName());
                historyBean.setAddress(Contant.primeObjectList.get(0).getAddress());
                historyBean.setId(Contant.primeObjectList.get(0).getPKID());
//            previewDXSStr= "";
//            previewFWStr= "";
            }else{
//            illegalAdapter.notifyDataSetChanged();
                historyBean.setCheckBaseInfoBean(Contant.checkBaseInfoBean);
                historyBean.setName(Contant.propertyObjectList.get(0).getName());
                historyBean.setAddress(Contant.propertyObjectList.get(0).getAddress());
                historyBean.setId(Contant.propertyObjectList.get(0).getProId());

//            PropertyBean propertyBean = Contant.propertyObjectList.get(0);
//            if(propertyBean.getGeneralBeans().size()==0){
//                previewDXSStr = "该项目下的所有地下室";
//            }else{
//                previewDXSStr = propertyBean.getGeneralBeans().size()+"处";
//                generalBeans = propertyBean.getGeneralBeans();
//            }
//
//            StringBuilder stringBuilder = new StringBuilder();
//            if(propertyBean.getHouseBeans().size()!=0){
//                houseBeans = propertyBean.getHouseBeans();
//                for(int i=0;i<propertyBean.getHouseBeans().size();i++){
//                    HouseSafeBean houseBean = propertyBean.getHouseBeans().get(i);
//                    if(i==0){
//                        stringBuilder.append(houseBean.getBUILD_SITE());
//                    }else{
//                        stringBuilder.append("、"+houseBean.getBUILD_SITE());
//                    }
//                }
//                previewFWStr = stringBuilder.toString();
//            }else{
//                previewFWStr = "该项目下的所有楼栋";
//            }
            }
            brokerId = historyBean.getId();


            setInfoFragment();

        }

        private void setInfoFragment(){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            InfoListFragment infoListFragment = new InfoListFragment();
            transaction.replace(R.id.frame_infolist, infoListFragment);
            transaction.commit();
        }
    }
