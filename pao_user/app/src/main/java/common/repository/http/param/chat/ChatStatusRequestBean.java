package common.repository.http.param.chat;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class ChatStatusRequestBean extends BaseRequestBean {
    private String userEasemobId;
    private String consultantEasemobId;
    private int serviceType;

    public String getUserEasemobId() {
        return userEasemobId;
    }

    public void setUserEasemobId(String userEasemobId) {
        this.userEasemobId = userEasemobId;
    }

    public String getConsultantEasemobId() {
        return consultantEasemobId;
    }

    public void setConsultantEasemobId(String consultantEasemobId) {
        this.consultantEasemobId = consultantEasemobId;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }
}
