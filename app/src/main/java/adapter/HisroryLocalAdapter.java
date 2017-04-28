package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import net.tsz.afinal.FinalDb;

import java.util.List;

import bean.CheckDBBean;
import bean.InspectHistoryBean;
import bean.PersonBean;
import constants.Contant;

/**
 * Created by admin on 2016/5/19.
 */
public class HisroryLocalAdapter extends BaseAdapter {

    private Context context;
    private List<InspectHistoryBean> historyBeans;
    //    private int i;
    private Handler mHandler;

    public HisroryLocalAdapter(Context context, List<InspectHistoryBean> historyBeans, Handler mHandler) {
        this.context = context;
        this.historyBeans = historyBeans;
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
            viewHolder.bt_issh = (TextView) convertView.findViewById(R.id.text_historyitem);
            viewHolder.name = (TextView) convertView.findViewById(R.id.nametext_itemhistory);
            viewHolder.address = (TextView) convertView.findViewById(R.id.text1_itemhistory);
            viewHolder.source = (TextView) convertView.findViewById(R.id.text2_itemhistory);
            viewHolder.inspectType = (TextView) convertView.findViewById(R.id.text3_itemhistory);
            viewHolder.inspectTime = (TextView) convertView.findViewById(R.id.text4_itemhistory);
            viewHolder.person = (TextView) convertView.findViewById(R.id.text5_itemhistory);
//            viewHolder.endChecked = (TextView) convertView.findViewById(R.id.text_history_endchecked);
            viewHolder.text_feed = (TextView) convertView.findViewById(R.id.text_historyitem_feed);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text_feed.setVisibility(View.GONE);
        viewHolder.bt_issh.setVisibility(View.VISIBLE);
        viewHolder.bt_issh.setText("删除");

//        if("1".equals(historyBeans.get(position).getNoticeState())){
//            viewHolder.endChecked.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.endChecked.setVisibility(View.GONE);
//        }
        if(historyBeans.get(position).getBussinesstype().contains(context.getResources().getString(R.string.other))){
            viewHolder.name.setText(historyBeans.get(position).getAddress());
        }else {
            viewHolder.name.setText(historyBeans.get(position).getName());
        }
        viewHolder.address.setText(historyBeans.get(position).getAddress());
        viewHolder.source.setText(historyBeans.get(position).getCheckBaseInfoBean().getSource());
        viewHolder.inspectType.setText(historyBeans.get(position).getBussinesstype());
        viewHolder.inspectTime.setText(historyBeans.get(position).getCheckBaseInfoBean().getInspectTime());
        List<PersonBean> personBeans = historyBeans.get(position).getCheckBaseInfoBean().getPersonBeans();
        StringBuilder sb = new StringBuilder();
        sb.append("");
        if(personBeans!=null&&personBeans.size()!=0){
            for(PersonBean personBean:personBeans){
                sb.append(personBean.getName()+" ");
            }
        }
        viewHolder.person.setText(sb.toString());
        viewHolder.bt_issh.setOnClickListener(new MyClick(position));
        return convertView;
    }

    class MyClick implements View.OnClickListener{

        private int position;

        public MyClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            initdialog(position);
        }
    }


    private void initdialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");
        builder.setMessage("确定要删除该记录？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FinalDb finalDb = FinalDb.create(context, Contant.dbName, true);
                finalDb.deleteById(CheckDBBean.class,historyBeans.get(position).getDbID());
                historyBeans.remove(historyBeans.get(position));
                notifyDataSetChanged();

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
        TextView bt_issh;
        TextView text_feed;

    }
}
