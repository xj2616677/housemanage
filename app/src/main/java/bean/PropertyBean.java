package bean;

import java.util.List;

/**
 * 物业Bean
 * Created by admin on 2016/4/28.
 */
public class PropertyBean {

    private String proId;
    private boolean isCheck;
    private String name;
    private String type;
    private String address;
    private String manageBranch;
    private String manageType;

    private List<GeneralBean> generalBeans;
    private List<HouseSafeBean> houseBeans;
    private boolean isGeneral;
    private boolean isHouse;


    public void toggle(){
        isCheck = !isCheck;
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

    public boolean isGeneral() {
        return isGeneral;
    }

    public void setIsGeneral(boolean isGeneral) {
        this.isGeneral = isGeneral;
    }

    public boolean isHouse() {
        return isHouse;
    }

    public void setIsHouse(boolean isHouse) {
        this.isHouse = isHouse;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManageBranch() {
        return manageBranch;
    }

    public void setManageBranch(String manageBranch) {
        this.manageBranch = manageBranch;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }
}
