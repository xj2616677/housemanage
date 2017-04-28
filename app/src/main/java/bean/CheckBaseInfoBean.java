package bean;

import java.util.List;

/**
 * Created by admin on 2016/5/19.
 */
public class CheckBaseInfoBean {

    private String State;
    private String InspectType;//检查类型
    private String Source;//任务来源
    private String sourceDetail;//详细任务来源，如检查任务名称、信访编号、咨询投诉平台编号
    private String ObjectType;//检查对象类别
    //    private String Bussinesstype;//检查业务类别
    private String conclusion;//检查结论1
    private String conclusionOther;//检查结论为其他时详情1
    private String Description;//检查描述1
    private List<PersonBean> personBeans;
    private String Treatment;//处理意见1
    private String inspectTime;//检查时间1
    private String streetId;

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
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

    public List<PersonBean> getPersonBeans() {
        return personBeans;
    }

    public void setPersonBeans(List<PersonBean> personBeans) {
        this.personBeans = personBeans;
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
}
