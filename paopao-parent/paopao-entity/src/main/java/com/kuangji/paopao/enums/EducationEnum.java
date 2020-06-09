package com.kuangji.paopao.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 年龄段类的枚举类型
 */
public enum EducationEnum {
    JUNIOR_COLLEGE("本科以下", 1),
    UNDERGRADUATE("本科或在读", 2),
    MASTER("硕士或在读", 3),
    DOCTOR("博士或在读", 4);
    // 成员变量
    private String index;

    private int value;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    EducationEnum(String index, int value) {
        this.index = index;
        this.value = value;
    }

    public static List<String> listEducationEnum() {
        List<String> list = new ArrayList<String>();
        for (EducationEnum e : EducationEnum.values()) {
            list.add(e.getIndex());
        }
        return list;
    }

}
