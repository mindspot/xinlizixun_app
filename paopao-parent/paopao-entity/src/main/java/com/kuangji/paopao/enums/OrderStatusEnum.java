package com.kuangji.paopao.enums;

/**
 * 订单状态枚举类型
 * @author Administrator
 */
public enum OrderStatusEnum {
	TRADE_CLOSE(-11,"交易关闭"),
	TRADE_FAIL(-10,"交易失败"),
	REFUND(-2,"卖家取消"),
	CANCEL_BUYER(-1,"买家取消"),
	CREATE_SUCC(0,"创建成功"),
	PAY_SUCC(1,"已支付"),
	SEND_SUCC(2,"已确认"),
	TRADE_SUCC(10,"交易完成");
	
	public int code;
    public String desc;
	OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
	
}