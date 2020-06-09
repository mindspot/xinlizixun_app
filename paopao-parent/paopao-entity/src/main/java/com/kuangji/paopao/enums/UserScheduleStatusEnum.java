package com.kuangji.paopao.enums;

/**
 * 
 * userEnum
 * 
 */
public enum UserScheduleStatusEnum {
	NORMAL(0,"可预约" ),
	BE_BOOKED(1,"已预约" ),
	REST(2,"休息" ) ,
	;
	// 成员变量
	private int index;

	private String value;

	private UserScheduleStatusEnum(int index, String value) {
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static String getValue(int code) {
		for (UserScheduleStatusEnum ele : UserScheduleStatusEnum.values()) {
			if(ele.getIndex()==code) {
				return ele.getValue();
			}
		}
		return null;
	}

}
