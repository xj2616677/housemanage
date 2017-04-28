package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.StreetBean;
import bean.StructureBean;
import constants.Contant;
import datepicker.cons.DPMode;
import datepicker.views.DatePicker;
import util.DensityUtil;
import util.NetUtil;
import util.RequestUtil;

/**
 * Created by admin on 2016/8/22.
 */
public class AddGeneralObjectActivity extends BaseActivity {


    private LinearLayout lin_addobject;
    private List<View> views;
    private ProgressDialog progressDialog;
    private String[] names;
    private String[] keys;
    private List<StreetBean> streets;
    private Spinner spinnerStreet;
    private String streetId = "";
    private Handler mHandler;
    private Gson gson;
    private String houseLocate = "";
    private String buildsite = "";
    private String markInfo;
    private int addIndex = 1;
    private String structureId = "";
    private List<StructureBean> structureBeans;
    private String floorStr = "";
    private List<String> listFloor;



    @Override
    protected void initView() {

        setTitle("详细信息");
        setRightText("保存");
        setContentLayout(R.layout.add_generallayout);

        lin_addobject = (LinearLayout) findViewById(R.id.lin_addobject);
        gson = new Gson();
        Intent intent = getIntent();
        String streetInfo = intent.getStringExtra("streetinfo");
        addIndex = intent.getIntExtra("index",1);
        streets = new ArrayList<>();
        streets.addAll((List<StreetBean>)(gson.fromJson(streetInfo,new TypeToken<List<StreetBean>>(){}.getType())));

        for(int i=0;i<streets.size();i++){
            String streetName = streets.get(i).getSTREET_NAME();
            if(streetName.equals("全部")){
                streets.remove(i);
            }
        }


        if(addIndex ==1){
            keys = getResources().getStringArray(R.array.generalinfokey);
            names = getResources().getStringArray(R.array.generalinfoname);
        }else if(addIndex==2){
            keys = getResources().getStringArray(R.array.addhouseinfokey);
            names = getResources().getStringArray(R.array.addhouseinfoname);
        }
        for(int i=0;i<keys.length;i++){
            if(keys[i].contains("STREET_NAME")){
                keys[i] = "street_id";
            }
        }
        views = new ArrayList<>();
        processView(names);




    }

    @Override
    protected void initData() {

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(progressDialog!=null){
                    progressDialog.dismiss();
                }

                String result = (String) msg.obj;
                String code = "0";
                String failReason = "";
                String succeedString = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("result");
                    failReason = jsonObject.getString("failReason");
                    succeedString = jsonObject.getString("succeedString");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if("0".equals(code)){
                    ShowToast(failReason);
                    Contant.isAddObject = false;
                }else {
                    switch (msg.what) {
                        case 1:
//                    {"failReason":"","result":"1","succeedString":"-5"}
                            Contant.houseLocate = houseLocate;
                            Contant.base_id = succeedString;
                            break;
                        case 2:
                            Contant.buildsite = buildsite;
                            Contant.buildNo = succeedString;
                            break;
                    }
                    Contant.isAddObject = true;
                    finish();
                }

            }
        };

    }

    private void processView(String[] names){

        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        if(names!=null&&names.length!=0){
            for(int i=0;i<names.length;i++){
                String name = names[i];
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth/4- DensityUtil.dip2px(this, 20),LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 20, 0, 0);
                TextView textView = new TextView(this);
                if(name.contains("面积")){
                    name = name+"(平方米)";
                }
                if(name.contains("所在层数")){
                    name = name+"(必选)";
                    textView.setTextColor(getResources().getColor(R.color.red));
                }
                if(name.contains("房屋坐落")||name.contains("坐落地址")){
                    name = name+"(必填)";
                    textView.setTextColor(getResources().getColor(R.color.red));
                }
                textView.setText(name);
                textView.setTextSize(20.0f);
                textView.setPadding(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
                linearLayout.addView(textView, layoutParams);

                LinearLayout.LayoutParams editlayoutParams = new LinearLayout.LayoutParams((screenWidth*3)/4-DensityUtil.dip2px(this, 20), LinearLayout.LayoutParams.WRAP_CONTENT);
                editlayoutParams.setMargins(20, 20, 20, 0);

                if(name.contains("街镇")){

                    Spinner spinnerStreet= new Spinner(this);
                    ArrayAdapter<StreetBean> arrayAdapterStreet = new ArrayAdapter<StreetBean>(AddGeneralObjectActivity.this,android.R.layout.simple_spinner_item,streets);
                    arrayAdapterStreet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStreet.setAdapter(arrayAdapterStreet);
                    views.add(spinnerStreet);
                    spinnerStreet.setPadding(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
                    linearLayout.addView(spinnerStreet, editlayoutParams);
                    if(streets.size()!=0){
                        streetId = streets.get(0).getSTREET_ID();
                    }
                    spinnerStreet.setSelection(1);
                    spinnerStreet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            StreetBean streetBean = streets.get(position);
                            streetId = streetBean.getSTREET_ID();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else if("房屋结构".equals(name)){

                    String[] structureIDs = new String[]{"100","101","102","103","104","105"};
                    String[] structureNames = getResources().getStringArray(R.array.structruename);

                     structureBeans = new ArrayList<>();
                    for(int j=0;j<structureIDs.length;j++){
                        StructureBean structureBean = new StructureBean();
                        structureBean.setId(structureIDs[j]);
                        structureBean.setName(structureNames[j]);
                        structureBeans.add(structureBean);
                        structureBean = null;
                    }


                    Spinner spinnerStructure= new Spinner(this);
                    ArrayAdapter<StructureBean> arrayAdapterStructure = new ArrayAdapter<StructureBean>(AddGeneralObjectActivity.this,android.R.layout.simple_spinner_item,structureBeans);
                    arrayAdapterStructure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStructure.setAdapter(arrayAdapterStructure);
                    views.add(spinnerStructure);
                    spinnerStructure.setPadding(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
                    linearLayout.addView(spinnerStructure, editlayoutParams);
                    structureId = structureBeans.get(0).getId();
                    spinnerStructure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            StructureBean structureBean = structureBeans.get(position);
                            structureId = structureBean.getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else{

                    if("竣工日期".equals(name)){
                        final TextView textValue = new TextView(this);

                        textValue.setTextSize(18);
                        views.add(textValue);
                        textValue.setBackground(getResources().getDrawable(R.drawable.layout_shape));
                        textValue.setPadding(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
                        linearLayout.addView(textValue, editlayoutParams);
                        textValue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initDialog(textValue);
                            }
                        });
                    }else if("所在层数(必选)".equals(name)){
                        listFloor = new ArrayList<>();
                        String[] floors = new String[]{"半地下","地下一层","地下二层","地下三层","地下四层","地下五层"};

                        LinearLayout lin_checkbox = new LinearLayout(this);
                        lin_checkbox.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        Params.setMargins(10, 20, 0, 0);
                        for(final String str:floors){
                            CheckBox checkBox = new CheckBox(this);
                            checkBox.setText(str);
                            lin_checkbox.addView(checkBox, Params);
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        listFloor.add(str);
                                    } else {
                                        listFloor.remove(str);
                                    }
                                }
                            });

                        }
                        views.add(lin_checkbox);
                        linearLayout.addView(lin_checkbox, editlayoutParams);
                    }else{

                        EditText textValue = new EditText(this);

                        if(name.contains("电话")||name.contains("手机")||name.contains("面积")){
                            textValue.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                        }

                        if("项目名称".equals(name)){
                            textValue.setText(Contant.proName);
                        }else{
                            textValue.setText("");
                        }

                        if (addIndex==1&&"房屋坐落(必填)".equals(name)) {
                            textValue.setHint("(必填)");
                        }else if(addIndex==2&&"坐落地址(必填)".equals(name)){
                            textValue.setHint("(必填)");
                        }


                        textValue.setTextSize(18);
                        views.add(textValue);
                        textValue.setBackground(getResources().getDrawable(R.drawable.layout_shape));
                        textValue.setPadding(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
                        linearLayout.addView(textValue, editlayoutParams);
                    }
                }

                lin_addobject.addView(linearLayout);
            }
        }

        TextView titleText = getCenterText();
        titleText.setFocusable(true);
        titleText.setFocusableInTouchMode(true);
        titleText.requestFocus();
    }


    private AlertDialog alertDialog;
    private DatePicker datePicker;

    private void initDialog(final TextView editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog= builder.create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.date_layout);
        datePicker = (DatePicker) alertDialog.findViewById(R.id.datepicker);
        alertDialog.setCanceledOnTouchOutside(true);
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        datePicker.setLayoutParams(new RelativeLayout.LayoutParams(width * 2 / 3, height / 2));
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        datePicker.setDate(year, month);
        datePicker.setMode(DPMode.SINGLE);
        datePicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                alertDialog.dismiss();
                editText.setText(date);
            }
        });
    }

    private String getMarkInfo(String[] newStrs){
        JSONObject jsonObject = new JSONObject();
        try {
            for(int i=0;i<names.length;i++){
                String key = keys[i];
                String value = newStrs[i];
                if("houseLocate".equals(key)){
                    houseLocate = value;
                }else if("currentLayer".equals(key)){
                    floorStr = value;
                }else if("BUILD_SITE".equals(key)){
                    buildsite = value;
                }
                jsonObject.put(key,value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_lefttext:
                finish();
                break;
            case R.id.title_righttext:
                if(names!=null&&names.length!=0) {
                    String[] newstrs = new String[names.length];
                    for (int i = 0; i < views.size(); i++) {
                        View view = views.get(i);
                        if (view instanceof EditText) {
                            String aaa = ((EditText) views.get(i)).getText().toString();
                                newstrs[i] = aaa;
                        } else if (view instanceof Spinner) {
                            if (names[i].contains("街镇")) {
                                newstrs[i] = streetId;
                            }else if(names[i].contains("房屋结构")){
                                newstrs[i] = structureId;
                            }
                        }else if(view instanceof TextView){
                            String aaa = ((TextView) views.get(i)).getText().toString();
                            newstrs[i] = aaa;
                        }else if(view instanceof LinearLayout){
                            StringBuilder builder = new StringBuilder();
                            if(listFloor!=null&&listFloor.size()!=0){
                                for(int j=0;j<listFloor.size();j++){
                                    if(j==0){
                                        builder.append(listFloor.get(j));
                                    }else{
                                        builder.append(","+listFloor.get(j));
                                    }
                                }
                                newstrs[i] = builder.toString();
                            }else{
                                newstrs[i] = "";
                            }
                        }
                    }
                    markInfo = getMarkInfo(newstrs);
                    if (addIndex==1&&"".equals(houseLocate)) {
                        ShowToast("请输入房屋坐落");
                    }else if(addIndex==1&&"".equals(floorStr)){
                        ShowToast("请选择所在层数");
                    }else if(addIndex==2&&"".equals(buildsite)){
                        ShowToast("请输入房屋坐落地址");
                    } else {
                        if (NetUtil.isNetworkAvailable(this)) {

                            if (progressDialog == null) {
                                progressDialog = showProgressDialog();
                            } else {
                                progressDialog.show();
                            }


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("loginName", Contant.userid);
                                    String result = "";
                                    Message msg = Message.obtain();
                                    if(addIndex==1){
                                        params.put("basementInfo", markInfo);
                                        result = RequestUtil.post(RequestUtil.AddCommonBasement, params);
                                        msg.what = 1;
                                    }else if(addIndex==2){
                                        params.put("buildingInfo", markInfo);
                                        result = RequestUtil.post(RequestUtil.AddBuildingBlocks, params);
                                        msg.what = 2;
                                    }
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);

                                }
                            }).start();
                        } else {
                            ShowToast(noNetText);
                        }
                    }
                }
                break;
        }
    }
}
