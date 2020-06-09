package common.repository.http.entity.order.councilor;

import common.repository.http.entity.order.user.MemberCaseInfoBean;
import common.repository.http.entity.order.user.UserOrderDetailInfoBean;

/**
 * Created by hpzhan on 2020/2/24.
 */

public class NowUserOrderDetailItemBean {
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
}
