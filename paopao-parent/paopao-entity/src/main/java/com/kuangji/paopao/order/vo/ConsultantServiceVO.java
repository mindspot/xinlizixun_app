package com.kuangji.paopao.order.vo;

import lombok.Data;

/**
 * 用于线上支付的交易订单信息
 * 
 * @author Administrator
 *
 */
@Data
public class ConsultantServiceVO implements java.io.Serializable {
	private static final long serialVersionUID = 1931232982165873380L;

	/** 订单编号 */
	private String orderNo = "";

	/** 下单人姓名 */
	private String operName = "";

	/** 订单金额，单位：分 */
	private Integer orderAmount = 0;

	// 日期
	private String consultantWorkDate;
	// 时间段
	private String consultantWorkTime;
	//语音视频方式
	private String goodsClassName;
}
