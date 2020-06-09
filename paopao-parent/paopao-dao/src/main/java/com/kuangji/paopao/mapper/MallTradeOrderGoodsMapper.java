package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.MallTradeOrderGoods;
import com.kuangji.paopao.order.vo.BaseOrderFormVO.BuyGoodsFormVO;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface MallTradeOrderGoodsMapper extends BaseMapper<MallTradeOrderGoods> {
	


	/**
	 * 根据id获取数据
	 * Author 金威正
	 * Date  2020-02-20
	 */
    public MallTradeOrderGoods getBuyGoods(BuyGoodsFormVO buyGoodsFormVO);
	

    
    //修改套餐次数
    public int updateSetMealBuyCount(String orderNo,int addCount);
    
    
    //根据订单编号 查询 对于的 goodsName 对应枚举类
    
    public String getGoodsByOrderNo(String orderNo);


}