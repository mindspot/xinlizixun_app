package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum  ConsultantTypeEnum {
    CONSULTANT(0,"咨询师"),
    SUPERVISOR(1,"督导师" );
    @Getter
    private Integer type;
    @Getter
    private String desc;
    public static ConsultantTypeEnum getConsultantTypeEnum(Integer type) {
        for (ConsultantTypeEnum consultantTypeEnum : ConsultantTypeEnum.values()) {
            if (consultantTypeEnum.getType().equals(type)) {
                return consultantTypeEnum;
            }
        }
        return null;
    }
}
