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
import android.widget.Toast;

import com.example.admin.housemanage.BaseActivity;
import com.example.admin.housemanage.CheckRecordActivity;
import com.example.admin.housemanage.CommitActivity;
import com.example.admin.housemanage.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.HisroryAdapter;
import adapter.HisroryLocalAdapter;
import bean.CheckBaseInfoBean;
import bean.CheckBean;
import bean.CheckDBBean;
import bean.CheckHeadBean;
import bean.CheckResultBean;
import bean.InspectHistoryBean;
import bean.InspectItemsResultBean;
import bean.PersonBean;
import constants.Contant;
import util.RequestUtil;

/**
 * Created by admin on 2016/5/19.
 */
public class StateLocalFragment extends Fragment {

    private ListView listView;
    private Handler mHandler;
    private Gson gson ;
    private List<InspectHistoryBean> historyBeans;
    private Activity activity;
    private FinalDb db;

    private CheckDBBean dbCheckBean;
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


                switch (msg.what) {
                    case 0:
                        String result = (String) msg.obj;
                        if (result != null && !result.equals("")) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String isSuccess = jsonObject.getString("result");
                                if (isSuccess.equals("1")) {
                                    Toast.makeText(activity, "操作成功", Toast.LENGTH_LONG).show();
                                } else if (isSuccess.equals("0")) {
                                    String failReason = jsonObject.getString("failReason");
                                    Toast.makeText(activity, failReason, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 1:

                        ((CheckRecordActivity) activity).dimissProgress();
                        List<InspectHistoryBean> dbBeans = (List<InspectHistoryBean>) msg.obj;
//                checkBeans = gson.fromJson(result, new TypeToken<List<CheckBean>>() {}.getType());
                        if(dbBeans.size()==0){
                            ((BaseActivity)activity).ShowToast("该用户没有本地记录");
                        }else{
                            historyBeans.clear();
                            historyBeans.addAll(dbBeans);
                            HisroryLocalAdapter hisroryAdapter = new HisroryLocalAdapter(activity, historyBeans, mHandler);
                            listView.setAdapter(hisroryAdapter);

                        }

                        break;
                }
            }
        };



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, CommitActivity.class);
                intent.setAction("checkRecordLocal");
                String history = gson.toJson(historyBeans.get(position));
                intent.putExtra("history", history);
                List<CheckResultBean> resultBeans = historyBeans.get(position).getInspectItemsResultBean().getResult();
                List<CheckHeadBean> heads = historyBeans.get(position).getInspectItemsResultBean().getHead();

                String resultstr = gson.toJson(resultBeans);
                String headstr = gson.toJson(heads);
//                String dbCheck = gson.toJson(historyBeans.get(position).getCheckDBBean());

                intent.putExtra("checklist",resultstr);
                intent.putExtra("headlist",headstr);
                intent.putExtra("inspectGuid","");
                intent.putExtra("isEnd","false");
//                    intent.putExtra("checkDB",dbCheck);
//                    intent.putExtra("type",historyBeans.get(position).getBussinesstype());
                activity.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((CheckRecordActivity)activity).showProDialog();
         db= ((BaseActivity)activity).getFinalDb(activity);

        new Thread(new Runnable() {
            @Override
            public void run() {


                List<CheckDBBean> checkDBBeans = db.findAllByWhere(CheckDBBean.class, "AddUser='" + Contant.userid + "'","id DESC");
                List<InspectHistoryBean> beans = DBToHistory(checkDBBeans);
                Message msg = Message.obtain();
                msg.obj = beans;
                msg.what =1;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private List<InspectHistoryBean> DBToHistory(List<CheckDBBean> dBBeans){
        List<InspectHistoryBean> historyBeans = new ArrayList<>();
        for(CheckDBBean bean:dBBeans){
            InspectHistoryBean historyBean = new InspectHistoryBean();
            historyBean.setCheckDBBean(bean);
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
            historyBean.setCheckBaseInfoBean(checkBaseInfoBean);
            historyBean.setBussinesstype(bean.getBussinesstype());
            historyBean.setName(bean.getObjectName());
            historyBean.setAddress(bean.getObjectAddress());
            InspectItemsResultBean inspectItemsResultBean = gson.fromJson(bean.getInspectItemsResult(),new TypeToken<InspectItemsResultBean>(){}.getType());
            historyBean.setInspectItemsResultBean(inspectItemsResultBean);
            historyBean.setId(bean.getObjectID());
            historyBean.setDbID(bean.getId());
            historyBean.setRectifDeadline(bean.getRectifDeadline());
            historyBean.setDXSList(bean.getDXSList());
            historyBean.setFWList(bean.getFWList());
            historyBean.setSpotImgPaths(bean.getSpotImgPaths());
            historyBean.setRectifyImgPath(bean.getRectifyImgPath());
            historyBean.setVideoPaths(bean.getVideoPaths());
            historyBean.setPeronSiagnPath(bean.getPersonSignPath());
            historyBean.setObjectSignPath(bean.getObjectSignPath());
            historyBean.setStreetInfo(bean.getStreetInfo());
            historyBean.setStreetId(bean.getStreetID());
            historyBeans.add(historyBean);
        }
        return historyBeans;
    }


    private List<PersonBean> getPersonBean(String personstr){
        List<PersonBean> personBeans = new ArrayList<>();
        if(!"".equals(personstr)){
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
        }
        return personBeans;
    }


}
