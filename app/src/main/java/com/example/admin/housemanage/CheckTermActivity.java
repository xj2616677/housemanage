package com.example.admin.housemanage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import adapter.MyExpandableListAdapter;
import bean.CheckTermBean;
import bean.CheckTermGroupBean;
import constants.Contant;
import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;
import widget.MyExpandableListView;

/**
 * 检查项选择页
 * Created by admin on 2016/4/13.
 */
public class CheckTermActivity extends BaseActivity {

    private Button bt_term_search;
    private EditText edit_term_search;
    private LinearLayout lin_term_content;
    private Handler mHandler;
    private Gson gson;
    private MyExpandableListView primeExpandListView;
    private MyExpandableListView propertyExpandListView;
    private MyExpandableListView generalExpandListView;
    private MyExpandableListView houseSafeExpandListView;
    private MyExpandableListAdapter primeExpandableListAdapter;
    private MyExpandableListAdapter propertyExpandableListAdapter;
    private MyExpandableListAdapter generalExpandableListAdapter;
    private MyExpandableListAdapter houseSafeExpandableListAdapter;
    private ProgressDialog progressDialog;
    @Override
    protected void initView() {
        setContentLayout(R.layout.checkterm_activity);
        ActivityManage.getInstance().addActivity(this);

        bt_term_search = (Button) findViewById(R.id.bt_checkterm_search);
        edit_term_search = (EditText) findViewById(R.id.edit_checkterm_search);
        lin_term_content = (LinearLayout) findViewById(R.id.lin_checkterm_content);

        for(int i=0;i< Contant.enforceList.size();i++){
            String enforce = Contant.enforceList.get(i);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(this);
            textView.setText(enforce);
            textView.setTextSize(20.0f);
            linearLayout.addView(textView);


            if(enforce.equals(getResources().getString(R.string.primebroker))){
                primeExpandListView = new MyExpandableListView(this);
                linearLayout.addView(primeExpandListView);
            }else if(enforce.equals(getResources().getString(R.string.propertymanage))){
                propertyExpandListView = new MyExpandableListView(this);
                linearLayout.addView(propertyExpandListView);
            }else if(enforce.equals(getResources().getString(R.string.generalbasement))){
                generalExpandListView = new MyExpandableListView(this);
                linearLayout.addView(generalExpandListView);
            }else if(enforce.equals(getResources().getString(R.string.housesafeuse))){
                houseSafeExpandListView = new MyExpandableListView(this);
                linearLayout.addView(houseSafeExpandListView);
            }

            TextView hHineText = new TextView(this);
            hHineText.setBackgroundColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams linelayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
            linelayoutParams.setMargins(0, 10, 0, 0);
            linearLayout.addView(hHineText, linelayoutParams);

            lin_term_content.addView(linearLayout);
        }




    }

    @Override
    protected void initData() {
        TextView titleText = getCenterText();
        titleText.setFocusable(true);
        titleText.setFocusableInTouchMode(true);
        titleText.requestFocus();


        gson = new Gson();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                String[] primeGroups = null;
                List<CheckTermBean> termBeans = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray array = jsonObject.getJSONArray("inspectClasses");
                    primeGroups = new String[array.length()];
                    for(int i=0;i<array.length();i++){
                        primeGroups[i] = (String) array.get(i);
                    }
                    termBeans = gson.fromJson(jsonObject.getJSONArray("items").toString(),new TypeToken<List<CheckTermBean>>(){}.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<List<CheckTermBean>> lists = getChilds(primeGroups,termBeans);
                List<CheckTermGroupBean> checkTermGroupBeans = new ArrayList<>();
                for(int i=0;i<lists.size();i++){
                    CheckTermGroupBean checkTermGroupBean = new CheckTermGroupBean();
                    checkTermGroupBean.setChecked(false);
                    checkTermGroupBean.setGroupName(primeGroups[i]);
                    checkTermGroupBean.setCheckTermBeans(lists.get(i));
                    checkTermGroupBeans.add(checkTermGroupBean);
                }
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                switch (msg.what){
                    case 1:
                        primeExpandableListAdapter = new MyExpandableListAdapter(checkTermGroupBeans,CheckTermActivity.this);
                        primeExpandListView.setAdapter(primeExpandableListAdapter);
                        break;
                    case 2:
                         propertyExpandableListAdapter = new MyExpandableListAdapter(checkTermGroupBeans,CheckTermActivity.this);
                        propertyExpandListView.setAdapter(propertyExpandableListAdapter);
                        break;
                    case 3:
                         generalExpandableListAdapter = new MyExpandableListAdapter(checkTermGroupBeans,CheckTermActivity.this);
                        generalExpandListView.setAdapter(generalExpandableListAdapter);
                        break;
                    case 4:
                         houseSafeExpandableListAdapter = new MyExpandableListAdapter(checkTermGroupBeans,CheckTermActivity.this);
                        houseSafeExpandListView.setAdapter(houseSafeExpandableListAdapter);
                        break;
                }
            }
        };

        if(NetUtil.isNetworkAvailable(this)){
            if(progressDialog==null){
                progressDialog = showProgressDialog();
            }else{
                progressDialog.show();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<Contant.enforceList.size();i++){
                        String enforce = Contant.enforceList.get(i);
                        Map<String,String> prarm = new HashMap<>();
                        prarm.put("jjxBussinesstype", Contant.enforceList.get(i));
                        String result = RequestUtil.post(RequestUtil.GetJCX,prarm);
                        Message msg = Message.obtain();
                        if(enforce.equals(getResources().getString(R.string.primebroker))){
                            msg.what = 1;
                        }else if(enforce.equals(getResources().getString(R.string.propertymanage))){
                            msg.what = 2;
                        }else if(enforce.equals(getResources().getString(R.string.generalbasement))){
                            msg.what = 3;
                        }else if(enforce.equals(getResources().getString(R.string.housesafeuse))){
                            msg.what = 4;
                        }
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        }else{
            ShowToast(noNetText);
        }

    }

    private List<List<CheckTermBean>> getChilds(String[] groups,List<CheckTermBean> checkTermBeans){
        List<List<CheckTermBean>> termLists = new ArrayList<>();
        if(checkTermBeans!=null&&checkTermBeans.size()!=0){
            for(String group:groups){
                List<CheckTermBean> termList = new ArrayList<>();
                for(int i=0;i<checkTermBeans.size();i++){
                    if(checkTermBeans.get(i).getInspectClass().equals(group)){
                        checkTermBeans.get(i).setIsCheck(false);
                        termList.add(checkTermBeans.get(i));
                    }
                }
                termLists.add(termList);
            }
        }
        return termLists;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_lefttext:
                finish();
                break;
            case R.id.title_righttext:
                for(int i=0;i< Contant.enforceList.size();i++){
                    String enforce = Contant.enforceList.get(i);
                    if(enforce.equals(getResources().getString(R.string.primebroker))){
                        Contant.checkedTerms.put("prime",primeExpandableListAdapter.getCheckTermGroupBeans());
                    }else if(enforce.equals(getResources().getString(R.string.propertymanage))){
                        Contant.checkedTerms.put("property", propertyExpandableListAdapter.getCheckTermGroupBeans());
                    }else if(enforce.equals(getResources().getString(R.string.generalbasement))){
                        Contant.checkedTerms.put("general", generalExpandableListAdapter.getCheckTermGroupBeans());
                    }else if(enforce.equals(getResources().getString(R.string.housesafeuse))){
                        Contant.checkedTerms.put("houseSafe", houseSafeExpandableListAdapter.getCheckTermGroupBeans());
                    }
                }
                Intent intent = new Intent(CheckTermActivity.this,CheckListActivity.class);
                this.startActivity(intent);
                break;
        }
    }
}
