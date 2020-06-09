package common.repository.http.entity.app;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */
public class ServicePhoneResponseBean extends BaseResponseBean {
    private List<DataBean> consultantCustomers;
    private List<DataBean> consultantCustomersNow;
    private DataBean customerServicePeriod;

    public List<DataBean> getConsultantCustomers() {
        return consultantCustomers;
    }

    public void setConsultantCustomers(List<DataBean> consultantCustomers) {
        this.consultantCustomers = consultantCustomers;
    }

    public List<DataBean> getConsultantCustomersNow() {
        return consultantCustomersNow;
    }

    public void setConsultantCustomersNow(List<DataBean> consultantCustomersNow) {
        this.consultantCustomersNow = consultantCustomersNow;
    }

    public DataBean getCustomerServicePeriod() {
        return customerServicePeriod;
    }

    public void setCustomerServicePeriod(DataBean customerServicePeriod) {
        this.customerServicePeriod = customerServicePeriod;
    }

    public static class DataBean {
        private String val;
        private String remark;

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

}
