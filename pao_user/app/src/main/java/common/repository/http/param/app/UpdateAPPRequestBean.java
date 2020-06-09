package common.repository.http.param.app;

import com.framework.http.bean.RequestBean;

import java.util.HashMap;
import java.util.Map;

import common.repository.http.param.BaseRequestBean;
import common.repository.http.param.RequestHeaderHelper;


public class UpdateAPPRequestBean extends BaseRequestBean {
    private String bundleId;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
}
