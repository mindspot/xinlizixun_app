package common.repository.http.entity.app;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */
public class ServicePhoneResponseBean extends BaseResponseBean {
    private List<DataBean> userCustomers;
    private List<DataBean> userCustomersNow;
    private DataBean customerServicePeriod;

    public List<DataBean> getUserCustomers() {
        return userCustomers;
    }

    public void setUserCustomers(List<DataBean> userCustomers) {
        this.userCustomers = userCustomers;
    }

    public List<DataBean> getUserCustomersNow() {
        return userCustomersNow;
    }

    public void setUserCustomersNow(List<DataBean> userCustomersNow) {
        this.userCustomersNow = userCustomersNow;
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
