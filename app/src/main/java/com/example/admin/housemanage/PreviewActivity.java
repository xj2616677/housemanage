package com.example.admin.housemanage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.IllegalAdapter;
import adapter.PersonSignAdapter;
import bean.CheckDBBean;
import bean.CheckTermBean;
import bean.HouseSafeBean;
import bean.InfoAttributeBean;
import bean.InspectBean;
import bean.InspectHistoryBean;
import bean.PrimePreviewBean;
import bean.PropertyBean;
import bean.PropertyPreviewBean;
import constants.Contant;
import util.ActivityManage;
import util.InstallPrint;
import util.NetUtil;
import util.RequestUtil;
import util.Util;
import widget.MyListView;

/**
 * Created by admin on 2016/5/31.
 */
public class PreviewActivity extends BaseActivity {

    private LinearLayout lin_propertyHead;
    private LinearLayout lin_projectName,lin_projectAddress;
    private LinearLayout lin_areaandequity,lin_projecttypeandmanager,lin_chargeandphone, lin_linkManAndPhone;
    private LinearLayout lin_general,lin_house;
    private LinearLayout lin_correctnow;

    private LinearLayout lin_primeHead;

    private TextView text_projectName,text_projectAddress,text_structurearea,text_equity,text_projectType,text_manager,text_charge,text_phone;
    private TextView text_general,text_house;
    private TextView text_linegeneral,text_linehouse;
    private TextView text_checktermShow;
    private TextView text_deadlinecorrect;
    private TextView text_linkMan,text_linkPhone;

    private TextView text_primeName,text_registerAddress,text_businessCode,text_recordCode,text_primeCharge,text_primeChargePhone,text_mobPhone;
    private TextView text_checkDate;
    private MyListView list_checkterm;
    private MyListView list_correctnow;

//    private ImageView img_checkedObject;
//    private ImageView img_checkPerson;
    private GridView grid_checkPerson;
    private GridView grid_checkedObject;
    private TextView text_branch;

    private TextView text_guid;



    private Gson gson;
    private List<CheckTermBean> illegalTerms;
    private List<InfoAttributeBean> infoList;
    private PrimePreviewBean primePreviewBean;
    private PropertyPreviewBean propertyPreviewBean;
    private int termCount = 0;
    private String xqDate = "";
    private InspectBean inspectBean;
    private CheckDBBean checkDBBean;
    private FinalDb finalDb;

    private ProgressDialog progressDialog;
    private Handler mHandler;

    private String inspectGuid = "";
    private ArrayList<String> bussinessType;
    private String action;
    private String DXSList;
    private String FWList;
    private String treatments = "";
    private String decribe = "";
    private String branchs = "";
    private String personSignUrl;
    private String objectSignUrl;
    private String inspectTime = "";
    private String inspectNo = "";
    private String objectName = "";

    private TextView text_treatment;


    @Override
    protected void initView() {
        setContentLayout(R.layout.preview_activity);
        setTitle("打印预览");
        setRightText("打印");
        ActivityManage.getInstance().addActivity(this);

        lin_propertyHead = (LinearLayout) findViewById(R.id.lin_preview_propertyhead);
        lin_projectName = (LinearLayout) findViewById(R.id.lin_preview_projectname);
        lin_projectAddress = (LinearLayout) findViewById(R.id.lin_preview_projectaddress);
        lin_areaandequity = (LinearLayout) findViewById(R.id.lin_preview_areaandequity);
        lin_projecttypeandmanager = (LinearLayout) findViewById(R.id.lin_preview_projecttypeandmanager);
        lin_chargeandphone = (LinearLayout) findViewById(R.id.lin_preview_chargeandphone);
        lin_general = (LinearLayout) findViewById(R.id.lin_preview_general);
        lin_house = (LinearLayout) findViewById(R.id.lin_preview_house);
        lin_correctnow = (LinearLayout) findViewById(R.id.lin_preview_correctnow);
        lin_linkManAndPhone = (LinearLayout) findViewById(R.id.lin_preview_linkmanandphone);

        lin_primeHead = (LinearLayout) findViewById(R.id.lin_preview_primehead);

        text_projectName = (TextView) findViewById(R.id.text_preview_projectname);
        text_projectAddress = (TextView) findViewById(R.id.text_preview_projectaddress);
        text_structurearea = (TextView) findViewById(R.id.text_preview_structurearea);
        text_equity = (TextView) findViewById(R.id.text_preview_equity);//产权方
        text_projectType = (TextView) findViewById(R.id.text_preview_projecttype);
        text_manager = (TextView) findViewById(R.id.text_preview_manager);
        text_charge = (TextView) findViewById(R.id.text_preview_chargeperson);
        text_phone = (TextView) findViewById(R.id.text_preview_chargephone);
        text_general = (TextView) findViewById(R.id.text_preview_general);
        text_house = (TextView) findViewById(R.id.text_preview_house);
        text_linkMan = (TextView) findViewById(R.id.text_preview_linkman);
        text_linkPhone = (TextView) findViewById(R.id.text_preview_linkphone);
        text_guid = (TextView) findViewById(R.id.text_preview_guid);

        text_primeName = (TextView) findViewById(R.id.text_preview_primename);
        text_registerAddress = (TextView) findViewById(R.id.text_preview_registeraddress);
        text_businessCode = (TextView) findViewById(R.id.text_preview_businesscode);
        text_recordCode = (TextView) findViewById(R.id.text_preview_recordcode);
        text_primeCharge = (TextView) findViewById(R.id.text_preview_primecharge);
        text_primeChargePhone = (TextView) findViewById(R.id.text_preview_primechargephone);
        text_mobPhone = (TextView) findViewById(R.id.text_preview_mobphone);

        text_linegeneral = (TextView) findViewById(R.id.text_preview_generalline);
        text_linehouse = (TextView) findViewById(R.id.text_preview_houseline);

        text_checktermShow = (TextView) findViewById(R.id.text_preview_checktermshow);
        list_checkterm = (MyListView) findViewById(R.id.list_preview_checkterm);
        list_correctnow = (MyListView) findViewById(R.id.list_preview_correctterm);

        text_deadlinecorrect = (TextView) findViewById(R.id.text_preview_deadlinecorrect);//限期整改
        text_treatment = (TextView) findViewById(R.id.text_preview_treatment);

        text_checkDate = (TextView) findViewById(R.id.text_preview_checkdate);

//        img_checkedObject = (ImageView) findViewById(R.id.img_preview_checkedobject);
//        img_checkPerson = (ImageView) findViewById(R.id.img_preview_checkperson);
        grid_checkPerson = (GridView) findViewById(R.id.grid_preview_checkPerson);
        grid_checkedObject = (GridView) findViewById(R.id.grid_preview_checkedobject);
//        img_checkedObject.setOnClickListener(this);
//        img_checkPerson.setOnClickListener(this);

        text_branch = (TextView) findViewById(R.id.text_preview_checkbranch);

    }

    @Override
    protected void initData() {

        gson = new Gson();
        Intent intent = getIntent();
        action = intent.getAction();
        illegalTerms = new ArrayList<>();
        infoList = new ArrayList<>();
        termCount = intent.getIntExtra("termCount", 0);
        bussinessType = intent.getStringArrayListExtra("bussinessType");
        String info = intent.getStringExtra("info");
        String illeaglTerm = intent.getStringExtra("illegalTerm");
        String date = intent.getStringExtra("xqDate");
        DXSList = intent.getStringExtra("previewDXSStr");
        FWList = intent.getStringExtra("previewFWStr");
        inspectGuid = intent.getStringExtra("inspectGuid");
        inspectTime = intent.getStringExtra("inspectTime");
        inspectNo = intent.getStringExtra("inspectNo");

        branchs = intent.getStringExtra("branchs");

        if("".equals(inspectNo)){
            text_guid.setText("检查单号:_____________");
        }else{
            text_guid.setText("检查单号:"+inspectNo);
        }

        if(action.equals("check")){
            String checkDB = intent.getStringExtra("DB");
            String inspect = intent.getStringExtra("inspect");
            inspectBean = gson.fromJson(inspect, new TypeToken<InspectBean>() {
            }.getType());
            checkDBBean = gson.fromJson(checkDB, new TypeToken<CheckDBBean>() {
            }.getType());
            objectName = inspectBean.getObjectName();
            treatments = inspectBean.getTreatment();
            decribe = inspectBean.getDescription();
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH)+1;
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            text_checkDate.setText("检查日期："+year+"年"+month+"月"+day+"日");
            text_checkDate.setText("检查日期："+inspectTime);
            if(!"".equals(date)){
                String[] strs = date.split("-");
                if(strs.length==3){
                    xqDate = strs[0]+"年"+strs[1]+"月"+strs[2]+"日";
                }
            }
//            if(Contant.objectBitmap!=null){
//                img_checkedObject.setImageBitmap(Contant.objectBitmap);
//            }
            if(Contant.personBitmapList!=null&&Contant.personBitmapList.size()!=0){
                PersonSignAdapter personSignAdapter = new PersonSignAdapter(this,Contant.personBitmapList);
                grid_checkPerson.setAdapter(personSignAdapter);
            }
            if(Contant.objectBitmapList!=null&&Contant.objectBitmapList.size()!=0){
                PersonSignAdapter objectSignAdapter = new PersonSignAdapter(this,Contant.objectBitmapList);
                grid_checkedObject.setAdapter(objectSignAdapter);
            }

        }else{
//            personSignUrl= intent.getStringExtra("personSign");
//            objectSignUrl = intent.getStringExtra("objectSign");
            decribe = intent.getStringExtra("decribe");
            objectName = intent.getStringExtra("objectName");
            String inspectTime = intent.getStringExtra("inspectTime");
            text_checkDate.setText("检查日期："+inspectTime);
            xqDate = date;
//
            treatments = intent.getStringExtra("treatments");
            if(treatments.contains(getResources().getString(R.string.xczg))){
                Contant.isXCZG = true;
            }
            if(treatments.contains(getResources().getString(R.string.xqzg))){
                Contant.isXQZG = true;
            }


//            if(Contant.objectBitmap!=null){
//                img_checkedObject.setImageBitmap(Contant.objectBitmap);
//            }
            if(Contant.personBitmapList!=null&&Contant.personBitmapList.size()!=0){
                PersonSignAdapter personSignAdapter = new PersonSignAdapter(this,Contant.personBitmapList);
                grid_checkPerson.setAdapter(personSignAdapter);
            }
            if(Contant.objectBitmapList!=null&&Contant.objectBitmapList.size()!=0){
                PersonSignAdapter objectSignAdapter = new PersonSignAdapter(this,Contant.objectBitmapList);
                grid_checkedObject.setAdapter(objectSignAdapter);
            }

//            if(Contant.personBitmap!=null){
//                img_checkPerson.setImageBitmap(Contant.personBitmap);
//            }
//
//            DisplayImageOptions options = new DisplayImageOptions.Builder()
//                    .cacheInMemory(false)
//                    .cacheOnDisk(false)
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .build();
//            if(!"".equals(personSignUrl)){
//                ImageLoader.getInstance().displayImage(personSignUrl,img_checkPerson,options);
//            }
//            if(!"".equals(objectSignUrl)){
//                ImageLoader.getInstance().displayImage(objectSignUrl,img_checkedObject,options);
//            }
//
//
        }

        grid_checkPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Contant.personBitmapList.size()>position){
                    Intent imgIntent = new Intent(PreviewActivity.this,SignShowActivity.class);
                    Contant.personBitmap = Contant.personBitmapList.get(position);
                    imgIntent.setAction("CheckPreview");
                    imgIntent.putExtra("index",1);
                    startActivity(imgIntent);
                }
            }
        });

        grid_checkedObject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Contant.objectBitmapList.size()>position){
                    Intent imgIntent = new Intent(PreviewActivity.this,SignShowActivity.class);
                    Contant.objectBitmap = Contant.objectBitmapList.get(position);
                    imgIntent.setAction("CheckPreview");
                    imgIntent.putExtra("index",1);
                    startActivity(imgIntent);
                }
            }
        });

        List<CheckTermBean> checkTermBeans = gson.fromJson(illeaglTerm, new TypeToken<List<CheckTermBean>>() {
        }.getType());
        List<InfoAttributeBean> infoBeans = gson.fromJson(info,new TypeToken<List<InfoAttributeBean>>(){}.getType());
        illegalTerms.addAll(checkTermBeans);

        if(decribe!=null&&!"".equals(decribe)){
            CheckTermBean termBean = new CheckTermBean();
            termBean.setTitle(decribe);
            termBean.setValue("");
            termBean.setIsDescribe(true);
            illegalTerms.add(termBean);
        }

        infoList.addAll(infoBeans);
        getInfoBeans();
        setHeadView();
        setIllegalTermView();



        text_branch.setText(branchs);



//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH)+1;
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        text_checkDate.setText("检查日期："+year+"年"+month+"月"+day+"日");



        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Object result = msg.obj;
                switch (msg.what){
                    case 1:
                        JSONObject jsonObject = null;
                        String isSuccess = "";
                        String failReason = "";
                        String guid = "";

                        try {
                            jsonObject = new JSONObject((String) result);
                            isSuccess = jsonObject.getString("result");
                            failReason = jsonObject.getString("failReason");
                            guid = jsonObject.getString("succeedString");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (isSuccess.equals("1")) {
                            inspectGuid = guid;
                            if(NetUtil.isNetworkAvailable(PreviewActivity.this)){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Map<String,String> params = new HashMap<String, String>();
                                        params.put("inspectGuid", inspectGuid);
                                        String creatwordResult = RequestUtil.post(RequestUtil.CheckTableToWord,params);
                                        Message msg = Message.obtain();
                                        msg.what = 2;
                                        msg.obj = creatwordResult;
                                        mHandler.sendMessage(msg);
                                    }
                                }).start();
                            }else{
                                ShowToast(noNetText);
                            }
                        } else if (isSuccess.equals("0")) {
                            if (finalDb == null) {
                                finalDb = getFinalDb(PreviewActivity.this);
                            }
                            finalDb.save(checkDBBean);
                            ShowToast("由于网络原因，上传服务器失败，已为您保存到本地，可在本地记录中查看");
                        }
                        break;
                    case 2:
                        try {
                            JSONObject object = new JSONObject((String) result);
                            String isSuccesss = object.getString("result");
                            if (isSuccesss.equals("0")) {
                                progressDialog.dismiss();
                                String failReasons = object.getString("failReason");
                                ShowToast(failReasons);
                            } else if (isSuccesss.equals("1")) {
                                final String url = object.getString("succeedString");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String[] split = url.split("\\/");
                                        String filePath = Contant.filepathword + split[split.length - 1];

                                        boolean isLoad = RequestUtil.downLoadFile(filePath, url);
                                        Message msg = Message.obtain();
                                        msg.what = 3;
                                        if(isLoad){
                                            msg.arg1 = 1;
                                        }else{
                                            msg.arg1 = 0;
                                        }
                                        msg.obj = filePath;
                                        mHandler.sendMessage(msg);
                                    }
                                }).start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        progressDialog.dismiss();
                        if (msg.arg1==1) {
                            ShowToast("word文件下载成功");
                            boolean isLaunch = Util.launchApp(PreviewActivity.this,(String)result);
                            if (!isLaunch) {
                                InstallPrint installPrint = new InstallPrint(PreviewActivity.this);
                                installPrint.checkUpdate();
                            }
                        } else {
                            ShowToast("word下载失败，请检查网络连接");
                        }
                        break;
                }
            }
        };
    }




    private void getInfoBeans(){

        if(bussinessType.contains(getResources().getString(R.string.primebroker))){
            primePreviewBean = new PrimePreviewBean();
            for(InfoAttributeBean info: infoList){
                String name = info.getName();
                String value = info.getValue();
                if("经纪机构名称".equals(name)){
                    primePreviewBean.setPrimeName(value);
                }
                if("注册地址".equals(name)){
                    primePreviewBean.setRegisterAddress(value);
                }
                if("营业执照号".equals(name)){
                    primePreviewBean.setBusinessCode(value);
                }
                if("备案证明号".equals(name)){
                    primePreviewBean.setRecordCode(value);
                }
                if("机构法人/负责人".equals(name)){
                    primePreviewBean.setCharge(value);
                }
                if("机构法人/负责人电话".equals(name)){
                    primePreviewBean.setChargePhone(value);
                }
                if("联系电话".equals(name)){
                    primePreviewBean.setMobPhone(value);
                }
            }
        }else{
            propertyPreviewBean = new PropertyPreviewBean();
            for(InfoAttributeBean infoBean:infoList){
                String name = infoBean.getName();
                String value = infoBean.getValue();
                if("项目名称".equals(name)){
                    propertyPreviewBean.setProjectName(value);
                }
                if("项目地址".equals(name)){
                    propertyPreviewBean.setProjectAddress(value);
                }
                if("管理方".equals(name)){
                    propertyPreviewBean.setManager(value);
                }
                if("项目负责人".equals(name)){
                    propertyPreviewBean.setChargeperson(value);
                }
                if("项目负责人电话".equals(name)){
                    propertyPreviewBean.setChargephone(value);
                }
                if("总建筑面积".equals(name)){
                    propertyPreviewBean.setStructurearea(value);
                }
                if("产权方".equals(name)){
                    propertyPreviewBean.setEquity(value);
                }
                if("项目联系人".equals(name)){
                    propertyPreviewBean.setLinkman(value);
                }
                if("联系人电话".equals(name)){
                    propertyPreviewBean.setLinkphone(value);
                }
                if("项目类型".equals(name)){
                    propertyPreviewBean.setProjecttype(value);
                }

//                if(action.equals("check")){
//                    PropertyBean propertyBean = Contant.propertyObjectList.get(0);
//                    if(propertyBean.getGeneralBeans().size()==0){
//                        propertyPreviewBean.setGeneralSize("该项目下的所有地下室");
//                    }else{
//                        propertyPreviewBean.setGeneralSize(propertyBean.getGeneralBeans().size()+"处");
//                    }
//
//                    StringBuilder stringBuilder = new StringBuilder();
//                    if(propertyBean.getHouseBeans().size()!=0){
//                        for(int i=0;i<propertyBean.getHouseBeans().size();i++){
//                            HouseSafeBean houseBean = propertyBean.getHouseBeans().get(i);
//                            if(i==0){
//                                stringBuilder.append(houseBean.getBUILD_SITE());
//                            }else{
//                                stringBuilder.append("、"+houseBean.getBUILD_SITE());
//                            }
//                        }
//                        propertyPreviewBean.setHouseName(stringBuilder.toString());
//                    }else{
//                        propertyPreviewBean.setHouseName("该项目下的所有楼栋");
//                    }
//                }else{
//                    propertyPreviewBean.setGeneralSize("");
//                    propertyPreviewBean.setHouseName("");
//                }
                propertyPreviewBean.setGeneralSize(DXSList);
                propertyPreviewBean.setHouseName(FWList);
            }
        }
    }


    private void setHeadView(){

        if(bussinessType.contains(getResources().getString(R.string.primebroker))){
            lin_primeHead.setVisibility(View.VISIBLE);
            lin_propertyHead.setVisibility(View.GONE);
            text_primeName.setText(primePreviewBean.getPrimeName());
            text_registerAddress.setText(primePreviewBean.getRegisterAddress());
            text_businessCode.setText(primePreviewBean.getBusinessCode());
            text_recordCode.setText(primePreviewBean.getRecordCode());
            text_primeCharge.setText(primePreviewBean.getCharge());
            text_primeChargePhone.setText(primePreviewBean.getChargePhone());
            text_mobPhone.setText(primePreviewBean.getMobPhone());
            int size = 0;
            if(decribe!=null&&!"".equals(decribe)){
                 size = illegalTerms.size()-1;
            }else{
                size = illegalTerms.size();
            }
            if(size==0){

                text_checktermShow.setText("针对经纪机构等方面进行了检查，检查项中未发现问题。");
            }else{
                text_checktermShow.setText("针对经纪机构等方面进行了检查，其中发现问题共"+size+"项，如下:");
            }
        }else{
            lin_primeHead.setVisibility(View.GONE);
            lin_propertyHead.setVisibility(View.VISIBLE);
            text_projectName.setText(propertyPreviewBean.getProjectName());
            text_projectAddress.setText(propertyPreviewBean.getProjectAddress());
            text_structurearea.setText(propertyPreviewBean.getStructurearea());
            text_equity.setText(propertyPreviewBean.getEquity());
            text_projectType.setText(propertyPreviewBean.getProjecttype());
            text_manager.setText(propertyPreviewBean.getManager());
            text_charge.setText(propertyPreviewBean.getChargeperson());
            text_phone.setText(propertyPreviewBean.getChargephone());
            text_linkMan.setText(propertyPreviewBean.getLinkman());
            text_linkPhone.setText(propertyPreviewBean.getLinkphone());
            text_general.setText(propertyPreviewBean.getGeneralSize());
            text_house.setText(propertyPreviewBean.getHouseName());
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0;i<bussinessType.size();i++){
                if(i==0){
                    stringBuilder.append(bussinessType.get(i));
                }else{
                    stringBuilder.append("、"+bussinessType.get(i));
                }
            }
            int size = 0;
            if(decribe!=null&&!"".equals(decribe)){
                size = illegalTerms.size()-1;
            }else{
                size = illegalTerms.size();
            }
            if(size==0){
                text_checktermShow.setText("针对"+stringBuilder.toString()+"等方面进行了检查，检查项中未发现问题。");
            }else{
                text_checktermShow.setText("针对"+stringBuilder.toString()+"等方面进行了检查，其中发现问题共"+illegalTerms.size()+"项，如下:");
            }

            if(!bussinessType.contains(getResources().getString(R.string.generalbasement))){
                lin_general.setVisibility(View.GONE);
                text_linegeneral.setVisibility(View.GONE);
            }
            if(!bussinessType.contains(getResources().getString(R.string.housesafeuse))){
                lin_house.setVisibility(View.GONE);
                text_linehouse.setVisibility(View.GONE);
            }
        }
    }

    private void setIllegalTermView(){
        IllegalAdapter illegalAdapter = new IllegalAdapter(this,illegalTerms,2);
        list_checkterm.setAdapter(illegalAdapter);
        if(Contant.isXCZG||Contant.isXQZG){
            text_treatment.setVisibility(View.GONE);
            if(Contant.isXCZG){
                lin_correctnow.setVisibility(View.VISIBLE);
                List<CheckTermBean> correctNowTerm = new ArrayList<>();
                for(CheckTermBean bean:illegalTerms){
                    if(bean.isXCZG()){
                        correctNowTerm.add(bean);
                    }
                }
                IllegalAdapter illegalAdapterCorrect = new IllegalAdapter(this,correctNowTerm,3);
                list_correctnow.setAdapter(illegalAdapterCorrect);
            }else{
                lin_correctnow.setVisibility(View.GONE);
            }
            if(Contant.isXQZG){
                text_deadlinecorrect.setVisibility(View.VISIBLE);
                text_deadlinecorrect.setText("限期整改：请于"+xqDate+"前对未处理问题完成整改。");
            }else{
                text_deadlinecorrect.setVisibility(View.GONE);
            }
        }else{
            lin_correctnow.setVisibility(View.GONE);
            text_deadlinecorrect.setVisibility(View.GONE);
            text_treatment.setVisibility(View.VISIBLE);
            text_treatment.setText(treatments);
//            text_deadlinecorrect.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
//            case R.id.img_preview_checkedobject:
//
//                Intent imgIntentobject = new Intent(this,SignShowActivity.class);
////                if("check".equals(action)){
//                    imgIntentobject.setAction("CheckPreview");
//
////                }else{
////                    imgIntentobject.setAction("preview");
////                }
//                imgIntentobject.putExtra("index",2);
//                imgIntentobject.putExtra("url", objectSignUrl);
//                startActivity(imgIntentobject);
//
//                break;
//            case R.id.img_preview_checkperson:
//
//                Intent imgIntent = new Intent(this,SignShowActivity.class);
////                if("check".equals(action)){
//                    imgIntent.setAction("CheckPreview");
////                }else{
////
////                    imgIntent.setAction("preview");
////                }
//                imgIntent.putExtra("index", 1);
//                imgIntent.putExtra("url", personSignUrl);
//                startActivity(imgIntent);
//
//                break;
            case R.id.title_lefttext:
                finish();
                break;
            case R.id.title_righttext:

                String fileName = inspectNo+"_"+objectName+".docx";
                String filePath =Contant.filepathword+fileName;
                File file = new File(filePath);

                if(file.exists()){
                    boolean isLaunch = Util.launchApp(PreviewActivity.this,filePath);
                    if (!isLaunch) {
                        InstallPrint installPrint = new InstallPrint(PreviewActivity.this);
                        installPrint.checkUpdate();
                    }
                }else {
                    if (NetUtil.isNetworkAvailable(this)) {
                        if (progressDialog == null) {
                            progressDialog = showProgressDialog();
                            progressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                            progressDialog.setCancelable(false);
                        } else {
                            progressDialog.show();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                if ("".equals(inspectNo)) {
                                    String tableInfo = gson.toJson(inspectBean);
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("loginName", Contant.userid);
                                    params.put("tableInfo", tableInfo);
                                    params.put("x", Contant.longitube);
                                    params.put("y", Contant.latitube);
                                    String result = RequestUtil.post(RequestUtil.SaveNewCheckTable, params);
                                    Message msg = Message.obtain();
                                    msg.what = 1;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                } else {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("inspectGuid", inspectGuid);
                                    String creatwordResult = RequestUtil.post(RequestUtil.CheckTableToWord, params);
                                    Message msg = Message.obtain();
                                    msg.what = 2;
                                    msg.obj = creatwordResult;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }).start();
                    } else {
                        if (action.equals("check")) {
                            if (finalDb == null) {
                                finalDb = getFinalDb(PreviewActivity.this);
                            }
                            finalDb.save(checkDBBean);
                            ShowToast("由于网络原因，上传服务器失败，已为您保存到本地，可在本地记录中查看");
                        } else {
                            ShowToast(noNetText);
                        }
                    }
                }
                break;
        }
    }
}
