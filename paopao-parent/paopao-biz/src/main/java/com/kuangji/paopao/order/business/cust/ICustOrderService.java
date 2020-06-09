package com.kuangji.paopao.order.business.cust;

import com.kuangji.paopao.dto.TradeOrderDTO;
import com.kuangji.paopao.order.vo.BaseOrderFormVO;
import com.kuangji.paopao.order.vo.OrderAmountVO;

/**
 * 买家订单服务
 * @ClassName:   IBuyerOrderService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author:      mail.chenc@foxmail.com
 * @date         2019年6月21日 上午11:10:04 
 *
 */
public interface ICustOrderService {

	/**
	 * 买家统一下单接口<br/>
	 * @param ovo					统一下单接口参数
	 * @return						返回交易订单信息
	 */
	public TradeOrderDTO dealUnifiedOrder(BaseOrderFormVO fvo);
	
	/**
	 * 支付接口<br/>
	 * 1.用于'待支付'订单 继续支付逻辑<br/>
	 * 2.掉用该接口会重新生成第三方交易流水号<br/>
	 * @param memberId				会员ID
	 * @param orderNo				订单编号
	 * @return: TradeOrderDTO      	返回交易订单信息
	 */
	public TradeOrderDTO dealPay(Long memberId, String orderNo);
	
	/**
	 * 支付成功更改结果
	 * @param orderId				订单ID
	 * @param orderType				订单类型
	 * @param transactionId			第三方交易流水
	 * @return						成功返回true，失败返回false
	 */
	public boolean dealPaySuccRes(Long orderId, Integer orderType, String transactionId);
	
	/**
	 * 计算订单金额
	 * @param fvo					统一下单接口参数
	 * @return: OrderAmountVO      	返回订单金额信息
	 */
	public OrderAmountVO calcOrderAmount(BaseOrderFormVO fvo);
	
	/**
	 * 买家取消订单<br/>
	 * 1.买家可以手动取消'待支付'的订单，其他状态下的订单不能取消<br/>
	 * 2.取消订单不会执行退款流程<br/>
	 * @param orderId 			订单ID
	 * @param buyerId 			卖家ID
	 * @return: boolean      	取消成功返回true，取消失败返回false
	 */
	public boolean dealCancel(Long orderId, Long buyerId);
	
	/**
	 * 买家申请退款<br/>
	 * 1.买家手动申请退款，要求必须是'已支付'并且订单未完成<br/>
	 * 2.申请退款后将执行退货流程<br/>
	 * @param
	 * @return: boolean      
	 */
	public boolean dealApplyRefund(Long orderId, Long[] goodsIds,Integer reasonType, String remark);
	
	/**
	 * 买家确认收货<br/>
	 * 1.买家手动确认收货，要求订单必须是已发货状态<br/>
	 * 1.确认收货后订单将变成'已完成'
	 * @param orderId 			订单ID
	 * @param buyerId 			卖家ID
	 * @return: void      
	 */
	public boolean dealConfirmAcce(Long orderId, Long buyerId);

}
