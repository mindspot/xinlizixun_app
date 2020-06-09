package common.repository.http.param.order;

import java.util.List;

import common.repository.bean.UploadCaseItemBean;
import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class SubmitUserCaseRequestBean extends BaseRequestBean {
    private String orderNo;
    private List<UploadCaseItemBean> list;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<UploadCaseItemBean> getList() {
        return list;
    }

    public void setList(List<UploadCaseItemBean> list) {
        this.list = list;
    }
}
