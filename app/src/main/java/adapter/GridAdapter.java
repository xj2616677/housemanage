package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import shareutil.PublicWay;

/**
 * Created by Administrator on 2015/11/16 0016.
 */
public class GridAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private boolean shape;
    private Context context;
    private List<ImageItem> imageItems;
    private int num;
    private boolean isHaveAdd;
    private RecycleBitmapListener recycleBitmapListener;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public GridAdapter(Context context, List<ImageItem> imageItems,int num,boolean isHaveAdd) {
        this.context = context;
        this.imageItems = imageItems;
        this.num = num;
        this.isHaveAdd = isHaveAdd;
        inflater = LayoutInflater.from(context);
        recycleBitmapListener = new RecycleBitmapListener();
    }


    public int getCount() {
        if(imageItems.size() == num){
            return num;
        }
        if(isHaveAdd){
            return imageItems.size()+1;
        }else{
            return imageItems.size();
        }
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_grida,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position ==imageItems.size()) {
                if (isHaveAdd){
                    holder.image.setVisibility(View.VISIBLE);
//                    holder.image.setImageBitmap(BitmapFactory.decodeResource(
//                            context.getResources(), R.drawable.icon_addpic_unfocused));
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .cacheInMemory(false)
                            .cacheOnDisk(false)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();
                    String url = ImageDownloader.Scheme.DRAWABLE.wrap("R.drawable.icon_addpic_unfocused");
                    ImageLoader.getInstance().displayImage(url, holder.image, options);
                    holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
                }else{
                    holder.image.setVisibility(View.GONE);
                }
            if (position == num) {
                holder.image.setVisibility(View.GONE);
            }else{
                holder.image.setVisibility(View.VISIBLE);
            }
        } else if(position<imageItems.size()){
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();
            String url = ImageDownloader.Scheme.FILE.wrap(imageItems.get(position).getImagePath());
            ImageLoader.getInstance().displayImage(url,holder.image,options,recycleBitmapListener);
        }

        return convertView;
    }

    public void clear(){
        if(recycleBitmapListener!=null){
            recycleBitmapListener.clearBitmap();
        }
    }


    class RecycleBitmapListener implements ImageLoadingListener {

        private List<Bitmap> bitmaps;

        public RecycleBitmapListener() {
            if(bitmaps==null){
                bitmaps = new ArrayList<>();
            }
        }

        public void clearBitmap(){
            if(bitmaps.size()>0){
                for(Bitmap bitmap:bitmaps){
                    if(bitmap!=null&&!bitmap.isRecycled()){
                        bitmap.recycle();
                    }
                }
                bitmaps = null;
            }
        }



        @Override
        public void onLoadingStarted(String s, View view) {

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            bitmaps.add(bitmap);


        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    }



    public class ViewHolder {
        public ImageView image;
    }



}
