package com.example.admin.housemanage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.AnalyListAdapter;
import adapter.AnalyListAdapter2;
import bean.AnalyBean;
import bean.AnalynoBean;
import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;


/**
 * Created by admin on 2016/5/23.
 */
public class AnalysisInfoActivity extends  BaseActivity {

    private LinearLayout lin_head1,lin_head2;
    private TextView text_type1,text_type2,text_head2,text_head3;
    private ListView list_analy;
    private Gson gson;
    private String startTime,endTime, houseManager,streetName,bType,source,analyType,branchName;
    private String inspector = "";
    private String objectName = "";
    private List<AnalynoBean> analynoBeans;
    private List<AnalyBean> analys;
    private String staticAnaly = "";
    private TextView text_line;
    private ProgressDialog progressDialog;
    private Handler mHandler;
    private String index ="";

    @Override
    protected void initView() {

        setContentLayout(R.layout.analysisinfo_activity);
        setTitle("统计分析");
        hideRightView();
        ActivityManage.getInstance().addActivity(this);

        lin_head1 = (LinearLayout) findViewById(R.id.lin_analy_head1);
        lin_head2 = (LinearLayout) findViewById(R.id.lin_analy_head2);
        text_head3 = (TextView) findViewById(R.id.text_analy2_three);
        text_head2 = (TextView) findViewById(R.id.text_analy2_two);
        text_type1 = (TextView) findViewById(R.id.text_analyinfo_type);
        text_type2 = (TextView) findViewById(R.id.text_analy2info_type);
        list_analy = (ListView) findViewById(R.id.list_analyinfo);
        text_line = (TextView) findViewById(R.id.text_analy2_line);


    }

    @Override
    protected void initData() {
        gson = new Gson();
        Intent intent = getIntent();
         staticAnaly= intent.getStringExtra("staticAnaly");
        houseManager = intent.getStringExtra("houseManager");
        branchName = intent.getStringExtra("branchName");
        streetName = intent.getStringExtra("streetName");
        bType = intent.getStringExtra("bType");
        inspector = intent.getStringExtra("inspector");
        source = intent.getStringExtra("source");
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        String result = intent.getStringExtra("result");
        index = intent.getStringExtra("index");
        setHead(result);

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                String result = (String) msg.obj;
                if (result==null||result.equals("")) {
                    ShowToast("服务器连接异常");
                } else if (result.equals("[]")) {
                    ShowToast("该条件没有查询结果");
                } else {
                    setHead(result);
                }
                progressDialog.dismiss();
            }
        };


//        list_analy.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                requestData();
//
//
//            }
//
//            @Override
//            public void onLoadMore() {
//
//            }
//        });


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_lefttext:
                finish();
                break;
        }
    }


    public void requestData(){
        if(NetUtil.isNetworkAvailable(this)) {
            if(progressDialog==null){
                progressDialog = showProgressDialog();
            }else{
                if(!progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("houseManager", houseManager);
                    params.put("BranchName", branchName);
                    params.put("streetName", streetName);
                    params.put("bType", bType);
                    params.put("inspector", inspector);
                    params.put("source", source);
                    params.put("startTime", startTime);
                    params.put("endTime", endTime);
                    params.put("staticType", staticAnaly);
                    String result = RequestUtil.post(RequestUtil.GetInspectStatcicData,params);
                    Message msg = Message.obtain();
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }else{
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            ShowToast(noNetText);
        }
    }

    private void setHead(String result){
        if(staticAnaly.equals("业务类别")||staticAnaly.equals("任务来源")){
            lin_head1.setVisibility(View.VISIBLE);
            lin_head2.setVisibility(View.GONE);
            text_type1.setText(staticAnaly);
            if(analys==null){
                analys = new ArrayList<>();
            }
            List<AnalyBean> analyBeans = gson.fromJson(result,new TypeToken<List<AnalyBean>>(){}.getType());
            analys.clear();
            analys.addAll(analyBeans);
            AnalyListAdapter analyListAdapter = new AnalyListAdapter(this,analys);
            list_analy.setAdapter(analyListAdapter);
            list_analy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (staticAnaly) {
                        case "业务类别":
                            bType = analys.get(position).getStaticField();
                            if(!"总计".equals(bType)) {
                                Intent intentTwo = new Intent(AnalysisInfoActivity.this, AnalysisInfoTwoActivity.class);
                                intentTwo.setAction("analy");
                                intentTwo.putExtra("houseManager", houseManager);
                                intentTwo.putExtra("branchName", branchName);
                                intentTwo.putExtra("streetName", streetName);
                                intentTwo.putExtra("objectName", objectName);
                                intentTwo.putExtra("bType", bType);
                                intentTwo.putExtra("inspector", inspector);
                                intentTwo.putExtra("source", source);
                                intentTwo.putExtra("startTime", startTime);
                                intentTwo.putExtra("endTime", endTime);
                                intentTwo.putExtra("index", index);
                                AnalysisInfoActivity.this.startActivity(intentTwo);
                            }
                            break;
                        case "任务来源":
                            source = analys.get(position).getStaticField();
                            if(!"总计".equals(source)) {
                                Intent intent = new Intent(AnalysisInfoActivity.this, HistoryInfoActivity.class);
                                intent.setAction("analy");
                                intent.putExtra("houseManager", houseManager);
                                intent.putExtra("branchName", branchName);
                                intent.putExtra("streetName", streetName);
                                intent.putExtra("objectName", objectName);
                                intent.putExtra("bType", bType);
                                intent.putExtra("inspector", inspector);
                                intent.putExtra("source", source);
                                intent.putExtra("startTime", startTime);
                                intent.putExtra("endTime", endTime);
                                intent.putExtra("index", index);
                                AnalysisInfoActivity.this.startActivity(intent);
                            }
                            break;
                    }


                }
            });

        }else if(staticAnaly.equals("检查人")){
            lin_head1.setVisibility(View.GONE);
            lin_head2.setVisibility(View.VISIBLE);
            text_type2.setText(staticAnaly);
            text_head2.setText("检查对象数");
            text_head3.setText("检查次数");
            if(analynoBeans==null){
                analynoBeans = new ArrayList<>();
            }
            List<AnalynoBean> analyBeans= gson.fromJson(result, new TypeToken<List<AnalynoBean>>() {
            }.getType());
            analynoBeans.clear();
            analynoBeans.addAll(analyBeans);
            AnalyListAdapter2 analyListAdapter = new AnalyListAdapter2(this,analynoBeans,2);
            list_analy.setAdapter(analyListAdapter);
            list_analy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    inspector = analynoBeans.get(position).getStaticField();
                    if(!"总计".equals(inspector)) {
                        Intent intent = new Intent(AnalysisInfoActivity.this, HistoryInfoActivity.class);
                        intent.setAction("analy");
                        intent.putExtra("houseManager", houseManager);
                        intent.putExtra("branchName", branchName);
                        intent.putExtra("streetName", streetName);
                        intent.putExtra("objectName", objectName);
                        intent.putExtra("bType", bType);
                        intent.putExtra("inspector", inspector);
                        intent.putExtra("source", source);
                        intent.putExtra("startTime", startTime);
                        intent.putExtra("endTime", endTime);
                        intent.putExtra("index", index);
                        AnalysisInfoActivity.this.startActivity(intent);
                    }
                }
            });
        }else if(staticAnaly.equals("检查对象")){
            lin_head1.setVisibility(View.GONE);
            lin_head2.setVisibility(View.VISIBLE);
            text_type2.setText(staticAnaly);
            text_head2.setText("检查次数");
            text_head3.setText("检查人次");
            if(analynoBeans==null){
                analynoBeans = new ArrayList<>();
            }
            List<AnalynoBean> analyBeans = gson.fromJson(result,new TypeToken<List<AnalynoBean>>(){}.getType());
            analynoBeans.clear();
            analynoBeans.addAll(analyBeans);
            AnalyListAdapter2 analyListAdapter = new AnalyListAdapter2(this,analynoBeans,3);
            list_analy.setAdapter(analyListAdapter);
            list_analy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    objectName = analynoBeans.get(position).getStaticField();
                    if("".equals(objectName)){
                        objectName = "未知";
                    }
                    if(!"总计".equals(objectName)) {
                        Intent intent = new Intent(AnalysisInfoActivity.this, HistoryInfoActivity.class);
                        intent.setAction("analy");
                        intent.putExtra("houseManager", houseManager);
                        intent.putExtra("branchName", branchName);
                        intent.putExtra("streetName", streetName);
                        intent.putExtra("objectName", objectName);
                        intent.putExtra("bType", bType);
                        intent.putExtra("inspector", inspector);
                        intent.putExtra("source", source);
                        intent.putExtra("startTime", startTime);
                        intent.putExtra("endTime", endTime);
                        intent.putExtra("index", index);
                        AnalysisInfoActivity.this.startActivity(intent);
                    }
                }
            });
        }else if("检查部门".equals(staticAnaly)){
            lin_head1.setVisibility(View.GONE);
            lin_head2.setVisibility(View.VISIBLE);
            text_type2.setText(staticAnaly);
            text_head2.setText("检查次数");
            text_head3.setText("检查人次");
            text_head3.setVisibility(View.GONE);
            text_line.setVisibility(View.GONE);
            if(analynoBeans==null){
                analynoBeans = new ArrayList<>();
            }
            List<AnalynoBean> analyBeans = gson.fromJson(result,new TypeToken<List<AnalynoBean>>(){}.getType());
            analynoBeans.clear();
            analynoBeans.addAll(analyBeans);
            AnalyListAdapter2 analyListAdapter = new AnalyListAdapter2(this,analynoBeans,4);
            list_analy.setAdapter(analyListAdapter);
            list_analy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    objectName = analynoBeans.get(position).getStaticField();
                    if("".equals(objectName)){
                        objectName = "未知";
                    }
                    if(!"总计".equals(objectName)) {
                        Intent intent = new Intent(AnalysisInfoActivity.this, HistoryInfoActivity.class);
                        intent.setAction("analy");
                        intent.putExtra("houseManager", houseManager);
                        intent.putExtra("branchName", branchName);
                        intent.putExtra("streetName", streetName);
                        intent.putExtra("objectName", objectName);
                        intent.putExtra("bType", bType);
                        intent.putExtra("inspector", inspector);
                        intent.putExtra("source", source);
                        intent.putExtra("startTime", startTime);
                        intent.putExtra("endTime", endTime);
                        intent.putExtra("index", index);
                        AnalysisInfoActivity.this.startActivity(intent);
                    }
                }
            });

        }
    }
}
