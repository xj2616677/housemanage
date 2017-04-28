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
import com.example.admin.housemanage.SpotCheckActivity;

import java.util.List;

import bean.PropertyBean;
import constants.Contant;

/**
 * Created by admin on 2016/4/28.
 */
public class PropertyListAdapter extends BaseAdapter {

    private Context context;
    private List<PropertyBean> propertyBeans;

    public PropertyListAdapter(Context context, List<PropertyBean> propertyBeans) {
        this.context = context;
        this.propertyBeans = propertyBeans;
    }


    @Override
    public int getCount() {
        return propertyBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return propertyBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        final int index = position;
        final ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.propertyobject_item,null);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_proobject_item);
            holder.textView = (TextView) convertView.findViewById(R.id.text_proobject_item);
            holder.text_general = (TextView) convertView.findViewById(R.id.text_proobject_general);
            holder.text_house = (TextView) convertView.findViewById(R.id.text_proobject_house);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(propertyBeans.get(position).getName());


//        holder.checkBox.setOnCheckedChangeListener(new MyOnCheckedClick(position, holder));
        holder.checkBox.setOnClickListener(new MyClicked(position,holder));
        holder.checkBox.setChecked(propertyBeans.get(position).isCheck());

        if(propertyBeans.get(position).isGeneral()&&propertyBeans.get(position).isCheck()){
            holder.text_general.setVisibility(View.VISIBLE);
            if(propertyBeans.get(position).getGeneralBeans().size()!=0){
                holder.text_general.setText("普通地下室("+propertyBeans.get(position).getGeneralBeans().size()+")");
            }else{
                holder.text_general.setText("普通地下室");
            }
        }else{
            holder.text_general.setVisibility(View.GONE);
        }

        if(propertyBeans.get(position).isHouse()&&propertyBeans.get(position).isCheck()){
            holder.text_house.setVisibility(View.VISIBLE);
            if(propertyBeans.get(position).getHouseBeans().size()!=0){
                holder.text_house.setText("房屋安全("+propertyBeans.get(position).getHouseBeans().size()+")");
            }else{
                holder.text_house.setText("房屋安全");
            }
        }else{
            holder.text_house.setVisibility(View.GONE);
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrokerInfoActivity.class);
                intent.putExtra("type", context.getResources().getString(R.string.propertymanage));
                intent.putExtra("id", propertyBeans.get(index).getProId());
                intent.setAction("object");
                context.startActivity(intent);
            }
        });

        holder.text_general.setOnClickListener(new MyClicked(position,holder));
        holder.text_house.setOnClickListener(new MyClicked(position,holder));
        return convertView;
    }
    class MyClicked implements View.OnClickListener{

        private int position;
        private ViewHolder viewHolder;

        public MyClicked(int position,ViewHolder viewHolder) {
            this.position = position;
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            Contant.infoAttributeBeans.clear();
            switch (v.getId()){
                case R.id.text_proobject_general:
                    ((SpotCheckActivity)context).initCheckObjectDialog(context.getResources().getString(R.string.generalbasement),propertyBeans.get(position).getName(),position);
                    break;
                case R.id.text_proobject_house:
                    ((SpotCheckActivity)context).initCheckObjectDialog(context.getResources().getString(R.string.housesafeuse),propertyBeans.get(position).getName(),position);
                    break;
                case R.id.cb_proobject_item:
                    propertyBeans.get(position).toggle();
                    viewHolder.checkBox.setChecked(propertyBeans.get(position).isCheck());
                    if(propertyBeans.get(position).isCheck()){
                        Contant.propertyObjectList.clear();
                        Contant.propertyObjectList.add(propertyBeans.get(position));
                        Contant.proName = propertyBeans.get(position).getName();
                        if("其他".equals(Contant.proName)){
                            Contant.proName = "";
                        }
                        if(propertyBeans.get(position).isGeneral()){
                            viewHolder.text_general.setVisibility(View.VISIBLE);
                        }else{
                            viewHolder.text_general.setVisibility(View.GONE);
                        }
                        if(propertyBeans.get(position).isHouse()){
                            viewHolder.text_house.setVisibility(View.VISIBLE);
                        }else{
                            viewHolder.text_house.setVisibility(View.GONE);
                        }
                        for(int i=0;i<propertyBeans.size();i++){
                            if(i!=position){
                                propertyBeans.get(i).setIsCheck(false);
                            }
                        }
                        notifyDataSetChanged();
                    }else{
                        Contant.propertyObjectList.clear();
                        viewHolder.text_general.setVisibility(View.GONE);
                        viewHolder.text_house.setVisibility(View.GONE);
                    }
                    break;
            }


        }
    }



    class ViewHolder{
        CheckBox checkBox;
        TextView textView;
        TextView text_general,text_house;

    }
}
