package common.repository.http.param.order;

import java.util.List;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class PayCouncilorOrderRequestBean extends BaseRequestBean {
    private int proportion;
    private int consultantId;
    private List<String> orderNos;

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
    }

    public int getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(int consultantId) {
        this.consultantId = consultantId;
    }

    public List<String> getOrderNos() {
        return orderNos;
    }

    public void setOrderNos(List<String> orderNos) {
        this.orderNos = orderNos;
    }
}
