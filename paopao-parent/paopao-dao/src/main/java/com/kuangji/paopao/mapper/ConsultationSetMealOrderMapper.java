package com.kuangji.paopao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.ConsultationSetMealOrder;
import com.kuangji.paopao.vo.ConsultantOrderSetMealVO;
import com.kuangji.paopao.vo.SetMealVO;

/**
* Created by Mybatis Generator 2020/03/14
*/
 public interface ConsultationSetMealOrderMapper extends BaseMapper<ConsultationSetMealOrder> {
	
	


    
    /**
	 * 
	 * Author 金威正
	 * Date  2020-02-21
	 * 套餐只存在一条
	 */
     ConsultationSetMealOrder getSetMealConsultationOrder(@Param("shopId")Integer shopId,@Param("goodsId")Integer goodsId,@Param("userId")Integer userId);
    
    
    
    /**
	 * 
	 * Author 金威正
	 * Date  2020-02-21
	 * 套餐只存在一条
	 */
     List<ConsultationSetMealOrder> listSetMealConsultationOrder(@Param("shopId")Integer shopId,@Param("userId")Integer userId);
    
    
    
    /**
  	 * 用户购买的套餐
  	 * Author 金威正
  	 * Date  2020-02-21
  	 */
       List<SetMealVO> listSetMeal(Integer userId);

    
    /**
	 * 根据GoodsId跟咨询者id 
	 * 查出当前的用户套餐卡情况
	 * Author 金威正
	 * Date  2020-02-20
	 */
     ConsultantOrderSetMealVO getOrderSetMeal(@Param("orderNo")String orderNo,@Param("userId")Integer userId);
    
    
    
	/**
	 * 修改套餐次数 减一
	 * Author 金威正
	 * Date  2020-02-21
	 */
     Integer updateResidualTimes(String orderNo);

     
     /**
 	 * 修改套餐次数 +1
 	 * Author 金威正
 	 * Date  2020-02-21
 	 */
    Integer updateAddResidualTimes(String orderNo);
     
     
     /**
   	 * 咨询师 端显示  用户购买的套餐
   	 * Author 金威正
   	 * Date  2020-02-21
   	 */
     List<SetMealVO> listConsultantSetMeal(Integer consultantId);
     
}