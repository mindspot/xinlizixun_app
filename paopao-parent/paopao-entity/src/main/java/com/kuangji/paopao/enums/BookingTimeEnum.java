package com.kuangji.paopao.enums;

/**
 * Author 金威正 Date 2020-02-27
 */

public enum BookingTimeEnum {
	 RESERVATIONS("可预约", 0),
	 BOOKING("已经被预约", 1),
	 REST("休息", 2);
	
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

	private BookingTimeEnum(String index, int value) {
		this.index = index;
		this.value = value;
	}	
   
	
	public static boolean isInclude(int key){
        boolean include = false;
        for (BookingTimeEnum e: BookingTimeEnum.values()){
            if(e.getValue()==key){
                include = true;
                break;
            }
        }
        return include;
    }
}