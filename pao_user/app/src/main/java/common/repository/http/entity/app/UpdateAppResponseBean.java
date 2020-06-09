package common.repository.http.entity.app;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class UpdateAppResponseBean extends BaseResponseBean {
    private String appVersion;
    private int forceUpdate;//1强制更新 0不强制更新
    private String downloadAddress;
    private String content;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
