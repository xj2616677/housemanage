package util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/12.
 */
public class ActivityManage {

    private static ActivityManage activityManage;
    private static List<Activity> activityList;
    private Activity mAtivity;

    public static ActivityManage getInstance(){
        if(activityManage==null){
            activityManage = new ActivityManage();
            activityList = new ArrayList<>();
        }
        return activityManage;
    }

    public void addActivity(Activity activity){
        if(activityList!=null){
            activityList.add(activity);
        }
    }

    public void addOneActivity(Activity activity){
        mAtivity = activity;
    }

    public void clearOtherActivity(Activity goalActivity){
        if(activityList!=null){
            for(Activity activity:activityList){
                if(activity==goalActivity){
                }else{
                    activity.finish();
                }

            }
        }
    }

    public void clearOneActivity(){
        if(mAtivity!=null){
            mAtivity.finish();
        }
    }

    public void exit(){
        if(activityList!=null){
            for(Activity activity:activityList){
                if(activity!=null){
                    activity.finish();
                }
            }
        }
    }


}
