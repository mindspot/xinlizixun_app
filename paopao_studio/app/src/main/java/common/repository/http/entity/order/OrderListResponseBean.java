package common.repository.http.entity.order;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */

public class OrderListResponseBean extends BaseResponseBean {
    private int total;
    private List<OrderListItemBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<OrderListItemBean> getList() {
        return list;
    }

    public void setList(List<OrderListItemBean> list) {
        this.list = list;
    }
}
