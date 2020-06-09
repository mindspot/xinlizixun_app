package com.kuangji.paopao.enums;

/**
 * 订单来源
 * @author Administrator
 */
public enum OrderFromEnum {
	/** 线上消费类订单 */
	ON_LINE_MP(10,"线上"),
	
	/** 线下下单 */
	OFF_LINE(20,"线下");
	
	public int code;
    public String desc;
    OrderFromEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static boolean include(int code){
        for (OrderFromEnum e: OrderFromEnum.values()){
            if(e.code == code){
            	return true; 
            }
        }
        return false;
    }
}

