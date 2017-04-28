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
public class StateNoSHFragment extends Fragment {

    private ListView listView;
    private Handler mHandler;
    private Gson gson ;
    //    private List<InspectHistoryBean> historyBeans;
    private List<HistoryBean> historyBeans;
    private HistoryBean historyBean;
    private String result = "";
    //    private List<CheckBean> checkBeans;
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


    private String branchs = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gson = new Gson();
        historyBeans = new ArrayList<>();
        activity = getActivity();



        View view = inflater.inflate(R.layout.checkfragment,null);
        listView = (ListView) view.findViewById(R.id.list_fragment);


        if(NetUtil.isNetworkAvailable(activity)){

            ((CheckRecordActivity)getActivity()).showProDialog();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,String> params = new HashMap<>();
                    params.put("loginName",Contant.userid);
                    params.put("state", "2");

                    String conclusion = RequestUtil.post(RequestUtil.GetInspectHistoryByLoginName, params);
                    Log.i("TAG","result----"+conclusion);

                    Message msg = Message.obtain();
                    msg.obj = conclusion;
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }else{
            ((BaseActivity)activity).ShowToast(((BaseActivity) activity).noNetText);
        }

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                result = (String) msg.obj;

                if(result!=null&&!result.equals("")){
                    if(result.equals("[]")){
                        ((BaseActivity)activity).ShowToast("该用户没有待上传记录");
                    }else {
                        switch (msg.what) {
                            case 0:
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String isSuccess = jsonObject.getString("result");
                                    if (isSuccess.equals("1")) {
                                        ((BaseActivity) activity).ShowToast("上传成功");
                                        int position = msg.arg1;
                                        historyBeans.remove(historyBeans.get(position));
                                        hisroryAdapter.notifyDataSetChanged();

                                    } else if (isSuccess.equals("0")) {
                                        String failReason = jsonObject.getString("failReason");
                                        ((BaseActivity) activity).ShowToast(failReason);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
//                                checkBeans = gson.fromJson(result, new TypeToken<List<CheckBean>>() {
//                                }.getType());
//
//                                historyBeans.clear();
//                                historyBeans.addAll(initData(checkBeans));
                                historyBeans.clear();
                                List<HistoryBean> historys = gson.fromJson(result, new TypeToken<List<HistoryBean>>() {
                                }.getType());
                                if(historys!=null){
                                    historyBeans.addAll(historys);
                                    hisroryAdapter = new HisroryAdapter(activity, historyBeans, 1, mHandler);
                                    listView.setAdapter(hisroryAdapter);
                                }

                                break;
                            case 3:
//                                int arg2 = msg.arg2;
//                                String[] keys = null;
//                                String[] names = null;
//                                if (arg2 == 3) {
//                                    keys = getResources().getStringArray(R.array.primeinfokey);
//                                    names = getResources().getStringArray(R.array.primeinfoname);
//                                } else if (arg2 == 4) {
//                                    keys = getResources().getStringArray(R.array.propertyinfokey);
//                                    names = getResources().getStringArray(R.array.propertyinfoname);
//                                }
//                                List<InfoAttributeBean> attributeBeans = getInfoList((String) result, keys, names);
//                                infoList = getHeadData(attributeBeans, headList);
//                                String info = gson.toJson(infoList);
//                                List<CheckResultBean> resultBeans = inspectHistoryBean.getInspectItemsResultBean().getResult();
//                                illegalTerm = getIllegalTerm(getTermGroups(resultBeans));
//                                String illegaltermstr = gson.toJson(illegalTerm);
//                                ArrayList<String> bussinessList = new ArrayList<>();
//                                String[] busstr = bussinessType.split(",");
//                                if (busstr != null && busstr.length != 0) {
//                                    for (String str : busstr) {
//                                        bussinessList.add(str);
//                                    }
//                                } else {
//                                    bussinessList.add(bussinessType);
//                                }
//
//                                Intent wordIntent = new Intent(activity, PreviewActivity.class);
//                                wordIntent.setAction("checkRecord");
//                                wordIntent.putExtra("info", info);
//                                wordIntent.putExtra("illegalTerm", illegaltermstr);
//                                wordIntent.putExtra("termCount", getTermSize(getTermGroups(resultBeans)));
//                                wordIntent.putExtra("xqDate", xqDate);
//                                wordIntent.putExtra("inspectTime", inspectTime);
//                                wordIntent.putExtra("inspectGuid", inspectGuid);
//                                wordIntent.putExtra("DXSList", DXSList);
//                                wordIntent.putExtra("FWList", FWList);
//                                wordIntent.putExtra("treatments", inspectHistoryBean.getCheckBaseInfoBean().getTreatment());
//                                wordIntent.putStringArrayListExtra("bussinessType", bussinessList);
//                                activity.startActivity(wordIntent);
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
                                if(checkBeans!=null&&checkBeans.size()!=0){
                                    CheckBean checkBean = checkBeans.get(0);
                                    inspectHistoryBean = getData(checkBean);
                                    inspectHistoryBean.setBranchs(branchs);

//                                    if(!inspectHistoryBean.getBussinesstype().contains(getResources().getString(R.string.other))&&"1".equals(inspectHistoryBean.getNoticeState())){
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
//                                        String[] keys = null;
//                                        String[] names = null;
//                                        if (bussinessType.contains(getResources().getString(R.string.primebroker))) {
//                                            keys = getResources().getStringArray(R.array.primeinfokey);
//                                            names = getResources().getStringArray(R.array.primeinfoname);
//                                        } else {
//                                            keys = getResources().getStringArray(R.array.propertyinfokey);
//                                            names = getResources().getStringArray(R.array.propertyinfoname);
//                                        }
//
//                                        if(infoList==null){
//                                            infoList = new ArrayList<>();
//                                        }else{
//                                            infoList.clear();
//                                        }
//                                        infoList.addAll(getInfoList(checkHeadBeans,keys,names));
//
//                                        String info = gson.toJson(infoList);
//                                        List<CheckResultBean> resultBeans = inspectHistoryBean.getInspectItemsResultBean().getResult();
//                                        illegalTerm = getIllegalTerm(getTermGroups(resultBeans));
//                                        String illegaltermstr = gson.toJson(illegalTerm);
//                                        ArrayList<String> bussinessList = new ArrayList<>();
//                                        String[] busstr = bussinessType.split(",");
//                                        if (busstr != null && busstr.length != 0) {
//                                            for (String str : busstr) {
//                                                bussinessList.add(str);
//                                            }
//                                        } else {
//                                            bussinessList.add(bussinessType);
//                                        }
//                                        Intent wordIntent = new Intent(activity, PreviewActivity.class);
//                                        wordIntent.setAction("checkRecord");
//                                        wordIntent.putExtra("info", info);
//                                        wordIntent.putExtra("illegalTerm", illegaltermstr);
//                                        wordIntent.putExtra("termCount", getTermSize(getTermGroups(resultBeans)));
//                                        wordIntent.putExtra("xqDate", xqDate);
//                                        wordIntent.putExtra("inspectTime", inspectTime);
//                                        wordIntent.putExtra("inspectGuid", inspectGuid);
//                                        wordIntent.putExtra("DXSList", DXSList);
//                                        wordIntent.putExtra("FWList", FWList);
//                                        wordIntent.putExtra("treatments", inspectHistoryBean.getCheckBaseInfoBean().getTreatment());
//                                        wordIntent.putStringArrayListExtra("bussinessType", bussinessList);
//                                        activity.startActivity(wordIntent);
////                                        if(NetUtil.isNetworkAvailable(activity)) {
////                                            ((CheckRecordActivity)getActivity()).showProDialog();
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
////                                                    } else if(!bussinessType.contains(getResources().getString(R.string.other))){
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
////                                        }else{
////                                            ((BaseActivity)activity).ShowToast(((BaseActivity) activity).noNetText);
////                                        }
//                                    }else{
                                    ((CheckRecordActivity)activity).dimissProgress();
                                    Intent intent = new Intent(activity, CommitActivity.class);
                                    if(msg.arg2==0){
                                        intent.setAction("checkRecordNO");
                                    }else if(msg.arg2==1){
                                        intent.setAction("feed");
                                    }
                                    String history = gson.toJson(inspectHistoryBean);
                                    intent.putExtra("history", history);
                                    List<CheckResultBean> results = inspectHistoryBean.getInspectItemsResultBean().getResult();
                                    List<CheckHeadBean> heads = inspectHistoryBean.getInspectItemsResultBean().getHead();
                                    String resultstr = gson.toJson(results);
                                    String headstr = gson.toJson(heads);
                                    intent.putExtra("inspectGuid",inspectHistoryBean.getInspectGuid());
                                    intent.putExtra("checklist",resultstr);
                                    if("1".equals(inspectHistoryBean.getNoticeState())){
                                        intent.putExtra("isEnd","true");
                                    }else{
                                        intent.putExtra("isEnd","false");
                                    }
                                    intent.putExtra("headlist",headstr);
                                    activity.startActivity(intent);
//                                    }
                                }
                                break;

                        }
                    }
                }else{
                    ((BaseActivity)activity).ShowToast("服务器连接失败");
                }
                ((CheckRecordActivity) activity).dimissProgress();
            }
        };




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
        historyBean.setVideoPaths("");
        historyBean.setPeronSiagnPath("");
        historyBean.setObjectSignPath("");
        historyBean.setFeedBackDes(bean.getFeedBackDes());
        historyBean.setFeedBackMan(bean.getFeedBackMan());
        historyBean.setFeedBackTime(bean.getFeedBackTime());
        return historyBean;
    }


    private String getObjectId(InspectItemsResultBean resultBean){
        String objectId = "";
        if(resultBean.getHead()!=null&&resultBean.getHead().size()!=0){
            objectId = resultBean.getHead().get(0).getObjid();
        }
        return objectId;
    }

}
