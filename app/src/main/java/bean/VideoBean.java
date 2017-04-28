package bean;

/**
 * Created by admin on 2016/8/29.
 */
public class VideoBean {

    private String ID;
    private String InspectGuid;
    private String InspectNo;
    private String Type;
    private String Path;
    private String Name;
    private String Note;
    private String url;
    private String Guid;
    private String IsValid;
    private String IsTemp;
    private boolean isLocal;

    public boolean isLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }

    public String getIsValid() {
        return IsValid;
    }

    public void setIsValid(String isValid) {
        IsValid = isValid;
    }

    public String getIsTemp() {
        return IsTemp;
    }

    public void setIsTemp(String isTemp) {
        IsTemp = isTemp;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
