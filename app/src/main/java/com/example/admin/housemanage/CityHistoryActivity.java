package com.example.admin.housemanage;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import adapter.InfoListAdapter;
import adapter.PersonAdapter;
import bean.CityCheckBean;
import bean.InfoAttributeBean;
import bean.PersonBean;
import widget.MyListView;

/**
 * Created by admin on 2016/11/25.
 */
public class CityHistoryActivity extends BaseActivity {

    private MyListView list_info;
    private EditText edit_content;
    private RadioGroup rg_opinion;
    private MyListView list_person;
    private TextView text_date;
    private Gson gson;
    private Intent intent;
    private CityCheckBean cityCheckBean;



    @Override
    protected void initView() {

        setTitle(getResources().getString(R.string.checkrecord));
        setRightText("");
        setRightBackGround(R.mipmap.main_logo);

        setContentLayout(R.layout.cityhistory_activity);

        list_info = (MyListView) findViewById(R.id.list_cityhistory_info);
        edit_content = (EditText) findViewById(R.id.edit_cityhistory_content);
        rg_opinion = (RadioGroup) findViewById(R.id.rg_cityhistory_opinion);
        list_person = (MyListView) findViewById(R.id.list_cityhistory_person);
        text_date = (TextView) findViewById(R.id.text_cityhistory_date);



    }

    @Override
    protected void initData() {
        gson = new Gson();
        intent = getIntent();
        String result = intent.getStringExtra("result");
        cityCheckBean = JSONJX(result);
        if(cityCheckBean!=null){
            edit_content.setText(cityCheckBean.getDescription());
            edit_content.setEnabled(false);

            setTreatment();
            setPersons();
            text_date.setText(cityCheckBean.getInspectTime());
            setInfo();
        }
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

    private CityCheckBean JSONJX(String result){

        if(result!=null&&!"".equals(result)){
            CityCheckBean cityCheckBean = new CityCheckBean();
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if(jsonObject.has("InspectItemsResult")){
                    JSONObject jsonObject1 = jsonObject.getJSONObject("InspectItemsResult");
                    cityCheckBean.setInspectItemsResult(jsonObject1.toString());
                }
                if(jsonObject.has("Description")){
                   cityCheckBean.setDescription(jsonObject.optString("Description"));
                }
                if(jsonObject.has("Treatment")){
                    cityCheckBean.setTreatment(jsonObject.optString("Treatment"));
                }
                if(jsonObject.has("inspector")){
                    cityCheckBean.setInspector(jsonObject.optString("inspector"));
                }
                if(jsonObject.has("inspectTime")){
                    cityCheckBean.setInspectTime(jsonObject.optString("inspectTime"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return cityCheckBean;
        }
        return null;
    }

    private void setInfo(){

        String infoStr = cityCheckBean.getInspectItemsResult().trim().replace("'","\"");
        try {
            JSONObject jsonObject = new JSONObject(infoStr);
            Iterator<String> keys = jsonObject.keys();

            List<InfoAttributeBean> attributeBeans = new ArrayList<>();
            while(keys.hasNext()){
                String key =  keys.next();
                String value = jsonObject.getString(key);
                if(value!=null&&!"".equals(value)){
                    InfoAttributeBean infoAttributeBean = new InfoAttributeBean();
                    infoAttributeBean.setName(key);
                    infoAttributeBean.setValue(value);
                    attributeBeans.add(infoAttributeBean);
                }
            }
            InfoListAdapter infoListAdapter = new InfoListAdapter(this,attributeBeans,1);
            list_info.setAdapter(infoListAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setTreatment(){
        String treatmentRecord = cityCheckBean.getTreatment();
        if(treatmentRecord.contains(getResources().getString(R.string.zlgz))){
            rg_opinion.check(R.id.rb_cityhistory_zlgz);
        }else if(treatmentRecord.contains(getResources().getString(R.string.jyxzcf))){
            rg_opinion.check(R.id.rb_cityhistory_jyxzcf);
        }else if(treatmentRecord.contains(getResources().getString(R.string.ybxzcf))){
            rg_opinion.check(R.id.rb_cityhistory_ybxzcf);
        }else{
            rg_opinion.check(R.id.rb_cityhistory_noproblem);
        }
        int count = rg_opinion.getChildCount();
        if(count>0){
            for(int i=0;i<count;i++){
                View view = rg_opinion.getChildAt(i);
                view.setEnabled(false);
            }
        }
    }


    private void setPersons(){
        List<PersonBean> personBeans = getPersonBean(cityCheckBean.getInspector());
        PersonAdapter personAdapter = new PersonAdapter(CityHistoryActivity.this,personBeans,false);
        list_person.setAdapter(personAdapter);

    }



}
