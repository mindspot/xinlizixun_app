package common.repository.http.param.artivles;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class ArtivlesRequestBean extends BaseRequestBean {
    private int commId;
    private int pageNum;

    public int getCommId() {
        return commId;
    }

    public void setCommId(int commId) {
        this.commId = commId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
