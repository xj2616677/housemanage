package bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class InspectItemsResultBean {
    private List<CheckHeadBean> head;
    private List<CheckResultBean> result;

    public List<CheckResultBean> getResult() {
        return result;
    }

    public void setResult(List<CheckResultBean> result) {
        this.result = result;
    }

    public List<CheckHeadBean> getHead() {
        return head;
    }

    public void setHead(List<CheckHeadBean> head) {
        this.head = head;
    }

}
