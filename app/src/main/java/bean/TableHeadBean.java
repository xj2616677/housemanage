package bean;

/**
 * Created by admin on 2016/5/5.
 */
public class TableHeadBean {

    private String Guid;
    private String type;
    private String name;
    private String field;
    private String isNeccessary;
    private String isNeccessay;

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getIsNeccessary() {
        if(isNeccessary!=null){
            return isNeccessary;
        }else{
            return isNeccessay;
        }
    }

    public void setIsNeccessary(String isNeccessary) {
        this.isNeccessary = isNeccessary;
    }
}
