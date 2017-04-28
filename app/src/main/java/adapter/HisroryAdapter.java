package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.housemanage.CheckRecordActivity;
import com.example.admin.housemanage.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.HistoryBean;
import bean.PersonBean;
import constants.Contant;
import util.NetUtil;
import util.RequestUtil;

/**
 * Created by admin on 2016/5/19.
 */
public class HisroryAdapter extends BaseAdapter {

    private Context context;
    private List<HistoryBean> historyBeans;
    private int i;
    private Handler mHandler;

    public HisroryAdapter(Context context, List<HistoryBean> historyBeans,int i,Handler mHandler) {
        this.context = context;
        this.historyBeans = historyBeans;
        this.i = i;
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {
        return historyBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return historyBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_historyinfo,null);
            viewHolder.text_issh = (TextView) convertView.findViewById(R.id.text_historyitem);
            viewHolder.name = (TextView) convertView.findViewById(R.id.nametext_itemhistory);
            viewHolder.address = (TextView) convertView.findViewById(R.id.text1_itemhistory);
            viewHolder.source = (TextView) convertView.findViewById(R.id.text2_itemhistory);
            viewHolder.inspectType = (TextView) convertView.findViewById(R.id.text3_itemhistory);
            viewHolder.inspectTime = (TextView) convertView.findViewById(R.id.text4_itemhistory);
            viewHolder.person = (TextView) convertView.findViewById(R.id.text5_itemhistory);
//            viewHolder.endChecked = (TextView) convertView.findViewById(R.id.text_history_endchecked);
            viewHolder.text_feed = (TextView) convertView.findViewById(R.id.text_historyitem_feed);
            viewHolder.lin_inspectNo = (LinearLayout) convertView.findViewById(R.id.lin_itemhistory_inspectno);
            viewHolder.lin_state = (LinearLayout) convertView.findViewById(R.id.lin_itemhistory_state);
            viewHolder.lin_result = (LinearLayout) convertView.findViewById(R.id.lin_itemhistory_result);
            viewHolder.text_inspectno = (TextView) convertView.findViewById(R.id.text_itemhistory_inspectno);
            viewHolder.text_state = (TextView) convertView.findViewById(R.id.text_itemhistory_state);
            viewHolder.text_resulttitle = (TextView) convertView.findViewById(R.id.text_itemhistory_resulttitle);
            viewHolder.text_result = (TextView) convertView.findViewById(R.id.text_itemhistory_result);


            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (i){
            case 1:
                int count = getHaveCertCount(getPersonBean(historyBeans.get(position).getInspector()));
                viewHolder.text_issh.setVisibility(View.VISIBLE);
                viewHolder.text_issh.setText("上传市系统");
                if(historyBeans.get(position).getBussinessType().contains("其他")||count<2){
                    viewHolder.text_issh.setVisibility(View.GONE);
                }else{
                    viewHolder.text_issh.setVisibility(View.VISIBLE);
                }
                if("".equals(historyBeans.get(position).getFeedBackMan())&&historyBeans.get(position).getTreatment().contains("限期整改")){
                    viewHolder.text_feed.setVisibility(View.VISIBLE);
                }else{
                    if(!historyBeans.get(position).getTreatment().contains("限期整改")){
                        viewHolder.text_feed.setVisibility(View.GONE);
                    }else if(!"".equals(historyBeans.get(position).getFeedBackMan())){
                        viewHolder.text_feed.setVisibility(View.VISIBLE);
                        viewHolder.text_feed.setText("已整改");
                        viewHolder.text_feed.setEnabled(false);
                    }
                }
                break;
            case 2:
                viewHolder.text_issh.setVisibility(View.GONE);
                viewHolder.lin_inspectNo.setVisibility(View.VISIBLE);
                viewHolder.lin_state.setVisibility(View.VISIBLE);
                viewHolder.lin_result.setVisibility(View.VISIBLE);
                viewHolder.text_inspectno.setText(historyBeans.get(position).getInspectNo());

                if("1".equals(historyBeans.get(position).getIsSuccess())){
                    //上传成功
                    viewHolder.text_state.setText("上传成功");
                    viewHolder.text_resulttitle.setText("市执法检查单编号:");
                    viewHolder.text_result.setText(historyBeans.get(position).getZfjc_bm());

                }else if ("0".equals(historyBeans.get(position).getIsSuccess())||"9".equals(historyBeans.get(position).getIsSuccess())){
                    //上传失败
                    viewHolder.text_state.setText("上传失败");
                    viewHolder.text_resulttitle.setText("上传失败原因:");
                    viewHolder.text_result.setText(historyBeans.get(position).getFailureReason());

                }else {
                    //市系统未处理
                    viewHolder.text_state.setText("已上传至中间库,待市系统处理");
                    viewHolder.lin_result.setVisibility(View.GONE);
                }

                if("".equals(historyBeans.get(position).getFeedBackMan())&&historyBeans.get(position).getTreatment().contains("限期整改")){
                    viewHolder.text_feed.setVisibility(View.VISIBLE);
                }else{
                    if(!historyBeans.get(position).getTreatment().contains("限期整改")){
                        viewHolder.text_feed.setVisibility(View.GONE);
                    }else if(!"".equals(historyBeans.get(position).getFeedBackMan())){
                        viewHolder.text_feed.setVisibility(View.VISIBLE);
                        viewHolder.text_feed.setText("已整改");
                        viewHolder.text_feed.setEnabled(false);
                    }
                }
                break;
        }



//        if("1".equals(historyBeans.get(position).getNoticeState())){
//            viewHolder.endChecked.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.endChecked.setVisibility(View.GONE);
//        }
        if(i==3&&historyBeans.get(position).getBussinessType().contains(context.getResources().getString(R.string.other))){
            viewHolder.name.setText(historyBeans.get(position).getObjectAddress());
        }else {
            viewHolder.name.setText(historyBeans.get(position).getObjectName());
        }
        viewHolder.address.setText(historyBeans.get(position).getObjectAddress());
        viewHolder.source.setText(historyBeans.get(position).getSource());
        viewHolder.inspectType.setText(historyBeans.get(position).getBussinessType());
        viewHolder.inspectTime.setText(historyBeans.get(position).getInspectTime());
        String inspector = historyBeans.get(position).getInspector();
        String[] sss = inspector.split(";");
        StringBuilder builder = new StringBuilder();
        if(sss!=null&&sss.length!=0){
            for(int j=0;j<sss.length;j++){
                String str = sss[j];
                String[] bbb = str.split(",");
                StringBuilder sb = new StringBuilder();
                if(bbb!=null&&bbb.length>=1){
                    for(int i=1;i<bbb.length;i++){
                        if(i==1){
                            sb.append(bbb[i]);
                        }else{
                            sb.append(","+bbb[i]);
                        }
                    }
                }
                if(j==0){
                    builder.append(sb.toString());
                }else{
                    builder.append(";"+sb.toString());
                }
            }
        }
        viewHolder.person.setText(builder.toString());
        viewHolder.text_issh.setOnClickListener(new MyClick(position));
        viewHolder.text_feed.setOnClickListener(new MyClick(position));
        return convertView;
    }

    class MyClick implements View.OnClickListener{

        private int position;

        public MyClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.text_historyitem:
                    if(i==1) {
                        initdialog(position);
                    }
                    break;
                case R.id.text_historyitem_feed:

                    if (NetUtil.isNetworkAvailable(context)) {

                        ((CheckRecordActivity)context).showProDialog();
                        final String inspectGuid = historyBeans.get(position).getInspectGuid();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("inspectGuid", inspectGuid);
                                String result = RequestUtil.post(RequestUtil.GetInspectDetailByGuid, params);
                                Message msg = Message.obtain();
                                msg.what = 4;
                                msg.obj = result;
                                msg.arg1 = position;
                                msg.arg2=1;
                                mHandler.sendMessage(msg);
                            }
                        }).start();

                    } else {
                        Toast.makeText(context, "无网络访问，请检查网络", Toast.LENGTH_SHORT).show();
                    }


                    break;
            }

        }
    }


    private int getHaveCertCount(List<PersonBean> personBeans){
        int count= 0;
        if(personBeans!=null&&personBeans.size()!=0){
            for(int i=0;i<personBeans.size();i++){
                PersonBean personBean = personBeans.get(i);
                if(personBean.getCertNo()!=null&&!"".equals(personBean.getCertNo())){
                    count+=1;
                }
            }
        }
        return count;
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


    private void initdialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");
        builder.setMessage("确定要上传到市系统吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (NetUtil.isNetworkAvailable(context)) {
                    ((CheckRecordActivity) context).showProDialog();
                    final String inspectGuid = historyBeans.get(position).getInspectGuid();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("loginName", Contant.userid);
                            params.put("inspectGuid", inspectGuid);

                            params.put("opName", "审核");

                            String result = RequestUtil.post(RequestUtil.UpdateInspectHistoryState, params);
                            Message msg = Message.obtain();
                            msg.what = 0;
                            msg.obj = result;
                            msg.arg1 = position;
                            mHandler.sendMessage(msg);
                        }
                    }).start();

                } else {
                    Toast.makeText(context, "无网络访问，请检查网络", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


    class ViewHolder{
        TextView name,address,source,inspectType,inspectTime,person;
        TextView text_issh;
        TextView text_feed;
        LinearLayout lin_inspectNo;
        LinearLayout lin_state;
        LinearLayout lin_result;
        TextView text_inspectno,text_state,text_resulttitle,text_result;

    }
}
