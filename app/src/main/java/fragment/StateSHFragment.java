package fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.admin.housemanage.BaseActivity;
import com.example.admin.housemanage.CheckRecordActivity;
import com.example.admin.housemanage.CommitActivity;
import com.example.admin.housemanage.PreviewActivity;
import com.example.admin.housemanage.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adapter.HisroryAdapter;
import bean.CheckBaseInfoBean;
import bean.CheckBean;
import bean.CheckHeadBean;
import bean.CheckResultBean;
import bean.CheckTermBean;
import bean.CheckTermGroupBean;
import bean.HistoryBean;
import bean.InfoAttributeBean;
import bean.InspectHistoryBean;
import bean.InspectItemsResultBean;
import bean.PersonBean;
import bean.TableHeadBean;
import constants.Contant;
import util.NetUtil;
import util.RequestUtil;

/**
 * Created by admin on 2016/5/19.
 */
public class StateSHFragment extends Fragment {

    private ListView listView;
    private Handler mHandler;
    private Gson gson ;
    //    private List<InspectHistoryBean> historyBeans;
    private String result = "";
//    private List<CheckBean> checkBeans;

    private List<HistoryBean> historyBeans;
    private HistoryBean historyBean;

    private Activity activity;
    private HisroryAdapter hisroryAdapter;

    private String bussinessType;
    private List<TableHeadBean> headList;
    private String brokerId;
    private String DXSList;
    private String FWList;
    private String xqDate;
    private List<InfoAttributeBean> infoList;
    private InspectHistoryBean inspectHistoryBean;
    private List<CheckTermBean> illegalTerm;
    private String inspectTime;
    private String inspectGuid;
    private String branchs= "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gson = new Gson();
        historyBeans = new ArrayList<>();
        activity = getActivity();



        View view = inflater.inflate(R.layout.checkfragment,null);
        listView = (ListView) view.findViewById(R.id.list_fragment);

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                result = (String) msg.obj;
                if(result!=null&&!result.equals("")){
                    if(result.equals("[]")){
                        ((BaseActivity)activity).ShowToast("该用户没有已上传记录");
                    }else {
                        switch (msg.what) {
                            case 0:
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String isSuccess = jsonObject.getString("result");
                                    if (isSuccess.equals("1")) {
                                        ((BaseActivity)activity).ShowToast("操作成功");
                                        int position = msg.arg1;
//                                        historyBeans.get(position).toggle();
                                        hisroryAdapter.notifyDataSetChanged();
                                    } else if (isSuccess.equals("0")) {
                                        String failReason = jsonObject.getString("failReason");
                                        ((BaseActivity)activity).ShowToast(failReason);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
//                                checkBeans = gson.fromJson(result, new TypeToken<List<CheckBean>>() {
//                                }.getType());
//
                                historyBeans.clear();
//                                historyBeans.addAll(initData(checkBeans));


                                List<HistoryBean> historys = gson.fromJson(result, new TypeToken<List<HistoryBean>>() {
                                }.getType());
                                if(historys!=null){
                                    historyBeans.addAll(historys);
                                    hisroryAdapter = new HisroryAdapter(activity, historyBeans, 2, mHandler);
                                    listView.setAdapter(hisroryAdapter);
                                }
                                break;
                            case 3:
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
                                List<InfoAttributeBean> attributeBeans = getInfoList((String) result, keys, names);
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

                                String decribe = inspectHistoryBean.getCheckBaseInfoBean().getDescription();

                                Intent wordIntent = new Intent(activity, PreviewActivity.class);
                                wordIntent.setAction("checkRecord");
                                wordIntent.putExtra("info", info);
                                wordIntent.putExtra("decribe", decribe);
                                wordIntent.putExtra("illegalTerm", illegaltermstr);
                                wordIntent.putExtra("termCount", getTermSize(getTermGroups(resultBeans)));
                                wordIntent.putExtra("xqDate", xqDate);
                                wordIntent.putExtra("inspectTime", inspectTime);
                                wordIntent.putExtra("inspectGuid", inspectGuid);
                                wordIntent.putExtra("DXSList", DXSList);
                                wordIntent.putExtra("FWList", FWList);
                                wordIntent.putExtra("treatments", inspectHistoryBean.getCheckBaseInfoBean().getTreatment());
                                wordIntent.putStringArrayListExtra("bussinessType", bussinessList);
                                activity.startActivity(wordIntent);
                                break;
                            case 4:

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
//                                if(checkBeans!=null&&checkBeans.size()!=0) {
                                CheckBean checkBean = checkBeans.get(0);
                                inspectHistoryBean = getData(checkBean);

                                inspectHistoryBean.setBranchs(branchs);


                                    ((CheckRecordActivity) activity).dimissProgress();
                                    Intent intent = new Intent(activity, CommitActivity.class);
                                    if (msg.arg2 == 0) {
                                        intent.setAction("checkRecordNO");
                                    } else if (msg.arg2 == 1) {
                                        intent.setAction("feed");
                                    }
                                    String history = gson.toJson(inspectHistoryBean);
                                    intent.putExtra("history", history);
                                    List<CheckResultBean> results = inspectHistoryBean.getInspectItemsResultBean().getResult();
                                    List<CheckHeadBean> heads = inspectHistoryBean.getInspectItemsResultBean().getHead();
                                    String resultstr = gson.toJson(results);
                                    String headstr = gson.toJson(heads);
                                    intent.putExtra("inspectGuid", inspectHistoryBean.getInspectGuid());
                                    intent.putExtra("checklist", resultstr);
                                    if ("1".equals(inspectHistoryBean.getNoticeState())) {
                                        intent.putExtra("isEnd", "true");
                                    } else {
                                        intent.putExtra("isEnd", "false");
                                    }
                                    intent.putExtra("headlist", headstr);
                                    activity.startActivity(intent);
//                                    } else {
//
//
//                                        bussinessType = inspectHistoryBean.getBussinesstype();
//                                        headList = getTableHead(inspectHistoryBean.getInspectItemsResultBean().getHead());
//                                        brokerId = inspectHistoryBean.getId();
//                                        DXSList = inspectHistoryBean.getDXSList();
//                                        FWList = inspectHistoryBean.getFWList();
//                                        xqDate = inspectHistoryBean.getRectifDeadline();
//                                        inspectGuid= inspectHistoryBean.getInspectGuid();
//                                        inspectTime = inspectHistoryBean.getCheckBaseInfoBean().getInspectTime();
//                                        List<CheckHeadBean>  checkHeadBeans =  inspectHistoryBean.getInspectItemsResultBean().getHead();
//
//                                        String[] keys1 = null;
//                                        String[] names1 = null;
//                                        if (bussinessType.contains(getResources().getString(R.string.primebroker))) {
//                                            keys1 = getResources().getStringArray(R.array.primeinfokey);
//                                            names1 = getResources().getStringArray(R.array.primeinfoname);
//                                        } else {
//                                            keys1 = getResources().getStringArray(R.array.propertyinfokey);
//                                            names1 = getResources().getStringArray(R.array.propertyinfoname);
//                                        }
//
//                                        if(infoList==null){
//                                            infoList = new ArrayList<>();
//                                        }else{
//                                            infoList.clear();
//                                        }
//                                        infoList.addAll(getInfoList(checkHeadBeans,keys1,names1));
//
//                                        String info1 = gson.toJson(infoList);
//                                        List<CheckResultBean> resultBeans1 = inspectHistoryBean.getInspectItemsResultBean().getResult();
//                                        illegalTerm = getIllegalTerm(getTermGroups(resultBeans1));
//                                        String illegaltermstr1 = gson.toJson(illegalTerm);
//                                        ArrayList<String> bussinessList1 = new ArrayList<>();
//                                        String[] busstr1 = bussinessType.split(",");
//                                        if (busstr1 != null && busstr1.length != 0) {
//                                            for (String str : busstr1) {
//                                                bussinessList1.add(str);
//                                            }
//                                        } else {
//                                            bussinessList1.add(bussinessType);
//                                        }
//
//                                        String personSign = inspectHistoryBean.getCHKORSIGN();
//                                        String objectSign = inspectHistoryBean.getOBJSIGN();
//
//                                        if(personSign==null){
//                                            personSign = "";
//                                        }
//
//                                        if(objectSign==null){
//                                            objectSign = "";
//                                        }
//                                        Intent wordIntent1 = new Intent(activity, PreviewActivity.class);
//                                        wordIntent1.setAction("checkRecord");
//                                        wordIntent1.putExtra("info", info1);
//                                        wordIntent1.putExtra("illegalTerm", illegaltermstr1);
//                                        wordIntent1.putExtra("termCount", getTermSize(getTermGroups(resultBeans1)));
//                                        wordIntent1.putExtra("xqDate", xqDate);
//                                        wordIntent1.putExtra("inspectTime", inspectTime);
//                                        wordIntent1.putExtra("inspectGuid", inspectGuid);
//                                        wordIntent1.putExtra("DXSList", DXSList);
//                                        wordIntent1.putExtra("FWList", FWList);
//                                        wordIntent1.putExtra("branchs",branchs);
//                                        wordIntent1.putExtra("personSign",personSign);
//                                        wordIntent1.putExtra("objectSign",objectSign);
//                                        wordIntent1.putExtra("treatments", inspectHistoryBean.getCheckBaseInfoBean().getTreatment());
//                                        wordIntent1.putStringArrayListExtra("bussinessType", bussinessList1);
//                                        activity.startActivity(wordIntent1);
//
//
//
////                                        bussinessType = inspectHistoryBean.getBussinesstype();
////                                        headList = getTableHead(inspectHistoryBean.getInspectItemsResultBean().getHead());
////                                        brokerId = inspectHistoryBean.getId();
////                                        DXSList = inspectHistoryBean.getDXSList();
////                                        FWList = inspectHistoryBean.getFWList();
////                                        xqDate = inspectHistoryBean.getRectifDeadline();
////                                        inspectGuid = inspectHistoryBean.getInspectGuid();
////                                        inspectTime = inspectHistoryBean.getCheckBaseInfoBean().getInspectTime();
////                                        if (NetUtil.isNetworkAvailable(activity)) {
////                                            ((CheckRecordActivity) activity).showProDialog();
////                                            new Thread(new Runnable() {
////                                                @Override
////                                                public void run() {
////                                                    String result = "";
////                                                    Message msg = Message.obtain();
////                                                    if (bussinessType.contains(getResources().getString(R.string.primebroker))) {
////                                                        Map<String, String> params = new HashMap<>();
////                                                        params.put("idlist", brokerId);
////                                                        result = RequestUtil.post(RequestUtil.GetJJJGDetail, params);
////                                                        msg.what = 3;
////                                                        msg.arg2 = 3;
////                                                        //经纪机构
////                                                    } else {
////                                                        Map<String, String> params = new HashMap<>();
////                                                        params.put("proID", brokerId);
////                                                        params.put("DXSIDList", DXSList);
////                                                        params.put("FWList", FWList);
////                                                        result = RequestUtil.post(RequestUtil.GetProDXSFWHeadInfo, params);
////                                                        msg.what = 3;
////                                                        msg.arg2 = 4;
////                                                        //物业管理
////                                                    }
////                                                    msg.obj = result;
////                                                    mHandler.sendMessage(msg);
////                                                }
////                                            }).start();
////                                        } else {
////                                            ((BaseActivity) activity).ShowToast(((BaseActivity) activity).noNetText);
////                                        }
////                                    }
                                break;
                        }
                    }
                }else{
                    ((BaseActivity)activity).ShowToast("服务器连接异常!");
                }
                ((CheckRecordActivity) activity).dimissProgress();
            }
        };

        if(NetUtil.isNetworkAvailable(activity)){

            ((CheckRecordActivity)activity).showProDialog();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,String> params = new HashMap<>();
                    params.put("loginName",Contant.userid);
                    params.put("state", "3");

                    String conclusion = RequestUtil.post(RequestUtil.GetInspectHistoryByLoginName, params);
                    Log.i("TAG","result----"+conclusion);

                    Message msg = Message.obtain();
                    msg.what=1;
                    msg.obj = conclusion;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }else{
            ((BaseActivity)activity).ShowToast(((BaseActivity) activity).noNetText);
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                historyBean = historyBeans.get(position);
                if(NetUtil.isNetworkAvailable(activity)) {
                    ((CheckRecordActivity)getActivity()).showProDialog();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("inspectGuid",historyBean.getInspectGuid());
                            String result = RequestUtil.post(RequestUtil.GetInspectDetailByGuid,params);
                            Message msg = Message.obtain();
                            msg.what = 4;
                            msg.obj = result;
                            msg.arg2 = 0;
                            mHandler.sendMessage(msg);
                        }
                    }).start();

                }else{
                    ((BaseActivity)activity).ShowToast(((BaseActivity) activity).noNetText);
                }




            }
        });


        return view;
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

    private InspectHistoryBean getData(CheckBean bean){
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
        historyBean.setInspectGuid(bean.getInspectGuid());
        historyBean.setInspectNo(bean.getInspectNo());
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
        return historyBean;
    }


    private int getTermSize(List<CheckTermGroupBean> termGroupBeans){
        int count = 0;

        for(CheckTermGroupBean groupBean:termGroupBeans){
            count+=groupBean.getCheckTermBeans().size();
        }

        return count;
    }


    private List<CheckTermGroupBean> getTermGroups(List<CheckResultBean> resultBeans){
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

    private List<CheckTermBean> getIllegalTerm(List<CheckTermGroupBean> checkTermGroupBeans){
        List<CheckTermBean> termBeans = new ArrayList<>();
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
                                    termBeans.add(bean);
                                    break;
                                }
                            }
                        }else{
                            if(bean.getValue().equals(bean.getIllegalOptions())){
//                                bean.setIsXCZG(false);
                                bean.setIsShow(false);
                                termBeans.add(bean);
                            }
                        }
                    }
                }
            }
        }
        return termBeans;
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


    private List<InfoAttributeBean> getHeadData(List<InfoAttributeBean> attributeBeans,List<TableHeadBean> headBeans){
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

    private List<InfoAttributeBean> getInfoList(String result,String[] keys,String[] names){
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

    private List<TableHeadBean> getTableHead(List<CheckHeadBean> heads){
        List<TableHeadBean> tableheads = new ArrayList<>();
        for(CheckHeadBean head:heads){
            TableHeadBean tableHead = new TableHeadBean();
            tableHead.setName(head.getTitle());
            tableHead.setField(head.getDbfield());
            tableHead.setType(head.getBtype());
            tableheads.add(tableHead);
        }
        return tableheads;
    }

    private List<InspectHistoryBean> initData(List<CheckBean> checkBeans){
        List<InspectHistoryBean> inspectHistoryBeans = new ArrayList<>();
        if(checkBeans.size()!=0){
            for(CheckBean bean :checkBeans){
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
                historyBean.setInspectGuid(bean.getInspectGuid());
                historyBean.setCheckBaseInfoBean(checkBaseInfoBean);
                historyBean.setBussinesstype(bean.getBussinesstype());
                historyBean.setName(bean.getObjectName());
                historyBean.setAddress(bean.getObjectAddress());
                historyBean.setInspectItemsResultBean(bean.getInspectItemsResult());
                historyBean.setId(getObjectId(bean.getInspectItemsResult()));
                inspectHistoryBeans.add(historyBean);
            }
        }
        return inspectHistoryBeans;
    }

    private List<PersonBean> getPersonBean(String personstr){
        List<PersonBean> personBeans = new ArrayList<>();
        String[] personss = personstr.split(";");
        if(personss!=null&&personss.length!=0){
            for(String person:personss){
                PersonBean personBean = new PersonBean();
                String[] ss = person.split(",");
                if(ss.length==4){
                    personBean.setPID(ss[0]);
                    personBean.setName(ss[1]);
                    personBean.setCertNo(ss[2]);
                    personBean.setBranch(ss[3]);
                }else if(ss.length==2){
                    personBean.setPID(ss[0]);
                    personBean.setName(ss[1]);
                    personBean.setCertNo("");
                }else if(ss.length==3){
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
        objectId = resultBean.getHead().get(0).getObjid();
        return objectId;
    }

}
