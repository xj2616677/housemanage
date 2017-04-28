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

/**
 * Created by admin on 2016/5/23.
 */
public class AnalyListAdapter extends BaseAdapter {

    private Context context;
    private List<AnalyBean> analyBeans;

    public AnalyListAdapter(Context context, List<AnalyBean> analyBeans) {
        this.context = context;
        this.analyBeans = analyBeans;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.analylist_item,null);
            viewHolder.type = (TextView) convertView.findViewById(R.id.text_analyitem_type);
            viewHolder.objectnum = (TextView) convertView.findViewById(R.id.text_analyitem_objectnum);
            viewHolder.checknum = (TextView) convertView.findViewById(R.id.text_analyitem_checknum);
            viewHolder.personnum = (TextView) convertView.findViewById(R.id.text_analyitem_personnum);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(analyBeans.get(position).getStaticField()==null||"".equals(analyBeans.get(position).getStaticField())){
            viewHolder.type.setText("未知");
        }else{
            viewHolder.type.setText(analyBeans.get(position).getStaticField());

        }

//        viewHolder.type.setText(analyBeans.get(position).getStaticField());
        viewHolder.objectnum.setText(analyBeans.get(position).getObjNum());
        viewHolder.checknum.setText(analyBeans.get(position).getCnum());
        viewHolder.personnum.setText(analyBeans.get(position).getpNum());

        return convertView;
    }

    class ViewHolder{
        TextView type,objectnum,checknum,personnum;
    }
}
