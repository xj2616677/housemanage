package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import bean.VideoBean;

/**
 * Created by admin on 2016/8/29.
 */
public class VideoListAdapter extends BaseAdapter {

    private Context context;
    private List<VideoBean> videoBeans;

    public VideoListAdapter(Context context, List<VideoBean> videoBeans) {
        this.context = context;
        this.videoBeans = videoBeans;
    }

    @Override
    public int getCount() {
        return videoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return videoBeans.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.videolist_item,null);
            viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_videoitem_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text_name.setText(videoBeans.get(position).getName());

        return convertView;
    }

    class ViewHolder {
        TextView text_name;
    }
}
