package bean;

/**
 * Created by Administrator on 2016/5/16.
 */
public class CheckHeadBean {
    private String btype;
    private String dbfield;
    private String objid;
    private String title;
    private String value;

    public String getBtype() {
        return btype;
    }

    public void setBtype(String btype) {
        this.btype = btype;
    }

    public String getObjid() {
        return objid;
    }

    public void setObjid(String objid) {
        this.objid = objid;
    }

    public String getDbfield() {
        return dbfield;
    }

    public void setDbfield(String dbfield) {
        this.dbfield = dbfield;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
