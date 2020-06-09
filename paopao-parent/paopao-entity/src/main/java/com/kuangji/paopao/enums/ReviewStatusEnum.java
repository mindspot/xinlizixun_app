package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ReviewStatusEnum {
    WAIT(0,"等到审核"),
    REVIEWING(1,"审核中" ),
    PASSED(2,"审核通过"),
    REJECT(3,"驳回");
    @Getter
    private Integer type;
    @Getter
    private String desc;
}
