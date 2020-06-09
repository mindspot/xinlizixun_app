package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CouponTypeEnum {
    FULL_REDUCTION(1,"满减"),
    REDUCTION(2,"立减"),
    DISCOUNT(3,"折扣"),
    REDEEM(4,"兑换码");
    public Integer type;
    public String desc;

    public static CouponTypeEnum getCouponTypeEnum(Integer type) {
        for (CouponTypeEnum couponTypeEnum : CouponTypeEnum.values()) {
            if (couponTypeEnum.type.equals(type)) {
                return couponTypeEnum;
            }
        }
        return null;
    }
}