package com.kuangji.paopao.enums;

/**
 * 
 * 年龄段类的枚举类型
 * 
 */
public enum UserLabelEnum {
	
	DOMAIN_LABEL_TYPE("咨询师领域擅长类别", 0);
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

	private UserLabelEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}

}
