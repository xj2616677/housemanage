package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by Administrator on 2016/2/24 0024.
 */
public class NetUtil {

    public static boolean isNetworkAvailable(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null){
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info!=null){
                boolean isAvailable = info.isAvailable();
            }
//            String aa = "";
//            Log.i("TAG",""+isAvailable);
            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            if(networkInfos!=null&&networkInfos.length!=0){
                for(NetworkInfo networkInfo:networkInfos){
                    if(networkInfo.getState()==NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }



}
