package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserTypeEnum {
    CONSULTANT(0,"咨询师"),
    MEMBER(1,"会员" ),
    ADMIN(2,"管理员");
    @Getter
    private Integer type;
    @Getter
    private String desc;
    public static UserTypeEnum getUserTypeEnum(Integer type) {
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if (userTypeEnum.getType().equals(type)) {
                return userTypeEnum;
            }
        }
        return null;
    }
}
