package common.repository.http.entity.order;

import java.util.List;

/**
 * Created by hpzhan on 2020/2/27.
 */

public class WorkTimeInfoItemBean {
    private String week;
    private List<WorkTimeItemBean> list;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<WorkTimeItemBean> getList() {
        return list;
    }

    public void setList(List<WorkTimeItemBean> list) {
        this.list = list;
    }
}
