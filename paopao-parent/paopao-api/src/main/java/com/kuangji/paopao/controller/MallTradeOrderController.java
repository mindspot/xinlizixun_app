package com.kuangji.paopao.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.api.ConsultationApi;
import com.kuangji.paopao.api.MallTradeOrderApi;
import com.kuangji.paopao.api.OrderApi;
import com.kuangji.paopao.order.dto.ConsultantServiceDTO;
import com.kuangji.paopao.service.ConsultationOrderService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.MallTradeOrderVO;
import com.kuangji.paopao.vo.SetMealVO;

/**
 * Author 金威正 Date 2020-02-20
 */
@RestController
public class MallTradeOrderController {

	@Autowired
	private ConsultationApi consultationApi;

	@Autowired
	private MallTradeOrderApi mallTradeOrderApi;

	@Autowired
	private ConsultationOrderService consultationOrderService;

	@Autowired
	private OrderApi orderApi;
	/*
	 * 
	 * 下咨询单
	 * 
	 */
	@PostMapping(value = { "/v1/order/consultantOrder" })
	public Object insert(@RequestBody(required = true) ConsultantServiceDTO consultantServiceDTO,
			HttpServletRequest request) {

		String token = request.getHeader("authorization").replace("Bearer ", "");
		Map<String, Object> map = orderApi.dealUnifiedOrder(token, consultantServiceDTO);

		return ServiceResultUtils.success(map);
	}

	
	/*
	 * 
	 * 客户取消订单
	 * 
	 */
	@PostMapping(value = { "/v1/order/canceOrder" })
	public Object canceOrder(HttpServletRequest request,String orderNo) {

		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
		orderApi.canceOrderUnlock(userId, orderNo);
		return ServiceResultUtils.success(null);
	}
	
	
	/*
	 * 下套餐单
	 */
	@PostMapping(value = { "/v1/order/consultantSetMealOrder" })
	public Object insertSetMeal(@RequestBody(required = true) ConsultantServiceDTO consultantServiceDTO,
			HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Map<String, Object> map = orderApi.dealUnifiedSetMealOrder(token, consultantServiceDTO);

		return ServiceResultUtils.success(map);
	}

	// 填写下资料时候 是不是接受过心理咨询 已经 标签
	@GetMapping("/v1/order/materials")
	public Object materials(HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");

		Map<String, Object> consultations = consultationApi.getConsultationMaterials(token);
		return ServiceResultUtils.success(consultations);
	}
	
	// 查看购买的订单
	@GetMapping("/v1/order/list/{page}")
	public Object listConsultantMallTradeOrder(HttpServletRequest request, @PathVariable("page") int pageNum) {

		String token = request.getHeader("authorization").replace("Bearer ", "");

		PageInfo<MallTradeOrderVO> lVos = mallTradeOrderApi.pageListConsultantMallTradeOrder(token, pageNum);
		return ServiceResultUtils.success(lVos);
	}

	// 查看购买的套餐
	@GetMapping("/v1/order/setMeal/list")
	public Object listConsultantSetMeal(HttpServletRequest request) {

		String token = request.getHeader("authorization").replace("Bearer ", "");

		List<SetMealVO> list = consultationOrderService.listSetMeal(token);

		return ServiceResultUtils.success(list);
	}

	// 填写下资料时候 支付方式类型 详细说明
	@GetMapping("/v1/order/payTypeDetailedDescription")
	public Object getPayTypeDetailedDescription(HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Map<String, Object> map = consultationApi.getPayTypeDetailedDescription(token);

		return ServiceResultUtils.success(map);
	}

}
