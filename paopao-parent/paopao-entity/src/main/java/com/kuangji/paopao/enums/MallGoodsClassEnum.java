package com.kuangji.paopao.enums;

/**
 * 
 * 商品段类的枚举类型
 * 
 */
public enum MallGoodsClassEnum {
	
	PAY_SET_MEAL("特殊类型,用于套餐支付", -1),
	
	CONSULTANT_SERVICE("咨询服务", 1), 
	
	COUPON("优惠劵", 4),
	
	SET_MEAL("咨询套餐", 6);

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

	private MallGoodsClassEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}
	public static String getValue(int code) {
		for (MallGoodsClassEnum ele : MallGoodsClassEnum.values()) {
			if(ele.getValue()==code) {
				return ele.index;
			}
		}
		return null;
	}
}
