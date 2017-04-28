package bean;

import java.util.List;

/**
 * Created by admin on 2016/5/18.
 */
public class BranchBean {

    private String Branch;
    private List<PersonBean> personList;
    private List<CompreBranchBean> compreBranchBeans;

    public List<CompreBranchBean> getCompreBranchBeans() {
        return compreBranchBeans;
    }

    public void setCompreBranchBeans(List<CompreBranchBean> compreBranchBeans) {
        this.compreBranchBeans = compreBranchBeans;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public void setListBranch(){
        if(personList!=null&&personList.size()!=0){
            for(PersonBean bean:personList){
                bean.setBranch(Branch);
            }
        }
    }

    public List<PersonBean> getPersonList() {
        return personList;
    }

    public void setPersonList(List<PersonBean> personList) {
        this.personList = personList;
    }
}
