package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum OperateTypeEnum {
    ADD(1,"新增"),
    DELETE(2,"删除" ),
    UPDATE(3,"修改");
    @Getter
    private Integer type;
    @Getter
    private String desc;
    public static OperateTypeEnum getOperateTypeEnum(Integer type) {
        for (OperateTypeEnum operateTypeEnum : OperateTypeEnum.values()) {
            if (operateTypeEnum.getType().equals(type)) {
                return operateTypeEnum;
            }
        }
        return null;
    }
}
