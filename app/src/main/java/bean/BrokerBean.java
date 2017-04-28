package bean;

import java.util.List;

/**
 * 机构bean
 * Created by admin on 2016/4/11.
 */
public class BrokerBean {

    private String name;
    private List<InfoAttributeBean> attributeBeans;

    public List<InfoAttributeBean> getAttributeBeans() {
        return attributeBeans;
    }

    public void setAttributeBeans(List<InfoAttributeBean> attributeBeans) {
        this.attributeBeans = attributeBeans;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
