package com.kuangji.paopao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.MallGoodsMapper;
import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.service.MallGoodsService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.vo.MallGoodsConsultantServiceVO;

/**
 * Author 金威正
 * Date  2020-02-20
 */
@Service
public class MallGoodsServiceImpl extends BaseServiceImpl<MallGoods, Integer> implements   MallGoodsService  {
    @Autowired
    private MallGoodsMapper mallGoodsMapper;
    
   

	@Override
	public List<MallGoodsConsultantServiceVO> listConsultantService(Integer shopId) {
	
		return mallGoodsMapper.listConsultantService(shopId);
	}


	@Override
	public BaseMapper<MallGoods> getMapper() {
		return mallGoodsMapper;
	}

	@Override
	public int doConsultantOnSale(Integer userId) {
		return mallGoodsMapper.upDownByShopId(userId);
	}

	@Override
	public int countMallGoods(MallGoods mallGoods) {
		return mallGoodsMapper.countMallGoods(mallGoods);
	}

	@Override
	public List<Integer> listMallGoodsByShopId(Integer shopId) {
		return mallGoodsMapper.listMallGoodsByShopId(shopId);
	}

	@Override
	public int batchLogicDelete(List<Integer> toRemoveList) {
		return mallGoodsMapper.batchLogisticDelete(toRemoveList);
	}
}
