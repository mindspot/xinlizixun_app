package common.repository.http.entity.counsel;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/22.
 */

public class CounselListResponseBean extends BaseResponseBean {
    private List<CounselListItemBean> list;
    private int recordTotal;

    public List<CounselListItemBean> getList() {
        return list;
    }

    public void setList(List<CounselListItemBean> list) {
        this.list = list;
    }

    public int getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
    }
}
