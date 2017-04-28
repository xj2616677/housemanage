package widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

/**
 * Created by admin on 2016/7/7.
 */
public class MyScrollView extends ScrollView {

    private float downY = 0;
    private float upY = 0;
    private Boolean flag = false;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                return true;
            case MotionEvent.ACTION_UP:
                upY = ev.getY();
                //相等为false
                flag = compareFloat();
                if (!flag) {
                    Activity activity = (Activity) getContext();
                    //收键盘
                    InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); //初始化InputMethodManager类
                    if (activity.getCurrentFocus() != null
                            && activity.getCurrentFocus().getWindowToken() != null) {
                        manager.hideSoftInputFromWindow(activity.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 比较y轴的起始点和终点的差距，判断是否是在同一个点上，允许200的误差
     * @return
     */
    private Boolean compareFloat() {
        //不相等
        if (Math.abs(upY - downY) > 200) {
            return true;
        } else {//相等  拦截设置false
            return false;
        }
    }
}
