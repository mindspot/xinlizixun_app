package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DeleteStatusEnum {
    YES(1, "有效"), NO(0, "无效");
    @Getter
    private Integer status;
    @Getter
    private String desc;
}
