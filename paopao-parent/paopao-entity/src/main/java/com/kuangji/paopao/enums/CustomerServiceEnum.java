package com.kuangji.paopao.enums;

public enum CustomerServiceEnum {
	CONSULTANT("咨询师客服", 0),
	CUSTOMER("客户客服", 1),
	CONSULTANT_SERVICE_NOW("咨询师在线客服", 2),
	CUSTOMER_SERVICE_NOW("用户在线客服", 3);

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

	private CustomerServiceEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}
}
