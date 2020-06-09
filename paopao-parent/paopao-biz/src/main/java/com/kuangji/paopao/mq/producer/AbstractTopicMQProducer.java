package com.kuangji.paopao.mq.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;
import com.kuangji.paopao.mq.listener.order.AbstractMessageListener;
import com.kuangji.paopao.mq.message.MessageBean;

public class AbstractTopicMQProducer {
	
	@Autowired
	@Qualifier("consultationOrderMessageListener")
	private AbstractMessageListener consultationOrderMessageListener;
	@Autowired
	@Qualifier("consultationSetMealOrderMessageListener")
	private AbstractMessageListener consultationSetMealOrderMessageListener;
	
	/**
	 * 发送异步消息
	 * @Description: TODO
	 * @param
	 * @return: void      
	 * @throws
	 */
	protected void sendTxtMessage(MessageBean message) {
		try{
			consultationOrderMessageListener.onMessage(JSONObject.toJSONString(message));
			consultationSetMealOrderMessageListener.onMessage(JSONObject.toJSONString(message));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
