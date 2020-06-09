package common.repository.http.param.user;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/28.
 */

public class ChatUserStatusRequestBean extends BaseRequestBean {
    private String easemobId;

    public String getEasemobId() {
        return easemobId;
    }

    public void setEasemobId(String easemobId) {
        this.easemobId = easemobId;
    }
}
