package common.repository.http.param.counsel;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/22.
 */

public class SearchCounselListRequestBean extends BaseRequestBean {
    private int pageNum;
    private String wd;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }
}
