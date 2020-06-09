package com.kuangji.paopao.order.vo;

import java.util.List;

import com.kuangji.paopao.model.MallTradeOrderDiscount;

import lombok.Data;

/**
 * 订单金额对象
 * @ClassName:   OrderAmountVO 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author:      mail.chenc@foxmail.com
 * @date         2019年6月26日 下午3:13:46 
 *
 */
@Data
public class OrderAmountVO implements java.io.Serializable{
	private static final long serialVersionUID = 8765571055469507039L;
	/** 订单金额/支付金额 */
	private Integer orderAmount;
	
	/** 商品金额 */
	private Integer goodsAmount;
	
	/** 总优惠金额 */
	private Integer discountAmount;
	
	
	/**优惠明细 */
	private List<MallTradeOrderDiscount> discountItems;
}
