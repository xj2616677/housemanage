package bean;

/**
 * Created by admin on 2016/5/23.
 */
public class StreetBean {

    private String STREET_ID;//街道id
    private String STREET_NAME;//街道名称
    private String HOUSEMANAGER_ID;//房管所id
    private String HOUSEMANAGER_NAME;//房管所名称

    public String getSTREET_ID() {
        return STREET_ID;
    }

    public void setSTREET_ID(String STREET_ID) {
        this.STREET_ID = STREET_ID;
    }

    public String getSTREET_NAME() {
        return STREET_NAME;
    }

    public void setSTREET_NAME(String STREET_NAME) {
        this.STREET_NAME = STREET_NAME;
    }

    public String getHOUSEMANAGER_ID() {
        return HOUSEMANAGER_ID;
    }

    public void setHOUSEMANAGER_ID(String HOUSEMANAGER_ID) {
        this.HOUSEMANAGER_ID = HOUSEMANAGER_ID;
    }

    public String getHOUSEMANAGER_NAME() {
        return HOUSEMANAGER_NAME;
    }

    public void setHOUSEMANAGER_NAME(String HOUSEMANAGER_NAME) {
        this.HOUSEMANAGER_NAME = HOUSEMANAGER_NAME;
    }

    @Override
    public String toString() {
        return STREET_NAME;
    }
}
