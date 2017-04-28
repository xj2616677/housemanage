package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.BranchDialogAdapter;
import adapter.GeneralListAdapter;
import adapter.HouseSafeListAdapter;
import adapter.ObjectChooseExpandAdapter;
import adapter.PrimeListAdapter;
import adapter.PropertyListAdapter;
import bean.BranchBean;
import bean.CheckBaseInfoBean;
import bean.CompreBranchBean;
import bean.GeneralBean;
import bean.HouseSafeBean;
import bean.PrimeBrokerBean;
import bean.PropertyBean;
import bean.PropertyProjectBean;
import bean.StreetBean;
import bean.TaskBean;
import constants.Contant;
import shareutil.Bimp;
import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;

/**
 * 基本信息选择界面
 * Created by admin on 2016/3/31.
 */
public class SpotCheckActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,CompoundButton.OnCheckedChangeListener{

    private RadioGroup rg_taskfrom;
    private RelativeLayout rel_house;
    private Spinner spinner_taskfrom_checktask;
    private CheckBox cb_generalbasement;
    private CheckBox cb_housesafeuse;
    private CheckBox cb_primebroker;
    private CheckBox cb_propertymanage;
    private CheckBox cb_other;
    private EditText edit_petitionletter;
    private EditText edit_consultcomplain;
    private EditText edit_other;
    private Spinner spinner_spot_street;
    private ListView list_spot_checkobject;
    private RelativeLayout rel_checkobject;
    private TextView text_comprecheck_value;
    private AlertDialog alertDialogBranch;
    private ExpandableListView expandableBranch;
    private LinearLayout lin_street;
    private EditText edit_spot_content;
    private Button bt_spot_search;
    private TextView text_add;

    private LinearLayout lin_search;

//    private EditText edit_otherObject;
//    private CheckBox cb_otherObject;

    private RelativeLayout rel_objectName;
    private EditText edit_objectName;

    private String moude;
    private String taskFrom;
    private String checkedObject;
    private Handler mainHandler;
    private Map<String,String> checkTaskMap;
    private ProgressDialog progressDialog;
    private String ojectName = "";
    private String type = "";
    private String action = "";

    private String objectInfo = "";
    private Gson gson;
    private List<StreetBean> streetAll;
    private List<StreetBean> streets;
    private List<String> enforces;

    private List<PrimeBrokerBean> searchPrimeBean;
    private List<PropertyBean> searchPropertyBean;
    private List<GeneralBean> chooseGeneralBeans;
    private List<HouseSafeBean> chooseHouseBeans;
    private PrimeListAdapter primeListAdapter;
    private PropertyListAdapter propertyListAdapter;
    private GeneralListAdapter generalListAdapter;
    private HouseSafeListAdapter houseSafeListAdapter;
    private BranchDialogAdapter branchDialogAdapter;

    private int proIndex = 0;
    private String streetID = "";
    private String streetName = "";

    private AlertDialog objectDialog;
    private TextView title;
    private EditText edit_newobject;
    private Button bt_newobject_search;
    private ListView list_object;
    private TextView text_sure,text_cancel;

    private int propertyPosition = -1;

    private String clickStr = "";

    private List<BranchBean> branchs;

    private boolean isFirstStreet = false;

    private int addindex = 1;

    private String oldStreetId = "";

    private TextView text_insertobject;

    @Override
    protected void initView() {
        setTitle("现场检查");
        setContentLayout(R.layout.spotcheck_activity);
        ActivityManage.getInstance().addActivity(this);

        rg_taskfrom = (RadioGroup) findViewById(R.id.rg_spot_taskfrom);
        rel_house = (RelativeLayout) findViewById(R.id.rel_spot_housechoose);
        cb_housesafeuse = (CheckBox) findViewById(R.id.cb_spot_housesafeuse);
        cb_generalbasement = (CheckBox) findViewById(R.id.cb_spot_generalbasement);
        cb_primebroker = (CheckBox) findViewById(R.id.cb_spot_primebroker);
        cb_propertymanage = (CheckBox) findViewById(R.id.cb_spot_propertymanage);
        cb_other = (CheckBox) findViewById(R.id.cb_spot_other);

        lin_street = (LinearLayout) findViewById(R.id.lin_street);

        spinner_taskfrom_checktask = (Spinner) findViewById(R.id.spinner_taskfrom_checktask);
        text_comprecheck_value = (TextView) findViewById(R.id.text_comprecheck_value);

        edit_spot_content = (EditText) findViewById(R.id.edit_spot_content);
        bt_spot_search = (Button) findViewById(R.id.bt_spot_search);
        bt_spot_search.setOnClickListener(this);

        edit_petitionletter = (EditText) findViewById(R.id.edit_taskfrom_petitionletter);
        edit_consultcomplain = (EditText) findViewById(R.id.edit_taskfrom_consultcomplain);
        edit_other = (EditText) findViewById(R.id.edit_taskfrom_other);

        spinner_spot_street = (Spinner) findViewById(R.id.spinner_spot_street);
        list_spot_checkobject = (ListView) findViewById(R.id.list_spot_checkobject);

        rel_checkobject = (RelativeLayout) findViewById(R.id.rel_spot_checkobject);

        rel_objectName = (RelativeLayout) findViewById(R.id.rel_spot_objectname);
        edit_objectName = (EditText) findViewById(R.id.edit_spot_objectname);
        lin_search = (LinearLayout) findViewById(R.id.lin_spot_search);

        text_insertobject = (TextView) findViewById(R.id.text_spot_insertobject);
        text_insertobject.setOnClickListener(this);
//        edit_otherObject = (EditText) findViewById(R.id.edit_spot_otherobject);
//        cb_otherObject = (CheckBox) findViewById(R.id.cb_spot_otherobject);
//        cb_otherObject.setOnCheckedChangeListener(this);

        cb_housesafeuse.setOnCheckedChangeListener(this);
        cb_generalbasement.setOnCheckedChangeListener(this);
        cb_primebroker.setOnCheckedChangeListener(this);
        cb_propertymanage.setOnCheckedChangeListener(this);
        cb_other.setOnCheckedChangeListener(this);

        rg_taskfrom.setOnCheckedChangeListener(this);
        text_comprecheck_value.setOnClickListener(this);

        rg_taskfrom.check(R.id.rb_taskfrom_todaypatrol);


        rel_house.setVisibility(View.VISIBLE);
    }
    @Override
    protected void initData() {
        Contant.primeObjectList.clear();
        Contant.propertyObjectList.clear();

        Contant.fileName = "";
        gson = new Gson();
        enforces = new ArrayList<>();

        Intent intent= getIntent();
        action = intent.getAction();
        if(action.equals("map")){
            lin_street.setVisibility(View.GONE);
            ojectName = intent.getStringExtra("name");
            type = intent.getStringExtra("type");

            if(type.contains(getResources().getString(R.string.primebroker))){
                cb_primebroker.setChecked(true);
                cb_primebroker.setEnabled(false);
                cb_other.setEnabled(false);
                cb_propertymanage.setEnabled(false);
                cb_generalbasement.setEnabled(false);
                cb_housesafeuse.setEnabled(false);
            }else{
                if(type.equals("物业管理")){
                    cb_propertymanage.setChecked(true);
                    cb_propertymanage.setEnabled(false);
                    cb_other.setEnabled(false);
                    cb_primebroker.setEnabled(false);
                    cb_generalbasement.setEnabled(false);
                    cb_housesafeuse.setEnabled(false);
                }else if(type.equals(getResources().getString(R.string.generalbasement))){
                    cb_generalbasement.setChecked(true);
                }else if(type.equals(getResources().getString(R.string.housesafeuse))){
                    cb_housesafeuse.setChecked(true);
                }
                type = getResources().getString(R.string.propertymanage);
            }

            //从地图界面点击现在检查过来后，请求检查对象的详情
            if(NetUtil.isNetworkAvailable(SpotCheckActivity.this)){
                if(progressDialog==null){
                    progressDialog = showProgressDialog();
                }else{
                    progressDialog.show();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Message msg = Message.obtain();
                        String result = "";
                        if (type.equals(getResources().getString(R.string.primebroker))) {

                            Map<String, String> params = new HashMap<>();
                            params.put("objName", ojectName);
                            result = RequestUtil.post(RequestUtil.GetJJJGDetailByName, params);
                            msg.what = 1;
                            //经纪机构
                        } else if (type.equals(getResources().getString(R.string.propertymanage))) {
                            Map<String, String> params = new HashMap<>();
                            params.put("objName", ojectName);
                            result = RequestUtil.post(RequestUtil.GetProWYDetailByName, params);
                            msg.what = 2;
                            //物业管理
                        }
//                        else if (type.equals(getResources().getString(R.string.generalbasement))) {
//                            Map<String, String> params = new HashMap<>();
//                            params.put("objName", ojectName);
//                            result = RequestUtil.post(RequestUtil.GetBasementDetailByName, params);
//                            Log.i("TAG", "++++++++++" + result);
//                            msg.what = 3;
//                            //普通地下室
//                        } else if (type.equals(getResources().getString(R.string.housesafeuse))) {
//                            Map<String, String> params = new HashMap<>();
//                            params.put("objName", ojectName);
//                            result = RequestUtil.post(RequestUtil.GetHouseDetailByName, params);
//                            msg.what = 4;
//                            //房屋安全
//                        }
                        msg.obj = result;
                        mainHandler.sendMessage(msg);
                    }
                }).start();
            }else{
                ShowToast(noNetText);
            }
        }else {
            initStreetData();
        }


        spinner_spot_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                streetID = streets.get(position).getSTREET_ID();
                streetName = streets.get(position).getSTREET_NAME();
                if(!isFirstStreet){
                    if(enforces.size()!=0&&!enforces.contains("其他")){
                        if(Contant.propertyObjectList.size()>0){
                            PropertyBean propertyBean =  Contant.propertyObjectList.get(0);
                            if("其他".equals(propertyBean.getName())){
                            }else{
                                initCheckObjectData("");
                            }
                        }else{
                            initCheckObjectData("");
                        }

                    }
                }else{
                    isFirstStreet = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Contant.checkBaseInfoBean = new CheckBaseInfoBean();



        mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;

                switch (msg.what){
                    case 5:
                        List<TaskBean> tasks = getTaskList(result);
                        spinner_taskfrom_checktask.setAdapter(new ArrayAdapter<TaskBean>(SpotCheckActivity.this, android.R.layout.simple_spinner_item, tasks));
                        break;
                    case 1:
                        if(result!=null&&!result.equals("")) {
                            PrimeBrokerBean primeBrokerBean = new PrimeBrokerBean();
                            try {
                                JSONObject jsonObject = new JSONArray(result).getJSONObject(0);
                                primeBrokerBean.setPKID(jsonObject.optString("PKID", ""));
                                primeBrokerBean.setName(jsonObject.optString("ORGNAME", ""));
                                primeBrokerBean.setType(jsonObject.optString("JGTYPE", ""));
                                primeBrokerBean.setZzid(jsonObject.optString("ZZID", ""));
                                primeBrokerBean.setAddress(jsonObject.optString("ADDR_REGISTER", ""));
                                primeBrokerBean.setIsCheck(true);
//                            objectInfo = gson.toJson(primeBrokerBean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (searchPrimeBean == null) {
                                searchPrimeBean = new ArrayList<>();
                            } else {
                                searchPrimeBean.clear();
                            }
                            searchPrimeBean.add(primeBrokerBean);
                            Contant.primeObjectList.add(primeBrokerBean);
                            primeListAdapter = new PrimeListAdapter(SpotCheckActivity.this, searchPrimeBean,1);
                            list_spot_checkobject.setAdapter(primeListAdapter);
                        }else{
                            ShowToast("服务器异常");
                        }

                        break;
                    case 2:
                        if(result!=null&&!result.equals("")) {
                            PropertyBean propertyBean = new PropertyBean();
                            try {
                                JSONObject jsonObject = new JSONArray(result).getJSONObject(0);
                                propertyBean.setProId(jsonObject.optString("RPOWYID", ""));
                                propertyBean.setName(jsonObject.optString("ENGNAME", ""));
                                propertyBean.setType("");
                                propertyBean.setAddress(jsonObject.optString("ENGADDR", ""));
                                propertyBean.setManageBranch(jsonObject.optString("ORGNAME", ""));
                                propertyBean.setManageType("");
                                propertyBean.setIsCheck(true);
                                propertyBean.setIsGeneral(false);
                                propertyBean.setIsHouse(false);
                                propertyBean.setGeneralBeans(new ArrayList<GeneralBean>());
                                propertyBean.setHouseBeans(new ArrayList<HouseSafeBean>());
//                            objectInfo = gson.toJson(propertyBean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (searchPropertyBean == null) {
                                searchPropertyBean = new ArrayList<>();
                            } else {
                                searchPropertyBean.clear();
                            }
                            searchPropertyBean.add(propertyBean);
                            Contant.propertyObjectList.add(propertyBean);
                            propertyListAdapter = new PropertyListAdapter(SpotCheckActivity.this, searchPropertyBean);
                            list_spot_checkobject.setAdapter(propertyListAdapter);
                        }else{
                            ShowToast("服务器异常");
                        }
                        break;
                    case 3:
                        GeneralBean generalBean = new GeneralBean();
                        try {
                            JSONObject jsonObject = new JSONArray(result).getJSONObject(0);
                            generalBean.setBase_id(jsonObject.optString("base_id",""));
                            generalBean.setName(jsonObject.optString("buildingName", ""));
                            generalBean.setType("");
                            generalBean.setAddress(jsonObject.optString("basementAddress", ""));
                            generalBean.setManageBranch(jsonObject.optString("management", ""));
                            generalBean.setRightsMan(jsonObject.optString("propertyPerson", ""));
                            generalBean.setIsCheck(true);
                            objectInfo = gson.toJson(generalBean);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 4:
                        HouseSafeBean houseSafeBean = new HouseSafeBean();
                        try {
                            JSONObject jsonObject = new JSONArray(result).getJSONObject(0);
                            houseSafeBean.setHouse_No(jsonObject.optString("house_No",""));
                            houseSafeBean.setHouseLocate(jsonObject.optString("houseLocate", ""));
                            houseSafeBean.setIsCheck(true);
                            objectInfo = gson.toJson(houseSafeBean);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 6:
                        if(result!=null&&!result.equals("")){
                            streetAll = new ArrayList<>();
                            List<StreetBean> streetBeans = gson.fromJson(result, new TypeToken<List<StreetBean>>() {
                            }.getType());
                            streetAll.addAll(streetBeans);

                            streets = new ArrayList<>();
                            if(streetAll.size()!=1){
                                StreetBean streetBean = new StreetBean();
                                streetBean.setSTREET_NAME("全部");
                                streetBean.setSTREET_ID("");
                                streets.add(streetBean);
                            }
//                            if("房管局".equals(Contant.USERBEAN.getBranchName())){
//                                streets.addAll(getManageStreet("第一房管所"));
//                            }else {
//                                streets.addAll(getManageStreet(Contant.USERBEAN.getBranchName()));
//                            }
                            streets.addAll(streetAll);
                            ArrayAdapter<StreetBean> arrayAdapterStreet = new ArrayAdapter<StreetBean>(SpotCheckActivity.this,android.R.layout.simple_spinner_item,streets);
                            arrayAdapterStreet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_spot_street.setAdapter(arrayAdapterStreet);
                            isFirstStreet = true;
                            Contant.streetBeans.clear();
                            Contant.streetBeans.addAll(streets);
                            Contant.streetInfo = result;

                        }else{
                            ShowToast("服务器异常");
                            progressDialog.dismiss();
                        }
                        break;
                    case 7:
                        String resultPrime = (String) msg.obj;
                        if("[]".equals(resultPrime)){
                            ShowToast("未搜索到相关对象");
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
                            primeListAdapter = new PrimeListAdapter(SpotCheckActivity.this,searchPrimeBean,1);
                            list_spot_checkobject.setAdapter(primeListAdapter);
                        }
                        Contant.primeObjectList.clear();
                        break;
                    case 8:
                        String resultProperty = (String) msg.obj;
                        if("[]".equals(resultProperty)){
                            ShowToast("未搜索到相关对象");
                        }else if("".equals(resultProperty)){
                            ShowToast("服务器连接异常");
                        }else{
                            if(searchPropertyBean==null){
                                searchPropertyBean = new ArrayList<>();
                            }else{
                                searchPropertyBean.clear();
                            }

//                            PropertyBean propertyBean = new PropertyBean();
//                            propertyBean.setProId("-1");
//                            propertyBean.setName("其他");
//                            propertyBean.setType("");
//                            propertyBean.setAddress("");
//                            propertyBean.setManageBranch("");
//                            propertyBean.setManageType("");
//                            searchPropertyBean.add(propertyBean);

                            List<PropertyBean> propertyBeans = gson.fromJson(resultProperty, new TypeToken<List<PropertyBean>>() {
                            }.getType());
                            searchPropertyBean.addAll(propertyBeans);
                            for (int i = 0; i < searchPropertyBean.size(); i++) {
                                searchPropertyBean.get(i).setIsCheck(false);
                                searchPropertyBean.get(i).setIsGeneral(false);
                                searchPropertyBean.get(i).setIsHouse(false);

                                if(enforces.contains(getResources().getString(R.string.generalbasement))&&!enforces.contains(getResources().getString(R.string.housesafeuse))){
                                    searchPropertyBean.get(i).setIsGeneral(true);
                                }else if(enforces.contains(getResources().getString(R.string.housesafeuse))&&!enforces.contains(getResources().getString(R.string.generalbasement))){
                                    searchPropertyBean.get(i).setIsHouse(true);
                                }else if(enforces.contains(getResources().getString(R.string.housesafeuse))&&enforces.contains(getResources().getString(R.string.generalbasement))){
                                    searchPropertyBean.get(i).setIsHouse(true);
                                    searchPropertyBean.get(i).setIsGeneral(true);
                                }


                                searchPropertyBean.get(i).setGeneralBeans(new ArrayList<GeneralBean>());
                                searchPropertyBean.get(i).setHouseBeans( new ArrayList<HouseSafeBean>());
                            }
                            propertyListAdapter = new PropertyListAdapter(SpotCheckActivity.this, searchPropertyBean);
                            list_spot_checkobject.setAdapter(propertyListAdapter);
                        }
                        Contant.propertyObjectList.clear();
                        break;
                    case 9:
                        String resultGeneral = (String) msg.obj;
                        if("[]".equals(resultGeneral)){
                            ShowToast("该街道下面没有对象存在");
                        }else if("".equals(resultGeneral)){
                            ShowToast("服务器连接异常");
                        }else{
                            if(chooseGeneralBeans==null){
                                chooseGeneralBeans = new ArrayList<>();
                            }else{
                                chooseGeneralBeans.clear();
                            }
                            chooseGeneralBeans.addAll((List<GeneralBean>)(gson.fromJson(resultGeneral, new TypeToken<List<GeneralBean>>() {
                            }.getType())));

                            List<GeneralBean> generalBeans = searchPropertyBean.get(propertyPosition).getGeneralBeans();
                            for(GeneralBean general:chooseGeneralBeans){
                                String id = general.getBase_id();
                                for(GeneralBean bean:generalBeans){
                                    if(id.equals(bean.getBase_id())){
                                        general.setIsCheck(true);
                                        break;
                                    }
                                }
                            }
                            generalListAdapter = new GeneralListAdapter(SpotCheckActivity.this,chooseGeneralBeans,1);
                            list_object.setAdapter(generalListAdapter);
                        }
                        break;
                    case 10:

                        String resultHouse = (String) msg.obj;
                        if("[]".equals(resultHouse)){
                            ShowToast("该街道下面没有对象存在");
                        }else if("".equals(resultHouse)){
                            ShowToast("服务器连接异常");
                        }else{
                            if(chooseHouseBeans==null){
                                chooseHouseBeans = new ArrayList<>();
                            }else{
                                chooseHouseBeans.clear();
                            }
                            chooseHouseBeans.addAll((List<HouseSafeBean>) (gson.fromJson(resultHouse, new TypeToken<List<HouseSafeBean>>() {
                            }.getType())));

                            List<HouseSafeBean> houseBeans = searchPropertyBean.get(propertyPosition).getHouseBeans();
                            for(HouseSafeBean house:chooseHouseBeans){
                                String id = house.getBUILD_NO();
                                for(HouseSafeBean bean:houseBeans){
                                    if(id.equals(bean.getBUILD_NO())){
                                        house.setIsCheck(true);
                                        break;
                                    }
                                }
                            }
                            houseSafeListAdapter = new HouseSafeListAdapter(SpotCheckActivity.this,chooseHouseBeans,1);
                            list_object.setAdapter(houseSafeListAdapter);
                        }
                        break;
                    case 11:
                        String resultBranch = (String) msg.obj;
                        if("[]".equals(resultBranch)||"".equals(resultBranch)){
                            ShowToast("服务器连接异常");
                        }else{
                            branchs = new ArrayList<>();
                            branchs.addAll(branchJX(resultBranch));
                            branchDialogAdapter= new BranchDialogAdapter(SpotCheckActivity.this,branchs);
                            expandableBranch.setAdapter(branchDialogAdapter);
                        }
                        break;
                    case 17:

                        JSONObject jsonObject = null;
                        String isSuccess = "";
                        String failReason = "";
                        String succeedString = "";

                        try {
                            jsonObject = new JSONObject((String) result);
                            isSuccess = jsonObject.getString("result");
                            failReason = jsonObject.getString("failReason");
                            succeedString = jsonObject.getString("succeedString");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if("1".equals(isSuccess)){
                            ShowToast("删除成功");


                        }else{
                            ShowToast(failReason);
                        }

                        break;
                }
                if(progressDialog!=null&&progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        };
    }


    public void requestDeleteObject(final int index, final String id){


        if(NetUtil.isNetworkAvailable(this)){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    switch (index){
                        case 1:

                            HashMap<String,String> params = new HashMap<>();
                            params.put("base_id", id);
                            String result = RequestUtil.post(RequestUtil.DelBaseMentInfo,params);
                            Message message = Message.obtain();
                            message.what = 17;
                            message.arg1 = index;
                            message.obj = result;
                            mainHandler.sendMessage(message);

                            break;
                        case 2:

                            HashMap<String,String> params1 = new HashMap<>();
                            Log.i("TAG","id"+id);
                            params1.put("ID", id);
                            String result1 = RequestUtil.post(RequestUtil.Del_BuildingFromDPT,params1);
                            Message message1 = Message.obtain();
                            message1.what = 17;
                            message1.arg1 = index;
                            message1.obj = result1;
                            mainHandler.sendMessage(message1);
                            break;
                    }
                }
            }).start();



        }else{
            ShowToast(noNetText);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        //添加普通地下室或者房屋后进行数据的更新
        if(Contant.isAddObject){
            if(addindex==1){
                GeneralBean generalBean = new GeneralBean();
                generalBean.setHouseLocate(Contant.houseLocate);
                generalBean.setBase_id(Contant.base_id);
                generalBean.setIsCheck(true);
                generalBean.setIsSelfAdd(true);
                generalBean.setAddman(Contant.userid);
                chooseGeneralBeans.add(0,generalBean);
                generalListAdapter.notifyDataSetChanged();
            }else if(addindex==2){
                HouseSafeBean houseSafeBean = new HouseSafeBean();
                houseSafeBean.setBUILD_NO(Contant.buildNo);
                houseSafeBean.setBUILD_SITE(Contant.buildsite);
                houseSafeBean.setIsCheck(true);
                houseSafeBean.setIsSelfAdd(true);
                houseSafeBean.setADDMAN(Contant.userid);
                chooseHouseBeans.add(0,houseSafeBean);
                houseSafeListAdapter.notifyDataSetChanged();
            }
        }

        if(Contant.isAddjjjgorpro){
            if(enforces.contains("经纪机构")){
                if(Contant.newAddprimeBrokerBean!=null){
                    searchPrimeBean.add(0,Contant.newAddprimeBrokerBean);
                    primeListAdapter.notifyDataSetChanged();
                }
            }else{
                if(Contant.newAddpropertyBean!=null){
                    searchPropertyBean.add(0,Contant.newAddpropertyBean);
                    propertyListAdapter.notifyDataSetChanged();
                }
            }
            Contant.isAddjjjgorpro = false;
        }
        //增加的代码
        Bimp.selectBitmaps.clear();
        Bimp.tempSelectBitmap0.clear();

        Contant.isAddObject = false;
        Contant.personBitmapList.clear();
        Contant.objectBitmapList.clear();
        Contant.isSign = false;
    }


    /**
     * 对综合执法部门的请求结果进行解析
     * @param result
     * @return
     */
    private List<BranchBean> branchJX(String result){
        List<BranchBean> branchList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            if(jsonArray.length()!=0){
                for(int i=0;i<jsonArray.length();i++){
                    BranchBean branchBean = new BranchBean();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String type = jsonObject.getString("type");
                    JSONArray list = jsonObject.getJSONArray("list");
                    List<CompreBranchBean> compreBranchBeans = new ArrayList<>();
                    for(int j=0;j<list.length();j++){
                        CompreBranchBean compreBranchBean = new CompreBranchBean();
                        compreBranchBean.setName(list.getString(j));
                        compreBranchBeans.add(compreBranchBean);
                    }
                    branchBean.setBranch(type);
                    branchBean.setCompreBranchBeans(compreBranchBeans);
                    branchList.add(branchBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return branchList;
    }


    /**
     * 对检查任务列表的请求结果进行解析
     * @param result
     * @return
     */
    private List<TaskBean> getTaskList(String result){
        List<TaskBean> taskList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("checktasks");
            Gson gson = new Gson();
            taskList = gson.fromJson(jsonArray.toString(), new TypeToken<List<TaskBean>>() {}.getType());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return taskList;
    }

    /**
     * 根据登录用户所在的房管所来进行街道的筛选
     * @param houseManage
     * @return
     */
    private List<StreetBean> getManageStreet(String houseManage){
        List<StreetBean> streets = new ArrayList<>();
        if(streetAll!=null&&streetAll.size()!=0){
            for(StreetBean street:streetAll){
                if(houseManage.equals(street.getHOUSEMANAGER_NAME())){
                    streets.add(street);
                }
            }
        }
        return streets;
    }




    /**
     * radiogroup的监听事件
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int radioId = group.getCheckedRadioButtonId();
        final RadioButton radioButton = (RadioButton) findViewById(radioId);
        switch (group.getId()){
            case R.id.rg_spot_taskfrom:
                taskFrom = radioButton.getText().toString().trim();
                if(taskFrom.equals(getResources().getString(R.string.checktask))){
                    text_comprecheck_value.setVisibility(View.INVISIBLE);
                    spinner_taskfrom_checktask.setVisibility(View.VISIBLE);
                    edit_petitionletter.setVisibility(View.INVISIBLE);
                    edit_consultcomplain.setVisibility(View.INVISIBLE);
                    edit_other.setVisibility(View.INVISIBLE);

                    checkTaskMap = new HashMap<>();
                    checkTaskMap.put("name", Contant.userid);

                    if(NetUtil.isNetworkAvailable(SpotCheckActivity.this)){
                        if(progressDialog==null){
                            progressDialog = showProgressDialog();
                        }else{
                            progressDialog.show();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = RequestUtil.post(RequestUtil.Checktask,checkTaskMap);
                                Message msg = Message.obtain();
                                msg.what = 5;
                                msg.obj = result;
                                mainHandler.sendMessage(msg);
                            }
                        }).start();
                    }else{
                        ShowToast(noNetText);
                    }

                }else if(taskFrom.equals(getResources().getString(R.string.petitionletter))){
                    text_comprecheck_value.setVisibility(View.INVISIBLE);
                    spinner_taskfrom_checktask.setVisibility(View.INVISIBLE);
                    edit_petitionletter.setVisibility(View.VISIBLE);
                    edit_consultcomplain.setVisibility(View.INVISIBLE);
                    edit_other.setVisibility(View.INVISIBLE);
                }else if(taskFrom.equals(getResources().getString(R.string.consultcomplain))){
                    text_comprecheck_value.setVisibility(View.INVISIBLE);
                    spinner_taskfrom_checktask.setVisibility(View.INVISIBLE);
                    edit_petitionletter.setVisibility(View.INVISIBLE);
                    edit_consultcomplain.setVisibility(View.VISIBLE);
                    edit_other.setVisibility(View.INVISIBLE);
                }else if(taskFrom.equals(getResources().getString(R.string.other))){
                    spinner_taskfrom_checktask.setVisibility(View.INVISIBLE);
                    edit_petitionletter.setVisibility(View.INVISIBLE);
                    edit_consultcomplain.setVisibility(View.INVISIBLE);
                    edit_other.setVisibility(View.VISIBLE);
                    text_comprecheck_value.setVisibility(View.INVISIBLE);
                }else
                if(taskFrom.equals(getResources().getString(R.string.comprehensivecheck))){
                    text_comprecheck_value.setVisibility(View.VISIBLE);
                    spinner_taskfrom_checktask.setVisibility(View.INVISIBLE);
                    edit_petitionletter.setVisibility(View.INVISIBLE);
                    edit_consultcomplain.setVisibility(View.INVISIBLE);
                    edit_other.setVisibility(View.INVISIBLE);
                }else
                {
                    text_comprecheck_value.setVisibility(View.INVISIBLE);
                    spinner_taskfrom_checktask.setVisibility(View.INVISIBLE);
                    edit_petitionletter.setVisibility(View.INVISIBLE);
                    edit_consultcomplain.setVisibility(View.INVISIBLE);
                    edit_other.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.text_spot_insertobject:
                if("全部".equals(streetName)){
                    ShowToast("请选择街镇");
                }else{
                    Intent intent = new Intent(this,InsertObjectActivity.class);
                    if(enforces.contains("经纪机构")){
                        intent.putExtra("bussiness","经纪机构");
                    }else{
                        intent.putExtra("bussiness","物业管理");
                    }
                    intent.putExtra("streetID",streetID);
                    this.startActivity(intent);
                }
                break;
            //对象搜索按钮
            case R.id.bt_spot_search:
                jianpandelete();
                String param= edit_spot_content.getText().toString().trim();
                initCheckObjectData(param);
                break;
            case R.id.title_lefttext:
                this.finish();
                break;
            case R.id.title_righttext:
                boolean isPass = true;
                Contant.checkBaseInfoBean.setInspectType("");
                Contant.checkBaseInfoBean.setSource(taskFrom);
                if(taskFrom.equals(getResources().getString(R.string.checktask))){
                    Contant.checkBaseInfoBean.setSourceDetail(((TaskBean) spinner_taskfrom_checktask.getSelectedItem()).getName());
                }
                else if(taskFrom.equals(getResources().getString(R.string.comprehensivecheck))){
                    String branch = text_comprecheck_value.getText().toString().trim();
                    if(branch.equals("")){
                        Toast.makeText(this,"请选择综合执法检查的部门",Toast.LENGTH_SHORT).show();
                        isPass = false;
                    }else{
                        Contant.checkBaseInfoBean.setSourceDetail(branch);
                        isPass = true;
                    }
                }else{
                    Contant.checkBaseInfoBean.setSourceDetail("");
                }
                if(isPass) {
                    Contant.checkBaseInfoBean.setObjectType(checkedObject);
                    Contant.checkBaseInfoBean.setStreetId(streetID);
                    Contant.enforceList.clear();
                    if (cb_primebroker.isChecked()) {
                        Contant.enforceList.add(getResources().getString(R.string.primebroker));
                    }
                    if (cb_generalbasement.isChecked()) {
                        Contant.enforceList.add(getResources().getString(R.string.generalbasement));
                    }
                    if (cb_housesafeuse.isChecked()) {
                        Contant.enforceList.add(getResources().getString(R.string.housesafeuse));
                    }

                    if (cb_propertymanage.isChecked()) {
                        Contant.enforceList.add(getResources().getString(R.string.propertymanage));
                    }
                    if(cb_other.isChecked()){
                        Contant.enforceList.add(getResources().getString(R.string.other));
                    }
                    if (Contant.enforceList.size() == 0) {
                        Toast.makeText(SpotCheckActivity.this, "请选择至少一个业务类型", Toast.LENGTH_SHORT).show();
                    } else {
                        if(!Contant.enforceList.contains(getResources().getString(R.string.other))){
                            boolean isNext = false;
                            if(Contant.enforceList.contains(getResources().getString(R.string.primebroker))){
                                if(Contant.primeObjectList.size()>0){
                                    isNext = true;
                                }else{
                                    isNext = false;
                                    ShowToast("请选择一个检查对象");
                                }
                            }else {
                                if(Contant.propertyObjectList.size()>0){
                                    PropertyBean propertyBean =  Contant.propertyObjectList.get(0);
                                    if("其他".equals(propertyBean.getName())&&"".equals(streetID)){
                                        isNext = false;
                                        ShowToast("请选择街镇");
                                    }else{
                                        isNext = true;
                                    }
                                }else{
                                    isNext = false;
                                    ShowToast("请选择一个检查对象");
                                }
                            }

//                            int count = getObjectCount();
//                            if (count == 0) {
//                                ShowToast("请选择一个对象");
//                            }
                            if(isNext){
                                Intent intent = new Intent();
                                if (action.equals("map")) {
                                    intent.setClass(this, TableHeaderActivity.class);
                                    intent.setAction("map");
                                    intent.putExtra("type", type);
                                    intent.putExtra("objectInfo", objectInfo);
                                } else {
                                    intent.setClass(this, TableHeaderActivity.class);
                                    intent.setAction("spot");
                                }
                                this.startActivity(intent);
                            }
                        }else{
                            String objectName = edit_objectName.getText().toString();
                            if("".equals(objectName)){
                                ShowToast("请输入被检对象地址");
                            }else if("".equals(streetID)){
                                ShowToast("请选择街镇");
                            }else{
                                Intent intent = new Intent(SpotCheckActivity.this,CommitActivity.class);
                                intent.setAction("objectAddress");
                                intent.putExtra("objectAddress",objectName);
                                SpotCheckActivity.this.startActivity(intent);
                            }
                        }
                    }
                }
                break;
            //选择普通地下室或者房屋dialog的“确定”按钮
            case R.id.text_newobjectdialog_sure:
                String titlestr = title.getText().toString();
                if(getResources().getString(R.string.generalbasement).equals(titlestr)){
                    searchPropertyBean.get(propertyPosition).getGeneralBeans().clear();
                    searchPropertyBean.get(propertyPosition).getGeneralBeans().addAll(getNewGeneral());
                }else if(getResources().getString(R.string.housesafeuse).equals(titlestr)){
                    searchPropertyBean.get(propertyPosition).getHouseBeans().clear();
                    searchPropertyBean.get(propertyPosition).getHouseBeans().addAll(getNewHouse());
                }
                propertyListAdapter.notifyDataSetChanged();
                objectDialog.dismiss();
                break;
            case R.id.text_newobjectdialog_cancel:
                objectDialog.dismiss();
                break;
            //普通地下室或者房屋dialog中的“添加”按钮
            case R.id.text_newobjectdialog_add:
                Intent intent = new Intent(SpotCheckActivity.this,AddGeneralObjectActivity.class);
                String streetinfo = gson.toJson(streets);
                intent.putExtra("streetinfo", streetinfo);
                intent.putExtra("index", addindex);
                SpotCheckActivity.this.startActivity(intent);
                break;
            //综合执法检查附加信息的textView
            case R.id.text_comprecheck_value:
                initComprecheckDialog();
                break;
            //综合执法检查附加信息的dialog的“确定”按钮
            case R.id.text_persondialog_sure:
                if(branchs==null||branchs.size()==0){
                    text_comprecheck_value.setText("");
                }else{
                    StringBuilder stringBuilder = new StringBuilder();
                    for(BranchBean bean:branchs){
                        List<CompreBranchBean> compreBranchBeans = bean.getCompreBranchBeans();
                        for(CompreBranchBean compreBranchBean:compreBranchBeans){
                            if(compreBranchBean.isCheck()){
                                if("".equals(stringBuilder.toString())){
                                    stringBuilder.append(compreBranchBean.getName());
                                }else{
                                    stringBuilder.append(","+compreBranchBean.getName());
                                }
                            }
                        }
                    }
                    text_comprecheck_value.setText(stringBuilder.toString());
                }
                alertDialogBranch.dismiss();
                break;
            case R.id.text_persondialog_cancel:
                alertDialogBranch.dismiss();
                break;
        }
    }

    /**
     * 综合执法检查的选项窗口
     */
    private void initComprecheckDialog(){

        if(alertDialogBranch==null||branchs==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            alertDialogBranch = builder.create();
            alertDialogBranch.show();
            alertDialogBranch.setContentView(R.layout.personchoose_dialog);
            TextView title = (TextView) alertDialogBranch.findViewById(R.id.text_persondialog_title);
            TextView more = (TextView) alertDialogBranch.findViewById(R.id.text_person_more);
            expandableBranch = (ExpandableListView) alertDialogBranch.findViewById(R.id.expandlist_persondialog);
            TextView text_branch_sure = (TextView) alertDialogBranch.findViewById(R.id.text_persondialog_sure);
            TextView text_branch_cancel = (TextView) alertDialogBranch.findViewById(R.id.text_persondialog_cancel);
            text_branch_sure.setOnClickListener(this);
            text_branch_cancel.setOnClickListener(this);
            title.setText("检查部门");
            more.setVisibility(View.GONE);
            requestCompreBranch();
            text_branch_sure.setOnClickListener(this);
            text_branch_cancel.setOnClickListener(this);
        }else{
            alertDialogBranch.show();
        }


    }

    /**
     * 请求综合执法的部门
     */
    private void requestCompreBranch(){
        if(NetUtil.isNetworkAvailable(this)){
            if(progressDialog==null){
                progressDialog = showProgressDialog();
            }else{
                progressDialog.show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,Object> params = new HashMap<String, Object>();
                    params.put("name", Contant.userid);
                    String result = RequestUtil.postob(RequestUtil.GetOtherBranchList, params);
                    Message msg = Message.obtain();
                    msg.what = 11;
                    msg.obj = result;
                    mainHandler.sendMessage(msg);
                }
            }).start();
        }else{
            ShowToast(noNetText);
        }

    }

    /**
     * 获取检查对象的个数
     * @return
     */
    private int getObjectCount(){
        int count = 0;
        if(Contant.enforceList.contains(getResources().getString(R.string.primebroker))){
            count = Contant.primeObjectList.size();
        }else{
            count = Contant.propertyObjectList.size();
        }
        return count;
    }

    /**
     * 获取普通地下室列表中选中的对象
     * @return
     */
    private List<GeneralBean> getNewGeneral(){
        List<GeneralBean> newGeneral  = new ArrayList<>();
        for(GeneralBean generalBean:chooseGeneralBeans){
            if(generalBean.isCheck()){
                newGeneral.add(generalBean);
            }
        }
        return newGeneral;
    }

    /**
     * 获取房屋列表中选中的对象
     * @return
     */
    private List<HouseSafeBean> getNewHouse(){
        List<HouseSafeBean> newHouse = new ArrayList<>();
        for(HouseSafeBean houseSafeBean:chooseHouseBeans){
            if(houseSafeBean.isCheck()){
                newHouse.add(houseSafeBean);
            }
        }
        return newHouse;
    }

    /**
     * 获取街道的数据
     */
    private void initStreetData(){


        if(NetUtil.isNetworkAvailable(this)){
            if(progressDialog==null){
                progressDialog = showProgressDialog();
            }else{
                progressDialog.show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("name", Contant.userid);
                    String result = RequestUtil.post(RequestUtil.GetStreetList, params);
                    Message msg = Message.obtain();
                    msg.what =6;
//                    msg.arg2=index;
                    msg.obj = result;
                    mainHandler.sendMessage(msg);

                }
            }).start();

        }else{
            ShowToast(noNetText);
        }

    }

    /**
     * 加载检查对象的数据，param为输入的搜索条件，根据index来确定物业对象是否显示普通地下室和房屋安全
     * 0 为都不显示
     * 1 为只显示普通地下室
     * 2 为只显示房屋
     * 3 为两个都显示
     *
     * @param param
     */
    private void initCheckObjectData(final String param){


        if (NetUtil.isNetworkAvailable(this)) {
            if(!progressDialog.isShowing()) {
                if (progressDialog == null) {
                    progressDialog = showProgressDialog();
                } else {
                    progressDialog.show();
                }
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,String> mapParam = new HashMap<String, String>();
                    mapParam.put("name", Contant.userid);
                    mapParam.put("param", param);
                    mapParam.put("streetID", streetID);
//                        String enforce = enforces.get(0);


                    if (enforces.size()==1&&enforces.contains(getResources().getString(R.string.primebroker))) {
                        //经纪机构
                        String result = RequestUtil.post(RequestUtil.GetJJJGListByStreetID, mapParam);
                        Message msg = Message.obtain();
                        msg.what = 7;
                        msg.obj = result;
                        mainHandler.sendMessage(msg);
                    } else {
                        String result = RequestUtil.post(RequestUtil.GetWYListByStreetID, mapParam);
                        Message msg = Message.obtain();
                        msg.what = 8;
                        msg.obj = result;
                        mainHandler.sendMessage(msg);
                    }
                }
            }).start();
        } else {
            ShowToast(noNetText);
        }
    }

    /**
     * 普通地下室或者房屋列表选择的dialog
     * @param titlestr
     * @param proName
     * @param position
     */
    public void initCheckObjectDialog(final String titlestr, final String proName,int position){

        if(position==propertyPosition&&clickStr.equals(titlestr)&&oldStreetId.equals(streetID)){
            objectDialog.show();
        }else {
            oldStreetId = streetID;
            clickStr = titlestr;
            propertyPosition = position;
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
            text_add = (TextView) objectDialog.findViewById(R.id.text_newobjectdialog_add);
            text_sure.setOnClickListener(this);
            text_cancel.setOnClickListener(this);
            text_add.setOnClickListener(this);

            title.setText(titlestr);

            title.setFocusable(true);
            title.setFocusableInTouchMode(true);
            title.requestFocus();

            requestDXSFWObject(proName, titlestr, "");

            bt_newobject_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String param = edit_newobject.getText().toString().trim();
                    requestDXSFWObject(proName, titlestr, param);

                }
            });

        }

    }

    private void requestDXSFWObject(final String proName, final String titlestr, final String param){

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
                    params.put("name", Contant.userid);
                    params.put("param", param);
                    params.put("streetID", streetID);
                    params.put("proName", proName);

                    if (getResources().getString(R.string.generalbasement).equals(titlestr)) {
                        //经纪机构
                        String result = RequestUtil.post(RequestUtil.GetDXSListByStreetIDAndProName, params);
                        Message msg = Message.obtain();
                        msg.what = 9;
                        msg.obj = result;
                        mainHandler.sendMessage(msg);
                        addindex = 1;
                    } else if (getResources().getString(R.string.housesafeuse).equals(titlestr)) {
                        String result = RequestUtil.post(RequestUtil.GetBuildingListByStreetIDAndProName, params);
                        Message msg = Message.obtain();
                        msg.what = 10;
                        msg.obj = result;
                        mainHandler.sendMessage(msg);
                        addindex = 2;
                    }
                }
            }).start();

        } else {
            ShowToast(noNetText);
        }

    }


    private List<GeneralBean> searchGeneralObject(String str){
        List<GeneralBean> generalBeans = new ArrayList<>();
        for(int i=0;i<chooseGeneralBeans.size();i++){
            GeneralBean generalBean  = chooseGeneralBeans.get(i);
            String name = generalBean.getHouseLocate();
            if(name.contains(str)){
                generalBeans.add(generalBean);
            }
        }
        return generalBeans;

    }

    private List<HouseSafeBean> searchHouseObject(String str){
        List<HouseSafeBean> houseBeans = new ArrayList<>();
        for(int i=0;i<chooseHouseBeans.size();i++){
            HouseSafeBean houseSafeBean  = chooseHouseBeans.get(i);
            String name = houseSafeBean.getBUILD_SITE();
            if(name.contains(str)){
                houseBeans.add(houseSafeBean);
            }
        }
        return houseBeans;

    }



    /**
     * checkbox选择改变的监听事件
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.cb_spot_primebroker:
                if(isChecked){
                    text_insertobject.setVisibility(View.VISIBLE);
                    lin_search.setVisibility(View.VISIBLE);
                    if(cb_other.isChecked()){
                        cb_other.setChecked(false);
                    }
                    if(cb_propertymanage.isChecked()||cb_generalbasement.isChecked()||cb_housesafeuse.isChecked()){
                        ShowToast("经纪机构不能与其他类别一起检查");
                        cb_primebroker.setChecked(false);
                    }else{
                        enforces.clear();
                        enforces.add(getResources().getString(R.string.primebroker));
                        if("main".equals(action)){
                            rel_checkobject.setVisibility(View.VISIBLE);
                            initCheckObjectData("");
                        }
                    }
                }else{
                    text_insertobject.setVisibility(View.GONE);
                    enforces.remove(getResources().getString(R.string.primebroker));
                    if(searchPrimeBean!=null){
                        searchPrimeBean.clear();
                        primeListAdapter.notifyDataSetChanged();
                    }
                    proIndex=0;
                }

                break;
            case R.id.cb_spot_propertymanage:
                if(isChecked){
                    text_insertobject.setVisibility(View.VISIBLE);
                    lin_search.setVisibility(View.VISIBLE);
                    if(cb_other.isChecked()){
                        cb_other.setChecked(false);
                    }
                    if(cb_primebroker.isChecked()){
                        ShowToast("经纪机构不能与其他类别一起检查");
                        cb_propertymanage.setChecked(false);
                    }else{
                        enforces.add(getResources().getString(R.string.propertymanage));
                        if("main".equals(action)) {
                            if (proIndex == 0) {
                                rel_checkobject.setVisibility(View.VISIBLE);
                                initCheckObjectData("");
                                proIndex++;
                            }
                        }
                    }
                }else{
                    enforces.remove(getResources().getString(R.string.propertymanage));
                    if(enforces.size()==0){
                        text_insertobject.setVisibility(View.GONE);
                        if(searchPropertyBean!=null){
                            searchPropertyBean.clear();
                            propertyListAdapter.notifyDataSetChanged();
                        }
                        proIndex=0;
                    }
                }

                break;
            case R.id.cb_spot_generalbasement:
                if(isChecked){
                    text_insertobject.setVisibility(View.VISIBLE);
                    lin_search.setVisibility(View.VISIBLE);
                    if(cb_other.isChecked()){
                        cb_other.setChecked(false);
                    }
                    if(cb_primebroker.isChecked()){
                        ShowToast("经纪机构不能与其他类别一起检查");
                        cb_generalbasement.setChecked(false);
                    }else{
                        enforces.add(getResources().getString(R.string.generalbasement));
                        if (proIndex == 0) {
                            if("main".equals(action)) {
                                rel_checkobject.setVisibility(View.VISIBLE);
                                initCheckObjectData("");
                                proIndex++;
                            }
                        } else {
                            if (searchPropertyBean != null && searchPropertyBean.size() != 0) {
                                for (PropertyBean propertyBean : searchPropertyBean) {
                                    propertyBean.setIsGeneral(true);
                                }
                                propertyListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }else{
                    enforces.remove(getResources().getString(R.string.generalbasement));
                    if(enforces.size()==0){
                        text_insertobject.setVisibility(View.GONE);
                        if(searchPropertyBean!=null){
                            searchPropertyBean.clear();
                            propertyListAdapter.notifyDataSetChanged();
                        }
                        proIndex=0;
                    }else{
                        if(searchPropertyBean!=null&&searchPropertyBean.size()!=0){
                            for(PropertyBean propertyBean:searchPropertyBean){
                                propertyBean.setIsGeneral(false);
                                propertyBean.getGeneralBeans().clear();
                            }
                            propertyListAdapter.notifyDataSetChanged();
                        }
                    }
                }

                break;
            case R.id.cb_spot_housesafeuse:
                if(isChecked){
                    text_insertobject.setVisibility(View.VISIBLE);
                    lin_search.setVisibility(View.VISIBLE);
                    if(cb_other.isChecked()){
                        cb_other.setChecked(false);
                    }
                    if(cb_primebroker.isChecked()){
                        ShowToast("经纪机构不能与其他类别一起检查");
                        cb_housesafeuse.setChecked(false);
                    }else{
                        enforces.add(getResources().getString(R.string.housesafeuse));

                        if (proIndex == 0) {
                            if("main".equals(action)) {
                                rel_checkobject.setVisibility(View.VISIBLE);
                                initCheckObjectData("");
                                proIndex++;
                            }
                        } else {
                            if (searchPropertyBean != null && searchPropertyBean.size() != 0) {
                                for (PropertyBean propertyBean : searchPropertyBean) {
                                    propertyBean.setIsHouse(true);
                                }
                                propertyListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }else{
                    enforces.remove(getResources().getString(R.string.housesafeuse));
                    if(enforces.size()==0){
                        text_insertobject.setVisibility(View.GONE);
                        if(searchPropertyBean!=null){
                            searchPropertyBean.clear();
                            propertyListAdapter.notifyDataSetChanged();
                        }
                        proIndex=0;
                    }else{
                        if(searchPropertyBean!=null&&searchPropertyBean.size()!=0){
                            for(PropertyBean propertyBean:searchPropertyBean){
                                propertyBean.setIsHouse(false);
                                propertyBean.getHouseBeans().clear();
                            }
                            propertyListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case R.id.cb_spot_other:
                if(isChecked){
                    text_insertobject.setVisibility(View.GONE);
                    enforces.clear();
                    enforces.add(getResources().getString(R.string.other));
                    rel_checkobject.setVisibility(View.VISIBLE);
                    rel_objectName.setVisibility(View.VISIBLE);
                    if(cb_primebroker.isChecked()){
                        cb_primebroker.setChecked(false);
                    }else {
                        if (cb_housesafeuse.isChecked()) {
                            cb_housesafeuse.setChecked(false);
                        }
                        if (cb_propertymanage.isChecked()) {
                            cb_propertymanage.setChecked(false);
                        }
                        if (cb_generalbasement.isChecked()) {
                            cb_generalbasement.setChecked(false);
                        }
                    }
                    list_spot_checkobject.setVisibility(View.GONE);
                    lin_search.setVisibility(View.GONE);

                }else{
                    text_insertobject.setVisibility(View.GONE);
                    enforces.remove(getResources().getString(R.string.other));
                    rel_objectName.setVisibility(View.GONE);
                    list_spot_checkobject.setVisibility(View.VISIBLE);
                    lin_search.setVisibility(View.VISIBLE);
                    proIndex = 0;
                }
                break;

        }

    }
}
