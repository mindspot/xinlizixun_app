package common.repository.http.param.user;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/28.
 */

public class FeedBackRequestBean extends BaseRequestBean {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
