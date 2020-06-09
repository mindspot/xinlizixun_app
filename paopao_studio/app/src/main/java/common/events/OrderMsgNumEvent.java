package common.events;

import common.repository.http.entity.order.OrderMsgNumResponseBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class OrderMsgNumEvent {
    private OrderMsgNumResponseBean bean;

    public OrderMsgNumEvent(OrderMsgNumResponseBean bean) {
        this.bean = bean;
    }

    public OrderMsgNumResponseBean getBean() {
        return bean;
    }

    public void setBean(OrderMsgNumResponseBean bean) {
        this.bean = bean;
    }
}
