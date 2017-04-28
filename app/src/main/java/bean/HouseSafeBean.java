package bean;

/**
 * Created by admin on 2016/4/29.
 */
public class HouseSafeBean {

    private String house_No;
    private String houseLocate;
    private boolean isCheck;

    private String BUILD_NO;//楼幢编号
    private String BUILD_NAME;//楼幢名称
    private String ESTDOORNO;//门牌号
    private String DELIVERYDATETIME;//投入使用时间
    private String BUILD_SITE;//楼幢坐落
    private String BUILD_TYPE;//楼幢用途
    private String ENGNAMEFromWY;//项目名称
    private boolean isSelfAdd = false;

    private String ADDMAN;

    public String getADDMAN() {
        return ADDMAN;
    }

    public void setADDMAN(String ADDMAN) {
        this.ADDMAN = ADDMAN;
    }

    public boolean isSelfAdd() {
        return isSelfAdd;
    }

    public void setIsSelfAdd(boolean isSelfAdd) {
        this.isSelfAdd = isSelfAdd;
    }

    public String getBUILD_NO() {
        return BUILD_NO;
    }

    public void setBUILD_NO(String BUILD_NO) {
        this.BUILD_NO = BUILD_NO;
    }

    public String getBUILD_NAME() {
        return BUILD_NAME;
    }

    public void setBUILD_NAME(String BUILD_NAME) {
        this.BUILD_NAME = BUILD_NAME;
    }

    public String getESTDOORNO() {
        return ESTDOORNO;
    }

    public void setESTDOORNO(String ESTDOORNO) {
        this.ESTDOORNO = ESTDOORNO;
    }

    public String getDELIVERYDATETIME() {
        return DELIVERYDATETIME;
    }

    public void setDELIVERYDATETIME(String DELIVERYDATETIME) {
        this.DELIVERYDATETIME = DELIVERYDATETIME;
    }

    public String getBUILD_SITE() {
        return BUILD_SITE;
    }

    public void setBUILD_SITE(String BUILD_SITE) {
        this.BUILD_SITE = BUILD_SITE;
    }

    public String getBUILD_TYPE() {
        return BUILD_TYPE;
    }

    public void setBUILD_TYPE(String BUILD_TYPE) {
        this.BUILD_TYPE = BUILD_TYPE;
    }

    public String getENGNAMEFromWY() {
        return ENGNAMEFromWY;
    }

    public void setENGNAMEFromWY(String ENGNAMEFromWY) {
        this.ENGNAMEFromWY = ENGNAMEFromWY;
    }

    public String getHouse_No() {
        return house_No;
    }

    public void setHouse_No(String house_No) {
        this.house_No = house_No;
    }

    public String getHouseLocate() {
        return houseLocate;
    }

    public void setHouseLocate(String houseLocate) {
        this.houseLocate = houseLocate;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
