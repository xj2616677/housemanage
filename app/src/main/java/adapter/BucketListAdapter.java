package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.housemanage.R;

import java.util.List;

import shareutil.ImageBucket;

/**
 * Created by admin on 2016/9/30.
 */
public class BucketListAdapter extends BaseAdapter {

    private Context context;
    private List<ImageBucket> imageBuckets;

    public BucketListAdapter(Context context, List<ImageBucket> imageBuckets) {
        this.context = context;
        this.imageBuckets = imageBuckets;
    }

    @Override
    public int getCount() {
        return imageBuckets.size();
    }

    @Override
    public Object getItem(int position) {
        return imageBuckets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView ==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bucketlist_item,null);
            viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_bucketitem_name);
            viewHolder.text_path = (TextView) convertView.findViewById(R.id.text_bucketitem_path);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text_name.setText(imageBuckets.get(position).getBucketName());
        viewHolder.text_path.setText(imageBuckets.get(position).getPath());



        return convertView;
    }

    class ViewHolder{
        TextView text_name,text_path;

    }
}
