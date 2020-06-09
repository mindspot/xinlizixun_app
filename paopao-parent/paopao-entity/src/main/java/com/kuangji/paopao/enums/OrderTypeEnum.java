package com.kuangji.paopao.enums;
/**
 * 订单类型枚举类型
 * @author Administrator
 */
public enum OrderTypeEnum {
	CONSULTANT(1,"咨询订单"),
	SET_MEAL(6,"套餐订单"),
	PAY_SET_MEAL(9,"套餐卡付费订单"),
	;
	
	public int code;
    public String desc;
    OrderTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static boolean include(int code){
        for (OrderTypeEnum e: OrderTypeEnum.values()){
            if(e.code == code){
            	return true; 
            }
        }
        return false;
    }
}
