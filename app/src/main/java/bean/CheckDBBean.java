package bean;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by Administrator on 2016/5/16.
 */
@Table(name = "CheckDBBean")
public class CheckDBBean {

    private int id;
    private String InspectGuid;//唯一单号
    private String InspectNo;//检查表编号
    private String State;//状态，参加数据库设计
    private String NoInCitySytem;//在市执法系统中的编号
    private String InspectType;//检查类型
    private String Source;//任务来源
    private String sourceDetail;//详细任务来源，如检查任务名称、信访编号、咨询投诉平台编号
    private String ObjectType;//检查对象类别
    private String Bussinesstype;//检查业务类别
    private String InspectClasses;//检查涉及的检查大项
    private String conclusion;//检查结论，发现违法违规行为、未发现违法违规行为、其他
    private String conclusionOther;//结论为其他时，手动录入的结果
    private String Description;
    private String Treatment;//处理结果
    private String inspectorNum;//检查员数量
    private String inspectTime;//检查时间
    private String inspector;//检查员信息:PID,姓名,执法证号;PID,姓名,执法证号
    private String InspectBranch;//参与检查的部门名称
    private String InspectItemsResult;//填写的检查单结果

    private String ObjectName;
    private String ObjectAddress;

    private String AddUser;//添加记录的登录名
    private String AddTime;//添加记录的时间
    private String SubmitTime;//提交时间
    private String ObjectID;//被检查对象的主键ID

    private String RectifDeadline;//限期整改日期
    private String DXSList;//地下室列表
    private String FWList;//房屋列表

    private String spotImgPaths;
    private String rectifyImgPath;

    private String videoPaths;

    private String personSignPath;
    private String objectSignPath;

    private String test;

    private String streetInfo;

    private String streetID;

    public String getStreetID() {
        return streetID;
    }

    public void setStreetID(String streetID) {
        this.streetID = streetID;
    }

    public String getStreetInfo() {
        return streetInfo;
    }

    public void setStreetInfo(String streetInfo) {
        this.streetInfo = streetInfo;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getPersonSignPath() {
        return personSignPath;
    }

    public void setPersonSignPath(String personSignPath) {
        this.personSignPath = personSignPath;
    }

    public String getObjectSignPath() {
        return objectSignPath;
    }

    public void setObjectSignPath(String objectSignPath) {
        this.objectSignPath = objectSignPath;
    }

    public String getVideoPaths() {
        return videoPaths;
    }

    public void setVideoPaths(String videoPaths) {
        this.videoPaths = videoPaths;
    }

    public String getSpotImgPaths() {
        return spotImgPaths;
    }

    public void setSpotImgPaths(String spotImgPaths) {
        this.spotImgPaths = spotImgPaths;
    }

    public String getRectifyImgPath() {
        return rectifyImgPath;
    }

    public void setRectifyImgPath(String rectifyImgPath) {
        this.rectifyImgPath = rectifyImgPath;
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
        ObjectAddress = objectAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInspectGuid() {
        return InspectGuid;
    }

    public void setInspectGuid(String inspectGuid) {
        InspectGuid = inspectGuid;
    }

    public String getInspectNo() {
        return InspectNo;
    }

    public void setInspectNo(String inspectNo) {
        InspectNo = inspectNo;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getNoInCitySytem() {
        return NoInCitySytem;
    }

    public void setNoInCitySytem(String noInCitySytem) {
        NoInCitySytem = noInCitySytem;
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

    public String getInspectorNum() {
        return inspectorNum;
    }

    public void setInspectorNum(String inspectorNum) {
        this.inspectorNum = inspectorNum;
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

    public String getInspectBranch() {
        return InspectBranch;
    }

    public void setInspectBranch(String inspectBranch) {
        InspectBranch = inspectBranch;
    }

    public String getInspectItemsResult() {
        return InspectItemsResult;
    }

    public void setInspectItemsResult(String inspectItemsResult) {
        InspectItemsResult = inspectItemsResult;
    }

    public String getAddUser() {
        return AddUser;
    }

    public void setAddUser(String addUser) {
        AddUser = addUser;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getSubmitTime() {
        return SubmitTime;
    }

    public void setSubmitTime(String submitTime) {
        SubmitTime = submitTime;
    }


    public String getInspectClasses() {
        return InspectClasses;
    }

    public void setInspectClasses(String inspectClasses) {
        InspectClasses = inspectClasses;
    }


    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }


}
