package common.repository.http.param.app;

import common.repository.http.param.BaseRequestBean;

public class GetCodeRequestBean extends BaseRequestBean {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
