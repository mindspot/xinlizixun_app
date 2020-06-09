package common.repository.http.entity.order;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */

public class ConsumeRecordResponseBean extends BaseResponseBean {
    private int total;
    private List<ConsumeRecordItemBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ConsumeRecordItemBean> getList() {
        return list;
    }

    public void setList(List<ConsumeRecordItemBean> list) {
        this.list = list;
    }
}
