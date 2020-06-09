package common.repository.http.param.time;

/**
 * Created by hpzhan on 2020/2/25.
 */

public class UploadCustomTimeItemBean {
    private int platformWorkingTimeId;
    private String scheduleStartTime;
    private String scheduleEndTime;

    public int getPlatformWorkingTimeId() {
        return platformWorkingTimeId;
    }

    public void setPlatformWorkingTimeId(int platformWorkingTimeId) {
        this.platformWorkingTimeId = platformWorkingTimeId;
    }

    public String getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(String scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    public String getScheduleEndTime() {
        return scheduleEndTime;
    }

    public void setScheduleEndTime(String scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }
}
