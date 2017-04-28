package com.example.admin.housemanage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.GeneralListAdapter;
import adapter.HouseSafeListAdapter;
import adapter.PrimeListAdapter;
import adapter.PropertyListAdapter;
import bean.GeneralBean;
import bean.HouseSafeBean;
import bean.PrimeBrokerBean;
import bean.PropertyBean;
import constants.Contant;
import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;

/**
 * Created by admin on 2016/5/24.
 */
public class NewCheckObjectActivity extends BaseActivity {


    private Button bt_search;
    private EditText edit_search;
    private ListView list_object;
    private ProgressDialog progressDialog;
    private String type = "";
    private Handler mHandler;
    private List<PrimeBrokerBean> searchPrimeBean;
    private List<PropertyBean> searchPropertyBean;
    private List<GeneralBean> searchGeneralBean;
    private List<HouseSafeBean> searchHouseBean;
    private PrimeListAdapter primeListAdapter;
    private PropertyListAdapter propertyListAdapter;
    private GeneralListAdapter generalListAdapter;
    private HouseSafeListAdapter houseSafeListAdapter;
    private Gson gson;


    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.checkobject));
        setContentLayout(R.layout.newobejct_activity);
        ActivityManage.getInstance().addActivity(this);
        bt_search = (Button) findViewById(R.id.bt_object_search);
        edit_search = (EditText) findViewById(R.id.edit_object_search);
        list_object = (ListView) findViewById(R.id.list_checkobject);
        bt_search.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        TextView titleText = getCenterText();
        titleText.setFocusable(true);
        titleText.setFocusableInTouchMode(true);
        titleText.requestFocus();
        gson = new Gson();
        Intent intent = getIntent();
        type = intent.getStringExtra("type");


        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                switch (msg.what){
                    case 1:
                        String resultPrime = (String) msg.obj;
                        if("[]".equals(resultPrime)){
                            ShowToast("您搜索的对象不存在");
                        }else if("".equals(resultPrime)){
                            ShowToast("连接服务器异常");
                        }else{
                            if(searchPrimeBean==null){
                                searchPrimeBean  = new ArrayList<>();
                            }else{
                                searchPrimeBean.clear();
                            }
                            List<PrimeBrokerBean> primeBrokerBeans = gson.fromJson(resultPrime, new TypeToken<List<PrimeBrokerBean>>() {
                            }.getType());
                            searchPrimeBean.addAll(primeBrokerBeans);
                            for (int i = 0; i < searchPrimeBean.size(); i++) {
                                searchPrimeBean.get(i).setIsCheck(false);
                            }
                            primeListAdapter = new PrimeListAdapter(NewCheckObjectActivity.this,searchPrimeBean,1);
                            list_object.setAdapter(primeListAdapter);
                        }
                        break;
                    case 2:
                        String resultProperty = (String) msg.obj;
                        if("[]".equals(resultProperty)){
                            ShowToast("您搜索的对象不存在");
                        }else if("".equals(resultProperty)){
                            ShowToast("服务器连接异常");
                        }else{
                            if(searchPropertyBean==null){
                                searchPropertyBean = new ArrayList<>();
                            }else{
                                searchPropertyBean.clear();
                            }
                            List<PropertyBean> propertyBean = gson.fromJson(resultProperty, new TypeToken<List<PropertyBean>>() {
                            }.getType());
                            searchPropertyBean.addAll(propertyBean);
                            for (int i = 0; i < searchPropertyBean.size(); i++) {
                                searchPropertyBean.get(i).setIsCheck(false);
                            }
                            propertyListAdapter = new PropertyListAdapter(NewCheckObjectActivity.this, searchPropertyBean);
                            list_object.setAdapter(propertyListAdapter);
                        }
                        break;
                    case 3:
                        String resultGeneral = (String) msg.obj;
                        if("[]".equals(resultGeneral)){
                            ShowToast("您搜索的对象不存在");
                        }else if("".equals(resultGeneral)){
                            ShowToast("服务器连接异常");
                        }else{
                            if(searchGeneralBean==null){
                                searchGeneralBean = new ArrayList<>();
                            }else{
                                searchGeneralBean.clear();
                            }
                            List<GeneralBean> generalBean = gson.fromJson(resultGeneral, new TypeToken<List<GeneralBean>>() {
                            }.getType());
                            searchGeneralBean.addAll(generalBean);
                            for (int i = 0; i < searchGeneralBean.size(); i++) {
                                searchGeneralBean.get(i).setIsCheck(false);
                            }
                            generalListAdapter = new GeneralListAdapter(NewCheckObjectActivity.this, searchGeneralBean,1);
                            list_object.setAdapter(generalListAdapter);
                        }
                        break;
                    case 4:
                        String resultHouseSafe = (String) msg.obj;

                        if("[]".equals(resultHouseSafe)){
                            ShowToast("您搜索的对象不存在");
                        }else if("".equals(resultHouseSafe)){
                            ShowToast("服务器连接异常");
                        }else{
                            if(searchHouseBean==null){
                                searchHouseBean = new ArrayList<>();
                            }else{
                                searchHouseBean.clear();
                            }
                            List<HouseSafeBean> houseBean = gson.fromJson(resultHouseSafe, new TypeToken<List<HouseSafeBean>>() {
                            }.getType());
                            searchHouseBean.addAll(houseBean);
                            for (int i = 0; i < searchHouseBean.size(); i++) {
                                searchHouseBean.get(i).setIsCheck(false);
                            }
                            houseSafeListAdapter = new HouseSafeListAdapter(NewCheckObjectActivity.this, searchHouseBean,1);
                            list_object.setAdapter(houseSafeListAdapter);
                        }
                        break;
                }
            }
        };


        if (NetUtil.isNetworkAvailable(this)) {
            if (progressDialog == null) {
                progressDialog = showProgressDialog();
            } else {
                progressDialog.show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,String> mapParam = new HashMap<String, String>();
                    mapParam.put("name", Contant.userid);
                    mapParam.put("param ", "");
                    if (getResources().getString(R.string.primebroker).equals(type)) {
                        //经纪机构
                        String result = RequestUtil.post(RequestUtil.GetJJJG, mapParam);
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    } else if (getResources().getString(R.string.propertymanage).equals(type)) {
                        //物业管理
                        String result = RequestUtil.post(RequestUtil.GetWY, mapParam);
                        Message msg = Message.obtain();
                        msg.what = 2;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    } else if (getResources().getString(R.string.generalbasement).equals(type)) {
                        //普通地下室
                        String result = RequestUtil.post(RequestUtil.GetDXS, mapParam);
                        Message msg = Message.obtain();
                        msg.what = 3;
                        msg.obj = result;

                        mHandler.sendMessage(msg);
                    } else if (getResources().getString(R.string.housesafeuse).equals(type)) {
                        //房屋安全
                        String result = RequestUtil.post(RequestUtil.GetFWList, mapParam);
                        Message msg = Message.obtain();
                        msg.what = 4;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        } else {
            ShowToast(noNetText);
        }
    }

    private void hideSoft(){
        View view = getWindow().peekDecorView();
        if(view!=null){
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_lefttext:
                finish();
                break;
            case R.id.title_righttext:


                Intent intentHeader = new Intent(this, TableHeaderActivity.class);
                this.startActivity(intentHeader);

                break;
            case R.id.bt_object_search:


                final String prarm = edit_search.getText().toString();
//                Log.i("TAG", "PARAM" + prarm);
//                Log.i("TAG", "CLICK" + clickedItem);
                if(prarm.equals("")){
                    ShowToast("请输入要搜索的机构名称或地址");
                }else {
                    hideSoft();

                    if (NetUtil.isNetworkAvailable(this)) {
                        if (progressDialog == null) {
                            progressDialog = showProgressDialog();
                        } else {
                            progressDialog.show();
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String,String> mapParam = new HashMap<String, String>();
                                mapParam.put("name", Contant.userid);
                                mapParam.put("param ", prarm);
                                if (getResources().getString(R.string.primebroker).equals(type)) {
                                    //经纪机构
                                    String result = RequestUtil.post(RequestUtil.GetJJJG, mapParam);
                                    Message msg = Message.obtain();
                                    msg.what = 1;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                } else if (getResources().getString(R.string.propertymanage).equals(type)) {
                                    //物业管理
                                    String result = RequestUtil.post(RequestUtil.GetWY, mapParam);
                                    Message msg = Message.obtain();
                                    msg.what = 2;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                } else if (getResources().getString(R.string.generalbasement).equals(type)) {
                                    //普通地下室
                                    String result = RequestUtil.post(RequestUtil.GetDXS, mapParam);
                                    Message msg = Message.obtain();
                                    msg.what = 3;
                                    msg.obj = result;

                                    mHandler.sendMessage(msg);
                                } else if (getResources().getString(R.string.housesafeuse).equals(type)) {
                                    //房屋安全
                                    String result = RequestUtil.post(RequestUtil.GetFWList, mapParam);
                                    Message msg = Message.obtain();
                                    msg.what = 4;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }).start();
                    } else {
                        ShowToast(noNetText);
                    }
                }
                break;
        }
    }
}
