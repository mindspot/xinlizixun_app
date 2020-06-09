package com.kuangji.paopao.order.business.cust;

import org.springframework.beans.BeanUtils;

import com.kuangji.paopao.dto.TradeOrderDTO;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.order.business.AbstractUnifiedOrderService;
import com.kuangji.paopao.order.vo.BaseOrderFormVO;


/**
 * 买家订单服务接口抽象类
 * @ClassName:   AbstractBuyerOrderService 
 * @Description: TODO(定义基础的Order订单服务接口) 
 * @author:      mail.chenc@foxmail.com
 * @date         2019年6月21日 下午1:32:52 
 */
public abstract class AbstractCustOrderService extends AbstractUnifiedOrderService implements ICustOrderService{
	

	@Override
	public TradeOrderDTO dealUnifiedOrder(BaseOrderFormVO fvo) {
		// TODO Auto-generated method stub
		MallTradeOrder  mallTradeOrder = this.dealCreateOrder(fvo);
		TradeOrderDTO tod = new TradeOrderDTO();
		BeanUtils.copyProperties(mallTradeOrder, tod);
		return tod;
	}

	@Override
	public TradeOrderDTO dealPay(Long memberId, String orderNo) {
		
		return null;
	}
	
	@Override
	public boolean dealPaySuccRes(Long orderId, Integer orderType, String transactionId) {
	
		
		return false;
	}

	@Override
	public boolean dealCancel(Long orderId, Long buyerId) {
		// TODO Auto-generated method stub
		
		this.onCancelSuccEvent(null);
		return true;
	}



	@Override
	public boolean dealApplyRefund(Long orderId, Long[] goodsIds,Integer reasonType, String remark){
		
		return true;
	}

	@Override
	public boolean dealConfirmAcce(Long orderId, Long buyerId) {
		
		
		this.onConfirmAcceSuccEvent(null);
		return true;
	}
	
	/**
	 * 取消成功事件：业务订单可以通过实现该方法完善自身业务的逻辑
	 * @param order			订单信息
	 */
	protected abstract void onCancelSuccEvent(final MallTradeOrder order);
	
	/**
	 * 确认收货事件：业务订单可以通过实现该方法完善自身业务的逻辑
	 * @param order			订单信息
	 */
	protected abstract void onConfirmAcceSuccEvent(final MallTradeOrder order);
}
