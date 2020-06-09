package com.kuangji.paopao.enums;

/**
 * 
 * userEnum
 * 
 */
public enum LoginEnum {
	LOGIN_TO_EXAMINE(0,"审核中" ), 
	LOGIN_RESTRICTED_ENTRY(1,"限制登入" ) ,
	LOGIN_NORMAL(2,"正常登入" ),
	LOGIN_REJECT(3,"驳回中" ),
	LOGIN_CONSULTANT_TYPE(0,"咨询师" ),
	LOGIN_USER_TYPE(1,"客户" ),
	;
	// 成员变量
	private int index;

	private String value;

	private LoginEnum(int index, String value) {
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
		for (LoginEnum ele : LoginEnum.values()) {
			if(ele.getIndex()==code) {
				return ele.getValue();
			}
		}
		return null;
	}

}
