package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.housemanage.BrokerInfoActivity;
import com.example.admin.housemanage.R;
import com.example.admin.housemanage.SpotCheckActivity;

import java.util.List;

import bean.GeneralBean;
import constants.Contant;

/**
 * Created by admin on 2016/4/28.
 */
public class GeneralListAdapter extends BaseAdapter {

    private Context context;
    private List<GeneralBean> generalBeans;
    private int index;

    public GeneralListAdapter(Context context, List<GeneralBean> generalBeans,int index) {
        this.context = context;
        this.generalBeans = generalBeans;
        this.index = index;
        }


    @Override
    public int getCount() {
        return generalBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return generalBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        final int positionCurrent = position;
        final ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dialoglist_item,null);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_dialog_item);
            holder.textView = (TextView) convertView.findViewById(R.id.text_dialog_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(generalBeans.get(position).getHouseLocate());
        if(index==1){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(new MyOnCheckedClick(position));
            holder.checkBox.setChecked(generalBeans.get(position).isCheck());
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrokerInfoActivity.class);
                intent.putExtra("type", context.getResources().getString(R.string.generalbasement));
                intent.putExtra("id", generalBeans.get(positionCurrent).getBase_id());
                intent.setAction("object");
                context.startActivity(intent);
            }
        });

        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(generalBeans!=null){
                    final GeneralBean generalBean = generalBeans.get(position);
                    if(Contant.userid.equals(generalBean.getAddman())){
                        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                        deleteDialog.setTitle("温馨提示");
                        deleteDialog.setMessage("确定删除该对象吗？");
                        deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ((SpotCheckActivity)context).requestDeleteObject(1,generalBean.getBase_id());

                                generalBeans.remove(position);
                                notifyDataSetChanged();
                                dialog.dismiss();

                            }
                        });

                        deleteDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        deleteDialog.create().show();

                    }else{
                        Toast.makeText(context, "该对象不是您添加的，您没有权限删除", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        return convertView;
    }

    class MyOnCheckedClick implements CompoundButton.OnCheckedChangeListener{

        private int position;

        public MyOnCheckedClick(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            generalBeans.get(position).setIsCheck(isChecked);
        }
    }
    class ViewHolder{
        CheckBox checkBox;
        TextView textView;
    }
}
