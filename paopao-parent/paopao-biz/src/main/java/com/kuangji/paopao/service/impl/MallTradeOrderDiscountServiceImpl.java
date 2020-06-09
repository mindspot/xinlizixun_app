package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.MallTradeOrderDiscountMapper;
import com.kuangji.paopao.model.MallTradeOrderDiscount;
import com.kuangji.paopao.service.MallTradeOrderDiscountService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

/**
 * Author 金威正
 * Date  2020-03-01
 */
@Service
public class MallTradeOrderDiscountServiceImpl  extends BaseServiceImpl<MallTradeOrderDiscount, Integer> implements   MallTradeOrderDiscountService {
    @Autowired
    private MallTradeOrderDiscountMapper mallTradeOrderDiscountMapper;

	@Override
	public BaseMapper<MallTradeOrderDiscount> getMapper() {
		// TODO Auto-generated method stub
		return mallTradeOrderDiscountMapper;
	}
    
    
}
