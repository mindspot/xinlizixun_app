package com.kuangji.paopao.enums;

/**
 * 
 * 头衔
 * 
 */
public enum  ConsultantTitleEnum {
	FIRST_LEVEL_CONSULTANT(1,"一级咨询师" ), 
	SECOND_LEVEL_CONSULTANT(2,"二级咨询师" ) ,
	LEVEL_THREE_CONSULTANT(3,"三级咨询师" ) ;
	private int index;

	private String value;

	private ConsultantTitleEnum(int index, String value) {
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
		for (ConsultantTitleEnum ele : ConsultantTitleEnum.values()) {
			if(ele.getIndex()==code) {
				return ele.getValue();
			}
		}
		return null;
	}

}
