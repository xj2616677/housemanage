package shareutil;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/30.
 */
public class ImageBitmapListener implements ImageLoadingListener {

    private List<Bitmap> bitmaps;

    public ImageBitmapListener() {
        bitmaps = new ArrayList<>();
    }

    public void clearBitmapList(){
        if(bitmaps.size()>0){
            for(int i = 0;i<bitmaps.size();i++){
                Bitmap b = bitmaps.get(i);
                if(b!=null&&!b.isRecycled()){
                    b.recycle();
                }
            }
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
