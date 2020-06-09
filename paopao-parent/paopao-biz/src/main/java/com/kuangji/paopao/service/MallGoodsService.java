package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.MallGoodsConsultantServiceVO;

/**
 * Author 金威正
 * Date  2020-02-20
 */
public interface MallGoodsService extends BaseService<MallGoods, Integer>{

   
    /**
	 * 根据传入添加查询
	 * Author 金威正
	 * Date  2020-02-20
	 */
    List<MallGoodsConsultantServiceVO> listConsultantService(Integer shopId);

	// 上架逻辑
	int doConsultantOnSale(Integer userId);

	int countMallGoods(MallGoods mallGoods);

	List<Integer> listMallGoodsByShopId(Integer shopId);

	int batchLogicDelete(List<Integer> toRemoveList);

}
