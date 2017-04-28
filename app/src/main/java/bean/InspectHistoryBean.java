package bean;

import java.util.List;

/**
 * Created by admin on 2016/5/9.
 */
public class InspectHistoryBean {

    private CheckBaseInfoBean checkBaseInfoBean;//基本信息
    private String Bussinesstype;//检查业务类别
    private String name;
    private String address;
    private String id;//被检对象id
    private String inspectGuid;
    private String inspectNo;
    private CheckDBBean checkDBBean;
    private boolean isTake;
    private String state;
    private int dbID;
    private String RectifDeadline;//限期整改日期
    private String DXSList;//地下室列表
    private String FWList;//房屋列表
    private String NoticeState;
    private String spotImgPaths;
    private String rectifyImgPath;
    private String videoPaths;
    private String peronSiagnPath;
    private String objectSignPath;

    private String FeedBackDes;
    private String FeedBackMan;
    private String FeedBackTime;

    private String CHKORSIGN;//检查人签名
    private String OBJSIGN;//检查对象签名

    private String branchs = "";

    private String streetId;
    private String streetInfo;

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getStreetInfo() {
        return streetInfo;
    }

    public void setStreetInfo(String streetInfo) {
        this.streetInfo = streetInfo;
    }

    public String getInspectNo() {
        return inspectNo;
    }

    public void setInspectNo(String inspectNo) {
        this.inspectNo = inspectNo;
    }

    public String getBranchs() {
        return branchs;
    }

    public void setBranchs(String branchs) {
        this.branchs = branchs;
    }

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

    public String getPeronSiagnPath() {
        return peronSiagnPath;
    }

    public void setPeronSiagnPath(String peronSiagnPath) {
        this.peronSiagnPath = peronSiagnPath;
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

    public String getNoticeState() {
        return NoticeState;
    }

    public void setNoticeState(String NoticeState) {
        this.NoticeState = NoticeState;
    }

    public String getRectifDeadline() {
        if(RectifDeadline==null){
            RectifDeadline = "";
        }
        return RectifDeadline;
    }

    public void setRectifDeadline(String rectifDeadline) {
        RectifDeadline = rectifDeadline;
    }

    public String getDXSList() {
        if(DXSList==null){
            DXSList="";
        }
        return DXSList;
    }

    public void setDXSList(String DXSList) {
        this.DXSList = DXSList;
    }

    public String getFWList() {
        if(FWList==null){
            FWList = "";
        }
        return FWList;
    }

    public void setFWList(String FWList) {
        this.FWList = FWList;
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public InspectHistoryBean() {
        isTake = false;
    }

    public boolean isTake() {
        return isTake;
    }

    public void setIsTake(boolean isTake) {
        this.isTake = isTake;
    }

    public void toggle(){
        isTake = !isTake;
    }

    public CheckDBBean getCheckDBBean() {
        return checkDBBean;
    }

    public void setCheckDBBean(CheckDBBean checkDBBean) {
        this.checkDBBean = checkDBBean;
    }

    public String getInspectGuid() {
        return inspectGuid;
    }

    public void setInspectGuid(String inspectGuid) {
        this.inspectGuid = inspectGuid;
    }

    private InspectItemsResultBean inspectItemsResultBean;

    public InspectItemsResultBean getInspectItemsResultBean() {
        return inspectItemsResultBean;
    }

    public void setInspectItemsResultBean(InspectItemsResultBean inspectItemsResultBean) {
        this.inspectItemsResultBean = inspectItemsResultBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CheckBaseInfoBean getCheckBaseInfoBean() {
        return checkBaseInfoBean;
    }

    public void setCheckBaseInfoBean(CheckBaseInfoBean checkBaseInfoBean) {
        this.checkBaseInfoBean = checkBaseInfoBean;
    }

    public String getBussinesstype() {
        return Bussinesstype;
    }

    public void setBussinesstype(String bussinesstype) {
        Bussinesstype = bussinesstype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
