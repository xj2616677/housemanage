package bean;

/**
 * Created by Administrator on 2016/5/16.
 */
public class CheckBean {

    private String ID;//检查单ID
    private String InspectGuid;//检查单guid
    private String InspectNo;//
    private String State;//状态，参加数据库设计
    private String NoInCitySytem;//在市执法系统中的编号
    private String InspectType;//检查类型
    private String Source;//任务来源
    private String sourceDetail;//详细任务来源，如检查任务名称、信访编号、咨询投诉平台编号
    private String ObjectType;//检查对象类别
    private String Bussinesstype;//检查业务类别
    private String ObjectName;//检查对象名称
    private String ObjectAddress;//检查地址
    private String enterpriseName;//管理方
    private String PerInCharge;//相关负责人
    private String InspectRegion;//检查地所属房管所
    private String InspectStreet;//检查地所属街镇
    private String conclusion;//检查结论，发现违法违规行为、未发现违法违规行为、其他
    private String conclusionOther;//结论为其他时，手动录入的结果
    private String Description;
    private String Treatment;//处理结果
    private String inspectorNum;//检查员数量
    private String inspectTime;//检查时间
    private String inspector;//检查员信息:PID,姓名,执法证号;PID,姓名,执法证号
    private String InspectBranch;//参与检查的部门名称
    private String RectifDeadline;//限期整改日期
    private String DXSList;//地下室列表
    private String FWList;//房屋列表
    private InspectItemsResultBean InspectItemsResult;//填写的检查单结果

    private String AddUser;//添加记录的登录名
    private String AddTime;//添加记录的时间
    private String SubmitTime;//提交时间
    private String CheckMan;//审核人
    private String CheckTime;//审核时间
    private String CancelMan;//取消审核人
    private String CancelTime;//取消审核时间
    private String InspectClasses;//检查涉及的检查大项
    private String sourceTaskID;//任务来源为检查任务时的检查任务ID
    private String TempleteGuid;//使用的模板ID
    private String ObjectID;//被检查对象的主键ID
    private String SerialGuid;//检查单的序列号，同一个告知单的序列号相同
    private String NoticeState;//结束检查state

    private String FeedBackDes;
    private String FeedBackMan;
    private String FeedBackTime;

    private String CHKORSIGN;//检查人签名
    private String OBJSIGN;//检查对象签名


    public String getCHKORSIGN() {
        return CHKORSIGN;
    }

    public void setCHKORSIGN(String CHKORSIGN) {
        this.CHKORSIGN = CHKORSIGN;
    }

    public String getOBJSIGN() {
        return OBJSIGN;
    }

    public void setOBJSIGN(String OBJSIGN) {
        this.OBJSIGN = OBJSIGN;
    }

    public String getFeedBackDes() {
        return FeedBackDes;
    }

    public void setFeedBackDes(String feedBackDes) {
        FeedBackDes = feedBackDes;
    }

    public String getFeedBackMan() {
        return FeedBackMan;
    }

    public void setFeedBackMan(String feedBackMan) {
        FeedBackMan = feedBackMan;
    }

    public String getFeedBackTime() {
        return FeedBackTime;
    }

    public void setFeedBackTime(String feedBackTime) {
        FeedBackTime = feedBackTime;
    }

    public String getNoticeState() {
        return NoticeState;
    }

    public void setNoticeState(String noticeState) {
        NoticeState = noticeState;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getPerInCharge() {
        return PerInCharge;
    }

    public void setPerInCharge(String perInCharge) {
        PerInCharge = perInCharge;
    }

    public String getInspectRegion() {
        return InspectRegion;
    }

    public void setInspectRegion(String inspectRegion) {
        InspectRegion = inspectRegion;
    }

    public String getInspectStreet() {
        return InspectStreet;
    }

    public void setInspectStreet(String inspectStreet) {
        InspectStreet = inspectStreet;
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

    public InspectItemsResultBean getInspectItemsResult() {
        return InspectItemsResult;
    }

    public void setInspectItemsResult(InspectItemsResultBean inspectItemsResult) {
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

    public String getCheckMan() {
        return CheckMan;
    }

    public void setCheckMan(String checkMan) {
        CheckMan = checkMan;
    }

    public String getCheckTime() {
        return CheckTime;
    }

    public void setCheckTime(String checkTime) {
        CheckTime = checkTime;
    }

    public String getCancelMan() {
        return CancelMan;
    }

    public void setCancelMan(String cancelMan) {
        CancelMan = cancelMan;
    }

    public String getCancelTime() {
        return CancelTime;
    }

    public void setCancelTime(String cancelTime) {
        CancelTime = cancelTime;
    }

    public String getInspectClasses() {
        return InspectClasses;
    }

    public void setInspectClasses(String inspectClasses) {
        InspectClasses = inspectClasses;
    }

    public String getSourceTaskID() {
        return sourceTaskID;
    }

    public void setSourceTaskID(String sourceTaskID) {
        this.sourceTaskID = sourceTaskID;
    }

    public String getTempleteGuid() {
        return TempleteGuid;
    }

    public void setTempleteGuid(String templeteGuid) {
        TempleteGuid = templeteGuid;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public String getSerialGuid() {
        return SerialGuid;
    }

    public void setSerialGuid(String serialGuid) {
        SerialGuid = serialGuid;
    }

}
