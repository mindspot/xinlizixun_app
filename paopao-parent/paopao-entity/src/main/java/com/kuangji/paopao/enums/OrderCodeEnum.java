package com.kuangji.paopao.enums;

/**
 * 订单状态枚举类型 配合前端调用
 * 
 */
public enum OrderCodeEnum {
    //1待支付  2 已经取消   3 已支付 4已完成
	PAY_SUCC(3,"已支付"),
	PAY_WAIT(1,"待支付"),
	CANCEL_BUYER(2,"已取消"),
	TRADE_SUCC(4,"已完成"),
	SEND_SUCC(5,"已确定"),
	RFUSE(11,"已拒绝");
	public int code;
    public String desc;
	OrderCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
	
	public static int mallOrderCode(int payStausCode, int orderStatus) {
		
		if (orderStatus==OrderStatusEnum.PAY_SUCC.code) {
			return OrderCodeEnum.PAY_SUCC.code;
		}
		if (orderStatus==OrderStatusEnum.SEND_SUCC.code) {
			return OrderCodeEnum.SEND_SUCC.code;
		
		}
		if (orderStatus==OrderStatusEnum.CREATE_SUCC.code) {
			return OrderCodeEnum.PAY_WAIT.code;
		}
		if (orderStatus==OrderStatusEnum.TRADE_SUCC.code) {
			return OrderCodeEnum.TRADE_SUCC.code;
		}
		//卖家取消状态
		if (orderStatus==OrderStatusEnum.REFUND.code) {
			return OrderCodeEnum.CANCEL_BUYER.code;
		}
		
		//买家取消状态
		if (orderStatus==OrderStatusEnum.CANCEL_BUYER.code) {
			return OrderCodeEnum.CANCEL_BUYER.code;
		}
		return 0;
		
	}
	
	
	// -1 已拒绝       // 1已申请   // 2已取消     //4已完结  //5已接单 
	public static int supervisorOrderCode( int orderStatus) {
	
		//SEND_SUCC(2,"已确认"),  
		if (orderStatus==OrderStatusEnum.SEND_SUCC.code) {
			//	5 已接单
			return OrderCodeEnum.SEND_SUCC.code;
		
		}
		// CREATE_SUCC(0,"创建成功"),  
		if (orderStatus==OrderStatusEnum.CREATE_SUCC.code) {
			// 1 已申请 
			return OrderCodeEnum.PAY_WAIT.code;
		}
		//TRADE_SUCC(10,"交易完成");
		if (orderStatus==OrderStatusEnum.TRADE_SUCC.code) {
			//4       已完成
			return OrderCodeEnum.TRADE_SUCC.code;
		}
		//REFUND(-2,"卖家取消"), 
		if (orderStatus==OrderStatusEnum.REFUND.code) {
			//-1 已拒绝
			//return OrderCodeEnum.RFUSE.code;
			return OrderCodeEnum.CANCEL_BUYER.code;
		}
		//CANCEL_BUYER(-1,"买家取消")
		if (orderStatus==OrderStatusEnum.CANCEL_BUYER.code) {
			//2 已取消
			return OrderCodeEnum.CANCEL_BUYER.code;
		}
		return 0;
		
	}
}