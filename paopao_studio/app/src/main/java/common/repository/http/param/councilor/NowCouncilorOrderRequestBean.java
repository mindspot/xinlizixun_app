package common.repository.http.param.councilor;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class NowCouncilorOrderRequestBean extends BaseRequestBean {
    private String easemobsId;

    public String getEasemobsId() {
        return easemobsId;
    }

    public void setEasemobsId(String easemobsId) {
        this.easemobsId = easemobsId;
    }
}
