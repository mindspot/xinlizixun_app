package common.repository.http.entity.order;

/**
 * Created by hpzhan on 2020/2/27.
 */

public class WorkTimeItemBean {
    private String consultantWorkStartTime;
    private String consultantWorkEndTime;
    private String dateStatus;
    private int type;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(String dateStatus) {
        this.dateStatus = dateStatus;
    }
}
