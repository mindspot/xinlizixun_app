package common.repository.http.entity.time;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class SystemPlanTimeItemBean extends BaseResponseBean {
    public static final int KEY_STATUS_USE = 0;
    public static final int KEY_STATUS_REST = 2;
    private String consultantWorkStartTime;
    private String consultantWorkEndTime;
    private int timeType;//0 可预约 2休息
    private int platformWorkingTimeId;

    public String getConsultantWorkStartTime() {
        return consultantWorkStartTime;
    }

    public void setConsultantWorkStartTime(String consultantWorkStartTime) {
        this.consultantWorkStartTime = consultantWorkStartTime;
    }

    public String getConsultantWorkEndTime() {
        return consultantWorkEndTime;
    }

    public void setConsultantWorkEndTime(String consultantWorkEndTime) {
        this.consultantWorkEndTime = consultantWorkEndTime;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public int getPlatformWorkingTimeId() {
        return platformWorkingTimeId;
    }

    public void setPlatformWorkingTimeId(int platformWorkingTimeId) {
        this.platformWorkingTimeId = platformWorkingTimeId;
    }
}
