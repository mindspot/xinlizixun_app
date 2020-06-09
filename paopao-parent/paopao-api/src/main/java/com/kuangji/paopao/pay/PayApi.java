package com.kuangji.paopao.pay;

import java.util.Map;

import com.kuangji.paopao.order.dto.ConsultantServiceDTO;

public interface PayApi {


	public void paySetMeal(String token,ConsultantServiceDTO consultantServiceDTO);

	//创建支付宝支付数据
	public String createAilPayData(String orderNo);
	
	//创建 微信支付数据
	public Map<String, String> createWxPayData(String orderNo);
	
	//余额支付
	public Object payBalancr(String orderNo,Integer  userId);
}
