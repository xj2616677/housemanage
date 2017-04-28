package fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.housemanage.AnalysisInfoTwoActivity;
import com.example.admin.housemanage.HistoryInfoActivity;
import com.example.admin.housemanage.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import adapter.AnalyListAdapter2;
import bean.AnalynoBean;

/**
 * Created by admin on 2016/11/7.
 */
public class JJJGHeadFragment extends Fragment {

    private AnalysisInfoTwoActivity activity;
    private View view;
    private ListView listview;
    private List<AnalynoBean> analynoBeans;
    private Gson gson;
    private Handler mHandler;
    private TextView text_pnum,text_line;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gson = new Gson();
        activity = (AnalysisInfoTwoActivity) getActivity();

        View view = inflater.inflate(R.layout.jjjgallfragment, null);
        listview = (ListView) view.findViewById(R.id.list_jjjgall);
        text_pnum = (TextView) view.findViewById(R.id.text_jjjgall_pnum);
        text_line = (TextView) view.findViewById(R.id.text_jjjgall_line);
        text_pnum .setVisibility(View.GONE);
        text_line.setVisibility(View.GONE);




        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                if (result == null || result.equals("")) {
                    activity.ShowToast("服务器连接异常");
                } else if (result.equals("[]")) {
                    activity.ShowToast("该条件没有查询结果");
                } else {
                    if (analynoBeans == null) {
                        analynoBeans = new ArrayList<>();
                    }
                    List<AnalynoBean> analyBeans = gson.fromJson(result, new TypeToken<List<AnalynoBean>>() {
                    }.getType());
                    analynoBeans.clear();
                    analynoBeans.addAll(analyBeans);
                    AnalyListAdapter2 analyListAdapter = new AnalyListAdapter2(activity, analynoBeans, 4);
                    listview.setAdapter(analyListAdapter);
                    activity.progressDialog.dismiss();
                }
            }
        };
        activity.requestData("经纪机构总支", mHandler);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String headName = analynoBeans.get(position).getStaticField();
                if("".equals(headName)){
                    headName = "未知";
                }
                Intent intent = new Intent(activity, HistoryInfoActivity.class);
                intent.setAction("analyJJJGHead");
                intent.putExtra("houseManager", activity.houseManager);
                intent.putExtra("branchName", activity.branchName);
                intent.putExtra("streetName", activity.streetName);
                intent.putExtra("objectName", headName);
                intent.putExtra("bType", activity.bType);
                intent.putExtra("inspector", activity.inspector);
                intent.putExtra("source", activity.source);
                intent.putExtra("startTime", activity.startTime);
                intent.putExtra("endTime", activity.endTime);
                intent.putExtra("index",activity.index);
                activity.startActivity(intent);
            }
        });

        return view;
    }
}
