package util;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by admin on 2016/9/5.
 */
public class CrashApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .build();
        ImageLoader.getInstance().init(configuration);


        CrashHandler.getInstance().init(getApplicationContext());
    }
}
