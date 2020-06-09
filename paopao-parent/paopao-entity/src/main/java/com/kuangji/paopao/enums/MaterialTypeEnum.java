package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MaterialTypeEnum {
    BOOK("书籍", 1),
    PAPER("论文", 2);
    @Getter
    private String desc;
    @Getter
    private Integer type;

}
