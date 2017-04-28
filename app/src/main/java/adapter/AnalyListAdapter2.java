package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.AnalyBean;
import bean.AnalynoBean;

/**
 * Created by admin on 2016/5/23.
 */
public class AnalyListAdapter2 extends BaseAdapter {

    private Context context;
    private List<AnalynoBean> analyBeans;
    private int index;

    public AnalyListAdapter2(Context context, List<AnalynoBean> analyBeans,int index) {
        this.context = context;
        this.analyBeans = analyBeans;
        this.index = index;
    }

    @Override
    public int getCount() {
        return analyBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return analyBeans.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.analylist_item2,null);
            viewHolder.type = (TextView) convertView.findViewById(R.id.text_analyitem2_type);
            viewHolder.objectnum = (TextView) convertView.findViewById(R.id.text_analyitem2_objecjnum);
            viewHolder.text_line = (TextView) convertView.findViewById(R.id.text_analyitem2_line);
            viewHolder.checknum = (TextView) convertView.findViewById(R.id.text_analyitem2_checknum);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(analyBeans.get(position).getStaticField()==null||"".equals(analyBeans.get(position).getStaticField())){
            viewHolder.type.setText("未知");
        }else{
            viewHolder.type.setText(analyBeans.get(position).getStaticField());

        }
        if(index==2){
            viewHolder.objectnum.setText(analyBeans.get(position).getObjNum());
            viewHolder.checknum.setText(analyBeans.get(position).getCnum());
        }else if(index==3){
            viewHolder.objectnum.setText(analyBeans.get(position).getCnum());
            viewHolder.checknum.setText(analyBeans.get(position).getpNum());
        }else if(index==4){
            viewHolder.objectnum.setVisibility(View.GONE);
            viewHolder.text_line.setVisibility(View.GONE);
            viewHolder.checknum.setText(analyBeans.get(position).getCnum());
        }


        return convertView;
    }

    class ViewHolder{
        TextView type,objectnum,checknum,text_line;
    }
}
