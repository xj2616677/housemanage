package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.admin.housemanage.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import bean.ImageItem;

/**
 * Created by admin on 2016/12/1.
 */
public class PersonSignAdapter extends BaseAdapter {

    private Context context;
    private List<Bitmap> bitmaps;
    private ArrayList<ImageItem> imageItems;
    private int index;

    public PersonSignAdapter(Context context, List<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
        index = 1;
    }

    public PersonSignAdapter(ArrayList<ImageItem> imageItems, Context context) {
        this.imageItems = imageItems;
        this.context = context;
        index = 2;
    }

    @Override
    public int getCount() {
        if(index==1){
            if(bitmaps.size()<=5){
                return bitmaps.size();
            }else{
                return 5;
            }
        }else{
            if(imageItems.size()<=5){
                return imageItems.size();
            }else{
                return 5;
            }
        }
    }

    @Override
    public Object getItem(int position) {
        if(index==1){
            return bitmaps.get(position);
        }else {
            return imageItems.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_published_grida,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(index==1){
            holder.image.setImageBitmap(bitmaps.get(position));
        }else if(index==2){
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();
            String url = ImageDownloader.Scheme.FILE.wrap(imageItems.get(position).getImagePath());
            ImageLoader.getInstance().displayImage(url,holder.image,options);
        }

        return convertView;
    }




    class ViewHolder{
        ImageView image;
    }
}
