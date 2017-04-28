package com.example.admin.housemanage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adapter.HeaderGridAdapter;
import adapter.MyExpandableListAdapter;
import bean.CheckResultBean;
import bean.CheckTermBean;
import bean.CheckTermGroupBean;
import bean.MoudeBean;
import bean.TableHeadBean;
import constants.Contant;
import util.ActivityManage;
import util.DensityUtil;
import util.NetUtil;
import util.RequestUtil;
import widget.MyExpandableListView;
import widget.MyGridView;

/**
 * 表头，检查项选择界面
 * Created by admin on 2016/4/12.
 */
public class TableHeaderActivity extends BaseActivity {

    private Handler mHandler;
    private Gson gson;
    private MyGridView primeGridView;
    private MyGridView propertyGridView;
    private ProgressDialog progressDialog;
    private RadioGroup radioGroup;
    private  RelativeLayout rel_content;
    private LinearLayout lin_prime;
    private LinearLayout lin_property;
    private LinearLayout lin_general;
    private LinearLayout lin_houseSafe;

    private MyExpandableListView primeExpandListView;
    private MyExpandableListView propertyExpandListView;
    private MyExpandableListView generalExpandListView;
    private MyExpandableListView houseSafeExpandListView;

    private MyExpandableListAdapter primeExpandableListAdapter;
    private MyExpandableListAdapter propertyExpandableListAdapter;
    private MyExpandableListAdapter generalExpandableListAdapter;
    private MyExpandableListAdapter houseSafeExpandableListAdapter;


    private Spinner sp_mode;


    private List<MoudeBean> moudeBeans;


    private Button bt_next;


    private Dialog dialog;

    private LinearLayout lin_content;
    private LinearLayout lin_head_content;
    private LinearLayout lin_radioline;

    private EditText edit_name;
    private EditText edit_description;
    private TextView text_save;
    private TextView text_cancel;
    private RadioGroup rg_moudesave;
    private RadioButton rb_yes;
    private RadioButton rb_no;
    private int isShare = 0;
    private List<RadioButton> radioButtons;
    private List<TextView> radioLines;

    private ArrayAdapter<MoudeBean> spinnerAdapter;

    private List<CheckTermGroupBean> checkTermGroupBeans;
    private List<CheckTermGroupBean> checkTermGroupBeans1;
    private List<CheckTermGroupBean> checkTermGroupBeans2;
    private List<CheckTermGroupBean> checkTermGroupBeans3;

    private boolean isFrist;
    @Override
    protected void initView() {
        setTitle("检查项选择");
        setRightText("保存模板");
        setContentLayout(R.layout.tableheader_activity);
        ActivityManage.getInstance().addActivity(this);

        radioGroup= (RadioGroup) findViewById(R.id.rg_header_type);
        lin_content = (LinearLayout) findViewById(R.id.lin_content);
        lin_head_content = (LinearLayout) findViewById(R.id.lin_header_content);
        lin_radioline = (LinearLayout) findViewById(R.id.lin_radioline);
        bt_next = (Button) findViewById(R.id.bt_tablehead_next);
        sp_mode = (Spinner) findViewById(R.id.sp_tablehead_mode);
        bt_next.setOnClickListener(this);


        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenWidth = metrics.widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        LinearLayout lin_head = new LinearLayout(this);
        lin_head.setOrientation(LinearLayout.VERTICAL);

        if(Contant.enforceList.size()==1&&Contant.enforceList.contains(getResources().getString(R.string.primebroker))){
            // 表头选择
            TextView textView = new TextView(this);
            textView.setText("表头选择");
            textView.setTextSize(22.0f);
            textView.setPadding(5, 5, 5, 5);
            textView.setTextColor(getResources().getColor(R.color.titleback));
            lin_head.addView(textView);

            primeGridView = new MyGridView(this);
            primeGridView.setNumColumns(2);
            lin_head.addView(primeGridView, layoutParams);
        }else{
            TextView textView = new TextView(this);
            textView.setText("表头选择");
            textView.setTextSize(22.0f);
            textView.setPadding(5, 5, 5, 5);
            textView.setTextColor(getResources().getColor(R.color.titleback));
            lin_head.addView(textView);

            propertyGridView = new MyGridView(this);
            propertyGridView.setNumColumns(2);
            lin_head.addView(propertyGridView, layoutParams);
        }

        lin_head_content.addView(lin_head);
        if(Contant.enforceList.size()==1){
            radioGroup.setVisibility(View.GONE);
        }else{
            radioGroup.setVisibility(View.VISIBLE);
        }

        rel_content = new RelativeLayout(this);

        radioButtons = new ArrayList<>();
        radioLines = new ArrayList<>();


        for(int i=0;i< Contant.enforceList.size();i++){

            String enforce = Contant.enforceList.get(i);
            //radioButton的动态添加
            RadioGroup.LayoutParams lineParams = new RadioGroup.LayoutParams(DensityUtil.dip2px(this, 1),RadioGroup.LayoutParams.MATCH_PARENT);
            TextView text_line = new TextView(this);
            text_line.setBackgroundColor(getResources().getColor(R.color.graymy));
            lineParams.setMargins(0, 5, 0, 5);
            RadioGroup.LayoutParams rb_layoutParams = new RadioGroup.LayoutParams(screenWidth/Contant.enforceList.size(), RadioGroup.LayoutParams.MATCH_PARENT);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(enforce);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setButtonDrawable(new ColorDrawable());
            radioButton.setTextColor(getResources().getColor(R.color.black));
            radioButton.setTextSize(20.0f);
            radioButton.setBackground(getResources().getDrawable(R.drawable.inputradiobutton_selector));
            radioButtons.add(radioButton);

            TextView textView = new TextView(this);
            if(i==0){
                textView.setBackgroundColor(getResources().getColor(R.color.titleback));
            }else{
                textView.setBackgroundColor(getResources().getColor(R.color.lightgray));
            }
            LinearLayout.LayoutParams radiolines = new LinearLayout.LayoutParams(screenWidth/Contant.enforceList.size(), DensityUtil.px2dip(this,2));
            radioLines.add(textView);
            lin_radioline.addView(textView,radiolines);

            radioGroup.addView(radioButton,rb_layoutParams);
            if(i==0){
                radioGroup.check(radioButton.getId());
            }

            LinearLayout.LayoutParams linelayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.px2dip(this,2));
            //表头和选择大项内容的动态添加
            LinearLayout.LayoutParams moudeParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if(enforce.equals(getResources().getString(R.string.primebroker))){
                lin_prime = new LinearLayout(this);
                lin_prime.setOrientation(LinearLayout.VERTICAL);
                if(i==0){
                    lin_prime.setVisibility(View.VISIBLE);
                }else{
                    lin_prime.setVisibility(View.GONE);
                }

                LinearLayout lin_termcontent = new LinearLayout(this);
                lin_termcontent.setOrientation(LinearLayout.VERTICAL);
                lin_termcontent.setBackgroundColor(getResources().getColor(R.color.white));
                lin_termcontent.setPadding(0,10,0,10);

//                LinearLayout lin_moude = new LinearLayout(this);
//                lin_moude.setOrientation(LinearLayout.HORIZONTAL);
//                TextView modeltext = new TextView(this);
//                modeltext.setText(getResources().getString(R.string.moudechoose));
//                modeltext.setTextSize(20.0f);
//                modeltext.setPadding(5, 5, 5, 5);
//                modeltext.setTextColor(getResources().getColor(R.color.black));
//                lin_moude.addView(modeltext);
//
//                sp_moude_prime = new Spinner(this);
//                sp_moude_prime.setPadding(5, 5, 5, 5);
//                lin_moude.addView(sp_moude_prime, moudeParams);

//                lin_termcontent.addView(lin_moude);


//                TextView hHineText = new TextView(this);
//                hHineText.setBackgroundColor(getResources().getColor(R.color.black));
//                linelayoutParams.setMargins(0, 10, 0, 0);
//                lin_termcontent.addView(hHineText, linelayoutParams);
                //大项的选择
                TextView textTerm = new TextView(this);
                textTerm.setText("检查项");
                textTerm.setTextSize(22.0f);
                textTerm.setPadding(5, 5, 5, 5);
                textTerm.setTextColor(getResources().getColor(R.color.titleback));
                lin_termcontent.addView(textTerm);


                primeExpandListView = new MyExpandableListView(this);
                primeExpandListView.setCacheColorHint(getResources().getColor(R.color.titleback));
                lin_termcontent.addView(primeExpandListView);
                lin_prime.addView(lin_termcontent);

//                buttonSavePrime = new Button(this);
//                buttonSavePrime.setText("保存为模板");
//                buttonSavePrime.setBackgroundColor(getResources().getColor(R.color.titleback));
//                buttonSavePrime.setPadding(DensityUtil.dip2px(this, 20), DensityUtil.dip2px(this, 20), DensityUtil.dip2px(this, 20), DensityUtil.dip2px(this, 20));
//                buttonSavePrime.setGravity(Gravity.CENTER_HORIZONTAL);
//                marginParams.setMargins(0,DensityUtil.dip2px(this, 20),0,0);
//                lin_prime.addView(buttonSavePrime,marginParams);

                rel_content.addView(lin_prime);
//                buttonSavePrime.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        savetoMoude(1);
//                    }
//                });
            }else if(enforce.equals(getResources().getString(R.string.propertymanage))){

                lin_property = new LinearLayout(this);
                lin_property.setOrientation(LinearLayout.VERTICAL);
                if(i==0){
                    lin_property.setVisibility(View.VISIBLE);
                }else{
                    lin_property.setVisibility(View.GONE);
                }

                LinearLayout lin_termcontent = new LinearLayout(this);
                lin_termcontent.setOrientation(LinearLayout.VERTICAL);
                lin_termcontent.setBackgroundColor(getResources().getColor(R.color.white));
                lin_termcontent.setPadding(0,10,0,10);


                TextView textTerm = new TextView(this);
                textTerm.setText("检查项");
                textTerm.setTextSize(22.0f);
                textTerm.setPadding(5, 5, 5, 5);
                textTerm.setTextColor(getResources().getColor(R.color.titleback));
                lin_termcontent.addView(textTerm);

                propertyExpandListView = new MyExpandableListView(this);
                propertyExpandListView.setCacheColorHint(getResources().getColor(R.color.titleback));
                lin_termcontent.addView(propertyExpandListView);
                lin_property.addView(lin_termcontent);


                rel_content.addView(lin_property);
            }else if(enforce.equals(getResources().getString(R.string.generalbasement))){

                lin_general = new LinearLayout(this);
                lin_general.setOrientation(LinearLayout.VERTICAL);
                if(i==0){
                    lin_general.setVisibility(View.VISIBLE);
                }else{
                    lin_general.setVisibility(View.GONE);
                }

                LinearLayout lin_termcontent = new LinearLayout(this);
                lin_termcontent.setOrientation(LinearLayout.VERTICAL);
                lin_termcontent.setBackgroundColor(getResources().getColor(R.color.white));
                lin_termcontent.setPadding(0, 10, 0, 10);


                TextView textTerm = new TextView(this);
                textTerm.setText("检查项");
                textTerm.setTextSize(22.0f);
                textTerm.setPadding(5, 5, 5, 5);
                textTerm.setTextColor(getResources().getColor(R.color.titleback));
                lin_termcontent.addView(textTerm);

                generalExpandListView = new MyExpandableListView(this);
                generalExpandListView.setCacheColorHint(getResources().getColor(R.color.titleback));
                lin_termcontent.addView(generalExpandListView);
                lin_general.addView(lin_termcontent);



                rel_content.addView(lin_general);
            }else if(enforce.equals(getResources().getString(R.string.housesafeuse))){
                lin_houseSafe = new LinearLayout(this);
                lin_houseSafe.setOrientation(LinearLayout.VERTICAL);
                if(i==0){
                    lin_houseSafe.setVisibility(View.VISIBLE);
                }else{
                    lin_houseSafe.setVisibility(View.GONE);
                }

                LinearLayout lin_termcontent = new LinearLayout(this);
                lin_termcontent.setOrientation(LinearLayout.VERTICAL);
                lin_termcontent.setBackgroundColor(getResources().getColor(R.color.white));
                lin_termcontent.setPadding(0,10,0,10);


                TextView textTerm = new TextView(this);
                textTerm.setText("检查项");
                textTerm.setTextSize(22.0f);
                textTerm.setPadding(5, 5, 5, 5);
                textTerm.setTextColor(getResources().getColor(R.color.titleback));
                lin_termcontent.addView(textTerm);

                houseSafeExpandListView = new MyExpandableListView(this);
                houseSafeExpandListView.setCacheColorHint(getResources().getColor(R.color.titleback));
                lin_termcontent.addView(houseSafeExpandListView);
                lin_houseSafe.addView(lin_termcontent);

                rel_content.addView(lin_houseSafe);
            }
        }
        lin_content.addView(rel_content);
    }



    @Override
    protected void initData() {
        gson = new Gson();
        mHandler = new Handler(){
            @Override
            public void handleMessage( Message msg) {
                super.handleMessage(msg);
                List<TableHeadBean> tableHeadBeans =  new ArrayList<>();
                List<CheckTermGroupBean> termGroupBeans = new ArrayList<>();
//
                int moudePosition = 0;
                if(msg.what==1||msg.what==2||msg.what==3||msg.what==4){
                    if(msg.arg1==1){
                        Bundle bundle = msg.getData();
                        String termResult = bundle.getString("term");
                        termGroupBeans = getTerms(termResult,1);
                    }else if(msg.arg1==2){
                        termGroupBeans = (List<CheckTermGroupBean>) msg.obj;
                    }

                }
                if("".equals(msg.obj)){
                    ShowToast("服务器连接异常,请返回上一界面重试");
                    if(progressDialog!=null&&progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }else if("[]".equals(msg.obj)){
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                }else {

                    switch (msg.what) {
                        case 1:
                            checkTermGroupBeans = new ArrayList<>();
                            checkTermGroupBeans.addAll(termGroupBeans);
                            primeExpandableListAdapter = new MyExpandableListAdapter(checkTermGroupBeans, TableHeaderActivity.this);
                            primeExpandableListAdapter.notifyDataSetChanged();
                            primeExpandListView.setAdapter(primeExpandableListAdapter);
                            break;
                        case 2:
                            checkTermGroupBeans1= new ArrayList<>();
                            checkTermGroupBeans1.addAll(termGroupBeans);
                            propertyExpandableListAdapter = new MyExpandableListAdapter(checkTermGroupBeans1, TableHeaderActivity.this);
                            propertyExpandListView.setAdapter(propertyExpandableListAdapter);
                            break;
                        case 3:
                            checkTermGroupBeans2= new ArrayList<>();
                            checkTermGroupBeans2.addAll(termGroupBeans);
                            generalExpandableListAdapter = new MyExpandableListAdapter(checkTermGroupBeans2, TableHeaderActivity.this);
                            generalExpandableListAdapter.notifyDataSetChanged();
                            generalExpandListView.setAdapter(generalExpandableListAdapter);
                            break;
                        case 4:

                            checkTermGroupBeans3 = new ArrayList<>();
                            checkTermGroupBeans3.addAll(termGroupBeans);
                            houseSafeExpandableListAdapter = new MyExpandableListAdapter(checkTermGroupBeans3, TableHeaderActivity.this);
                            houseSafeExpandListView.setAdapter(houseSafeExpandableListAdapter);
                            break;
                        case 5:
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            break;
                        case 6:
                            progressDialog.dismiss();
                            String moudeResult = (String) msg.obj;
                            if (moudeResult != null && !moudeResult.equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(moudeResult);
                                    JSONArray itemArray = jsonObject.getJSONArray("items");
                                    List<CheckTermBean> termBeans = gson.fromJson(itemArray.toString(), new TypeToken<List<CheckTermBean>>() {
                                    }.getType());
                                    if (termBeans != null) {
                                        Map<String, List<CheckTermGroupBean>> stringListMap = getMapTerm(termBeans);
                                        Iterator<String> typeKeys = stringListMap.keySet().iterator();
                                        while (typeKeys.hasNext()) {
                                            String bussiness = typeKeys.next();
                                            List<CheckTermGroupBean> groupBeans = stringListMap.get(bussiness);
                                            Message message = Message.obtain();
                                            switch (bussiness) {
                                                case "经纪机构":
                                                    message.what = 1;
                                                    break;
                                                case "物业管理":
                                                    message.what = 2;
                                                    break;
                                                case "普通地下室":
                                                    message.what = 3;
                                                    break;
                                                case "房屋安全":
                                                    message.what = 4;
                                                    break;
                                            }
                                            message.arg1 = 2;
                                            message.obj = groupBeans;
                                            mHandler.sendMessage(message);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case 7:
                            progressDialog.dismiss();
                            String result = (String) msg.obj;
                            Bundle bundle = msg.getData();
                            String name = bundle.getString("name");
                            if (result != null && !result.equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String str = jsonObject.optString("result");
                                    String reason = jsonObject.optString("failReason");
                                    String modeGuid = jsonObject.optString("succeedString");
                                    if (str.equals("1")) {
                                        Toast.makeText(TableHeaderActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                                        MoudeBean moudeBean = new MoudeBean();
                                        moudeBean.setGuid(modeGuid);
                                        moudeBean.setName(name);
                                        if (moudeBeans == null) {
                                            moudeBeans = new ArrayList<>();

                                            MoudeBean moudeBean1 = new MoudeBean();
                                            moudeBean1.setName("无");
                                            moudeBeans.add(moudeBean1);
                                            isFrist = false;
                                            spinnerAdapter = new ArrayAdapter<MoudeBean>(TableHeaderActivity.this, android.R.layout.simple_spinner_item, moudeBeans);
                                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            sp_mode.setAdapter(spinnerAdapter);
                                            sp_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    if (isFrist) {
                                                        MoudeBean moudeBean = moudeBeans.get(position);
                                                        if (moudeBean.getName().equals("无")) {
                                                            initializeData();
                                                        } else {
                                                            String moudeGuid = moudeBean.getGuid();
                                                            requestMoudeInfo(moudeGuid, position);
                                                        }
                                                    }
                                                    isFrist = true;
                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                }
                                            });
                                            sp_mode.setSelection(moudePosition, true);
                                        }else{
                                            moudeBeans.add(moudeBean);
                                            spinnerAdapter.notifyDataSetChanged();
                                        }
                                    } else if (str.equals("0")) {
                                        Toast.makeText(TableHeaderActivity.this, "保存失败-" + reason, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case 8:
                            String headResult = (String) msg.obj;
                            tableHeadBeans.addAll((List<TableHeadBean>) (gson.fromJson(headResult, new TypeToken<List<TableHeadBean>>() {
                            }.getType())));
                            Contant.primeHeadList.clear();

                            HeaderGridAdapter primeheaderAdapter = new HeaderGridAdapter(tableHeadBeans, TableHeaderActivity.this, getResources().getString(R.string.primebroker));
                            primeGridView.setAdapter(primeheaderAdapter);

                            break;
                        case 9:
                            String headResultpro = (String) msg.obj;
                            tableHeadBeans.addAll((List<TableHeadBean>) (gson.fromJson(headResultpro, new TypeToken<List<TableHeadBean>>() {
                            }.getType())));
                            Contant.propertyHeadList.clear();
                            HeaderGridAdapter proHeaderAdapter = new HeaderGridAdapter(tableHeadBeans, TableHeaderActivity.this, getResources().getString(R.string.propertymanage));
                            propertyGridView.setAdapter(proHeaderAdapter);
                            break;
                        case 10:
                            String moudelist = (String) msg.obj;
                            if (moudeBeans == null) {
                                moudeBeans = new ArrayList<>();
                            } else {
                                moudeBeans.clear();
                            }
                            if (msg.arg1 == 1) {
                                MoudeBean moudeBean = new MoudeBean();
                                moudeBean.setName("无");
                                moudeBeans.add(moudeBean);
                            }
                            moudeBeans.addAll((List<MoudeBean>) (gson.fromJson(moudelist, new TypeToken<List<MoudeBean>>() {
                            }.getType())));
                            isFrist = false;
                            spinnerAdapter = new ArrayAdapter<MoudeBean>(TableHeaderActivity.this, android.R.layout.simple_spinner_item, moudeBeans);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_mode.setAdapter(spinnerAdapter);
                            sp_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (isFrist) {
                                        MoudeBean moudeBean = moudeBeans.get(position);
                                        if (moudeBean.getName().equals("无")) {
                                            initializeData();
                                        } else {
                                            String moudeGuid = moudeBean.getGuid();
                                            requestMoudeInfo(moudeGuid, position);
                                        }
                                    }
                                    isFrist = true;
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            sp_mode.setSelection(moudePosition, true);
                            break;
                    }
                }

            }
        };


        initializeData();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = ((RadioButton) radioGroup.findViewById(checkedId));
                String str = radioButton.getText().toString();
                int index = radioButtons.indexOf(radioButton);
                for (int i = 0; i < radioButtons.size(); i++) {
                    RadioButton button = radioButtons.get(i);
                    button.setTextColor(TableHeaderActivity.this.getResources().getColor(R.color.black));
                    if (i == index) {
                        radioLines.get(i).setBackgroundColor(getResources().getColor(R.color.titleback));
                    } else {
                        radioLines.get(i).setBackgroundColor(getResources().getColor(R.color.lightgray));
                    }
                }
                radioButton.setTextColor(TableHeaderActivity.this.getResources().getColor(R.color.titleback));
                if (str.equals(getResources().getString(R.string.primebroker))) {
                    setVisibile(lin_prime, View.VISIBLE);
                    setVisibile(lin_property, View.GONE);
                    setVisibile(lin_general, View.GONE);
                    setVisibile(lin_houseSafe, View.GONE);
                } else if (str.equals(getResources().getString(R.string.propertymanage))) {
                    setVisibile(lin_prime, View.GONE);
                    setVisibile(lin_property, View.VISIBLE);
                    setVisibile(lin_general, View.GONE);
                    setVisibile(lin_houseSafe, View.GONE);
                } else if (str.equals(getResources().getString(R.string.generalbasement))) {
                    setVisibile(lin_prime, View.GONE);
                    setVisibile(lin_property, View.GONE);
                    setVisibile(lin_general, View.VISIBLE);
                    setVisibile(lin_houseSafe, View.GONE);
                } else if (str.equals(getResources().getString(R.string.housesafeuse))) {
                    setVisibile(lin_prime, View.GONE);
                    setVisibile(lin_property, View.GONE);
                    setVisibile(lin_general, View.GONE);
                    setVisibile(lin_houseSafe, View.VISIBLE);
                }
            }
        });
    }

    /**
     * 获取表头，检查项，模板的信息
     */
    private void initializeData(){
        if(NetUtil.isNetworkAvailable(this)) {
            if (progressDialog == null) {
                progressDialog = showProgressDialog();
            } else {
                progressDialog.show();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = "";
                    if(Contant.enforceList.size()==1&&Contant.enforceList.contains(getResources().getString(R.string.primebroker))){
                        Map<String, String> prarm = new HashMap<>();
                        prarm.put("bttype", Contant.enforceList.get(0));
                        result = RequestUtil.post(RequestUtil.GetBT, prarm);
                        Message msg = Message.obtain();
                        msg.what=8;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }else{
                        Map<String, String> prarm = new HashMap<>();
                        prarm.put("bttype", getResources().getString(R.string.propertymanage));
                        result = RequestUtil.post(RequestUtil.GetBT, prarm);
                        Message msg = Message.obtain();
                        msg.what=9;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }

                    StringBuilder builder = new StringBuilder();
                    for(int i=0;i<Contant.enforceList.size();i++){
                        if(i==0){
                            builder.append(Contant.enforceList.get(i));
                        }else{
                            builder.append(","+Contant.enforceList.get(i));
                        }
                    }
                    Map<String, String> prarmMoude = new HashMap<>();
                    prarmMoude.put("name", Contant.userid);
                    prarmMoude.put("bussinessType", builder.toString());
                    String resultMoude = RequestUtil.post(RequestUtil.GetMBLB, prarmMoude);

                    Message modeMsg = Message.obtain();
                    modeMsg.what=10;
                    modeMsg.arg1=1;
                    modeMsg.obj = resultMoude;
                    mHandler.sendMessage(modeMsg);

                    for (int i = 0; i < Contant.enforceList.size(); i++) {
                        String enforce = Contant.enforceList.get(i);
                        Map<String, String> prarmJCX = new HashMap<>();
                        prarmJCX.put("jjxBussinesstype", Contant.enforceList.get(i));
                        String resultJCX = RequestUtil.post(RequestUtil.GetJCX, prarmJCX);
                        Message msg = Message.obtain();
                        if (enforce.equals(getResources().getString(R.string.primebroker))) {
                            msg.what = 1;
                        } else if (enforce.equals(getResources().getString(R.string.propertymanage))) {
                            msg.what = 2;
                        } else if (enforce.equals(getResources().getString(R.string.generalbasement))) {
                            msg.what = 3;
                        } else if (enforce.equals(getResources().getString(R.string.housesafeuse))) {
                            msg.what = 4;
                        }
                        msg.arg1 = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("term", resultJCX);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                        if (i == Contant.enforceList.size() - 1) {
                            Message message = Message.obtain();
                            message.what = 5;
                            mHandler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }else{
            ShowToast(noNetText);
        }
    }


    private Map<String,List<CheckTermGroupBean>> getMapTerm(List<CheckTermBean> termBeans){
        Map<String,List<CheckTermGroupBean>> mapGroups = new HashMap<>();
        Map<String,List<CheckTermBean>> mapTerms = new HashMap<>();
        for(CheckTermBean termBean:termBeans){
            String bussinessType = termBean.getBussinesstype();
            if(!mapTerms.containsKey(bussinessType)){
                List<CheckTermBean> checkTermBeans = new ArrayList<>();
                checkTermBeans.add(termBean);
                mapTerms.put(bussinessType, checkTermBeans);
            }else{
                mapTerms.get(bussinessType).add(termBean);
            }
        }
        Iterator<String> typeIterator = mapTerms.keySet().iterator();
        while(typeIterator.hasNext()) {
            String bussinessType = typeIterator.next();
            List<CheckTermGroupBean> termGroupBeans = new ArrayList<>();
            List<CheckTermBean> checkTermBeans = mapTerms.get(bussinessType);
            Map<String, List<CheckTermBean>> classMaps = new HashMap<>();
            for (CheckTermBean bean : checkTermBeans) {
                String inspectClass = bean.getInspectClass();
                if (classMaps.containsKey(inspectClass)) {
                    classMaps.get(inspectClass).add(bean);
                } else {
                    List<CheckTermBean> beans = new ArrayList<>();
                    beans.add(bean);
                    classMaps.put(inspectClass, beans);
                }
            }
            Iterator<String> stringIterator = classMaps.keySet().iterator();
            while (stringIterator.hasNext()) {
                String name = stringIterator.next();
                CheckTermGroupBean checkTermGroupBean = new CheckTermGroupBean();
                checkTermGroupBean.setGroupName(name);
                checkTermGroupBean.setCheckTermBeans(classMaps.get(name));
                termGroupBeans.add(checkTermGroupBean);
            }
            mapGroups.put(bussinessType,termGroupBeans);
        }
        return mapGroups;
    }

    /**
     * 保存模板的dialog
     */
    private void savetoMoude(){

        if(dialog==null){
            dialog = new Dialog(this);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            alertDialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
            dialog.show();
            dialog.setContentView(R.layout.moudesave_dialog);
            edit_name = (EditText) dialog.findViewById(R.id.edit_moudesave_name);
            edit_description = (EditText) dialog.findViewById(R.id.edit_moudesave_description);
            text_save = (TextView) dialog.findViewById(R.id.text_moudesave_sure);
            text_cancel = (TextView) dialog.findViewById(R.id.text_moudesave_cancel);
            rg_moudesave = (RadioGroup) dialog.findViewById(R.id.rg_moudesave);
            rb_yes = (RadioButton) dialog.findViewById(R.id.rb_moudesave_yes);
            rb_no = (RadioButton) dialog.findViewById(R.id.rb_moudesave_no);
        }else{
            dialog.show();
        }
        rg_moudesave.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = rg_moudesave.getCheckedRadioButtonId();
                if (id == rb_yes.getId()) {
                    isShare = 1;
                } else if (id == rb_no.getId()) {
                    isShare = 0;
                }
            }
        });
        text_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_name.getText().toString();
                String description = edit_description.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(TableHeaderActivity.this, "请输入模板名称", Toast.LENGTH_LONG).show();
                } else {
                    if (NetUtil.isNetworkAvailable(TableHeaderActivity.this)) {
                        requestSaveMoude(name, description);
                        dialog.dismiss();
                    } else {
                        ShowToast(noNetText);
                    }
                }
            }
        });
        text_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 请求保存模板
     * @param name
     * @param description
     */
    private void requestSaveMoude(final String name, final String description){

        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {


                Map<String,Object> params = new HashMap<>();
                params.put("loginName",Contant.userid);
                params.put("templeteName", name);
                params.put("description", description);
                params.put("isShared", isShare);
                StringBuilder builder = new StringBuilder();
                for(int i=0;i<Contant.enforceList.size();i++){
                    if(i==0){
                        builder.append(Contant.enforceList.get(i));
                    }else{
                        builder.append(","+Contant.enforceList.get(i));
                    }
                }
                params.put("bussinessType", builder.toString());
                params.put("headGuidList", "");
                StringBuilder sb = new StringBuilder();
                for(int j=0;j<Contant.enforceList.size();j++) {
                    switch (Contant.enforceList.get(j)) {
                        case "经纪机构":
//                            StringBuilder stringBuilder = new StringBuilder();
//                            for (int i = 0; i < Contant.primeHeadList.size(); i++) {
//                                if (i == 0) {
//                                    stringBuilder.append(Contant.primeHeadList.get(i).getGuid());
//                                } else {
//                                    stringBuilder.append("," + Contant.primeHeadList.get(i).getGuid());
//                                }
//                            }

                            List<CheckTermGroupBean> checkTermGroupBeans = new ArrayList<>();
                            checkTermGroupBeans.addAll(primeExpandableListAdapter.getCheckTermGroupBeans());
                            List<CheckTermBean> termBeans = getItemTermList(checkTermGroupBeans);
                            if (termBeans != null && termBeans.size() != 0) {
                                for (int i = 0; i < termBeans.size(); i++) {
                                    if (i == 0) {
                                        sb.append(termBeans.get(i).getGuid());
                                    } else {
                                        sb.append("," + termBeans.get(i).getGuid());

                                    }
                                }
                            }

                            break;
                        case "物业管理":
                            List<CheckTermGroupBean> checkTermGroupBeans2 = new ArrayList<>();
                            checkTermGroupBeans2.addAll(propertyExpandableListAdapter.getCheckTermGroupBeans());
                            List<CheckTermBean> termBeans2 = getItemTermList(checkTermGroupBeans2);
                            if (termBeans2 != null && termBeans2.size() != 0) {
                                for (int i = 0; i < termBeans2.size(); i++) {
                                    if (i == 0) {
                                        sb.append(termBeans2.get(i).getGuid());
                                    } else {
                                        sb.append("," + termBeans2.get(i).getGuid());

                                    }
                                }
                            }
                            break;
                        case "普通地下室":
                            List<CheckTermGroupBean> checkTermGroupBeans3 = new ArrayList<>();
                            checkTermGroupBeans3.addAll(generalExpandableListAdapter.getCheckTermGroupBeans());
                            List<CheckTermBean> termBeans3 = getItemTermList(checkTermGroupBeans3);
                            if (termBeans3 != null && termBeans3.size() != 0) {
                                for (int i = 0; i < termBeans3.size(); i++) {
                                    if (i == 0) {
                                        sb.append(termBeans3.get(i).getGuid());
                                    } else {
                                        sb.append("," + termBeans3.get(i).getGuid());

                                    }
                                }
                            }
                            break;
                        case "房屋安全":
                            List<CheckTermGroupBean> checkTermGroupBeans4 = new ArrayList<>();
                            checkTermGroupBeans4.addAll(houseSafeExpandableListAdapter.getCheckTermGroupBeans());
                            List<CheckTermBean> termBeans4 = getItemTermList(checkTermGroupBeans4);
                            if (termBeans4 != null && termBeans4.size() != 0) {
                                for (int i = 0; i < termBeans4.size(); i++) {
                                    if (i == 0) {
                                        sb.append(termBeans4.get(i).getGuid());
                                    } else {
                                        sb.append("," + termBeans4.get(i).getGuid());

                                    }
                                }
                            }
                            break;
                    }
                }
                params.put("itemGuidList", sb.toString());
                String moudeSaveResult = RequestUtil.postob(RequestUtil.SaveNewTempleteHeadAndItem,params);
                Message msg = new Message();
                msg.what = 7;
                msg.obj =moudeSaveResult;
                Bundle bundle = new Bundle();
                bundle.putString("result",moudeSaveResult);
                bundle.putString("name",name);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 获取被选中的检查项
     * @param checkTermGroupBeans
     * @return
     */
    private List<CheckTermBean> getItemTermList(List<CheckTermGroupBean> checkTermGroupBeans){
        List<CheckTermBean> newTermBeans = new ArrayList<>();
        for(int j=0;j<checkTermGroupBeans.size();j++) {

            CheckTermGroupBean checkTermGroupBean = checkTermGroupBeans.get(j);
            List<CheckTermBean> termBeans = checkTermGroupBean.getCheckTermBeans();

            for (CheckTermBean bean : termBeans) {
                if (bean.isCheck()) {
                    newTermBeans.add(bean);
                }
            }
        }
        return newTermBeans;
    }


    /**
     * 请求moude的详细信息
     * @param id
     * @param position
     */
    private void requestMoudeInfo(String id, final int position){
        progressDialog.show();
        final String moudeId =id;
        final int modeposition = position;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> params = new HashMap<>();
                params.put("templeteGuid", moudeId);
                String moudeResult = RequestUtil.post(RequestUtil.GetJCTJCX, params);
                Message msg = Message.obtain();
                msg.what = 6;
                msg.obj = moudeResult;
                msg.arg2 = modeposition;
                mHandler.sendMessage(msg);
            }
        }).start();
    }


    /**
     * 对请求到的检查项进行解析
     * @param result
     * @param index
     * @return
     */
    private List<CheckTermGroupBean> getTerms(String result,int index){
        List<CheckTermGroupBean> checkTermGroupBeans = new ArrayList<>();
        List<String> groups = new ArrayList<>();
        List<CheckTermBean> termBeans = null;
        String items = "";
        try{
            if(index==1){
                JSONObject jsonObject = new JSONObject(result);
                JSONArray array = jsonObject.getJSONArray("inspectClasses");
                for(int i=0;i<array.length();i++){
                    groups.add(array.get(i).toString());
                }
                items = jsonObject.getJSONArray("items").toString();
                termBeans = gson.fromJson(items, new TypeToken<List<CheckTermBean>>() {
                }.getType());
            }else if(index==2){
                items = result;
                termBeans = gson.fromJson(items,new TypeToken<List<CheckTermBean>>(){}.getType());
                for(int i=0;i<termBeans.size();i++){
                    CheckTermBean bean = termBeans.get(i);
                    String inpectClass = bean.getInspectClass();
                    boolean ishas = false;
                    if(groups.size()!=0){
                        for(String str:groups){
                            if(str.equals(inpectClass)){
                                ishas = true;
                            }
                        }
                    }
                    if(!ishas){
                        groups.add(inpectClass);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(termBeans!=null&&termBeans.size()!=0){
            for(String group:groups){
                CheckTermGroupBean checkTermGroupBean = new CheckTermGroupBean();
                checkTermGroupBean.setGroupName(group);
                List<CheckTermBean> termList = new ArrayList<>();
                for(int i=0;i<termBeans.size();i++){
                    if(termBeans.get(i).getInspectClass().equals(group)){
                        termBeans.get(i).setIsCheck(false);
                        termList.add(termBeans.get(i));
                    }
                }
                checkTermGroupBean.setCheckTermBeans(termList);
                checkTermGroupBean.setChecked(false);
//                checkTermGroupBean.setIsExpand(false);
                checkTermGroupBeans.add(checkTermGroupBean);
            }
        }
        return checkTermGroupBeans;
    }

    private void setVisibile(View view,int visiblity){
        if(view!=null){
            view.setVisibility(visiblity);
        }
    }

    /**
     * 获取被选中的检查项，作为参数传递到commitActivity
     * @param checkTermGroupBeans
     * @return
     */
    private List<CheckTermGroupBean> getNewList(List<CheckTermGroupBean> checkTermGroupBeans){
        List<CheckTermGroupBean> newTermGroupBeans = new ArrayList<>();
        for(int j=0;j<checkTermGroupBeans.size();j++){

            CheckTermGroupBean checkTermGroupBean = checkTermGroupBeans.get(j);
            List<CheckTermBean> termBeans = checkTermGroupBean.getCheckTermBeans();

            CheckTermGroupBean newCheckTermGroupBean = new CheckTermGroupBean();
            List<CheckTermBean> newTermBeans = new ArrayList<>();

            for(CheckTermBean bean:termBeans){
                if(bean.isCheck()){
                    newTermBeans.add(bean);
                }
            }
            newCheckTermGroupBean.setGroupName(checkTermGroupBean.getGroupName());
            newCheckTermGroupBean.setCheckTermBeans(newTermBeans);
            if(newCheckTermGroupBean.getCheckTermBeans().size()!=0){
                newTermGroupBeans.add(newCheckTermGroupBean);
            }
        }

        if(newTermGroupBeans.size()==0){
            newTermGroupBeans.addAll(checkTermGroupBeans);
        }
        return newTermGroupBeans;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_lefttext:
                finish();
                break;
            case R.id.title_righttext:
                savetoMoude();
                break;
            case R.id.bt_tablehead_next:
                boolean isPass = true;
                if(Contant.enforceList==null){
                    isPass = false;
                }else if(Contant.enforceList.contains(getResources().getString(R.string.primebroker))){
                    if(checkTermGroupBeans==null||checkTermGroupBeans.size()==0){
                        isPass = false;
                    }
                }else{
                    if(Contant.enforceList.contains(getResources().getString(R.string.propertymanage))&&(checkTermGroupBeans1==null||checkTermGroupBeans1.size()==0)){
                        isPass = false;
                    }else if(Contant.enforceList.contains(getResources().getString(R.string.generalbasement))&&(checkTermGroupBeans2==null||checkTermGroupBeans2.size()==0)){
                        isPass = false;
                    }else if(Contant.enforceList.contains(getResources().getString(R.string.housesafeuse))&&(checkTermGroupBeans3==null||checkTermGroupBeans3.size()==0)){
                        isPass = false;
                    }
                }
                if(isPass){
                    for(int i=0;i< Contant.enforceList.size();i++){
                        String enforce = Contant.enforceList.get(i);
                        if(enforce.equals(getResources().getString(R.string.primebroker))){
                            List<CheckTermGroupBean> checkTermGroupBeans = new ArrayList<>();
                            if(primeExpandableListAdapter!=null){
                                checkTermGroupBeans.addAll(getNewList(primeExpandableListAdapter.getCheckTermGroupBeans()));
                                Contant.checkedTerms.put("prime",checkTermGroupBeans);
                            }
                        }else if(enforce.equals(getResources().getString(R.string.propertymanage))){
                            List<CheckTermGroupBean> checkTermGroupBeans = new ArrayList<>();
                            if(propertyExpandableListAdapter!=null){
                                checkTermGroupBeans.addAll(getNewList(propertyExpandableListAdapter.getCheckTermGroupBeans()));
                                Contant.checkedTerms.put("property",checkTermGroupBeans );
                            }
                        }else if(enforce.equals(getResources().getString(R.string.generalbasement))){
                            List<CheckTermGroupBean> checkTermGroupBeans = new ArrayList<>();
                            if(generalExpandableListAdapter!=null){
                                checkTermGroupBeans.addAll(getNewList(generalExpandableListAdapter.getCheckTermGroupBeans()));
                                Contant.checkedTerms.put("general", checkTermGroupBeans);
                            }
                        }else if(enforce.equals(getResources().getString(R.string.housesafeuse))){
                            List<CheckTermGroupBean> checkTermGroupBeans = new ArrayList<>();
                            if(houseSafeExpandableListAdapter!=null){
                                checkTermGroupBeans.addAll(getNewList(houseSafeExpandableListAdapter.getCheckTermGroupBeans()));
                                Contant.checkedTerms.put("houseSafe",checkTermGroupBeans);
                            }
                        }
                    }
                    Intent intent = new Intent(this,CommitActivity.class);
                    intent.setAction("check");
                    this.startActivity(intent);
                }else{
                    ShowToast("检查项未加载完成,请返回上一步重新点击“下一步”");
                }
                break;
        }
    }
}
