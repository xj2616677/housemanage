package com.example.admin.housemanage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.HisroryInfoAdapter;
import bean.CheckBean;
import bean.CheckHeadBean;
import bean.CheckResultBean;
import bean.CheckTermBean;
import bean.CityCheckBean;
import bean.HistoryBean;
import bean.InfoAttributeBean;
import bean.InspectHistoryBean;
import bean.PersonBean;
import bean.TableHeadBean;
import constants.Contant;
import shareutil.Bimp;
import util.ActivityManage;
import util.NetUtil;
import util.RequestUtil;


/**
 * Created by Administrator on 2016/4/15.
 */
public class HistoryInfoActivity extends BaseActivity {
    private ListView listView;
    private Gson gson;

    private String bussinessType;
    private String brokerId;
    private String DXSList;
    private String FWList;
    private String xqDate;
    private String branchs = "";
    private List<TableHeadBean> headList;
    private List<InfoAttributeBean> infoList;
    private List<CheckTermBean> illegalTerm;
    private InspectHistoryBean inspectHistoryBean;
    private String inspectTime;
    private String inspectGuid;
    private ProgressDialog progressDialog;
    private Handler mHandler;

    private List<HistoryBean> historys;
    private HistoryBean historyBean;
    private String action  = "";
    private HisroryInfoAdapter hisroryInfoAdapter;
    private String startTime,endTime, houseManager,streetName,bType,source,analyType,branchName;
    private String inspector = "";
    private String objectName = "";
    private String index;

    private TextView text_count;


    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.historyinfo));
        hideRightView();
        setContentLayout(R.layout.historyinformation);
        ActivityManage.getInstance().addActivity(this);

        listView=(ListView)findViewById(R.id.listview_historyinfo);
        text_count = (TextView) findViewById(R.id.text_historyinfo_count);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bimp.selectBitmaps.clear();
        Contant.infoAttributeBeans.clear();
        Bimp.isCarame = true;
        Bimp.tempSelectBitmap0.clear();
        ImageLoader.getInstance().clearDiskCache();
        Contant.personBitmapList.clear();
        Contant.objectBitmapList.clear();
        System.gc();
    }

    @Override
    protected void initData() {


        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                if(result==null||"".equals(result)){
                    progressDialog.dismiss();
                    ShowToast("连接服务器失败");
                }else if("[]".equals(result)){
                    progressDialog.dismiss();
                    ShowToast("没有查询到数据");
                }else{
                    switch (msg.what) {
                        case 1:
                            int arg2 = msg.arg2;
                            String[] keys = null;
                            String[] names = null;
                            if (arg2 == 3) {
                                keys = getResources().getStringArray(R.array.primeinfokey);
                                names = getResources().getStringArray(R.array.primeinfoname);
                            } else if (arg2 == 4) {
                                keys = getResources().getStringArray(R.array.propertyinfokey);
                                names = getResources().getStringArray(R.array.propertyinfoname);
                            }
                            List<InfoAttributeBean> attributeBeans = getInfoList(result, keys, names);
                            infoList = getHeadData(attributeBeans, headList);
                            String info = gson.toJson(infoList);
                            List<CheckResultBean> resultBeans = inspectHistoryBean.getInspectItemsResultBean().getResult();
                            illegalTerm = getIllegalTerm(getTermGroups(resultBeans));
                            String illegaltermstr = gson.toJson(illegalTerm);
                            ArrayList<String> bussinessList = new ArrayList<>();
                            String[] busstr = bussinessType.split(",");
                            if (busstr != null && busstr.length != 0) {
                                for (String str : busstr) {
                                    bussinessList.add(str);
                                }
                            } else {
                                bussinessList.add(bussinessType);
                            }

                            progressDialog.dismiss();
                            Intent wordIntent = new Intent(HistoryInfoActivity.this, PreviewActivity.class);
                            wordIntent.setAction("checkRecord");
                            wordIntent.putExtra("info", info);
                            wordIntent.putExtra("illegalTerm", illegaltermstr);
                            wordIntent.putExtra("termCount", getTermSize(getTermGroups(resultBeans)));
                            wordIntent.putExtra("xqDate", xqDate);
                            wordIntent.putExtra("inspectTime", inspectTime);
                            wordIntent.putExtra("inspectGuid", inspectGuid);
                            wordIntent.putExtra("DXSList", DXSList);
                            wordIntent.putExtra("FWList", FWList);
                            wordIntent.putExtra("treatments", inspectHistoryBean.getCheckBaseInfoBean().getTreatment());
                            wordIntent.putStringArrayListExtra("bussinessType", bussinessList);
                            HistoryInfoActivity.this.startActivity(wordIntent);
                            break;
                        case 2:

                            String[] arrays = result.split("\\^_\\^");
                            String jsonResult = "";
                            if(arrays!=null&&arrays.length==2){
                                 jsonResult = arrays[0];
                                String branchJson = arrays[1];
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(branchJson);
                                    branchs = ((JSONObject)jsonArray.get(0)).getString("branchname");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            List<CheckBean> checkBeans = gson.fromJson(jsonResult,new TypeToken<List<CheckBean>>(){}.getType());
                            if(checkBeans!=null&&checkBeans.size()!=0) {
                                CheckBean checkBean = checkBeans.get(0);
                                inspectHistoryBean = getData(checkBean);
                                inspectHistoryBean.setBranchs(branchs);
                                bussinessType = inspectHistoryBean.getBussinesstype();
                                headList = getTableHead(inspectHistoryBean.getInspectItemsResultBean().getHead());
                                brokerId = inspectHistoryBean.getId();
                                DXSList = inspectHistoryBean.getDXSList();
                                FWList = inspectHistoryBean.getFWList();
                                xqDate = inspectHistoryBean.getRectifDeadline();
                                inspectGuid = inspectHistoryBean.getInspectGuid();
                                inspectTime = inspectHistoryBean.getCheckBaseInfoBean().getInspectTime();
//                                if (inspectHistoryBean.getBussinesstype().contains("其他")||(Contant.userid.equals(historyBean.getAddUser().toLowerCase())&&"0".equals(historyBean.getNoticeState())&&Integer.parseInt(historyBean.getState())< 3)) {

                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    Intent intent = new Intent(HistoryInfoActivity.this, CommitActivity.class);
                                    intent.setAction("checkRecordNO");
                                    String history = gson.toJson(inspectHistoryBean);
                                    intent.putExtra("history", history);
                                    List<CheckResultBean> results = inspectHistoryBean.getInspectItemsResultBean().getResult();
                                    List<CheckHeadBean> heads = inspectHistoryBean.getInspectItemsResultBean().getHead();
                                    String resultstr = gson.toJson(results);
                                    String headstr = gson.toJson(heads);
                                    intent.putExtra("inspectGuid", inspectHistoryBean.getInspectGuid());
                                    intent.putExtra("checklist", resultstr);
                                    if ("1".equals(inspectHistoryBean.getNoticeState())||!Contant.userid.equals(historyBean.getAddUser())) {
                                        intent.putExtra("isEnd", "true");
                                    } else {
                                        intent.putExtra("isEnd", "false");
                                    }
                                    intent.putExtra("headlist", headstr);
                                    HistoryInfoActivity.this.startActivity(intent);
//                                } else {
//
//                                    bussinessType = inspectHistoryBean.getBussinesstype();
//                                    headList = getTableHead(inspectHistoryBean.getInspectItemsResultBean().getHead());
//                                    brokerId = inspectHistoryBean.getId();
//                                    DXSList = inspectHistoryBean.getDXSList();
//                                    FWList = inspectHistoryBean.getFWList();
//                                    xqDate = inspectHistoryBean.getRectifDeadline();
//                                    inspectGuid= inspectHistoryBean.getInspectGuid();
//                                    inspectTime = inspectHistoryBean.getCheckBaseInfoBean().getInspectTime();
////                                    if(inspectHistoryBean!=null){
////                                        List<PersonBean> personBeans = inspectHistoryBean.getCheckBaseInfoBean().getPersonBeans();
////                                        branchs = getCheckBranch(personBeans);
////                                    }
//                                    List<CheckHeadBean>  checkHeadBeans =  inspectHistoryBean.getInspectItemsResultBean().getHead();
//
//                                    String[] keys1 = null;
//                                    String[] names1 = null;
//                                    if (bussinessType.contains(getResources().getString(R.string.primebroker))) {
//                                        keys1 = getResources().getStringArray(R.array.primeinfokey);
//                                        names1 = getResources().getStringArray(R.array.primeinfoname);
//                                    } else {
//                                        keys1 = getResources().getStringArray(R.array.propertyinfokey);
//                                        names1 = getResources().getStringArray(R.array.propertyinfoname);
//                                    }
//
//                                    if(infoList==null){
//                                        infoList = new ArrayList<>();
//                                    }else{
//                                        infoList.clear();
//                                    }
//                                    infoList.addAll(getInfoList(checkHeadBeans,keys1,names1));
//
//                                    String info1 = gson.toJson(infoList);
//                                    List<CheckResultBean> resultBeans1 = inspectHistoryBean.getInspectItemsResultBean().getResult();
//                                    illegalTerm = getIllegalTerm(getTermGroups(resultBeans1));
//                                    String illegaltermstr1 = gson.toJson(illegalTerm);
//                                    ArrayList<String> bussinessList1 = new ArrayList<>();
//                                    String[] busstr1 = bussinessType.split(",");
//                                    if (busstr1 != null && busstr1.length != 0) {
//                                        for (String str : busstr1) {
//                                            bussinessList1.add(str);
//                                        }
//                                    } else {
//                                        bussinessList1.add(bussinessType);
//                                    }
//                                    if (progressDialog != null) {
//                                        progressDialog.dismiss();
//                                    }
//
//                                    String personSign = inspectHistoryBean.getCHKORSIGN();
//                                    String objectSign = inspectHistoryBean.getOBJSIGN();
//
//                                    if(personSign==null){
//                                        personSign = "";
//                                    }
//
//                                    if(objectSign==null){
//                                        objectSign = "";
//                                    }
//
//                                    Intent wordIntent1 = new Intent(HistoryInfoActivity.this, PreviewActivity.class);
//                                    wordIntent1.setAction("checkRecord");
//                                    wordIntent1.putExtra("info", info1);
//                                    wordIntent1.putExtra("illegalTerm", illegaltermstr1);
//                                    wordIntent1.putExtra("termCount", getTermSize(getTermGroups(resultBeans1)));
//                                    wordIntent1.putExtra("xqDate", xqDate);
//                                    wordIntent1.putExtra("inspectTime", inspectTime);
//                                    wordIntent1.putExtra("inspectGuid", inspectGuid);
//                                    wordIntent1.putExtra("DXSList", DXSList);
//                                    wordIntent1.putExtra("FWList", FWList);
//                                    wordIntent1.putExtra("branchs",branchs);
//                                    wordIntent1.putExtra("personSign",personSign);
//                                    wordIntent1.putExtra("objectSign",objectSign);
//                                    wordIntent1.putExtra("decribe",inspectHistoryBean.getCheckBaseInfoBean().getDescription());
//                                    wordIntent1.putExtra("treatments", inspectHistoryBean.getCheckBaseInfoBean().getTreatment());
//                                    wordIntent1.putStringArrayListExtra("bussinessType", bussinessList1);
//                                    HistoryInfoActivity.this.startActivity(wordIntent1);
//
//
////                                    if (NetUtil.isNetworkAvailable(HistoryInfoActivity.this)) {
////                                        if (progressDialog == null) {
////                                            progressDialog = showProgressDialog();
////                                        } else {
////                                            if (!progressDialog.isShowing()) {
////                                                progressDialog.show();
////                                            }
////                                        }
////                                        new Thread(new Runnable() {
////                                            @Override
////                                            public void run() {
////                                                String result = "";
////                                                Message msg = Message.obtain();
////                                                if (bussinessType.contains(getResources().getString(R.string.primebroker))) {
////                                                    Map<String, String> params = new HashMap<>();
////                                                    params.put("idlist", brokerId);
////                                                    result = RequestUtil.post(RequestUtil.GetJJJGDetail, params);
////                                                    msg.what = 1;
////                                                    msg.arg2 = 3;
////                                                    //经纪机构
////                                                } else {
////                                                    Map<String, String> params = new HashMap<>();
////                                                    params.put("proID", brokerId);
////                                                    params.put("DXSIDList", DXSList);
////                                                    params.put("FWList", FWList);
////                                                    result = RequestUtil.post(RequestUtil.GetProDXSFWHeadInfo, params);
////                                                    msg.what = 1;
////                                                    msg.arg2 = 4;
////                                                    //物业管理
////                                                }
////                                                msg.obj = result;
////                                                mHandler.sendMessage(msg);
////                                            }
////                                        }).start();
////                                    } else {
////                                        if (progressDialog != null) {
////                                            progressDialog.dismiss();
////                                        }
////                                        ShowToast(noNetText);
////                                    }
//                                }
                            }
                            break;
                        case 3:
                            String[] array = result.split("\\+");
                            if(array!=null&&array.length>=2) {
                                text_count.setText(array[array.length-1]);
                                String resultNew ="";
                                for(int i=0;i<array.length;i++){
                                    if(i==0){
                                        resultNew += array[i];
                                    }else if(i!=array.length-1){
                                        resultNew += "+"+array[i];
                                    }
                                }

                                List<HistoryBean> beans = gson.fromJson(resultNew, new TypeToken<List<HistoryBean>>() {
                                }.getType());
                                historys.clear();
                                historys.addAll(beans);
                                hisroryInfoAdapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                            break;
                        case 4:

                            if(progressDialog!=null&& progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Intent cityIntent = new Intent(HistoryInfoActivity.this,CityHistoryActivity.class);
                            cityIntent.putExtra("result",result);
                            HistoryInfoActivity.this.startActivity(cityIntent);





                            break;
                    }
                }
            }
        };
        gson = new Gson();

        historys = new ArrayList<>();
        hisroryInfoAdapter= new HisroryInfoAdapter(this,historys);
        listView.setAdapter(hisroryInfoAdapter);

        Intent intent =getIntent();
        action = intent.getAction();
        if(action.equals("mapTrack")){
            inspector = intent.getStringExtra("inspector");
            startTime =intent.getStringExtra("startDate");
            endTime = intent.getStringExtra("endDate");
            if(NetUtil.isNetworkAvailable(HistoryInfoActivity.this)) {
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
                        params.put("btype","");
                        params.put("inspectType","");
                        params.put("objName","");
                        params.put("inspector",inspector);
                        params.put("startDate",startTime);
                        params.put("endDate",endTime);
                        String result = RequestUtil.post(RequestUtil.GetInspectHistory,params);
                        Message msg = Message.obtain();
                        msg.what = 3;
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


        }else if(action.equals("analy")){
            houseManager = intent.getStringExtra("houseManager");
            branchName = intent.getStringExtra("branchName");
            streetName = intent.getStringExtra("streetName");
            objectName = intent.getStringExtra("objectName");
            bType = intent.getStringExtra("bType");
            inspector = intent.getStringExtra("inspector");
            source = intent.getStringExtra("source");
            startTime =intent.getStringExtra("startTime");
            endTime = intent.getStringExtra("endTime");
            index = intent.getStringExtra("index");

            if(NetUtil.isNetworkAvailable(HistoryInfoActivity.this)) {
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
                        params.put("houseManager",houseManager);
                        params.put("branchName",branchName);
                        params.put("streetName",streetName);
                        params.put("objName",objectName);
                        params.put("bType",bType);
                        params.put("inspector",inspector);
                        params.put("source",source);
                        params.put("startTime",startTime);
                        params.put("endTime",endTime);
                        params.put("index",index);
                        String result = RequestUtil.post(RequestUtil.GetInspectHistoryByParamsInStatic,params);
                        Message msg = Message.obtain();
                        msg.what = 3;
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

        }else if("analyJJJGHead".equals(action)){
            houseManager = intent.getStringExtra("houseManager");
            branchName = intent.getStringExtra("branchName");
            streetName = intent.getStringExtra("streetName");
            objectName = intent.getStringExtra("objectName");
            bType = intent.getStringExtra("bType");
            inspector = intent.getStringExtra("inspector");
            source = intent.getStringExtra("source");
            startTime =intent.getStringExtra("startTime");
            endTime = intent.getStringExtra("endTime");
            index = intent.getStringExtra("index");
            if(NetUtil.isNetworkAvailable(HistoryInfoActivity.this)) {
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
                        params.put("houseManager",houseManager);
                        params.put("branchName",branchName);
                        params.put("streetName",streetName);
                        params.put("objName",objectName);
                        params.put("bType",bType);
                        params.put("inspector",inspector);
                        params.put("source",source);
                        params.put("startTime",startTime);
                        params.put("endTime",endTime);
                        params.put("index",index);
                        String result = RequestUtil.post(RequestUtil.GetInspectHistoryByParamsInStaticJJJGHead,params);
                        Message msg = Message.obtain();
                        msg.what = 3;
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
        }else{
            String result = intent.getStringExtra("result");
            String[] array = result.split("\\+");
            if(array!=null&&array.length==2){
                text_count.setText(array[1]);
                String resultNew = array[0];
                List<HistoryBean> beans = gson.fromJson(resultNew,new TypeToken<List<HistoryBean>>(){}.getType());
                historys.clear();
                historys.addAll(beans);
                hisroryInfoAdapter.notifyDataSetChanged();
            }
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                historyBean = historys.get(position);
                if (NetUtil.isNetworkAvailable(HistoryInfoActivity.this)) {
                    if (progressDialog == null) {
                        progressDialog = showProgressDialog();
                    } else {
                        if (!progressDialog.isShowing()) {
                            progressDialog.show();
                        }
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("inspectGuid", historyBean.getInspectGuid());
                            if("4".equals(historyBean.getState())){
                                String result = RequestUtil.post(RequestUtil.GetInspectDetailByGuidCity, params);
                                Message msg = Message.obtain();
                                msg.what = 4;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }else {
                                String result = RequestUtil.post(RequestUtil.GetInspectDetailByGuid, params);
                                Message msg = Message.obtain();
                                msg.what = 2;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }).start();

                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    ShowToast(noNetText);
                }
            }
        });
    }


    private String getCheckBranch(List<PersonBean> personBeans){
        StringBuilder sb = new StringBuilder();
        if(personBeans!=null&&personBeans.size()!=0){
            for(PersonBean personBean:personBeans){
                String branch = personBean.getBranch();
                if("".equals(sb.toString())){
                    sb.append(branch);
                }else{
                    if(!sb.toString().contains(branch)){
                        sb.append(";"+branch);
                    }
                }
            }
        }
        return sb.toString();
    }


    private List<InfoAttributeBean> getInfoList(List<CheckHeadBean> headBeans,String[] keys,String[] names){
        List<InfoAttributeBean> attributeBeans = new ArrayList<>();
        for(int i=0;i<keys.length;i++){
            InfoAttributeBean attribute = new InfoAttributeBean();
            String key = keys[i];
            String name = names[i];
            CheckHeadBean headBean = null;
            if(headBeans!=null&&headBeans.size()!=0){
                for(CheckHeadBean headBean1:headBeans){
                    if (key.equals(headBean1.getDbfield())){
                        headBean = headBean1;
                        break;
                    }
                }
            }
            String value = "";
            if(headBean!=null){
                value = headBean.getValue();
            }
            attribute.setName(name);
            attribute.setValue(value);
            attribute.setKey(key);
            attributeBeans.add(attribute);
        }

        return attributeBeans;
    }




    @Override
    public void onClick(View v) {
        // 开启Fragment事务
        switch (v.getId()) {
            case R.id.title_lefttext:
                this.finish();
                break;
        }

    }

}
