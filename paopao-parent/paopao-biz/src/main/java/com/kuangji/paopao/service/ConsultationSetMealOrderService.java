package com.kuangji.paopao.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.model.ConsultationSetMealOrder;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.SetMealVO;

/**
 * Author 金威正
 * Date  2020-03-10
 */
public interface ConsultationSetMealOrderService extends BaseService<ConsultationSetMealOrder, Integer>{

	 /**
		 * int shopId,int goodsId,int userId 只存在一条 
		 * Author 金威正
		 * Date  2020-02-21
		 */
	     ConsultationSetMealOrder getSetMealConsultationOrder(Integer shopId,Integer goodsId,Integer userId);
	    
	    

	    
	    /**
	  	 * 用户购买的套餐
	  	 * Author 金威正
	  	 * Date  2020-02-21
	  	 */
	     List<SetMealVO> listSetMeal(String  token);
	
	    
	    
	    /**
		 * 根据id获取数据
		 * Author 金威正
		 * Date  2020-02-21
		 */
	     List<ConsultationSetMealOrder> listSetMealConsultationOrder(Integer shopId,Integer userId);
	    
	
	    /**
	  	 * 咨询师 端显示  用户购买的套餐
	  	 * Author 金威正
	  	 * Date  2020-02-21
	  	 */
	 	PageInfo<SetMealVO> listConsultantSetMeal(Integer consultantId, Integer pageNum);
	 	
	 	
	    boolean isPaySetMeal(Integer shopId,Integer userId);
	    
	    
	    boolean  insertSetMealOrder(String orderNo);
	    
	    
	    boolean  checkSetMealOrder(String orderNo);
	    
	    
	    
	    boolean updateAddResidualTimes(String  orderNo);

}
