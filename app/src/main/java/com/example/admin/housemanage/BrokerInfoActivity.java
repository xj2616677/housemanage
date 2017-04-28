package com.example.admin.housemanage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.InfoAttributeBean;
import util.ActivityManage;
import util.DensityUtil;
import util.NetUtil;
import util.RequestUtil;

/**
 * Created by admin on 2016/4/18.
 */
public class BrokerInfoActivity extends BaseActivity {

    private LinearLayout lin_total;
    private String type;
    private String result = "";
    private String brokerId = "";
//    private List<EditText> views;
    private Handler mHandler;
    private List<InfoAttributeBean> attributeBeans;
    private ProgressDialog progressDialog;
    private String action = "";
    private String name = "";

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.info));
        setContentLayout(R.layout.brokerinfo_activity);
//        setRightText(getResources().getString(R.string.save));
        hideRightView();
        ActivityManage.getInstance().addActivity(this);

        lin_total = (LinearLayout) findViewById(R.id.lin_brokerinfo_total);



//        views = new ArrayList<>();

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                if(result!=null&&!result.equals("")){
                    String[] keys = new String[]{};
                    String[] names = new String[]{};
                    switch (msg.what){
                        case 1:
                            if(action.equals("map")) {
                                try {
                                    brokerId = new JSONArray(result).getJSONObject(0).getString("PKID");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            keys = getResources().getStringArray(R.array.primeinfokey);
                            names = getResources().getStringArray(R.array.primeinfoname);
                            attributeBeans = getInfoList(result, keys, names);
                            processView();
                            break;
                        case 2:
                            if(action.equals("map")) {
                                try {
                                    brokerId = new JSONArray(result).getJSONObject(0).getString("RPOWYID");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            keys = getResources().getStringArray(R.array.propertyinfokey);
                            names = getResources().getStringArray(R.array.propertyinfoname);
                            attributeBeans = getInfoList(result, keys, names);
                            processView();
                            break;
                        case 3:
                            if(action.equals("map")) {
                                try {
                                    brokerId = new JSONArray(result).getJSONObject(0).getString("base_id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            keys = getResources().getStringArray(R.array.generalinfokey);
                            names = getResources().getStringArray(R.array.generalinfoname);
                            attributeBeans = getInfoList(result, keys, names);
                            processView();
                            break;
                        case 4:
                            if(action.equals("map")) {
                                try {
                                    brokerId = new JSONArray(result).getJSONObject(0).getString("house_No");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            keys = getResources().getStringArray(R.array.houseinfokey);
                            names = getResources().getStringArray(R.array.houseinfoname);
                            attributeBeans = getInfoList(result, keys, names);
                            processView();
                            break;
                        case 5:
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String isSuccess = jsonObject.getString("result");
                                if(isSuccess.equals("1")){
                                    Toast.makeText(BrokerInfoActivity.this,"保存成功",Toast.LENGTH_LONG).show();
                                }else if(isSuccess.equals("0")){
                                    String failReason = jsonObject.getString("failReason");
                                    Toast.makeText(BrokerInfoActivity.this,failReason,Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }

                }else{
                    Toast.makeText(BrokerInfoActivity.this,"服务器异常!",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        };

    }

    private void processView(){

        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        if(attributeBeans!=null&&attributeBeans.size()!=0){
            for(int i=0;i<attributeBeans.size();i++){
                InfoAttributeBean attributeBean = attributeBeans.get(i);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth/3-DensityUtil.dip2px(this, 20),LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 20, 0, 0);
                TextView textView = new TextView(this);
                String name = attributeBean.getName();
                if(name.contains("面积")){
                    name = name+"(平方米)";
                }
                textView.setText(name);
                textView.setTextSize(20.0f);
                textView.setPadding(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
                linearLayout.addView(textView, layoutParams);

                LinearLayout.LayoutParams editlayoutParams = new LinearLayout.LayoutParams((screenWidth*2)/3-DensityUtil.dip2px(this, 20), LinearLayout.LayoutParams.WRAP_CONTENT);
                editlayoutParams.setMargins(20, 20, 20, 0);
                TextView textValue = new TextView(this);

                if(attributeBean.getName().contains("电话")||attributeBean.getName().contains("手机")||attributeBean.getName().contains("面积")){
                    textValue.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                }

                textValue.setText(attributeBean.getValue());
                textValue.setTextSize(18);
//                views.add(editText);
                textValue.setBackground(getResources().getDrawable(R.drawable.layout_shape));
                textValue.setPadding(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
                linearLayout.addView(textValue, editlayoutParams);
                lin_total.addView(linearLayout);
            }
        }

        TextView titleText = getCenterText();
        titleText.setFocusable(true);
        titleText.setFocusableInTouchMode(true);
        titleText.requestFocus();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        action =intent.getAction();

        type = intent.getStringExtra("type");
        if(action.equals("object")){
            brokerId = intent.getStringExtra("id");
        }else if(action.equals("map")){
            name = intent.getStringExtra("name");
        }

        if(NetUtil.isNetworkAvailable(this)){

            if(progressDialog==null){
                progressDialog =  showProgressDialog();
            }else{
                progressDialog.show();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Message msg = Message.obtain();

                    if(type.equals(getResources().getString(R.string.primebroker))){

                        Map<String ,String> params = new HashMap<>();
                        if(action.equals("object")){
                            params.put("idlist",brokerId);
                            result = RequestUtil.post(RequestUtil.GetJJJGDetail,params);
                        }else if(action.equals("map")){
                            params.put("objName",name);
                            result = RequestUtil.post(RequestUtil.GetJJJGDetailByName,params);
                        }
                        msg.what = 1;
                        //经纪机构
                    }else if(type.equals(getResources().getString(R.string.propertymanage))){
                        Map<String ,String> params = new HashMap<>();
                        if(action.equals("object")){
                            params.put("ProIDList",brokerId);
                            result = RequestUtil.post(RequestUtil.GetProWYDetail,params);

                        }else if(action.equals("map")){
                            params.put("objName",name);
                            result = RequestUtil.post(RequestUtil.GetProWYDetailByName,params);
                        }
                        msg.what = 2;
                        //物业管理
                    }else if(type.equals(getResources().getString(R.string.generalbasement))){
                        Map<String ,String> params = new HashMap<>();
                        if(action.equals("object")){
                            params.put("baseIdList",brokerId);

                            result = RequestUtil.post(RequestUtil.GetBasementDetail,params);
                        }else if(action.equals("map")){
                            params.put("objName",name);
                            result = RequestUtil.post(RequestUtil.GetBasementDetailByName,params);
                        }
                        msg.what = 3;
                        //普通地下室
                    }else if(type.equals(getResources().getString(R.string.housesafeuse))){
                        Map<String ,String> params = new HashMap<>();
                        if(action.equals("object")){
                            params.put("houseNoList ",brokerId);
                            result = RequestUtil.post(RequestUtil.GetHouseDetail,params);

                        }else if(action.equals("map")){
                            params.put("objName",name);
                            result = RequestUtil.post(RequestUtil.GetHouseDetailByName,params);
                        }
                        msg.what = 4;
                        //房屋安全
                    }
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }else{
            ShowToast(noNetText);
        }

    }

//    private List<InfoAttributeBean> getInfoList(String result,String[] keys,String[] names){
//        List<InfoAttributeBean> attributeBeans = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(result);
//            JSONObject jsonObject = jsonArray.getJSONObject(0);
//            for(int i=0;i<keys.length;i++){
//                InfoAttributeBean attribute = new InfoAttributeBean();
//                String key = keys[i];
//                String name = names[i];
//                String value = jsonObject.optString(key,"");
//                attribute.setKey(key);
//                attribute.setName(name);
//                attribute.setValue(value);
//                attributeBeans.add(attribute);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return attributeBeans;
//    }

    private String getMarkInfo(String[] newStrs){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OBJID",brokerId);
            jsonObject.put("OBJType",type);
            jsonObject.put("OBJName","");
            JSONArray jsonArray = new JSONArray();
            for(int i=0;i<attributeBeans.size();i++){
                InfoAttributeBean attributeBean = attributeBeans.get(i);
                JSONObject object = new JSONObject();
                object.put("MarkField",attributeBean.getKey());
                object.put("MarkTile",attributeBean.getName());
                object.put("OldValue",newStrs[i]);
                object.put("MarkValue",attributeBean.getValue());
                jsonArray.put(object);
            }
            jsonObject.put("FieldList",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_lefttext:
                finish();
                break;
//            case R.id.title_righttext:
//                if(NetUtil.isNetworkAvailable(this)){
//                    if(attributeBeans!=null&&attributeBeans.size()!=0){
//                        String[] newstrs = new String[attributeBeans.size()];
//                        for(int i=0;i< views.size();i++){
//                            String aaa = views.get(i).getText().toString();
//                            newstrs[i] = aaa;
//                        }
//                        final String markInfo = getMarkInfo(newstrs);
//
//                        progressDialog.show();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Map<String,String> params = new HashMap<String, String>();
//                                params.put("loginName", Contant.userid);
//                                params.put("markInfo", markInfo);
//                                String result = RequestUtil.post(RequestUtil.MarkBasicInfo,params);
//                                Message msg = Message.obtain();
//                                msg.what = 5;
//                                msg.obj = result;
//                                mHandler.sendMessage(msg);
//
//                            }
//                        }).start();
//                    }
//                }else{
//                    ShowToast(noNetText);
//                }
//                break;
        }
    }
}
