package common.repository.http.param.time;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class SetAllDayRestRequestBean extends BaseRequestBean {
    private String date;
    private int timeType;//0恢复默认排班  2 休息

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }
}
