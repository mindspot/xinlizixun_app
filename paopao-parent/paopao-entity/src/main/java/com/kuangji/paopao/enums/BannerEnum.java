package com.kuangji.paopao.enums;

/**
 * 
 * 年龄段类的枚举类型
 * 
 */
public enum BannerEnum {
	HIDE("隐藏", 0), 
	SHOW("显示", 1);
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

	private BannerEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}

}
