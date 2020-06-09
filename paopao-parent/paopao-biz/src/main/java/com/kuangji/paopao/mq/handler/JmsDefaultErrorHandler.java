package com.kuangji.paopao.mq.handler;

import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Service("jmsDefaultErrorHandler")
public class JmsDefaultErrorHandler implements ErrorHandler{
	
    public void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
