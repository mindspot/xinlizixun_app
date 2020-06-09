package common.repository.http.entity.order;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/3/9.
 */

public class SetMealResponseBean extends BaseResponseBean {
    private int total;
    private List<SetMealItemBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SetMealItemBean> getList() {
        return list;
    }

    public void setList(List<SetMealItemBean> list) {
        this.list = list;
    }
}
