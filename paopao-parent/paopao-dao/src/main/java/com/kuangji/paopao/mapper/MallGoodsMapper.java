package com.kuangji.paopao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.order.vo.BaseOrderFormVO.BuyGoodsFormVO;
import com.kuangji.paopao.vo.MallGoodsConsultantServiceVO;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface MallGoodsMapper extends BaseMapper<MallGoods> {
	


    /**
	 * 根据传入添加查询
	 * Author 金威正
	 * Date  2020-02-20
	 */
     MallGoods getBuyGoods(BuyGoodsFormVO buyGoodsFormVO);

	/**
	 * 查询咨询师普通商品情况
	 * Author 金威正
	 * Date  2020-02-20
	 */
     List<MallGoodsConsultantServiceVO> listConsultantService(@Param("shopId")Integer shopId);

    
     int  deleteSetInByShopId(Integer shopId);
     
     /**
 	 * 上架商品
 	 */ 
     int upDownByShopId(Integer shopId);
     
     
     /**
  	 * 下架商品
  	 */ 
     int upLoweByShopId(Integer shopId);
     
     
     //单次服务最低价格
     int minSellPriceOne(Integer shopId);

     int logicDelete(Integer shopId);

	int countMallGoods(MallGoods mallGoods);

	List<Integer> listMallGoodsByShopId(Integer shopId);

	int batchLogisticDelete(@Param("idList") List<Integer> idList);
}