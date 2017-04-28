package com.example.admin.housemanage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.CheckBaseInfoBean;
import bean.CheckBean;
import bean.CheckHeadBean;
import bean.CheckResultBean;
import bean.CheckTermBean;
import bean.CheckTermGroupBean;
import bean.InfoAttributeBean;
import bean.InspectHistoryBean;
import bean.InspectItemsResultBean;
import bean.PersonBean;
import bean.TableHeadBean;
import constants.Contant;
import util.Util;

/**
 * Created by admin on 2016/3/30.
 */
public abstract  class BaseActivity extends Activity implements View.OnClickListener {


    private TextView leftText;
    private  TextView centerText;
    private TextView rightText;
    private RelativeLayout relContent;
    private RelativeLayout titleView;

    public String noNetText = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baselayout);

        noNetText = getResources().getString(R.string.no_net);


        titleView = (RelativeLayout) findViewById(R.id.titleview);
        leftText = (TextView) findViewById(R.id.title_lefttext);
        centerText = (TextView) findViewById(R.id.title_centertext);
        rightText = (TextView) findViewById(R.id.title_righttext);
        relContent = (RelativeLayout) findViewById(R.id.contentview);
        leftText.setOnClickListener(this);
        rightText.setOnClickListener(this);
//        try {
        initView();
        initData();
//        }catch (OutOfMemoryError e){
//            ShowToast("内存不足");
//            ActivityManage.getInstance().exit();
//        }
//        catch(Exception e){
//            ActivityManage.getInstance().exit();
//        }

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }



    @Override
    protected void onResume() {
        super.onResume();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String nowTime = year + "-" + month + "-" + day+" "+hour+":"+minute+":"+second;

        if(Util.isRestart(Contant.loginTime,nowTime)){
            Intent reIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            reIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(reIntent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    protected InspectHistoryBean getData(CheckBean bean){
        InspectHistoryBean historyBean = new InspectHistoryBean();
        CheckBaseInfoBean checkBaseInfoBean = new CheckBaseInfoBean();
        checkBaseInfoBean.setState(bean.getState());
        checkBaseInfoBean.setInspectType(bean.getInspectType());
        checkBaseInfoBean.setSource(bean.getSource());
        checkBaseInfoBean.setSourceDetail(bean.getSourceDetail());
        checkBaseInfoBean.setObjectType(bean.getObjectType());
        checkBaseInfoBean.setConclusion(bean.getConclusion());
        checkBaseInfoBean.setConclusionOther(bean.getConclusionOther());
        checkBaseInfoBean.setDescription(bean.getDescription());
        checkBaseInfoBean.setPersonBeans(getPersonBean(bean.getInspector()));
        checkBaseInfoBean.setTreatment(bean.getTreatment());
        checkBaseInfoBean.setInspectTime(bean.getInspectTime());
        historyBean.setState(bean.getState());
        historyBean.setInspectNo(bean.getInspectNo());
        historyBean.setInspectGuid(bean.getInspectGuid());
        historyBean.setCheckBaseInfoBean(checkBaseInfoBean);
        historyBean.setBussinesstype(bean.getBussinesstype());
        historyBean.setName(bean.getObjectName());
        historyBean.setAddress(bean.getObjectAddress());
        historyBean.setInspectItemsResultBean(bean.getInspectItemsResult());
        historyBean.setId(getObjectId(bean.getInspectItemsResult()));
        historyBean.setRectifDeadline(bean.getRectifDeadline());
        historyBean.setDXSList(bean.getDXSList());
        historyBean.setFWList(bean.getFWList());
        historyBean.setNoticeState(bean.getNoticeState());
        historyBean.setSpotImgPaths("");
        historyBean.setRectifyImgPath("");
        historyBean.setVideoPaths("");
        historyBean.setPeronSiagnPath("");
        historyBean.setObjectSignPath("");
        historyBean.setFeedBackDes(bean.getFeedBackDes());
        historyBean.setFeedBackMan(bean.getFeedBackMan());
        historyBean.setFeedBackTime(bean.getFeedBackTime());
        historyBean.setCHKORSIGN(bean.getCHKORSIGN());
        historyBean.setOBJSIGN(bean.getOBJSIGN());

        return historyBean;
    }


    public List<PersonBean> getPersonBean(String personstr){
        List<PersonBean> personBeans = new ArrayList<>();
        String[] personss = personstr.split(";");
        if(personss!=null&&personss.length!=0){
            for(String person:personss){
                PersonBean personBean = new PersonBean();
                String[] ss = person.split(",");
                if(ss.length==4){//编号，姓名，执法证号，所在部门都有
                    personBean.setPID(ss[0]);
                    personBean.setName(ss[1]);
                    personBean.setCertNo(ss[2]);
                    personBean.setBranch(ss[3]);
                }else if(ss.length==2){//编号和姓名的，最早以前的版本
                    personBean.setPID(ss[0]);
                    personBean.setName(ss[1]);
                    personBean.setCertNo("");
                    personBean.setBranch("");
                }else if(ss.length==3){//编号，姓名，执法证号，中间有过的版本
                    personBean.setPID(ss[0]);
                    personBean.setName(ss[1]);
                    personBean.setCertNo(ss[2]);
                    personBean.setBranch("");
                }
                personBeans.add(personBean);
            }
        }
        return personBeans;
    }

    private String getObjectId(InspectItemsResultBean resultBean){
        String objectId = "";
        if(resultBean.getHead()!=null&&resultBean.getHead().size()!=0) {
            objectId = resultBean.getHead().get(0).getObjid();
        }
        return objectId;
    }

    protected List<TableHeadBean> getTableHead(List<CheckHeadBean> heads){
        List<TableHeadBean> tableheads = new ArrayList<>();
        if(heads!=null&&heads.size()!=0){
            for(CheckHeadBean head:heads){
                TableHeadBean tableHead = new TableHeadBean();
                tableHead.setName(head.getTitle());
                tableHead.setField(head.getDbfield());
                tableHead.setType(head.getBtype());
                tableheads.add(tableHead);
            }
        }
        return tableheads;
    }

    public List<InfoAttributeBean> getHeadData(List<InfoAttributeBean> attributeBeans,List<TableHeadBean> headBeans){
        List<InfoAttributeBean> infoList = new ArrayList<>();
        for(InfoAttributeBean attributeBean:attributeBeans){
            String attributeName = attributeBean.getName();
            for(TableHeadBean tableHeadBean:headBeans){
                String name = tableHeadBean.getName();
                if(attributeName.equals(name)){
                    infoList.add(attributeBean);
                    break;
                }
            }
        }
        return infoList;
    }

    public List<InfoAttributeBean> getInfoList(String result,String[] keys,String[] names){
        List<InfoAttributeBean> attributeBeans = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            for(int i=0;i<keys.length;i++){
                InfoAttributeBean attribute = new InfoAttributeBean();
                String key = keys[i];
                String name = names[i];
                String value = jsonObject.optString(key,"");
                attribute.setName(name);
                attribute.setValue(value);
                attribute.setKey(key);
                attributeBeans.add(attribute);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attributeBeans;
    }

    protected int getTermSize(List<CheckTermGroupBean> termGroupBeans){
        int count = 0;

        for(CheckTermGroupBean groupBean:termGroupBeans){
            count+=groupBean.getCheckTermBeans().size();
        }

        return count;
    }

    protected List<CheckTermGroupBean> getTermGroups(List<CheckResultBean> resultBeans){
        List<CheckTermGroupBean> termGroupBeans = new ArrayList<>();

        Map<String,List<CheckResultBean>> map=new HashMap<>();
        for(CheckResultBean resultBean:resultBeans){
            String inspectClass = resultBean.getInspectclass();
            List<CheckResultBean> list=null;
            if(!map.containsKey(inspectClass)){
                list=new ArrayList<>();
                list.add(resultBean);
                map.put(inspectClass,list);

            }else{
                map.get(resultBean.getInspectclass()).add(resultBean);
            }
        }
        Set<String> mapstr = map.keySet();
        for(String key:mapstr){
            CheckTermGroupBean groupBean = new CheckTermGroupBean();
            groupBean.setGroupName(key);
            List<CheckResultBean> checkResultBeans =  map.get(key);
            List<CheckTermBean> terms = new ArrayList<>();
            for(CheckResultBean bean:checkResultBeans){
                terms.add(resultToTermBean(bean));
            }
            groupBean.setCheckTermBeans(terms);
            termGroupBeans.add(groupBean);
        }
        return termGroupBeans;
    }

    private CheckTermBean resultToTermBean(CheckResultBean resultBean){
        CheckTermBean termBean = new CheckTermBean();

        termBean.setGuid(resultBean.getItemguid());
        termBean.setBussinesstype(resultBean.getBtype());
        termBean.setTitle(resultBean.getTitle());
        termBean.setInspectClass(resultBean.getInspectclass());
        termBean.setOptions(resultBean.getOptions());
        termBean.setIsInLaw(resultBean.getIsinlaw());
        termBean.setTextOptions(resultBean.getTextoptions());
        termBean.setTextTitles(resultBean.getTexttitle());
//        termBean.setTextType(resultBean.getT);
        termBean.setIllegalOptions(resultBean.getIllegaloptions());
        termBean.setEnterprisePoints(resultBean.getEnterpricepoints());
        termBean.setPerInChargePoints(resultBean.getPerinchargepoints());
        termBean.setTextneednum(resultBean.getTextneednum());
        termBean.setValue(resultBean.getValue());
        if("1".equals(resultBean.getIscorrected())){
            termBean.setIsXCZG(true);
        }else{
            termBean.setIsXCZG(false);
        }

        return termBean;
    }

    /**
     * 从检查项中获取到有问题的检查项
     * @param checkTermGroupBeans
     * @return
     */
    protected List<CheckTermBean> getIllegalTerm(List<CheckTermGroupBean> checkTermGroupBeans){
        List<CheckTermBean> termBeans = new ArrayList<>();
        if(checkTermGroupBeans!=null&& checkTermGroupBeans.size()>0){
            for(CheckTermGroupBean groupBean :checkTermGroupBeans){
                List<CheckTermBean> beans = groupBean.getCheckTermBeans();
                if(beans!=null&&beans.size()!=0){
                    for(CheckTermBean bean:beans){
                        if(bean.getValue()!=null&&!bean.getValue().equals("")){
                            String[] options = bean.getIllegalOptions().split(";");
                            if(options!=null&&options.length!=0){
                                for(String str:options){
                                    if(str.equals(bean.getValue())){
//                                    bean.setIsXCZG(false);
                                        bean.setIsShow(false);
                                        bean.setIsEdit(true);
                                        termBeans.add(bean);
                                        break;
                                    }
                                }
                            }else{
                                if(bean.getValue().equals(bean.getIllegalOptions())){
//                                bean.setIsXCZG(false);
                                    bean.setIsShow(false);
                                    bean.setIsEdit(true);
                                    termBeans.add(bean);
                                }
                            }
                        }
                    }
                }
            }
        }
        return termBeans;
    }


    /**
     * 初始化控件View
     */
    protected abstract void  initView();

    /**
     * 初始化数据
     */
    protected abstract void  initData();

    public void ShowToast(String title){
        Toast toast = Toast.makeText(BaseActivity.this, title, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * 为内容区域添加布局
     * @param relId
     */
    public void setContentLayout(int relId){
        View contentView = LayoutInflater.from(this).inflate(relId,null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relContent.addView(contentView, layoutParams);
    }

    public ProgressDialog showProgressDialog(){
        ProgressDialog proDialog = ProgressDialog.show(BaseActivity.this, "连接中..",
                "连接中..请稍后....", true, true);
        proDialog.setCanceledOnTouchOutside(false);
        return proDialog;
    }

    public FinalDb getFinalDb(Context context){
        FinalDb.DaoConfig daoConfig = new FinalDb.DaoConfig();
        daoConfig.setContext(context);
        daoConfig.setDbName(Contant.dbName);
        daoConfig.setDbVersion(Contant.dbVersion);
        daoConfig.setDebug(true);
        daoConfig.setDbUpdateListener(new FinalDb.DbUpdateListener() {
            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

                switch (i) {
                    case 1:
                        //删除这个表重建，为了在版本2中进行统一字段
                        String dropTable = "DROP TABLE CheckDBBean";
                        sqLiteDatabase.execSQL(dropTable);
                        break;
                    case 2:
                        String alterAdd = "alter table CheckDBBean add streetInfo varchar(255)";
                        sqLiteDatabase.execSQL(alterAdd);
                        String alterAdd1 = "alter table CheckDBBean add streetID varchar(255)";
                        sqLiteDatabase.execSQL(alterAdd1);
                        break;
                }
            }

        });
        FinalDb finalDb = FinalDb.create(daoConfig);
        return finalDb;
    }

    /**
     * 点击空白软键盘消失
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        jianpandelete();
        return super.onTouchEvent(event);
    }



    public void jianpandelete(){
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(getCurrentFocus()!=null){
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }




    /**
     * 为内容区域添加布局
     * @param view
     */
    public void setContentLayout(View view){
        relContent.addView(view);
    }

    /**
     * 获取内容布局View
     * @return
     */
    public View getContentView(){
        return relContent;
    }

    /**
     * 获取左边按钮View
     * @return
     */
    public TextView getCenterText(){
        return centerText;
    }

    /**
     * 获取左边按钮View
     * @return
     */
    public TextView getLeftText(){
        return leftText;
    }

    /**
     * 获取右边按钮View
     * @return
     */
    public TextView getRightText(){
        return rightText;
    }

    /**
     * 设置中间title内容
     * @param title
     */
    public void setTitle(String title){
        centerText.setText(title);
    }

    /**
     * 设置左边按钮背景
     * @param relId
     */
    public void setLeftBackGround(int relId){
        leftText.setBackgroundResource(relId);
    }

    /**
     * 设置左边按钮背景
     * @param drawable
     */
    public void setLeftBackGround(Drawable drawable){
        leftText.setBackground(drawable);
    }

    /**
     * 设置右边按钮背景
     * @param relId
     */
    public void setRightBackGround(int relId){
        rightText.setBackgroundResource(relId);
    }

    /**
     * 设置右边按钮 背景
     * @param drawable
     */
    public void setRightBackGround(Drawable drawable){
        rightText.setBackground(drawable);
    }

    /**
     * 设置左边按钮文字
     * @param text
     */
    public void setLeftText(String text){
        leftText.setText(text);
    }
    /**
     * 设置右边按钮文字
     * @param text
     */
    public void setRightText(String text){
        rightText.setText(text);
    }

    /**
     * 隐藏title栏
     */
    public void hideTitleView(){
        titleView.setVisibility(View.GONE);
    }

    /**
     * 隐藏左边按钮
     */
    public void hideLeftView(){
        leftText.setVisibility(View.GONE);
    }

    /**
     * 隐藏右边按钮
     */
    public void hideRightView(){
        rightText.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

    }
}
