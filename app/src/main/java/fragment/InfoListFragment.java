package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.admin.housemanage.CommitActivity;
import com.example.admin.housemanage.NewCommitActivity;
import com.example.admin.housemanage.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.InfoListAdapter;
import bean.InfoAttributeBean;
import bean.TableHeadBean;
import constants.Contant;
import util.NetUtil;
import util.RequestUtil;
import widget.MyListView;

/**
 * Created by admin on 2016/11/24.
 */
public class InfoListFragment extends Fragment {


    private MyListView listInfo;
    private NewCommitActivity activity;
    private List<InfoAttributeBean> infoList;
    private InfoListAdapter infoListAdapter;
    private List<String > bussinessType;
    private List<TableHeadBean> headList;
    private MyHandler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        activity = (NewCommitActivity) getActivity();
        View view = inflater.inflate(R.layout.infolistfragment,null);
        listInfo = (MyListView) view.findViewById(R.id.list_infolistfrag);

        mHandler = new MyHandler();
        initData();

        return view;
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            Object result = msg.obj;
            if ("".equals(result)) {
                if (activity.progressDialog != null) {
                    activity.progressDialog.dismiss();
                }
                activity.ShowToast("连接服务器异常");
            } else if ("[]".equals(result)) {
                if (activity.progressDialog != null) {
                    activity.progressDialog.dismiss();
                }
            } else {
                if (msg.what < 5) {
                    String[] keys = new String[]{};
                    String[] names = new String[]{};
                    switch (msg.what) {
                        case 1:
                            keys = getResources().getStringArray(R.array.primeinfokey);
                            names = getResources().getStringArray(R.array.primeinfoname);
                            break;
                        case 2:
                            keys = getResources().getStringArray(R.array.propertyinfokey);
                            names = getResources().getStringArray(R.array.propertyinfoname);
                            break;
                    }
                    List<InfoAttributeBean> attributeBeans = activity.getInfoList((String) result, keys, names);
                    if(infoList==null){
                        infoList = new ArrayList<>();
                    }else{
                        infoList.clear();
                    }
                    infoList.addAll(activity.getHeadData(attributeBeans, headList));
                    if (infoListAdapter == null) {
                        infoListAdapter = new InfoListAdapter(activity, infoList, bussinessType, activity.historyBean.getName(), activity.brokerId);
                        listInfo.setAdapter(infoListAdapter);
                    } else {
                        infoListAdapter.notifyDataSetChanged();
                    }

                    activity.progressDialog.dismiss();
                }
            }
        }
    }





    private void initData() {
        bussinessType = activity.bussinessType;
        if (Contant.infoAttributeBeans.size() != 0) {
            infoList.clear();
            infoList.addAll(Contant.infoAttributeBeans);
            if (infoListAdapter == null) {
                infoListAdapter = new InfoListAdapter(activity, infoList, bussinessType, activity.historyBean.getName(), activity.brokerId);
                listInfo.setAdapter(infoListAdapter);
            } else {
                infoListAdapter.notifyDataSetChanged();
            }
            if (bussinessType.contains(getResources().getString(R.string.primebroker))) {
                headList = Contant.primeHeadList;
            } else {
                headList = Contant.propertyHeadList;
            }
        } else {
            if ("check".equals(activity.action)) {
                if (bussinessType.contains(getResources().getString(R.string.primebroker))) {
                    headList = Contant.primeHeadList;
                } else {
                    headList = Contant.propertyHeadList;
                }


                if (NetUtil.isNetworkAvailable(activity)) {
                    if (activity.progressDialog == null) {
                        activity.progressDialog = activity.showProgressDialog();
                    } else {
                        activity.progressDialog.show();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String result = "";
                            Message msg = Message.obtain();
                            if (bussinessType.size() == 1 && bussinessType.contains(getResources().getString(R.string.primebroker))) {
                                if (activity.action.equals("check")) {
                                    headList = Contant.primeHeadList;
                                }
                                Map<String, String> params = new HashMap<>();
                                params.put("idlist", activity.brokerId);
                                result = RequestUtil.post(RequestUtil.GetJJJGDetail, params);
                                msg.what = 1;
                                //经纪机构
                            } else {
                                if (activity.action.equals("check")) {
                                    headList = Contant.propertyHeadList;
                                }
                                Map<String, String> params = new HashMap<>();
                                params.put("proID", activity.brokerId);
                                params.put("DXSIDList", "");
                                params.put("FWList", "");
                                result = RequestUtil.post(RequestUtil.GetProDXSFWHeadInfo, params);
                                msg.what = 2;
                                //物业管理
                            }
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                } else {
                    if (activity.progressDialog != null) {
                        activity.progressDialog.dismiss();
                    }
                    activity.ShowToast(activity.noNetText);
                }
            }
        }
    }

    }
