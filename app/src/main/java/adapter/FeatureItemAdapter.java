package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esri.core.map.Feature;
import com.example.admin.housemanage.R;

import java.util.ArrayList;
import java.util.List;

import bean.FeatrueItemBean;

/**
 * Created by Administrator on 2015/12/8.
 */
public class FeatureItemAdapter extends BaseAdapter {
    private Context context;
    private FeatrueItemBean featureItem;
    private Feature feature;

    private List<FeatrueItemBean> list = new ArrayList<FeatrueItemBean>();


    public FeatureItemAdapter(Context context, List<FeatrueItemBean> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder viewholder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_selectname,null);
            viewholder = new ViewHolder();
            viewholder.tvfwh = (TextView) convertView.findViewById(R.id.text1_nameitem);
            viewholder.tvgcdd = (TextView) convertView.findViewById(R.id.text2_nameitem);
            convertView.setTag(viewholder);

        }else{
            viewholder = (ViewHolder) convertView.getTag();
        }
        featureItem = list.get(position);
        feature = featureItem.getFeature();

        viewholder.tvfwh.setText(feature.getAttributes().get("ORGNAME").toString());
        viewholder.tvgcdd.setText("11111");



        return convertView;
    }

    private  class ViewHolder
    {
        TextView tvfwh;
        TextView tvgcdd;
    }
}
