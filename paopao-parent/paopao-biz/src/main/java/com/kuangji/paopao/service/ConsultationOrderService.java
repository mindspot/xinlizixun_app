package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.model.ConsultationOrder;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.SetMealVO;

/**
 * Author 金威正
 * Date  2020-02-21
 */
public interface ConsultationOrderService extends BaseService<ConsultationOrder, Integer>{

    
    /**
	 * 用户购买的套餐
	 * Author 金威正
	 * Date  2020-02-21
	 */
    public List<SetMealVO> listSetMeal(String token);

    
 

}
