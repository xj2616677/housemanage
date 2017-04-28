package bean;

import java.io.Serializable;

/**
 * Created by admin on 2016/7/11.
 */
public class ImageFileBean implements Serializable{

    private String Type;
    private String Name;
    private String Note;
    private String Guid;
    private String url;
    private boolean isFail ;

    public boolean isFail() {
        return isFail;
    }

    public void setIsFail(boolean isFail) {
        this.isFail = isFail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }
}
