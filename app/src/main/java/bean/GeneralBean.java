package bean;

/**
 * 普通地下室
 * Created by admin on 2016/4/28.
 */
public class GeneralBean {

    private String base_id;
    private String name;
    private String type;
    private String address;
    private String manageBranch;
    private String rightsMan;

    private String houseLocate;//房屋坐落
    private String buildingName;//漏洞地址
    private String roomUseTypeName;//楼幢类别
    private String basementAddress;//地下室详细地址
    private String management;//管理方
    private String propertyPerson;//产权方

    private String addman;

    private boolean isCheck;

    private boolean isSelfAdd = false;

    public String getAddman() {
        return addman;
    }

    public void setAddman(String addman) {
        this.addman = addman;
    }

    public boolean isSelfAdd() {
        return isSelfAdd;
    }

    public void setIsSelfAdd(boolean isSelfAdd) {
        this.isSelfAdd = isSelfAdd;
    }

    public String getHouseLocate() {
        return houseLocate;
    }

    public void setHouseLocate(String houseLocate) {
        this.houseLocate = houseLocate;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getRoomUseTypeName() {
        return roomUseTypeName;
    }

    public void setRoomUseTypeName(String roomUseTypeName) {
        this.roomUseTypeName = roomUseTypeName;
    }

    public String getBasementAddress() {
        return basementAddress;
    }

    public void setBasementAddress(String basementAddress) {
        this.basementAddress = basementAddress;
    }

    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getPropertyPerson() {
        return propertyPerson;
    }

    public void setPropertyPerson(String propertyPerson) {
        this.propertyPerson = propertyPerson;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    private String projectName;//项目名称

    public void toggle(){
        isCheck = !isCheck;
    }

    public String getBase_id() {
        return base_id;
    }

    public void setBase_id(String base_id) {
        this.base_id = base_id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getManageBranch() {
        return manageBranch;
    }

    public void setManageBranch(String manageBranch) {
        this.manageBranch = manageBranch;
    }

    public String getRightsMan() {
        return rightsMan;
    }

    public void setRightsMan(String rightsMan) {
        this.rightsMan = rightsMan;
    }
}
