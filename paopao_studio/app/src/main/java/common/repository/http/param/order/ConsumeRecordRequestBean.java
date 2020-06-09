package common.repository.http.param.order;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class ConsumeRecordRequestBean extends BaseRequestBean {
    private int pageNo;
    private int pageSize = 10;
    private int type = 0;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
