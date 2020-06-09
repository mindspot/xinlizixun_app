package common.repository.http.param.app;

import common.repository.http.param.BaseRequestBean;

public class ConfigRequestBean extends BaseRequestBean {

    private String configVersion;//配置版本号

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }


}
