package com.kuangji.paopao.order.business.cust.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.enums.DelayQueueManagerTypeEnum;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.order.business.cust.AbstractCustOrderService;
import com.kuangji.paopao.order.business.cust.consulting.ConsultingOrderPayServiceImpl;
import com.kuangji.paopao.order.vo.BaseOrderFormVO;
import com.kuangji.paopao.redis.AsyncTask;
import com.kuangji.paopao.schedule.delayqueue.DelayQueueManager;
import com.kuangji.paopao.schedule.delayqueue.DelayTask;
import com.kuangji.paopao.schedule.delayqueue.TaskBase;
import com.kuangji.paopao.service.BookingTimeService;
import com.kuangji.paopao.service.MallTradeOrderService;


/**
 * 订单服务接口
 * @author Administrator
 */
@Service("defaultCustOrderService")
public class DefaultCustOrderServiceImpl extends AbstractCustOrderService {

	@Autowired
	public MallTradeOrderService mallTradeOrderService;
	
	@Autowired
	public BookingTimeService bookingTimeService;
	
	
	@Autowired
	public DelayQueueManager delayQueueManager;
	
	//半小时消息
	public static final int  expire=30*60*1000;
	
	
	@Autowired
	public AsyncTask asyncTask;
	
    public final Logger logger = LoggerFactory.getLogger(ConsultingOrderPayServiceImpl.class);
    
	public static String TO_BE_PAID="待支付";
	
	@Override
	protected void onCancelSuccEvent(MallTradeOrder order) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	protected void onConfirmAcceSuccEvent(MallTradeOrder order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getOrderType() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	protected void onUnifiedOrderSuccEvent(BaseOrderFormVO fvo, MallTradeOrder order) {
		String orderNo=order.getOrderNo();
		String unKey=mallTradeOrderService.getBookingUnKey(orderNo);
		bookingTimeService.updateBookingStatusbuyUnKey(unKey);
		

		//发送消息给 咨询师
		asyncTask.sendUpdateOrderMsg(orderNo,TO_BE_PAID);
		
		logger.info("订单下单  延时半小时取消订单任务开始  ");
		TaskBase taskBase=new TaskBase(orderNo, DelayQueueManagerTypeEnum.TEMP_UPDATE_ORDER.code);
		DelayTask delayTask =new DelayTask(taskBase,expire);
		delayQueueManager.put(delayTask);
	}

	

	
}
