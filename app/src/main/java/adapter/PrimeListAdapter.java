package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.admin.housemanage.BrokerInfoActivity;
import com.example.admin.housemanage.R;

import java.util.List;

import bean.PrimeBrokerBean;
import constants.Contant;

/**
 * Created by admin on 2016/4/28.
 */
public class PrimeListAdapter extends BaseAdapter {

    private Context context;
    private List<PrimeBrokerBean> primeBeans;
    private int index;
    public PrimeListAdapter(Context context, List<PrimeBrokerBean> primeBeans,int index) {
        this.context = context;
        this.primeBeans = primeBeans;
        this.index = index;
        }


    @Override
    public int getCount() {
        return primeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return primeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(  int position, View convertView, ViewGroup parent) {
        final int index = position;
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

        holder.textView.setText(primeBeans.get(position).getName());
//        holder.checkBox.setOnCheckedChangeListener(new MyOnCheckedClick(position));
        holder.checkBox.setOnClickListener(new MyOnCheckedClick(position));
        holder.checkBox.setChecked(primeBeans.get(position).isCheck());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BrokerInfoActivity.class);
                intent.putExtra("type",context.getResources().getString(R.string.primebroker));
                intent.putExtra("id",primeBeans.get(index).getPKID());
                intent.setAction("object");
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class MyOnCheckedClick implements View.OnClickListener{

        private int position;

        public MyOnCheckedClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            primeBeans.get(position).toggle();
            Contant.infoAttributeBeans.clear();
            if(index==1){
                if(primeBeans.get(position).isCheck()){
                    Contant.primeObjectList.clear();
                    Contant.primeObjectList.add(primeBeans.get(position));
                    for(int i=0;i<primeBeans.size();i++){
                        if(i!=position){
                            primeBeans.get(i).setIsCheck(false);
                        }
                    }
                    notifyDataSetChanged();
                }else{
                    Contant.primeObjectList.clear();
                }
            }else {
                if (primeBeans.get(position).isCheck()) {
                    for (int i = 0; i < primeBeans.size(); i++) {
                        if (i != position) {
                            primeBeans.get(i).setIsCheck(false);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        }
    }
    class ViewHolder{
        CheckBox checkBox;
        TextView textView;
    }
}
