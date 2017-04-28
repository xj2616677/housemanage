package com.example.admin.housemanage;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.CheckBean;
import util.ActivityManage;
import util.RequestUtil;

/**
 * Created by Administrator on 2016/4/18.
 */
public class CheckInfoActivity extends BaseActivity {
    private ListView listView;
    private Handler mHandler;
    private String jsonStr,endDate,startDate,inspector,titleText;
    private List<CheckBean> checkBeanList =new ArrayList<>();
    private String[] a={"序号","检查人","检查时间","业务类型","是否有违法违规"};
    @Override
    protected void initView() {
        hideRightView();
        setContentLayout(R.layout.checkinfo_activity);
        ActivityManage.getInstance().addActivity(this);
        endDate=getIntent().getStringExtra("endDate2");
        startDate=getIntent().getStringExtra("staratDate2");
        inspector=getIntent().getStringExtra("name2");
        Log.d("TAG",endDate);
        listView=(ListView)findViewById(R.id.list_checkinfo);

    }

    @Override
    protected void initData() {
        Log.d("TAG", "1111111111");
        final Gson gson = new Gson();
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                jsonStr= (String) msg.obj;
                Gson gson = new Gson();
                checkBeanList = gson.fromJson(jsonStr, new TypeToken<List<CheckBean>>() {
                }.getType());
                Log.d("TAG","checkBeanList"+ String.valueOf(checkBeanList.size()));
                if (checkBeanList.size()!=0) {
                    SimpleAdapter simpleAdapter = new SimpleAdapter(CheckInfoActivity.this, getdata(), R.layout.item_checkinfo, new String[]{"text1", "text2", "text3", "text4", "text5"}, new int[]{R.id.text1_itemcheckinfo, R.id.text2_itemcheckinfo, R.id.text3_itemcheckinfo, R.id.text4_itemcheckinfo, R.id.text5_itemcheckinfo});
                    listView.setAdapter(simpleAdapter);
                }
                setTitle(inspector);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> params = new HashMap<>();
                params.put("btype", "");
                params.put("inspectType", "");
                params.put("objName", inspector);
                params.put("inspector", "");
                params.put("startDate", startDate);
                params.put("endDate", endDate);
                String result = RequestUtil.post(RequestUtil.GetInspectHistory, params);
                Message msg = Message.obtain();
                msg.obj=result;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_lefttext:
                this.finish();
                break;
        }
    }

    public List<Map<String,Object>> getdata() {
        List<Map<String,Object>>dataList =  new ArrayList<Map<String, Object>>();

        Map<String,Object>map=new HashMap<String,Object>();
        map.put("text1",a[0]);
        map.put("text2",a[1]);
        map.put("text3",a[2]);
        map.put("text4",a[3]);
        map.put("text5",a[4]);
        dataList.add(map);
        for (int i=0;i<checkBeanList.size();i++){
            map=new HashMap<String,Object>();
            map.put("text1",i+1);
            String b=checkBeanList.get(i).getInspector();
            StringBuilder  sb = new StringBuilder (b);
            int index= b.indexOf(";");
            sb.insert(index+1,"\n");
            String marStrNew = sb.toString();
            map.put("text2",marStrNew);
            map.put("text3",checkBeanList.get(i).getInspectTime());
            map.put("text4",checkBeanList.get(i).getBussinesstype());
            map.put("text5",checkBeanList.get(i).getConclusion());
            dataList.add(map);
        }

        return dataList;
    }
}
