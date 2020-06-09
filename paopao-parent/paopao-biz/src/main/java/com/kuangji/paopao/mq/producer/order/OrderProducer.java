package com.kuangji.paopao.mq.producer.order;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.kuangji.paopao.mq.message.MessageBean;
import com.kuangji.paopao.mq.producer.AbstractTopicMQProducer;

/**
 * MQ：支付成功后的消息通知
 * @Description: TODO(这里用一句话描述这个类的作用) 
 *
 */
@Service
public class OrderProducer extends AbstractTopicMQProducer{
	
	/**
	 * 支付成功消息
	 * @param orderNO			订单编号
	 * @return					
	 */
	public void sendMessage(Map<String, Object> map){
		MessageBean mb = new MessageBean();
		mb.setEvent(MessageBean.MQEvent.PAY_SUCC.code);
		mb.addData("order", map);
		this.sendTxtMessage(mb);
	}
}
