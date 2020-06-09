package common.repository.http.param.app;

import common.repository.http.param.BaseRequestBean;


public class UpdateAPPRequestBean extends BaseRequestBean {
    private String bundleId;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
}
