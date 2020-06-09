
package com.kuangji.paopao.mq.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.vo.MQTradeOrderVO;

@Component
public class MallOrderFactory {
    @Autowired
    private MallTradeOrderMapper mallTradeOrderMapper;
    public  MQTradeOrderVO create(String  orderNo) {
    	
    	MQTradeOrderVO vo=mallTradeOrderMapper.getMallTradeOrderByOrderNo(orderNo);
    
		return vo;
        
    }
}