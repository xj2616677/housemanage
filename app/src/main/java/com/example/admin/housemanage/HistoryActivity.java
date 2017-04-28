package com.example.admin.housemanage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
public class HistoryActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    private Button button;
    private RadioGroup radioGroup1;
    private EditText editText1;
    private TextView text_person;
    private CheckBox cb_prime,cb_property,cb_general,cb_house,cb_other;
    private TextView text_starttime,text_endtime;

    private List<String> typelist;
    private List<String> checklist;
    private ProgressDialog progressDialog;
    private Map<String,String> params;
    private Handler mHandler;

    private AlertDialog alertDialog;
    private DatePicker datePicker;

    private TextView text_more;
    private ExpandableListView listView_person;
    private  TextView text_person_sure;
    private  TextView text_person_cancel;
    private Button bt_clearperson;
    private AlertDialog personAlertDialog;

    private List<BranchBean> personDialogBeans;
    private PersonDialogAdapter personDialogAdapter;
    private Gson gson;


    private EditText edit_inspectno,edit_sxt_bm;
    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.trackobject));
        hideRightView();
        setContentLayout(R.layout.historyactivity);
        ActivityManage.getInstance().addActivity(this);
        radioGroup1=(RadioGroup)findViewById(R.id.rg_historyactivity);
        cb_prime = (CheckBox) findViewById(R.id.rb_historyactivity);
        cb_property = (CheckBox) findViewById(R.id.rb2_historyactivity);
        cb_general = (CheckBox) findViewById(R.id.rb3_historyactivity);
        cb_house = (CheckBox) findViewById(R.id.rb4_historyactivity);
        cb_other = (CheckBox) findViewById(R.id.cb_history_other);

        cb_prime.setOnCheckedChangeListener(this);
        cb_property.setOnCheckedChangeListener(this);
        cb_general.setOnCheckedChangeListener(this);
        cb_house.setOnCheckedChangeListener(this);
        cb_other.setOnCheckedChangeListener(this);


        editText1=(EditText)findViewById(R.id.ed3_historyactivity);
        text_person=(TextView)findViewById(R.id.text_history_person);
        text_starttime = (TextView) findViewById(R.id.text_history_startdate);
        text_endtime = (TextView) findViewById(R.id.text_history_enddate);
        button= (Button) findViewById(R.id.button_historyactivity);
        bt_clearperson = (Button) findViewById(R.id.bt_history_clearperson);
        edit_inspectno = (EditText) findViewById(R.id.edit_history_inspectno);
        edit_sxt_bm = (EditText) findViewById(R.id.edit_history_szt_bm);
        button.setOnClickListener(this);
        text_starttime.setOnClickListener(this);
        text_endtime.setOnClickListener(this);
        text_person.setOnClickListener(this);
        bt_clearperson.setOnClickListener(this);

    }
    @Override
    protected void initData() {
        gson = new Gson();
        typelist = new ArrayList<>();
        checklist = new ArrayList<>();

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.dismiss();
                String result = "";
                try{
                    result = (String) msg.obj;
                }catch(OutOfMemoryError e){
                }
                switch (msg.what){
                    case 1:
                        if(result.equals("")){
                            ShowToast("服务器连接失败");
                        }else if(result.equals("[]")){
                            ShowToast("该条件下无查询结果");
                        }else{
                            Intent intent=new Intent(HistoryActivity.this,HistoryInfoActivity.class);
                            intent.setAction("history");
                            intent.putExtra("result",result);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if (personDialogBeans == null) {
                            personDialogBeans = new ArrayList<>();
                            personDialogBeans.addAll(getPersonBeans((String) result));
                            personDialogAdapter = new PersonDialogAdapter(HistoryActivity.this, personDialogBeans,2);
                            listView_person.setAdapter(personDialogAdapter);
                        } else {
                            personDialogBeans.clear();
                            personDialogBeans.addAll(getPersonBeans((String) result));
                            personDialogAdapter.notifyDataSetChanged();
                        }
                        break;
                }

            }
        };

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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_lefttext:
                this.finish();
                break;
            case R.id.text_history_person:
                initPersonDialog();
                break;
            case R.id.button_historyactivity:
                String objName =editText1.getText().toString().trim();
                String inspector=text_person.getText().toString().trim();
                String btype =listTostr(typelist);
                String inspectType=listTostr(checklist);
                String startTime = text_starttime.getText().toString().trim();
                String endTime = text_endtime.getText().toString().trim();
                String inspectNo = edit_inspectno.getText().toString().trim();
                String sxt_bm = edit_sxt_bm.getText().toString().trim();

                if(!startTime.equals("")&&!"".equals(endTime)&&!Util.compare_date(startTime, endTime)) {
                    ShowToast("您选择的结束时间在开始时间之后");
                }else{

                    if(NetUtil.isNetworkAvailable(this)){
                        if(progressDialog==null){
                            progressDialog = showProgressDialog();
                        }else{
                            progressDialog.show();
                        }
                        params = new HashMap<String, String>();
                        params.put("btype", btype);
                        params.put("inspectType", inspectType);
                        params.put("objName", objName);
                        params.put("inspector", inspector);
                        params.put("startDate", startTime);
                        params.put("endDate ",endTime);
                        params.put("inspectNo",inspectNo);
                        params.put("sxtbm",sxt_bm);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = "";
                                try {
                                    result = RequestUtil.post(RequestUtil.GetInspectHistoryObject, params);
                                }catch(OutOfMemoryError e){
                                }
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
                break;
            case R.id.text_history_enddate:
                initDialog(2);
                break;
            case R.id.text_history_startdate:
                initDialog(1);
                break;
//            case R.id.text_persondialog_cancel:
//                personAlertDialog.dismiss();
//                break;
            case R.id.text_person_more:
                requestPerson(1);
                break;
            case R.id.bt_history_clearperson:
                text_person.setText("");
                break;
//            case R.id.text_persondialog_sure:
//                personAlertDialog.dismiss();
//
//                break;
        }
    }
    private String listTostr(List<String> strs){
        StringBuilder stringBuilder = new StringBuilder();
        if(strs.size()!=0){
            for(int i=0;i<strs.size();i++){
                if(i==0){
                    stringBuilder.append(strs.get(i));
                }else{
                    stringBuilder.append(","+strs.get(i));
                }
            }
            return stringBuilder.toString();
        }else{
            return "";
        }
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
                text_person.setText(personDialogBeans.get(groupPosition).getPersonList().get(childPosition).getName());
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
                    msg.what = 2;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }else{
            ShowToast(noNetText);
        }

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
                    text_starttime.setText(date);
                }else if(index==2){
                    text_endtime.setText(date);
                }
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.rb_historyactivity:
                if(isChecked){
                    typelist.add(getResources().getString(R.string.primebroker));
                }else{
                    typelist.remove(getResources().getString(R.string.primebroker));
                }
                break;
            case R.id.rb2_historyactivity:
                if(isChecked){
                    typelist.add(getResources().getString(R.string.propertymanage));
                }else{
                    typelist.remove(getResources().getString(R.string.propertymanage));
                }
                break;
            case R.id.rb3_historyactivity:
                if(isChecked){
                    typelist.add(getResources().getString(R.string.generalbasement));
                }else{
                    typelist.remove(getResources().getString(R.string.generalbasement));
                }
                break;
            case R.id.rb4_historyactivity:
                if(isChecked){
                    typelist.add(getResources().getString(R.string.housesafeuse));
                }else{
                    typelist.remove(getResources().getString(R.string.housesafeuse));
                }
                break;
            case R.id.cb_history_other:
                if(isChecked){
                    typelist.add(getResources().getString(R.string.other));
                }else{
                    typelist.remove(getResources().getString(R.string.other));
                }
                break;
        }
    }
}
