package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CouponValidTypeEnum {
    FIXED_DATE(1,"固定日期"),
    FIXED_DAYS(2,"固定天数");
    public int type;
    public String desc;
}
