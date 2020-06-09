package common.repository.http.entity.order;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class ConsultantOrderResponseBean extends BaseResponseBean {
    private ConsultationServerOrderBean consultationServerOrder;
    private String prepayment;

    public ConsultationServerOrderBean getConsultationServerOrder() {
        return consultationServerOrder;
    }

    public void setConsultationServerOrder(ConsultationServerOrderBean consultationServerOrder) {
        this.consultationServerOrder = consultationServerOrder;
    }

    public String getPrepayment() {
        return prepayment;
    }

    public void setPrepayment(String prepayment) {
        this.prepayment = prepayment;
    }

    public static class ConsultationServerOrderBean {
        private String orderNo;
        private int payType;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }
    }
}
