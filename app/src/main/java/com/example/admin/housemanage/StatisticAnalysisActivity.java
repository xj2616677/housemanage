package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.PersonDialogAdapter;
import bean.BranchBean;
import bean.StreetBean;
import bean.UserBean;
import constants.Contant;
import datepicker.cons.DPMode;
import datepicker.views.DatePicker;
import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;
import util.Util;

/**
 * Created by admin on 2016/3/31.
 */
public class StatisticAnalysisActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{

    private ProgressDialog progressDialog;
    private Handler mHandler;

    private TextView text_startTime,text_endTime;
    private Spinner sp_street,sp_bType,sp_source,sp_staticType,sp_housemanage,sp_checkbranch;
    private String[] houseManageData,bTypeData,sourceData,staticTypeData,branch;

    private List<StreetBean> streetAll ;
    private Gson gson;
    private Button bt_sure;

    private AlertDialog alertDialog;
    private DatePicker datePicker;
    private LinearLayout lin_btype,lin_tasksource;

    private List<StreetBean> streets;

    private String startTime,endTime, houseManager,streetName,bType,source,analyType,checkBranch = "";
    private String inspector = "";

    private TextView text_analy_person;
    private RelativeLayout rel_person;
    private Button bt_clearPerson;

    private TextView text_more;
    private ExpandableListView listView_person;
    private  TextView text_person_sure;
    private  TextView text_person_cancel;
    private AlertDialog personAlertDialog;
    private List<BranchBean> personDialogBeans;
    private PersonDialogAdapter personDialogAdapter;

    private RadioGroup rg_chooseData;
    private String choosedataIndex = "1";


    @Override
    protected void initView() {

        setContentLayout(R.layout.analysis_activity);
        setTitle("统计分析");
        ActivityManage.getInstance().addActivity(this);
        hideRightView();
        text_startTime = (TextView) findViewById(R.id.text_analysis_starttime);
        text_endTime = (TextView) findViewById(R.id.text_analysis_endtime);
        sp_street = (Spinner) findViewById(R.id.spinner_street);
        sp_bType = (Spinner) findViewById(R.id.spinner_btype);
//        sp_inspectType = (Spinner) findViewById(R.id.spinner_analysis_inspectType);
        sp_source = (Spinner) findViewById(R.id.spinner_analysis_tasksource);
        sp_staticType = (Spinner) findViewById(R.id.spinner_staticType);
        sp_housemanage = (Spinner) findViewById(R.id.spinner_houseManager);
        sp_checkbranch = (Spinner) findViewById(R.id.spinner_checkbranch);
        bt_sure = (Button) findViewById(R.id.bt_analysis_sure);

        rel_person = (RelativeLayout) findViewById(R.id.rel_analy_person);
        text_analy_person = (TextView) findViewById(R.id.text_analy_person);
        bt_clearPerson = (Button) findViewById(R.id.bt_analy_clearperson);
        text_analy_person.setOnClickListener(this);
        bt_clearPerson.setOnClickListener(this);

        lin_btype = (LinearLayout) findViewById(R.id.lin_btype);
        lin_tasksource = (LinearLayout) findViewById(R.id.lin_tasksource);
        lin_tasksource.setVisibility(View.GONE);


        rg_chooseData = (RadioGroup) findViewById(R.id.rg_analy_choosedata);
        rg_chooseData.check(R.id.rb_analy_selfdata);

        sp_street.setOnItemSelectedListener(this);
        sp_bType.setOnItemSelectedListener(this);
//        sp_inspectType.setOnItemSelectedListener(this);
        sp_source.setOnItemSelectedListener(this);
        sp_staticType.setOnItemSelectedListener(this);
        sp_housemanage.setOnItemSelectedListener(this);
        sp_checkbranch.setOnItemSelectedListener(this);
        bt_sure.setOnClickListener(this);
        text_startTime.setOnClickListener(this);
        text_endTime.setOnClickListener(this);

    }

    @Override
    protected void initData() {


        rg_chooseData.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                switch (radioId){
                    case R.id.rb_analy_selfdata:
                        choosedataIndex = "1";
                        break;
                    case R.id.rb_analy_succcessdata:
                        choosedataIndex = "2";
                        break;
                    case R.id.rb_analy_nodata:
                        choosedataIndex = "3";
                        break;
                    case R.id.rb_analy_citydata:
                        choosedataIndex = "4";
                        break;
                    case R.id.rb_analy_allalldata:
                        choosedataIndex = "5";
                        break;
                }
            }
        });

        gson = new Gson();
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                switch (msg.what){
                    case 1:

                        if(result!=null&&!result.equals("")){
                            streetAll = new ArrayList<>();
                            List<StreetBean> streetBeans = gson.fromJson(result, new TypeToken<List<StreetBean>>() {
                            }.getType());
                            streetAll.addAll(streetBeans);

                            streets= getManageStreet(houseManager);
                            ArrayAdapter<StreetBean> arrayAdapterStreet = new ArrayAdapter<StreetBean>(StatisticAnalysisActivity.this,android.R.layout.simple_spinner_item,streets);
                            arrayAdapterStreet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_street.setAdapter(arrayAdapterStreet);
                            streetName = streets.get(0).getSTREET_NAME();

                        }else{
                            ShowToast("服务器异常");
                        }
                        break;
                    case 2:
                        if(result==null||result.equals("")){
                            ShowToast("服务器连接异常");
                        }else if(result.equals("[]")){
                            ShowToast("该条件没有查询结果");
                        }else{
                            if(analyType.equals("检查部门")){
                                Intent intent = new Intent(StatisticAnalysisActivity.this,CheckBranchStatisticActivity.class);
                                intent.putExtra("houseManager", houseManager);
                                intent.putExtra("branchName", checkBranch);
                                intent.putExtra("streetName", streetName);
                                intent.putExtra("bType", bType);
                                intent.putExtra("inspector", inspector);
                                intent.putExtra("source", source);
                                intent.putExtra("startTime", startTime);
                                intent.putExtra("endTime", endTime);
                                intent.putExtra("staticAnaly", analyType);
                                intent.putExtra("result", result);
                                intent.putExtra("index",choosedataIndex);
                                StatisticAnalysisActivity.this.startActivity(intent);
                            }else {
                                Intent intent = new Intent(StatisticAnalysisActivity.this, AnalysisInfoActivity.class);
                                intent.putExtra("houseManager", houseManager);
                                intent.putExtra("branchName", checkBranch);
                                intent.putExtra("streetName", streetName);
                                intent.putExtra("bType", bType);
                                intent.putExtra("inspector", inspector);
                                intent.putExtra("source", source);
                                intent.putExtra("startTime", startTime);
                                intent.putExtra("endTime", endTime);
                                intent.putExtra("staticAnaly", analyType);
                                intent.putExtra("result", result);
                                intent.putExtra("index",choosedataIndex);
                                StatisticAnalysisActivity.this.startActivity(intent);
                            }

                        }
                        break;
                    case 3:
                        if(result==null||result.equals("")){
                            ShowToast("服务器连接异常");
                        }else {
                            if (personDialogBeans == null) {
                                personDialogBeans = new ArrayList<>();
                                personDialogBeans.addAll(getPersonBeans(result));
                                personDialogAdapter = new PersonDialogAdapter(StatisticAnalysisActivity.this, personDialogBeans, 2);
                                listView_person.setAdapter(personDialogAdapter);
                            } else {
                                personDialogBeans.clear();
                                personDialogBeans.addAll(getPersonBeans(result));
                                personDialogAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
                progressDialog.dismiss();
            }
        };


        UserBean userBean = Contant.USERBEAN;
        String branchName = userBean.getBranchName();
        switch (branchName){
            case "第一房管所":
                houseManageData = new String[]{"第一房管所"};
                branch = new String[]{"第一房管所"};

                break;
            case "第二房管所":
                houseManageData = new String[]{"第二房管所"};
                branch = new String[]{"第二房管所"};
                break;
            case "第三房管所":
                houseManageData = new String[]{"第三房管所"};
                branch = new String[]{"第三房管所"};
                break;
            case "第四房管所":
                houseManageData = new String[]{"第四房管所"};
                branch = new String[]{"第四房管所"};
                break;
            case "第五房管所":
                houseManageData = new String[]{"第五房管所"};
                branch = new String[]{"第五房管所"};
                break;
            case "第六房管所":
                houseManageData = new String[]{"第六房管所"};
                branch = new String[]{"第六房管所"};
                break;
            case "第七房管所":
                houseManageData = new String[]{"第七房管所"};
                branch = new String[]{"第七房管所"};
                break;
            default:
                houseManageData = getResources().getStringArray(R.array.housemanage);
                branch = getResources().getStringArray(R.array.branch);
                break;
        }
        bTypeData = getResources().getStringArray(R.array.bType);
        sourceData = getResources().getStringArray(R.array.source);
        staticTypeData = getResources().getStringArray(R.array.staticType);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,houseManageData);
        ArrayAdapter<String> arrayAdapterbranch = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,branch);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,bTypeData);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sourceData);
        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,staticTypeData);
        houseManager = houseManageData[0];
        checkBranch = branch[0];
        bType = bTypeData[0];
        source = sourceData[0];
        analyType = staticTypeData[0];

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterbranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_checkbranch.setAdapter(arrayAdapterbranch);
        sp_housemanage.setAdapter(arrayAdapter);
        sp_bType.setAdapter(arrayAdapter1);
        sp_source.setAdapter(arrayAdapter3);
        sp_staticType.setAdapter(arrayAdapter4);

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
                    msg.what =1;
                    msg.obj = result;
                    mHandler.sendMessage(msg);

                }
            }).start();
        }else{
            ShowToast(noNetText);
        }

    }

    private List<StreetBean> getManageStreet(String houseManage){
        List<StreetBean> streets = new ArrayList<>();
        StreetBean streetBean = new StreetBean();
        streetBean.setSTREET_NAME("全部");
        streets.add(streetBean);
        if(!"全部".equals(houseManage)){
            if(streetAll!=null&&streetAll.size()!=0){
                for(StreetBean street:streetAll){
                    if(houseManage.equals(street.getHOUSEMANAGER_NAME())){
                        streets.add(street);
                    }
                }
            }
        }else{
            if(streetAll!=null){
                streets.addAll(streetAll);
            }
        }
        return streets;
    }

    private List<BranchBean> getPersonBeans(String result){
        List<BranchBean> beans = new ArrayList<>();
        if(!result.equals("")){
            beans = gson.fromJson(result, new TypeToken<List<BranchBean>>() {
            }.getType());
            if(beans!=null&&beans.size()!=0){
                for(BranchBean branchBean:beans){
                    branchBean.setListBranch();
                }
            }
        }
        return beans;
    }


    private void initDialog(final int index){
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
                if(index==1){
                    text_startTime.setText(date);
                }else if(index==2){
                    text_endTime.setText(date);
                }
            }
        });
    }


    private void initPersonDialog(){
        if(personAlertDialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            personAlertDialog = builder.create();
            personAlertDialog.show();
            personAlertDialog.setCanceledOnTouchOutside(true);
            personAlertDialog.setContentView(R.layout.personchoose_dialog);
            listView_person= (ExpandableListView) personAlertDialog.findViewById(R.id.expandlist_persondialog);
            text_more = (TextView) personAlertDialog.findViewById(R.id.text_person_more);
            text_person_sure = (TextView) personAlertDialog.findViewById(R.id.text_persondialog_sure);
            text_person_cancel = (TextView) personAlertDialog.findViewById(R.id.text_persondialog_cancel);
            LinearLayout lin_bottom = (LinearLayout)personAlertDialog.findViewById(R.id.lin_bottom);
            lin_bottom.setVisibility(View.GONE);
        }else{
            personAlertDialog.show();
        }
        requestPerson(0);


//        text_person_sure.setOnClickListener(this);
//        text_person_cancel.setOnClickListener(this);
        text_more.setOnClickListener(this);

        listView_person.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                personAlertDialog.dismiss();
                text_analy_person.setText(personDialogBeans.get(groupPosition).getPersonList().get(childPosition).getName());
                return false;
            }
        });
    }

    private void requestPerson(final int isAll){
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
                    params.put("isAll", isAll);
                    String result = RequestUtil.postob(RequestUtil.GetCheckManList, params);
                    Message msg = Message.obtain();
                    msg.what = 3;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }else{
            ShowToast(noNetText);
        }

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.text_analy_person:
                initPersonDialog();
                break;
            case R.id.bt_analy_clearperson:
                text_analy_person.setText("");
                break;
            case R.id.text_person_more:
                requestPerson(1);
                break;
            case R.id.bt_analysis_sure:
                startTime = text_startTime.getText().toString();
                endTime = text_endTime.getText().toString();
                inspector = text_analy_person.getText().toString();
//                if(startTime.equals("")){
//                    ShowToast("请选择开始时间");
//                }else if(endTime.equals("")){
//                    ShowToast("请选择结束时间");
//                }else
                Log.i("TAG","startTime---"+startTime+"---endTime---"+endTime);
                if((!startTime.equals("")&&!endTime.equals(""))&&!Util.compare_date(startTime,endTime)) {
                    ShowToast("您选择的结束时间在开始时间之后");
                }else{
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
                                if("全部".equals(houseManager)){
                                    houseManager = "";
                                }
                                if("全部".equals(checkBranch)){
                                    checkBranch = "";
                                }
                                if("全部".equals(streetName)){
                                    streetName = "";
                                }
                                if("全部".equals(bType)){
                                    bType = "";
                                }
                                if("全部".equals(source)){
                                    source = "";
                                }
                                params.put("houseManager", houseManager);
                                params.put("BranchName", checkBranch);
                                params.put("streetName", streetName);
                                params.put("bType", bType);
                                params.put("inspector", inspector);
                                params.put("source", source);
                                params.put("startTime", startTime);
                                params.put("endTime", endTime);
                                params.put("staticType", analyType);
                                params.put("index",""+choosedataIndex);
                                String result = "";
                                if("检查部门".equals(analyType)){
                                    result = RequestUtil.post(RequestUtil.GetInspectStatcicDataByBranch, params);
                                }else {
                                    result = RequestUtil.post(RequestUtil.GetInspectStatcicData, params);
                                }
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
                break;
            case R.id.title_lefttext:
                finish();
                break;
            case R.id.text_analysis_starttime:
                initDialog(1);
                break;
            case R.id.text_analysis_endtime:
                initDialog(2);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.spinner_checkbranch:
                checkBranch = branch[position];
                break;
            case R.id.spinner_houseManager:
                houseManager = houseManageData[position];
                streets = getManageStreet(houseManager);
                ArrayAdapter<StreetBean> arrayAdapterStreet = new ArrayAdapter<StreetBean>(this,android.R.layout.simple_spinner_item,streets);
                arrayAdapterStreet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_street.setAdapter(arrayAdapterStreet);
                break;
            case R.id.spinner_staticType:
                analyType = staticTypeData[position];
                switch (analyType){
                    case "业务类别":
                        lin_btype.setVisibility(View.VISIBLE);
                        lin_tasksource.setVisibility(View.GONE);
                        rel_person.setVisibility(View.GONE);
                        source = "";
                        text_analy_person.setText("");
                        break;
                    case "任务来源":
                        bType = "";
                        lin_btype.setVisibility(View.GONE);
                        lin_tasksource.setVisibility(View.VISIBLE);
                        rel_person.setVisibility(View.GONE);
                        bType = "";
                        text_analy_person.setText("");
                        break;
                    case "检查人":
                        bType = "";
                        lin_btype.setVisibility(View.GONE);
                        lin_tasksource.setVisibility(View.GONE);
                        rel_person.setVisibility(View.VISIBLE);
                        bType = "";
                        source = "";
                        break;
                    case "检查对象":
                        lin_btype.setVisibility(View.GONE);
                        lin_tasksource.setVisibility(View.GONE);
                        rel_person.setVisibility(View.GONE);
                        bType = "";
                        source = "";
                        text_analy_person.setText("");
                        break;
                    case "检查部门":
                        lin_btype.setVisibility(View.GONE);
                        lin_tasksource.setVisibility(View.GONE);
                        rel_person.setVisibility(View.GONE);
                        bType = "";
                        source = "";
                        text_analy_person.setText("");
                        break;
                }
                break;
            case R.id.spinner_analysis_tasksource:
                source = sourceData[position];
                break;
//            case R.id.spinner_analysis_inspectType:
//                inspectType = inspectTypeData[position];
//                break;
            case R.id.spinner_btype:
                bType = bTypeData[position];
                break;
            case R.id.spinner_street:
                streetName = streets.get(position).getSTREET_NAME();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
