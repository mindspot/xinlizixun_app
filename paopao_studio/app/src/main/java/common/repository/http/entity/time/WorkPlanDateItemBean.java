package common.repository.http.entity.time;

import java.util.List;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class WorkPlanDateItemBean {
    private String week;
    private List<WorkPlanTimeItemBean> list;
    private boolean stopBooking;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<WorkPlanTimeItemBean> getList() {
        return list;
    }

    public void setList(List<WorkPlanTimeItemBean> list) {
        this.list = list;
    }

    public boolean isStopBooking() {
        return stopBooking;
    }

    public void setStopBooking(boolean stopBooking) {
        this.stopBooking = stopBooking;
    }
}
