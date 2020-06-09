package com.kuangji.paopao.api;

import java.util.Map;

import com.kuangji.paopao.order.dto.ConsultantServiceDTO;

public interface OrderApi {


	
	//提交咨询订单
	public Map<String, Object> dealUnifiedOrder(String token,ConsultantServiceDTO consultantServiceDTO);

	
	
	//提交咨询套餐
	public Map<String, Object> dealUnifiedSetMealOrder(String token,ConsultantServiceDTO consultantServiceDTO);

		

	int  canceOrderUnlock(Integer  userId,String orderNo);
}
