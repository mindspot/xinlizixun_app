package com.kuangji.paopao.mq.listener.order;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kuangji.paopao.enums.BookingTimeEnum;
import com.kuangji.paopao.enums.DelayQueueManagerTypeEnum;
import com.kuangji.paopao.mapper.BookingTimeMapper;
import com.kuangji.paopao.mapper.ConsultationOrderMapper;
import com.kuangji.paopao.mapper.ConsultationSetMealOrderMapper;
import com.kuangji.paopao.mapper.MallTradeOrderGoodsMapper;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.BookingTime;
import com.kuangji.paopao.model.ConsultantScheduleStatus;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.mq.factory.MallOrderFactory;
import com.kuangji.paopao.mq.message.MessageBean;
import com.kuangji.paopao.redis.AsyncTask;
import com.kuangji.paopao.redis.send.SendService;
import com.kuangji.paopao.schedule.delayqueue.DelayQueueManager;
import com.kuangji.paopao.schedule.delayqueue.DelayTask;
import com.kuangji.paopao.schedule.delayqueue.TaskBase;
import com.kuangji.paopao.service.PointHistoryService;
import com.kuangji.paopao.util.DateUtils;
import com.kuangji.paopao.vo.MQTradeOrderVO;

import tk.mybatis.mapper.entity.Example;

public abstract class AbstractMessageListener {

	@Autowired
	MallOrderFactory mallOrderFactory;

	@Autowired
	MallTradeOrderMapper mallTradeOrderMapper;

	@Autowired
	ConsultationOrderMapper consultationOrderMapper;


	@Autowired
	BookingTimeMapper bookingTimeMapper;

	@Autowired
	MallTradeOrderGoodsMapper mallTradeOrderGoodsMapper;

	@Autowired
	ConsultationSetMealOrderMapper consultationSetMealOrderMapper;

	@Autowired
	StringRedisTemplate redisTemplate;

	@Autowired
	AsyncTask asyncTask;
	
	@Autowired
	SendService  sendService;
	
	@Autowired
	DelayQueueManager  delayQueueManager;
	
	@Autowired
	UserMapper  userMapper;

	@Autowired
	RedissonClient redissonClient;

	@Autowired
	PointHistoryService pointHistoryService;
	
     final Logger logger = LoggerFactory.getLogger(AbstractMessageListener.class);

	/**
	 * 解析消息参数 @Description: TODO @param @return: MessageBean @throws
	 */
	public MessageBean parseMessage(String message) {
		if (message != null) {
			try {

				if (StringUtils.isBlank(message)) {
					return null;
				}
				MessageBean msg = JSON.parseObject(message, MessageBean.class);
				return msg;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}

	public abstract void onMessage(String msg);

	protected void checkConsultantScheduleStatus(ConsultantScheduleStatus userScheduleStatus) {
		
		int status=BookingTimeEnum.BOOKING.getValue();
		BookingTime bookingTime =new BookingTime();
		bookingTime.setTimeType(status);
		Example example =new Example(BookingTime.class);
		String date=userScheduleStatus.getScheduleDate();
		String start=userScheduleStatus.getScheduleStartTime();
		String end=userScheduleStatus.getScheduleEndTime();
		example.createCriteria()
				.andEqualTo("consultantId", userScheduleStatus.getConsultantId())
				.andEqualTo("consultantWorkDate",date )
				.andEqualTo("consultantWorkStartTime", start)
				.andEqualTo("consultantWorkEndTime", end);
		
		bookingTimeMapper.updateByExampleSelective(bookingTime, example);
		Integer shopId=userScheduleStatus.getConsultantId();
		StringBuffer unkey =new StringBuffer();
		unkey.append(shopId)
			 .append("-")
			 .append(date)
			 .append("-")
			 .append(start);
		RLock lock = redissonClient.getLock(unkey.toString());
		if (lock.isLocked()) {
			lock.forceUnlock();
		}
	}

	public abstract boolean exitOrder(String orderNo);

	// 修改该时间段 没有支付的订单 改为已经取消
	protected void updateOrderCancel(MQTradeOrderVO mallTradeOrder) {
	//		String orderNo = mallTradeOrder.getOrderNo();
	//		String ext = mallTradeOrder.getExt();
	//		mallTradeOrderMapper.updateOrderCancel(orderNo, ext);
	}

	public boolean upateOrder(JSONObject msg) {
		String orderNo = (String) msg.get("orderNo");
		Integer orderType = (Integer) msg.get("orderType");
		Integer payType = (Integer) msg.get("payType");
		String tradeNo = (String) msg.get("tradeNo");
		MallTradeOrder mallTradeOrder =new MallTradeOrder();
		mallTradeOrder.setOrderNo(orderNo);
		mallTradeOrder.setOrderType(orderType);
		mallTradeOrder.setOutTradeNo(tradeNo);
		mallTradeOrder.setPayType(payType);
		return mallTradeOrderMapper.updateOrderPayStatus(mallTradeOrder)>0;
	}
	

	public void sendStartOrder(String orderNo,String date,String startTime) {
		logger.info("发送新订单消息给咨询师 ",orderNo);
		sendService.sendNewOrder(orderNo);
		//支付时间
		long payTime=new Date().getTime();
		//预约开始时间
		String time=date+" "+startTime+":00";
		long timestamp=DateUtils.getTimestamp(time);
		
		long temp=timestamp-payTime;
		
		TaskBase taskBase2 =new TaskBase(orderNo, DelayQueueManagerTypeEnum.TWENT_FOUR_HOURS.code);
		logger.info("到预定时间 未接单  的 咨询师 取消  "+orderNo+" 时间为"+temp);
		
		DelayTask twentyFourTask =new DelayTask(taskBase2,temp);
		delayQueueManager.put(twentyFourTask);
		
	}	
	
}
