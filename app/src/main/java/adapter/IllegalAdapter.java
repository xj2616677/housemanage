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

import bean.CheckTermBean;

/**
 * Created by admin on 2016/5/20.
 */
public class IllegalAdapter extends BaseAdapter {

    private Context context;
    private List<CheckTermBean> checkTermBeans;
    private int index = -1;

    public IllegalAdapter(Context context, List<CheckTermBean> checkTermBeans,int index) {
        this.context = context;
        this.checkTermBeans = checkTermBeans;
        this.index = index;
    }

    @Override
    public int getCount() {
        return checkTermBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return checkTermBeans.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_result_illegal,null);
            viewHolder.illegal_id = (TextView) convertView.findViewById(R.id.text_illegal_id);
            viewHolder.illegal_title = (TextView) convertView.findViewById(R.id.text_illegal_title);
            viewHolder.illegal_value = (TextView) convertView.findViewById(R.id.text_illegal_value);
            viewHolder.cb_illegal = (CheckBox) convertView.findViewById(R.id.cb_illegal);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.illegal_title.setText(checkTermBeans.get(position).getTitle());
        viewHolder.illegal_value.setText
                (checkTermBeans.get(position).getValue());
        viewHolder.illegal_value.setTextColor(context.getResources().getColor(R.color.titleback));

        if(checkTermBeans.get(position).isEdit()){
            viewHolder.cb_illegal.setClickable(true);
        }else{
            viewHolder.cb_illegal.setClickable(false);
        }
        if(index==1){
            viewHolder.illegal_id.setVisibility(View.GONE);
            if(checkTermBeans.get(position).isShow()){
                viewHolder.cb_illegal.setVisibility(View.VISIBLE);
            }else{
                viewHolder.cb_illegal.setVisibility(View.INVISIBLE);
            }

            viewHolder.cb_illegal.setOnCheckedChangeListener(new MyChecked(position));

            if(checkTermBeans.get(position).isXCZG()){
                viewHolder.cb_illegal.setChecked(true);
            }else{
                viewHolder.cb_illegal.setChecked(false);
            }
        }else if(index==2){
            viewHolder.cb_illegal.setVisibility(View.GONE);
            viewHolder.illegal_id.setVisibility(View.VISIBLE);

            viewHolder.illegal_title.setTextColor(context.getResources().getColor(R.color.gray));
            viewHolder.illegal_value.setTextColor(context.getResources().getColor(R.color.gray));

            if(checkTermBeans.get(position).isDescribe()){
//                viewHolder.illegal_id.setVisibility(View.INVISIBLE);
                viewHolder.illegal_id.setText("补充说明:");
                viewHolder.illegal_value.setVisibility(View.GONE);
            }else{
                viewHolder.illegal_id.setText((position + 1) + "、");
//                viewHolder.illegal_id.setVisibility(View.VISIBLE);
                viewHolder.illegal_value.setVisibility(View.VISIBLE);
            }
//            if(position==checkTermBeans.size()-1){
//                viewHolder.illegal_value.setVisibility(View.GONE);
//            }else{
//                viewHolder.illegal_value.setVisibility(View.VISIBLE);
//            }
        }else if(index==3){
            viewHolder.cb_illegal.setVisibility(View.GONE);
            viewHolder.illegal_id.setVisibility(View.VISIBLE);
            viewHolder.illegal_id.setText((position + 1) + "、");
            viewHolder.illegal_title.setTextColor(context.getResources().getColor(R.color.gray));
            viewHolder.illegal_value.setVisibility(View.GONE);
        }
        return convertView;
    }

    class MyChecked implements CompoundButton.OnCheckedChangeListener{

        private int position;

        public MyChecked(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkTermBeans.get(position).setIsXCZG(isChecked);
        }
    }

    class ViewHolder{
        TextView illegal_title,illegal_value,illegal_id;
        CheckBox cb_illegal;
    }
}
