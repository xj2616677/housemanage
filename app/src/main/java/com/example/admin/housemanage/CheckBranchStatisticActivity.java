package com.example.admin.housemanage;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import adapter.AnalyBranchListAdapter;
import adapter.AnalyListAdapter2;
import bean.AnalyBranchBean;
import bean.AnalynoBean;
import util.ActivityManage;

/**
 * Created by admin on 2016/10/27.
 */
public class CheckBranchStatisticActivity extends BaseActivity{


    private ListView listView;
    private List<AnalyBranchBean> analyBranchBeans;
    private Gson gson;
    private String[] branchArray;
    public String startTime,endTime, houseManager,streetName,bType,source,analyType,branchName = "";
    public String inspector = "";
    public String objectName = "";
    private String staticAnaly = "";
    private String index;


    @Override
    protected void initView() {
        setContentLayout(R.layout.checkbranchstatistic_activity);
//        hideTitleView();
        hideRightView();
        ActivityManage.getInstance().addActivity(this);
        listView = (ListView) findViewById(R.id.list_analybranch);


    }

    private void orderBeans(List<AnalyBranchBean> analyBeans){
        for(int j=0;j<branchArray.length;j++){
            String branch = branchArray[j];
            for(int i=0;i<analyBeans.size();i++){
                String name = analyBeans.get(i).getBranchname();
                if(name.equals(branch)){
                    analyBranchBeans.add(0,analyBeans.get(i));
                    break;
                }
            }
        }
    }

    @Override
    protected void initData() {

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
        index = intent.getStringExtra("index");
        String result = intent.getStringExtra("result");


        gson = new Gson();
        branchArray = getResources().getStringArray(R.array.branchArray);
        if (analyBranchBeans == null) {
            analyBranchBeans = new ArrayList<>();
        }
        List<AnalyBranchBean> analyBeans = gson.fromJson(result, new TypeToken<List<AnalyBranchBean>>() {
        }.getType());
        analyBranchBeans.clear();
//                analynoBeans.addAll(analyBeans);
        orderBeans(analyBeans);
        AnalyBranchListAdapter analyListAdapter = new AnalyBranchListAdapter(this, analyBranchBeans);
        listView.setAdapter(analyListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String branchName = analyBranchBeans.get(position).getBranchname();
                if(!"总计".equals(branchName)){
                    Intent intent = new Intent(CheckBranchStatisticActivity.this, HistoryInfoActivity.class);
                    intent.setAction("analy");
                    intent.putExtra("houseManager", branchName);
                    intent.putExtra("branchName", branchName);
                    intent.putExtra("streetName", streetName);
                    intent.putExtra("objectName", "");
                    intent.putExtra("bType", bType);
                    intent.putExtra("inspector", inspector);
                    intent.putExtra("source", source);
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("endTime", endTime);
                    intent.putExtra("index", index);
                    CheckBranchStatisticActivity.this.startActivity(intent);
                }
            }
        });

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
}
