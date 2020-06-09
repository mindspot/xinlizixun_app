package common.repository.http.entity.order.councilor;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */

public class CouncilorResponseBean extends BaseResponseBean {
    private List<CouncilorItemBean> list;
    private int recordTotal;

    public List<CouncilorItemBean> getList() {
        return list;
    }

    public void setList(List<CouncilorItemBean> list) {
        this.list = list;
    }

    public int getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
    }
}
