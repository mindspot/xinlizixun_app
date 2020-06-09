package com.kuangji.paopao.redis;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.kuangji.paopao.enums.MallGoodsClassEnum;
import com.kuangji.paopao.enums.OrderTypeEnum;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.service.AccountService;
import com.kuangji.paopao.service.MallTradeOrderService;

@Component
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    @Value("${spring.redis.key.prefix.orderCall}")
    private String REDIS_KEY_PREFIX_ORDER_CALL;
  

    @Value("${spring.redis.key.prefix.beforeTheEndOrderCall}")
    private String REDIS_KEY_PREFIX_BEFORE_THE_END_CALL;
    
	@Value("${spring.redis.key.prefix.stopOrderCall}")
	private String REDIS_KEY_PREFIX_STOP_ORDER_CALL;
	
    @Autowired
    private  AsyncTask  asyncTask;
    
    @Autowired
	private AccountService accountService;
    
    @Autowired
	private MallTradeOrderService mallTradeOrderService;
    
	private static final Logger LOG = LoggerFactory.getLogger(KeyExpiredListener.class);

	public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}
 
	@Override
	public void onMessage(Message message, byte[] pattern) {
		//过期的key
		String key = new String(message.getBody(),StandardCharsets.UTF_8);
		LOG.info(key+" :过期");
		if (key.startsWith(REDIS_KEY_PREFIX_ORDER_CALL)) {
			
			String orderNo=key.replace(REDIS_KEY_PREFIX_ORDER_CALL, "");
			if (orderNo.startsWith("B")) {
				int result=mallTradeOrderService.closeOrder(orderNo);
				if (result>0) {
					MallTradeOrder order=mallTradeOrderService.findOne(new MallTradeOrder(orderNo));
					if (order.getOrderType()!=OrderTypeEnum.PAY_SET_MEAL.code) {
						accountService.doSubCommissionRecord(orderNo);
					}
					asyncTask.sendStopServiceMsg(orderNo,MallGoodsClassEnum.CONSULTANT_SERVICE.getValue());
					return;
				}
			}
			if (orderNo.startsWith("C")) {
				// -1 督导订单
				asyncTask.sendStopServiceMsg(orderNo,-1);
				return;
			}
		}
		
		if (key.startsWith(REDIS_KEY_PREFIX_BEFORE_THE_END_CALL)) {
			
			String orderNo=key.replace(REDIS_KEY_PREFIX_BEFORE_THE_END_CALL, "");
			asyncTask.sendBeforeStopServiceMsg(orderNo);
			return;
		}
		
		if (key.startsWith(REDIS_KEY_PREFIX_STOP_ORDER_CALL)) {
			
			String orderNo=key.replace(REDIS_KEY_PREFIX_STOP_ORDER_CALL, "");
			asyncTask.consultantSendStopServiceMsg(orderNo);
			return;
		}
		
		
		
	}
}