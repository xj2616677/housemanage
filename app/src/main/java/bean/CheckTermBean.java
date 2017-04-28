package bean;

/**
 * Created by admin on 2016/5/3.
 */
public class CheckTermBean {

    private String Guid;
    private String Bussinesstype;
    private String Title;
    private String inspectClass;
    private String InspectClass;
    private String Options;
    private String IsInLaw;
    private String TextOptions;
    private String TextTitles;
    private String TextType;
    private String illegalOptions;
    private String enterprisePoints;
    private String PerInChargePoints;
    private String textneednum;
    private String value;
    private boolean isEdit = true;

    private boolean isShow;

    private boolean isCheck;

    private boolean isXCZG;

    private boolean isDescribe = false;

    public boolean isDescribe() {
        return isDescribe;
    }

    public void setIsDescribe(boolean isDescribe) {
        this.isDescribe = isDescribe;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean isXCZG() {
        return isXCZG;
    }

    public void setIsXCZG(boolean isXCZG) {
        this.isXCZG = isXCZG;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public String getTextneednum() {
        return textneednum;
    }

    public void setTextneednum(String textneednum) {
        this.textneednum = textneednum;
    }

    public void toggle() {
        this.isCheck = !this.isCheck;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }

    public String getBussinesstype() {
        return Bussinesstype;
    }

    public void setBussinesstype(String bussinesstype) {
        Bussinesstype = bussinesstype;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getInspectClass() {
        if(inspectClass==null){
            return InspectClass;
        }else{
            return inspectClass;
        }

    }

    public void setInspectClass(String inspectClass) {
        this.inspectClass = inspectClass;
    }

    public String getOptions() {
        return Options;
    }

    public void setOptions(String options) {
        Options = options;
    }

    public String getIsInLaw() {
        return IsInLaw;
    }

    public void setIsInLaw(String isInLaw) {
        IsInLaw = isInLaw;
    }

    public String getTextOptions() {
        return TextOptions;
    }

    public void setTextOptions(String textOptions) {
        TextOptions = textOptions;
    }

    public String getTextTitles() {
        return TextTitles;
    }

    public void setTextTitles(String textTitles) {
        TextTitles = textTitles;
    }

    public String getTextType() {
        return TextType;
    }

    public void setTextType(String textType) {
        TextType = textType;
    }

    public String getIllegalOptions() {
        return illegalOptions;
    }

    public void setIllegalOptions(String illegalOptions) {
        this.illegalOptions = illegalOptions;
    }

    public String getEnterprisePoints() {
        return enterprisePoints;
    }

    public void setEnterprisePoints(String enterprisePoints) {
        this.enterprisePoints = enterprisePoints;
    }

    public String getPerInChargePoints() {
        return PerInChargePoints;
    }

    public void setPerInChargePoints(String perInChargePoints) {
        PerInChargePoints = perInChargePoints;
    }
}
