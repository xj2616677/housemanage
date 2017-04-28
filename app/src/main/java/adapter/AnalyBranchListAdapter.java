package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.AnalyBranchBean;
import bean.AnalynoBean;

/**
 * Created by admin on 2016/5/23.
 */
public class AnalyBranchListAdapter extends BaseAdapter {

    private Context context;
    private List<AnalyBranchBean> analyBranchBeans;
    private int index;

    public AnalyBranchListAdapter(Context context, List<AnalyBranchBean> analyBranchBeans) {
        this.context = context;
        this.analyBranchBeans = analyBranchBeans;
    }

    @Override
    public int getCount() {
        return analyBranchBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return analyBranchBeans.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.analybranchlist_item,null);
            viewHolder.text_branch = (TextView) convertView.findViewById(R.id.text_analybranchitem_branch);
            viewHolder.text_broke = (TextView) convertView.findViewById(R.id.text_analybranchitem_broke);
            viewHolder.text_property = (TextView) convertView.findViewById(R.id.text_analybranchitem_property);
            viewHolder.text_general = (TextView) convertView.findViewById(R.id.text_analybranchitem_general);
            viewHolder.text_house = (TextView) convertView.findViewById(R.id.text_analybranchitem_house);
            viewHolder.text_other = (TextView) convertView.findViewById(R.id.text_analybranchitem_other);
            viewHolder.text_cnum = (TextView) convertView.findViewById(R.id.text_analybranchitem_cnum);
            viewHolder.text_pnum = (TextView) convertView.findViewById(R.id.text_analybranchitem_pnum);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text_branch.setText(analyBranchBeans.get(position).getBranchname());
        viewHolder.text_broke.setText(analyBranchBeans.get(position).getBroke());
        viewHolder.text_property.setText(analyBranchBeans.get(position).getProperty());
        viewHolder.text_general.setText(analyBranchBeans.get(position).getGeneral());
        viewHolder.text_house.setText(analyBranchBeans.get(position).getHouse());
        viewHolder.text_other.setText(analyBranchBeans.get(position).getOther());
        viewHolder.text_cnum.setText(analyBranchBeans.get(position).getObjnum());
        viewHolder.text_pnum.setText(analyBranchBeans.get(position).getPersonnum());

        return convertView;
    }

    class ViewHolder{
        TextView text_branch,text_broke,text_property,text_general,text_house,text_other,text_cnum,text_pnum;
    }
}
