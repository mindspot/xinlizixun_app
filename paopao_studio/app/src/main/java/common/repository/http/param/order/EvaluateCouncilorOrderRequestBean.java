package common.repository.http.param.order;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class EvaluateCouncilorOrderRequestBean extends BaseRequestBean {
    private String orderNo;
    private String content;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
