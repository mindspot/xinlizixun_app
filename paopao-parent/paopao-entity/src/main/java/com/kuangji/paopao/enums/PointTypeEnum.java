package com.kuangji.paopao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum PointTypeEnum {
    RECHARGE("充值", 1),
    WITHDRAW("提现", 2),
    SETTLE("咨询订单-分佣结算", 3),
    REFUND("咨询订单-退款",4),
    CONSULTATION_PAYMENT("咨询订单-余额支付", 5),
    CONSULTATION_ZFB_PAYMENT("咨询订单-支付宝支付", 6),
    CONSULTATION_WX_PAYMENT("咨询订单-微信支付", 7),
    REJECT("提现驳回",8),
    SUPERVISION_REFUND("督导订单-退款",9),
    SUPERVISION_PAYMENT("督导订单-余额支付", 10),
    SUPERVISION_SETTLE("督导订单-分佣结算", 11),
    ;
	
    @Getter
    private String desc;
    @Getter
    private Integer type;
    
    
    public static String getPointTypeEnumValue(int type) {
		for (PointTypeEnum ele : PointTypeEnum.values()) {
			if(ele.getType().equals(type)) {
				return ele.desc;
			}
		}
		return null;
	}
}
