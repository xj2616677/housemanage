package bean;

import java.util.List;

/**
 * Created by admin on 2016/5/3.
 */
public class CheckTermGroupBean {

    private String groupName;
    private List<CheckTermBean> checkTermBeans;
    private boolean isChecked;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public CheckTermBean getChild(int position) {
        return checkTermBeans.get(position);
    }

    public List<CheckTermBean> getCheckTermBeans() {
        return checkTermBeans;
    }

    public int getChildCount(){
        return checkTermBeans.size();
    }

    public void setCheckTermBeans(List<CheckTermBean> checkTermBeans) {
        this.checkTermBeans = checkTermBeans;
    }

    public void toggle() {
        this.isChecked = !this.isChecked;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
