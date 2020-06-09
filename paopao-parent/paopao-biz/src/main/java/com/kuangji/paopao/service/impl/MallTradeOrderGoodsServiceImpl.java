package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.MallTradeOrderGoodsMapper;
import com.kuangji.paopao.model.MallTradeOrderGoods;
import com.kuangji.paopao.service.MallTradeOrderGoodsService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

/**
 * Author 金威正
 * Date  2020-02-20
 */
@Service
public class MallTradeOrderGoodsServiceImpl extends BaseServiceImpl<MallTradeOrderGoods, Integer> implements  MallTradeOrderGoodsService {
    @Autowired
    private MallTradeOrderGoodsMapper mallTradeOrderGoodsMapper;

	@Override
	public BaseMapper<MallTradeOrderGoods> getMapper() {
		// TODO Auto-generated method stub
		return mallTradeOrderGoodsMapper;
	}

	@Override
	public String getGoodsByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return mallTradeOrderGoodsMapper.getGoodsByOrderNo(orderNo);
	}
   

	
}
