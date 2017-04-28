package bean;

/**
 * Created by admin on 2016/5/19.
 */
public class InspectBean {

    private String State;//状态1
    private String InspectType;//检查类型1
    private String Source;//检查来源1
    private String sourceDetail;
    private String sourceTaskID;//具体来源~~编号1
    private String ObjectType;//被检查对象类型1
    private String Bussinesstype;//执法业务类型1
    private String conclusion;//检查结论1
    private String conclusionOther;//检查结论为其他时详情1
    private String Description;//检查描述1
    private String Treatment;//处理意见1
    private String inspectTime;//检查时间1
    private String inspector;//检查人员
    private String ObjectName;
    private String ObjectAddress;
    private String RectifDeadline;//限期整改日期
    private String DXSList;//地下室列表
    private String FWList;//房屋列表
    private String streetId;//街道id

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getObjectName() {
        return ObjectName;
    }

    public void setObjectName(String objectName) {
        ObjectName = objectName;
    }

    public String getObjectAddress() {
        return ObjectAddress;
    }

    public void setObjectAddress(String objectAddress) {
        this.ObjectAddress = objectAddress;
    }

    public String getRectifDeadline() {
        return RectifDeadline;
    }

    public void setRectifDeadline(String rectifDeadline) {
        RectifDeadline = rectifDeadline;
    }

    public String getDXSList() {
        return DXSList;
    }

    public void setDXSList(String DXSList) {
        this.DXSList = DXSList;
    }

    public String getFWList() {
        return FWList;
    }

    public void setFWList(String FWList) {
        this.FWList = FWList;
    }

    private InspectItemsResultBean InspectItemsResult;//检查表项填写结果1



    public String getSourceTaskID() {
        return sourceTaskID;
    }

    public void setSourceTaskID(String sourceTaskID) {
        this.sourceTaskID = sourceTaskID;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getInspectType() {
        return InspectType;
    }

    public void setInspectType(String inspectType) {
        InspectType = inspectType;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getSourceDetail() {
        return sourceDetail;
    }

    public void setSourceDetail(String sourceDetail) {
        this.sourceDetail = sourceDetail;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public String getBussinesstype() {
        return Bussinesstype;
    }

    public void setBussinesstype(String bussinesstype) {
        Bussinesstype = bussinesstype;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getConclusionOther() {
        return conclusionOther;
    }

    public void setConclusionOther(String conclusionOther) {
        this.conclusionOther = conclusionOther;
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

    public String getInspectTime() {
        return inspectTime;
    }

    public void setInspectTime(String inspectTime) {
        this.inspectTime = inspectTime;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public InspectItemsResultBean getInspectItemsResult() {
        return InspectItemsResult;
    }

    public void setInspectItemsResult(InspectItemsResultBean inspectItemsResult) {
        InspectItemsResult = inspectItemsResult;
    }

}
