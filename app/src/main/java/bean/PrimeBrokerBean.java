package bean;

import java.util.List;

/**
 * 经纪机构
 * Created by admin on 2016/4/27.
 */
public class PrimeBrokerBean {

    private String PKID;
    private String name;
    private String type;
    private String zzid;
    private String address;
    private boolean isCheck;

    public void toggle(){
        isCheck = !isCheck;
    }


    public String getPKID() {
        return PKID;
    }

    public void setPKID(String PKID) {
        this.PKID = PKID;
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

    public String getZzid() {
        return zzid;
    }

    public void setZzid(String zzid) {
        this.zzid = zzid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
