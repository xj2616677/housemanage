package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.GeneralListAdapter;
import adapter.InsertInfoListAdapter;
import adapter.PrimeListAdapter;
import bean.CheckHeadBean;
import bean.GeneralBean;
import bean.HouseSafeBean;
import bean.InfoAttributeBean;
import bean.PrimeBrokerBean;
import bean.PropertyBean;
import constants.Contant;
import util.NetUtil;
import util.RequestUtil;

/**
 * Created by admin on 2016/10/31.
 */
public class InsertObjectActivity extends BaseActivity {


    private ListView list_insertobject;
    private String bussinesstype = "";
    private String streetID = "";
    private List<InfoAttributeBean> attributeBeans;
    private ProgressDialog progressDialog;
    private Gson gson;
    private  Handler mHandler;
    private String objName = "";
    private Spinner spinner_type;
    private LinearLayout lin_head,lin_type;
    private TextView text_head;
    private AlertDialog objectDialog;
    private EditText edit_newobject;
    private Button bt_newobject_search;
    private ListView list_object;
    private TextView text_sure,text_cancel;
    private TextView title;
    private List<PrimeBrokerBean> headBrokes;
    private List<InfoAttributeBean> newInfoAttributeBeans;



    @Override
    protected void initView() {

        setTitle("新增信息");
        setRightText("保存");
        setContentLayout(R.layout.insertobject_activity);

        list_insertobject = (ListView) findViewById(R.id.list_insertobject);
        spinner_type = (Spinner) findViewById(R.id.spinner_insertobject_type);
        lin_head = (LinearLayout) findViewById(R.id.lin_insertobject_head);
        text_head = (TextView) findViewById(R.id.text_insertobject_headvalue);
        lin_type = (LinearLayout) findViewById(R.id.lin_insertobject_type);
        text_head.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        gson = new Gson();
        Intent intent  = getIntent();
        bussinesstype = intent.getStringExtra("bussiness");
        streetID = intent.getStringExtra("streetID");
        String[] keys = null;
        String[] names = null;

        attributeBeans = new ArrayList<>();
        newInfoAttributeBeans = new ArrayList<>();

        if("经纪机构".equals(bussinesstype)){
            keys = getResources().getStringArray(R.array.insertprimeinfokey);
            names = getResources().getStringArray(R.array.insertprimeinfoname);
            lin_head.setVisibility(View.GONE);
            InfoAttributeBean infoAttributeBean = new InfoAttributeBean();
            infoAttributeBean.setName("机构类别");
            infoAttributeBean.setKey("JGTYPE");
            infoAttributeBean.setValue("总支");
            newInfoAttributeBeans.add(infoAttributeBean);

            final String[] strs = new String[]{"总支", "分支"};
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strs);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_type.setAdapter(arrayAdapter);
            spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String type = strs[position];
                    if("分支".equals(type)){
                        lin_head.setVisibility(View.VISIBLE);
                    }
                    int index = isHaveKey("JGTYPE");
                    if(index==-1){
                        InfoAttributeBean infoAttributeBean = new InfoAttributeBean();
                        infoAttributeBean.setName("机构类别");
                        infoAttributeBean.setKey("JGTYPE");
                        infoAttributeBean.setValue(type);
                        newInfoAttributeBeans.add(infoAttributeBean);
                    }else{
                        newInfoAttributeBeans.get(index).setValue(type);
                    }
                    if(type.equals("分支")){
                        lin_head.setVisibility(View.VISIBLE);
                    }else{
                        lin_head.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            keys = getResources().getStringArray(R.array.propertyinfokey);
            names = getResources().getStringArray(R.array.propertyinfoname);

            lin_head.setVisibility(View.GONE);
            lin_type.setVisibility(View.GONE);
        }



        for(int i=0;i<keys.length;i++){
            InfoAttributeBean attribute = new InfoAttributeBean();
            String key = keys[i];
            String name = names[i];
            String value = "";
            attribute.setName(name);
            attribute.setValue(value);
            attribute.setKey(key);
            attributeBeans.add(attribute);
        }

        InsertInfoListAdapter insertInfoListAdapter = new InsertInfoListAdapter(this,attributeBeans);
        list_insertobject.setAdapter(insertInfoListAdapter);


        mHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String result = (String) msg.obj;
                        JSONObject jsonObject = null;
                        String isSuccess = "";
                        String failReason = "";
                        String succeedString = "";

                        try {
                            jsonObject = new JSONObject(result);
                            isSuccess = jsonObject.getString("result");
                            failReason = jsonObject.getString("failReason");
                            succeedString = jsonObject.getString("succeedString");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if("1".equals(isSuccess)){
                            ShowToast("添加成功");
                            Contant.isAddjjjgorpro = true;
                            String[] array = succeedString.split(";");
                            String id ="";
                            String name = "";
                            if(array!=null&&array.length==2){
                                id = array[0];
                                name = array[1];
                            }
                            if("经纪机构".equals(bussinesstype)){
                                Contant.newAddpropertyBean = null;
//                                if(Contant.newAddprimeBrokerBean==null){
                                PrimeBrokerBean primeBrokerBean = new PrimeBrokerBean();
                                primeBrokerBean.setPKID(id);
                                primeBrokerBean.setName(name);
                                primeBrokerBean.setIsCheck(false);
                                Contant.newAddprimeBrokerBean = primeBrokerBean;
//                                }
//                                else{
//                                    Contant.newAddprimeBrokerBean.setPKID(id);
//                                    Contant.newAddprimeBrokerBean.setName(name);
//                                    Contant.newAddprimeBrokerBean.setIsCheck(false);
//                                }
                            }else{
                                Contant.newAddprimeBrokerBean =null;
//                                if(Contant.newAddpropertyBean==null){
                                PropertyBean propertyBean = new PropertyBean();
                                propertyBean.setProId(id);
                                propertyBean.setName(name);
                                propertyBean.setIsCheck(false);
                                propertyBean.setGeneralBeans(new ArrayList<GeneralBean>());
                                propertyBean.setHouseBeans(new ArrayList<HouseSafeBean>());
                                Contant.newAddpropertyBean = propertyBean;
//                                }else{
//                                    Contant.newAddpropertyBean.setProId(id);
//                                    Contant.newAddpropertyBean.setName(name);
//                                    Contant.newAddpropertyBean.setIsCheck(false);
//                                }
                            }
                            InsertObjectActivity.this.finish();

                        }else{
                            ShowToast(failReason);
                        }
                        if(progressDialog!=null&&progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        break;
                    case 2:

                        String headResult = (String) msg.obj;

                        if("[]".equals(headResult)){
                            ShowToast("没有您搜索的对象，请新增经纪机构总支");
                        }else if("".equals(headResult)){
                            ShowToast("服务器连接异常");
                        }else{
                            if(headBrokes==null){
                                headBrokes = new ArrayList<>();
                            }else{
                                headBrokes.clear();
                            }
                            headBrokes.addAll((List<PrimeBrokerBean>)(gson.fromJson(headResult, new TypeToken<List<PrimeBrokerBean>>() {
                            }.getType())));

                            for(PrimeBrokerBean brokerBean:headBrokes){
                                brokerBean.setIsCheck(false);
                            }
                            PrimeListAdapter primeListAdapter =new PrimeListAdapter(InsertObjectActivity.this,headBrokes,0);

                            list_object.setAdapter(primeListAdapter);
                        }
                        if(progressDialog!=null&&progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        break;
                }

            }
        };


    }




    public void initCheckObjectDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        objectDialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.newchooseobject_dialog,null);
        objectDialog.setView(view);
        objectDialog.show();

        edit_newobject = (EditText) objectDialog.findViewById(R.id.edit_newobject);
        bt_newobject_search = (Button) objectDialog.findViewById(R.id.bt_newobject_search);
        title = (TextView) objectDialog.findViewById(R.id.text_newobject_title);
        list_object = (ListView) objectDialog.findViewById(R.id.list_newobject_object);
        text_sure = (TextView) objectDialog.findViewById(R.id.text_newobjectdialog_sure);
        text_cancel = (TextView) objectDialog.findViewById(R.id.text_newobjectdialog_cancel);
        TextView text_add = (TextView) objectDialog.findViewById(R.id.text_newobjectdialog_add);
        text_add.setVisibility(View.GONE);
        text_sure.setOnClickListener(this);
        text_cancel.setOnClickListener(this);

        title.setText("经纪机构总支");

        title.setFocusable(true);
        title.setFocusableInTouchMode(true);
        title.requestFocus();

        requestDXSFWObject("");

        bt_newobject_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param = edit_newobject.getText().toString().trim();
                requestDXSFWObject(param);

            }
        });
    }


    private void requestDXSFWObject(final String param){

        if (NetUtil.isNetworkAvailable(this)) {
            if (progressDialog == null) {
                progressDialog = showProgressDialog();
            } else {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                progressDialog.show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("param", param);

                    String result = RequestUtil.post(RequestUtil.GetJJJGHead, params);
                    Message msg = Message.obtain();
                    msg.what = 2;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }).start();

        } else {
            ShowToast(noNetText);
        }

    }


    private List<CheckHeadBean> attriToHead(List<InfoAttributeBean> attributeBeans){
        List<CheckHeadBean> checkHeadBeans = new ArrayList<>();

        if(attributeBeans.size()!=0){
            CheckHeadBean headBean = null;
            for(InfoAttributeBean infoAttributeBean:attributeBeans){
                headBean = new CheckHeadBean();
                headBean.setDbfield(infoAttributeBean.getKey());
                headBean.setTitle(infoAttributeBean.getName());
                headBean.setValue(infoAttributeBean.getValue());
                checkHeadBeans.add(headBean);
                headBean = null;
            }
        }

        return checkHeadBeans;
    }


    private void requestInsert(){
        if(NetUtil.isNetworkAvailable(this)){
            if(progressDialog==null){
                progressDialog = showProgressDialog();
            }else{
                progressDialog.show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {

                    HashMap<String,String> params = new HashMap<String, String>();
                    String tableinfo = "{\"head\":"+gson.toJson(attriToHead(newInfoAttributeBeans))+"}";
                    params.put("type",bussinesstype);
                    params.put("tableinfo",tableinfo);
                    params.put("streetid",streetID);
                    String result = RequestUtil.post(RequestUtil.NewAddNewObjectToTable,params);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = result;
                    mHandler.sendMessage(msg);

                }
            }).start();

        }
    }

    private boolean isHaveValue(String name){
        if(newInfoAttributeBeans!=null&&newInfoAttributeBeans.size()!=0){
            for(InfoAttributeBean infoAttributeBean:newInfoAttributeBeans){
                if(name.equals(infoAttributeBean.getName())&&(infoAttributeBean.getValue()==null||"".equals(infoAttributeBean.getValue()))){
                    return false;
                }
            }
        }
        return true;
    }

    private int isHaveKey(String key){
        if(newInfoAttributeBeans!=null&&newInfoAttributeBeans.size()!=0){
            for(int i=0;i<newInfoAttributeBeans.size();i++){
                InfoAttributeBean infoAttributeBean = newInfoAttributeBeans.get(i);
                if(key.equals(infoAttributeBean.getKey())){
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     *
     * @return
     */
    private int getChoosedHeadindex(){
        if(headBrokes!=null&&headBrokes.size()!=0){
            for(int i=0;i<headBrokes.size();i++){
                PrimeBrokerBean brokerBean = headBrokes.get(i);
                if(brokerBean.isCheck()){
                    return i;
                }
            }
        }
        return -1;
    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_lefttext:
                this.finish();
                break;
            case R.id.title_righttext:
                newInfoAttributeBeans.addAll(attributeBeans);
                if("经纪机构".equals(bussinesstype)){
                    if(!isHaveValue("经纪机构名称")){
                        ShowToast("请输入经纪机构名称");
                    }else if(!isHaveValue("注册地址")){
                        ShowToast("请输入注册地址");
                    }else if(!isHaveValue("营业执照号")){
                        ShowToast("请输入营业执照号");
                    }else if(!isHaveValue("联系电话")){
                        ShowToast("请输入联系电话");
                    }else{
                        requestInsert();
                    }
                }else{
                    if(!isHaveValue("项目名称")){
                        ShowToast("请输入项目名称");
                    }else if(!isHaveValue("项目地址")){
                        ShowToast("请输入项目地址");
                    }else if(!isHaveValue("联系人")){
                        ShowToast("请输入联系人");
                    }else if(!isHaveValue("联系电话")){
                        ShowToast("请输入联系电话");
                    }else{
                        requestInsert();
                    }
                }
                break;
            case R.id.text_insertobject_headvalue:
                if(objectDialog==null){
                    initCheckObjectDialog();
                }else{
                    objectDialog.show();
                }
                break;
            case R.id.text_newobjectdialog_sure:
                int index = getChoosedHeadindex();
                if(index==-1){
                }else{
                    PrimeBrokerBean brokerBean = headBrokes.get(index);
                    text_head.setText(brokerBean.getName());
                    int headIndex = isHaveKey("HEADID");
                    if(headIndex==-1){
                        InfoAttributeBean infoAttributeBean = new InfoAttributeBean();
                        infoAttributeBean.setName("总支ID");
                        infoAttributeBean.setKey("HEADID");
                        infoAttributeBean.setValue(brokerBean.getPKID());
                        newInfoAttributeBeans.add(infoAttributeBean);
                    }else{
                        newInfoAttributeBeans.get(headIndex).setValue(brokerBean.getPKID());
                    }
                }
                objectDialog.dismiss();
                break;
            case R.id.text_newobjectdialog_cancel:
                objectDialog.dismiss();
                break;
        }

    }
}
