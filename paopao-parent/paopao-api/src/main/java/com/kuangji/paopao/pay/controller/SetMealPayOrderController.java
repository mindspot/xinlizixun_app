package com.kuangji.paopao.pay.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.order.dto.ConsultantServiceDTO;
import com.kuangji.paopao.pay.PayApi;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正 Date 2020-02-20
 */
@RestController
@RequestMapping(value = "/v1/pay")
public class SetMealPayOrderController {

	@Autowired
	@Qualifier("defaultPayApi")
	private PayApi payApi;

	/*
	 * 套餐支付 
	 */
	@PutMapping(value = { "/setMeal" })
	public Object update(
			HttpServletRequest request,
			@RequestBody ConsultantServiceDTO  consultantServiceDTO
			) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		this.payApi.paySetMeal(token, consultantServiceDTO);
		
		return ServiceResultUtils.success(null);
	}

	
}
