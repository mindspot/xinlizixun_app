package com.kuangji.paopao.enums;

/**
 * 订单状态枚举类型
 * @author Administrator
 */
public enum DelayQueueManagerTypeEnum {
	TEMP_UPDATE_ORDER(1,"临时修改临时状态 ,订单锁"),
	START_ORDER(2,"提前半小时发送订单开始消息"),
	TWENT_FOUR_HOURS(3,"24小时咨询师未接单"),
	SET_MEAL_TWENT_FOUR_HOURS(4,"24小时咨询师未接套餐单");
	public int code;
    public String desc;
    DelayQueueManagerTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
	
}