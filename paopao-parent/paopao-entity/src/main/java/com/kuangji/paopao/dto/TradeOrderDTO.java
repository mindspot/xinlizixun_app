package com.kuangji.paopao.dto;
import java.util.Date;

import lombok.Data;

/**
 * 用于线上支付的交易订单信息
 * @author Administrator
 *
 */
@Data
public class TradeOrderDTO implements java.io.Serializable{
	private static final long serialVersionUID = 1931232982165873380L;
	
	/** 订单编号 */
	private String orderNo = "";
	
	/** 下单时间 */
	private Date orderTime = null;
	
	/** 下单人姓名 */
	private String operName = "";
	
	/** 支付方式：1=微信公众号支付，2=支付宝 */
	private Integer payType = 0;
	
	/** 订单金额，单位：分 */
	private Integer orderAmount = 0;
	
	/** 商户交易单号，用于第三方支付 */
	private String outTradeNo = "";
	
	/** 订单内容描述，一般用于支付 */
	private String orderDetail = "";
	
	/** 支付请求客户端ip */
	private String ip = "";
	

}
