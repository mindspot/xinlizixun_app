package com.kuangji.paopao.enums;

/**
 * 
 * 是否接受过心理咨询
 * 
 */
public enum ConsultationServiceEnum {
	NOT_BUY_SET_MEAL("没有购买套餐服务", 0), 
	BUY_SET_MEAL("购买套餐服务", 1),
	PAY_SET_MEAL("支付套餐服务", -3);
	
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

	private ConsultationServiceEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}

}
