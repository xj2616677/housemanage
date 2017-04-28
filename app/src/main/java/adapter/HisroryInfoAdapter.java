package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.HistoryBean;

/**
 * Created by admin on 2016/5/19.
 */
public class HisroryInfoAdapter extends BaseAdapter {

    private Context context;
    private List<HistoryBean> historyBeans;

    public HisroryInfoAdapter(Context context, List<HistoryBean> historyBeans) {
        this.context = context;
        this.historyBeans = historyBeans;
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
            viewHolder.bt_issh = (TextView) convertView.findViewById(R.id.text_historyitem);
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
        viewHolder.bt_issh.setVisibility(View.GONE);
        viewHolder.text_feed.setVisibility(View.GONE);
        String name = "";
        String objectName = "";
        if(historyBeans.get(position).getObjectName()!=null&&!"".equals(historyBeans.get(position).getObjectName())){
            objectName = historyBeans.get(position).getObjectName();
        }else{
            objectName = historyBeans.get(position).getObjectAddress();
        }
        if("0".equals(historyBeans.get(position).getIs_Pay())){
            name = objectName+"(补录)";
        }else if("4".equals(historyBeans.get(position).getState())){
            name = objectName+"(市系统录入数据)";
        }else{
            name = objectName;
        }
        viewHolder.name.setText(name);
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
                if(bbb!=null&&bbb.length>=1){
                    builder.append(bbb[1]+" ");
                }
            }
        }

        viewHolder.person.setText(builder.toString());

        if("3".equals(historyBeans.get(position).getState())){
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
        }else if("4".equals(historyBeans.get(position).getState())){
            viewHolder.lin_inspectNo.setVisibility(View.GONE);
            viewHolder.lin_state.setVisibility(View.GONE);
            viewHolder.lin_result.setVisibility(View.GONE);
        }else{
            viewHolder.lin_inspectNo.setVisibility(View.VISIBLE);
            viewHolder.lin_state.setVisibility(View.GONE);
            viewHolder.lin_result.setVisibility(View.GONE);
            viewHolder.text_inspectno.setText(historyBeans.get(position).getInspectNo());
        }

//        if("1".equals(historyBeans.get(position).getNoticeState())){
//            viewHolder.endChecked.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.endChecked.setVisibility(View.GONE);
//        }
        return convertView;
    }


    class ViewHolder{
        TextView name,address,source,inspectType,inspectTime,person;
        TextView bt_issh;
        TextView text_feed;
        LinearLayout lin_inspectNo;
        LinearLayout lin_state;
        LinearLayout lin_result;
        TextView text_inspectno,text_state,text_resulttitle,text_result;

    }
}
