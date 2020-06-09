package com.kuangji.paopao.service;

import com.kuangji.paopao.model.MallTradeOrderGoods;
import com.kuangji.paopao.service.base.BaseService;

/**
 * Author 金威正
 * Date  2020-02-20
 */
public interface MallTradeOrderGoodsService extends BaseService<MallTradeOrderGoods, Integer> {

    //根据订单编号 查询 对于的 goodsName 对应枚举类
    
    public String getGoodsByOrderNo(String orderNo);

}
