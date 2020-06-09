package common.repository.http.param.order;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class UserOrderDetailRequestBean extends BaseRequestBean {
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
