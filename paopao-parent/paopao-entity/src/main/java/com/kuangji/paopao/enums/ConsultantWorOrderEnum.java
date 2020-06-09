package com.kuangji.paopao.enums;

/**
 * 
 * 性别类的枚举类型
 * 
 */
public enum ConsultantWorOrderEnum {
	HAVE_IN_HAND("进行中的订单", 0),
	END("已完结订单", 1);

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

	private ConsultantWorOrderEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}

}
