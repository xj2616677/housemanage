package bean;

import java.util.List;

/**
 * Created by admin on 2016/5/31.
 */
public class PropertyProjectBean {

    private String projectName;
    private List<GeneralBean> generalBeans;
    private List<HouseSafeBean> houseBeans;
    private boolean isCheck;


    public void toggle(){
        isCheck = !isCheck;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<GeneralBean> getGeneralBeans() {
        return generalBeans;
    }

    public void setGeneralBeans(List<GeneralBean> generalBeans) {
        this.generalBeans = generalBeans;
    }

    public List<HouseSafeBean> getHouseBeans() {
        return houseBeans;
    }

    public void setHouseBeans(List<HouseSafeBean> houseBeans) {
        this.houseBeans = houseBeans;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
