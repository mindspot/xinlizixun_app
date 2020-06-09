package common.repository.http.param.time;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class UploadWorkPlanRequestBean extends BaseRequestBean {
    private String date;
    private String body;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
