package common.repository.http.entity.app;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */
public class ServiceUrlResponseBean extends BaseResponseBean {
    private DataBean serviceSetInUrl;
    private DataBean setInUrl;

    public DataBean getServiceSetInUrl() {
        return serviceSetInUrl;
    }

    public void setServiceSetInUrl(DataBean serviceSetInUrl) {
        this.serviceSetInUrl = serviceSetInUrl;
    }

    public DataBean getSetInUrl() {
        return setInUrl;
    }

    public void setSetInUrl(DataBean setInUrl) {
        this.setInUrl = setInUrl;
    }

    public static class DataBean {
        private String val;

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

}
