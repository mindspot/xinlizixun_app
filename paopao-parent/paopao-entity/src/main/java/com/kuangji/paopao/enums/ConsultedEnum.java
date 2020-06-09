package com.kuangji.paopao.enums;

/**
 * 
 * 是否接受过心理咨询
 * 
 */
public enum ConsultedEnum {
	CONSULTED_YES("是", 0), 
	CONSULTED_NO("否", 1);
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

	private ConsultedEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}

}
