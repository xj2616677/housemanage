package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.TableHeadBean;
import constants.Contant;

/**
 * Created by admin on 2016/4/12.
 */
public class HeaderGridAdapter extends BaseAdapter {

    private List<TableHeadBean> tableHeadBeans;
    private Context context;
    private String enforce;

    public HeaderGridAdapter(List<TableHeadBean> tableHeadBeans, Context context,String enforce) {
        this.tableHeadBeans = tableHeadBeans;
        this.context = context;
        this.enforce = enforce;
    }

    @Override
    public int getCount() {
        return tableHeadBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return tableHeadBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final int index = position;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.headergrid_item,null);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_griditem_content);
            viewHolder.leftline = (TextView) convertView.findViewById(R.id.text_griditem_leftline);
            viewHolder.topline = (TextView) convertView.findViewById(R.id.text_griditem_topline);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkBox.setText(tableHeadBeans.get(position).getName());

        if(position==0||position==1){
            viewHolder.topline.setVisibility(View.VISIBLE);
        }else{
            viewHolder.topline.setVisibility(View.GONE);
        }

        if(position%2==0){
            viewHolder.leftline.setVisibility(View.VISIBLE);
        }else{
            viewHolder.leftline.setVisibility(View.GONE);
        }

        if(tableHeadBeans.get(position).getIsNeccessary().equals("1")){
            viewHolder.checkBox.setChecked(true);
            viewHolder.checkBox.setClickable(false);
            if(enforce.equals(context.getResources().getString(R.string.primebroker))){
                if(!Contant.primeHeadList.contains(tableHeadBeans.get(position))){
                    Contant.primeHeadList.add(tableHeadBeans.get(position));
                }
            }else if(enforce.equals(context.getResources().getString(R.string.propertymanage))){
                if(!Contant.propertyHeadList.contains(tableHeadBeans.get(position))){
                    Contant.propertyHeadList.add(tableHeadBeans.get(position));
                }
            }else if(enforce.equals(context.getResources().getString(R.string.generalbasement))){
                if(!Contant.generalHeadList.contains(tableHeadBeans.get(position))){
                    Contant.generalHeadList.add(tableHeadBeans.get(position));
                }
            }else if(enforce.equals(context.getResources().getString(R.string.housesafeuse))){
                if(!Contant.houseHeadList.contains(tableHeadBeans.get(position))){
                    Contant.houseHeadList.add(tableHeadBeans.get(position));
                }
            }
        }else{
            viewHolder.checkBox.setClickable(true);
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(enforce.equals(context.getResources().getString(R.string.primebroker))){
                    if(isChecked){
                        if(!Contant.primeHeadList.contains(tableHeadBeans.get(index))){
                            Contant.primeHeadList.add(tableHeadBeans.get(index));
                        }
                    }else{
                        Contant.primeHeadList.remove(tableHeadBeans.get(index));
                    }
                }else if(enforce.equals(context.getResources().getString(R.string.propertymanage))){
                    if(isChecked){
                        if(!Contant.propertyHeadList.contains(tableHeadBeans.get(index))){
                            Contant.propertyHeadList.add(tableHeadBeans.get(index));
                        }
                    }else{
                        Contant.propertyHeadList.remove(tableHeadBeans.get(index));
                    }
                }else if(enforce.equals(context.getResources().getString(R.string.generalbasement))){
                    if(isChecked){
                        if(!Contant.generalHeadList.contains(tableHeadBeans.get(index))){
                            Contant.generalHeadList.add(tableHeadBeans.get(index));
                        }
                    }else{
                        Contant.generalHeadList.remove(tableHeadBeans.get(index));
                    }
                }else if(enforce.equals(context.getResources().getString(R.string.housesafeuse))){
                    if(isChecked){
                        if(!Contant.houseHeadList.contains(tableHeadBeans.get(index))){
                            Contant.houseHeadList.add(tableHeadBeans.get(index));
                        }
                    }else{
                        Contant.houseHeadList.remove(tableHeadBeans.get(index));
                    }
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        CheckBox checkBox;
        TextView leftline,topline;
    }
}
