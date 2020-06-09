package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CouponStatusEnum {
    WAIT_COLLECT(0,"待领取"),
    WAIT_USE(1,"待使用"),
    USED(2,"已使用"),
    OVERDUE(3,"已过期"),
    CANCELLED(4,"已取消");
    public int status;
    public String desc;
}
