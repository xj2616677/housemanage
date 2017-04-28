package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.housemanage.R;

/**
 * Created by admin on 2016/3/31.
 */
public class MainGridAdapter extends BaseAdapter {

    private Context context;
    private String[] gridName;
    private int[] imagId;

    public MainGridAdapter(Context context, String[] gridName,int[] imagId) {
        this.context = context;
        this.gridName = gridName;
        this.imagId = imagId;
    }

    @Override
    public int getCount() {
        return gridName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item,null);
            holder.image = (ImageView) convertView.findViewById(R.id.img_griditem);
            holder.text = (TextView) convertView.findViewById(R.id.text_griditem);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image.setBackground(context.getResources().getDrawable(imagId[position]));
        holder.text.setText(gridName[position]);


        return convertView;
    }

    class ViewHolder{
        ImageView image;
        TextView text;
    }
}
