package common.repository.http.entity.order;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/27.
 */

public class OrderMsgNumResponseBean extends BaseResponseBean {
    private int unConsultationOrderNum;
    private int unConsultantSupervisorOrderNum;
    private int unSupervisorOrderNum;

    public int getUnConsultationOrderNum() {
        return unConsultationOrderNum;
    }

    public void setUnConsultationOrderNum(int unConsultationOrderNum) {
        this.unConsultationOrderNum = unConsultationOrderNum;
    }

    public int getUnConsultantSupervisorOrderNum() {
        return unConsultantSupervisorOrderNum;
    }

    public void setUnConsultantSupervisorOrderNum(int unConsultantSupervisorOrderNum) {
        this.unConsultantSupervisorOrderNum = unConsultantSupervisorOrderNum;
    }

    public int getUnSupervisorOrderNum() {
        return unSupervisorOrderNum;
    }

    public void setUnSupervisorOrderNum(int unSupervisorOrderNum) {
        this.unSupervisorOrderNum = unSupervisorOrderNum;
    }
}
