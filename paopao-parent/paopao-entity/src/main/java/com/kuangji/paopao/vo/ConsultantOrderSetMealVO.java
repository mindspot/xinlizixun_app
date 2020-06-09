package com.kuangji.paopao.vo;

import lombok.Data;
/**
 * 查出对应订单的 咨询套餐情况
 * Author 金威正
 * Date  2020-02-20
 */
@Data
public class ConsultantOrderSetMealVO {
    
    private Integer id;
 
   
    private Integer  residualTimes;
    
    
    private Integer  shopId;
    
    
    private Integer goodsClass;
    
    
    private String orderNo ;
    
    private Integer  goodsId;
}