package common.repository.http.entity.order;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/27.
 */

public class UseCouncilorResponseBean extends BaseResponseBean {
    private List<UseCouncilorItemBean> list;
    private int total;

    public List<UseCouncilorItemBean> getList() {
        return list;
    }

    public void setList(List<UseCouncilorItemBean> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
