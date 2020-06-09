package common.repository.http.param.order;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class UserOrderRequestBean extends BaseRequestBean {
    private int pageNum;
    private int type;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
