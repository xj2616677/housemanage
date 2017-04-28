package bean;

/**
 * Created by admin on 2016/4/26.
 */
public class TaskBean {


    private String ID;
    private String Name;
    private String StartTime;
    private String EndTime;
    private String CheckType;
    private String ReceiveBranchs;
    private String ReceivePersons;
    private String TaskDescription;
    private String InspectTempleteID;
    private String AddLoginName;
    private String AddTime;
    private String attachment;
    private String state;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getCheckType() {
        return CheckType;
    }

    public void setCheckType(String checkType) {
        CheckType = checkType;
    }

    public String getReceiveBranchs() {
        return ReceiveBranchs;
    }

    public void setReceiveBranchs(String receiveBranchs) {
        ReceiveBranchs = receiveBranchs;
    }

    public String getReceivePersons() {
        return ReceivePersons;
    }

    public void setReceivePersons(String receivePersons) {
        ReceivePersons = receivePersons;
    }

    public String getTaskDescription() {
        return TaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        TaskDescription = taskDescription;
    }

    public String getInspectTempleteID() {
        return InspectTempleteID;
    }

    public void setInspectTempleteID(String inspectTempleteID) {
        InspectTempleteID = inspectTempleteID;
    }

    public String getAddLoginName() {
        return AddLoginName;
    }

    public void setAddLoginName(String addLoginName) {
        AddLoginName = addLoginName;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return Name;
    }
}
