package com.example.admin.housemanage;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import adapter.CheckListExpandableAdapter;
import bean.CheckTermBean;
import bean.CheckTermGroupBean;
import constants.Contant;
import util.ActivityManage;
import widget.MyExpandableListView;

/**
 * 检查表单页
 * Created by admin on 2016/3/31.
 */
public class CheckListActivity extends BaseActivity {

    private Button bt_checklist_search;
    private EditText edit_checklist_search;
    private LinearLayout lin_checklist_content;
    private Handler mHandler;
    private Gson gson;
    private MyExpandableListView primeExpandListView;
    private MyExpandableListView propertyExpandListView;
    private MyExpandableListView generalExpandListView;
    private MyExpandableListView houseSafeExpandListView;

    private List<CheckTermGroupBean> primeGroups;
    private List<CheckTermGroupBean> propertyGroups;
    private List<CheckTermGroupBean> generalGroups;
    private List<CheckTermGroupBean> houseGroups;



    @Override
    protected void initView() {


        setTitle(getResources().getString(R.string.checktablelist));
        setContentLayout(R.layout.checklist_activity);
        ActivityManage.getInstance().addActivity(this);


        bt_checklist_search = (Button) findViewById(R.id.bt_checklist_search);
        edit_checklist_search = (EditText) findViewById(R.id.edit_checklist_search);
        lin_checklist_content = (LinearLayout) findViewById(R.id.lin_checklist_content);

        for(int i=0;i< Contant.enforceList.size();i++){
            String enforce = Contant.enforceList.get(i);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(this);
            textView.setText(enforce);
            textView.setTextSize(20.0f);
            textView.setBackgroundColor(getResources().getColor(R.color.gray));
            linearLayout.addView(textView,textParam);


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

            lin_checklist_content.addView(linearLayout);
        }

    }

    @Override
    protected void initData() {
        TextView titleText = getCenterText();
        titleText.setFocusable(true);
        titleText.setFocusableInTouchMode(true);
        titleText.requestFocus();
         gson = new Gson();

        for(int i=0;i<Contant.enforceList.size();i++){
            String enforce = Contant.enforceList.get(i);
            if(enforce.equals(getResources().getString(R.string.primebroker))&&Contant.checkedTerms.containsKey("prime")){
                List<CheckTermGroupBean> termGroupBeans = new ArrayList<>();
                termGroupBeans.addAll(Contant.checkedTerms.get("prime"));
                primeGroups = new ArrayList<>();
                primeGroups .addAll(getNewList(termGroupBeans));
                CheckListExpandableAdapter checkListExpandableAdapter = new CheckListExpandableAdapter(primeGroups,this,1);
                primeExpandListView.setAdapter(checkListExpandableAdapter);
            }else if(enforce.equals(getResources().getString(R.string.propertymanage))&&Contant.checkedTerms.containsKey("property")){
                List<CheckTermGroupBean> termGroupBeans = new ArrayList<>();
                termGroupBeans.addAll(Contant.checkedTerms.get("property"));
                propertyGroups = new ArrayList<>();
                propertyGroups .addAll(getNewList(termGroupBeans));
                CheckListExpandableAdapter checkListExpandableAdapter = new CheckListExpandableAdapter(propertyGroups,this,1);
                propertyExpandListView.setAdapter(checkListExpandableAdapter);
            }else if(enforce.equals(getResources().getString(R.string.generalbasement))&&Contant.checkedTerms.containsKey("general")){
                List<CheckTermGroupBean> termGroupBeans = new ArrayList<>();
                termGroupBeans.addAll(Contant.checkedTerms.get("general"));
                generalGroups = new ArrayList<>();
                generalGroups .addAll(getNewList(termGroupBeans));
                CheckListExpandableAdapter checkListExpandableAdapter = new CheckListExpandableAdapter(generalGroups,this,1);
                generalExpandListView.setAdapter(checkListExpandableAdapter);
            }else if(enforce.equals(getResources().getString(R.string.housesafeuse))&&Contant.checkedTerms.containsKey("houseSafe")){
                List<CheckTermGroupBean> termGroupBeans = new ArrayList<>();
                termGroupBeans.addAll(Contant.checkedTerms.get("houseSafe"));
                houseGroups = new ArrayList<>();
                houseGroups.addAll(getNewList(termGroupBeans));
                CheckListExpandableAdapter checkListExpandableAdapter = new CheckListExpandableAdapter(houseGroups,this,1);
                houseSafeExpandListView.setAdapter(checkListExpandableAdapter);
            }
        }
    }

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
//                Intent intent = new Intent(this,CheckResultActivity.class);
//                intent.setAction("checklist");
//                for(int i=0;i<Contant.enforceList.size();i++){
//                    String enforce = Contant.enforceList.get(i);
//                    if(enforce.equals(getResources().getString(R.string.primebroker))){
//                        String group = gson.toJson(primeGroups);
//                        intent.putExtra("checkTermPrime",group);
//                    }else if(enforce.equals(getResources().getString(R.string.propertymanage))){
//                        String group = gson.toJson(propertyGroups);
//                        intent.putExtra("checkTermProperty",group);
//                    }else if(enforce.equals(getResources().getString(R.string.generalbasement))){
//                        String group = gson.toJson(generalGroups);
//                        intent.putExtra("checkTermGeneral",group);
//                    }else if(enforce.equals(getResources().getString(R.string.housesafeuse))){
//                        String group = gson.toJson(houseGroups);
//                        intent.putExtra("checkTermHouse",group);
//                    }
//                }
//                this.startActivity(intent);
                break;
        }
    }
}
