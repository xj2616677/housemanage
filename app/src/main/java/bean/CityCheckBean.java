package bean;

/**
 * Created by admin on 2016/11/27.
 */
public class CityCheckBean {

    private String InspectItemsResult;
    private String Description;
    private String Treatment;
    private String inspector;
    private String inspectTime;

    public String getInspectItemsResult() {
        return InspectItemsResult;
    }

    public void setInspectItemsResult(String inspectItemsResult) {
        InspectItemsResult = inspectItemsResult;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTreatment() {
        return Treatment;
    }

    public void setTreatment(String treatment) {
        Treatment = treatment;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getInspectTime() {
        return inspectTime;
    }

    public void setInspectTime(String inspectTime) {
        this.inspectTime = inspectTime;
    }
}
