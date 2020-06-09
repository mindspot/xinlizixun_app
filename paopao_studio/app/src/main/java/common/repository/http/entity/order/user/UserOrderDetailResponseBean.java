package common.repository.http.entity.order.user;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */

public class UserOrderDetailResponseBean extends BaseResponseBean {
    private boolean isDiagnosis;
    private UserOrderDetailInfoBean orderInfo;
    private MemberCaseInfoBean memberCase;

    public UserOrderDetailInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(UserOrderDetailInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    public MemberCaseInfoBean getMemberCase() {
        return memberCase;
    }

    public void setMemberCase(MemberCaseInfoBean memberCase) {
        this.memberCase = memberCase;
    }

    public boolean isDiagnosis() {
        return isDiagnosis;
    }

    public void setDiagnosis(boolean diagnosis) {
        isDiagnosis = diagnosis;
    }
}
