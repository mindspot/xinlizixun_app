package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.admin.vo.OrderDiagnosisVO.DiagnosisVO;
import com.kuangji.paopao.admin.vo.OrderInfoVO;
import com.kuangji.paopao.admin.vo.SetMealInfoVO;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.param.MallTradeOrderParam;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.ConsultationSetMealOrder;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.service.AccountService;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.service.ConsultantOrderDiagnosisService;
import com.kuangji.paopao.service.ConsultationSetMealOrderService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.MemberCaseService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正 Date 2020-02-20
 */
@RestController
public class WebOrderController {

	@Autowired
	private MallTradeOrderService mallTradeOrderService;
	
	@Autowired
	private ConsultationSetMealOrderService consultationSetMealOrderService;

	@Autowired
	private ConsultantOrderDiagnosisService consultantOrderDiagnosisService;

	@Autowired
	private MemberCaseService memberCaseService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	//后台查看订单
	@PostMapping("/web/order/orderList")
	public Object adminListOrderList(@RequestBody MallTradeOrderParam param,HttpServletRequest request) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
    	Integer userId=JwtUtils.getUserId(token);
    	if (!userService.isAdmin(userId)) {
    		param.setUserIds(userService.containSelfUserIds(userId));
		}
		List<com.kuangji.paopao.admin.vo.MallTradeOrderVO> list = mallTradeOrderService.listConsultantMallTradeOrderByGoodsClass(param);

		return ServiceResultUtils.success(new PageInfo<>(list));
	}
	
	//后台套餐订单详情
	@PostMapping("/web/order/setMealOrderInfo")
	public Object setMealInfo(Integer id) {
		
		SetMealInfoVO setMealInfoVO=mallTradeOrderService.getSetMealInfo(id);
		return ServiceResultUtils.success(setMealInfoVO);
	}
	
	//后台普通订单详情
	@PostMapping("/web/order/orderInfo")
	public Object orderInfo(Integer id) {
		OrderInfoVO orderInfoVO=mallTradeOrderService.getOrderInfo(id);
		return ServiceResultUtils.success(orderInfoVO);
	}
	//病例诊断内容
	@PostMapping("/web/order/diagnosis")
	public Object diagnosis(String orderNo) {
		List<DiagnosisVO> orderDiagnosisVO=consultantOrderDiagnosisService.getCaseDiagnosis(orderNo);
	
		return ServiceResultUtils.success(orderDiagnosisVO);
	}
	//病例诊断内容
	@PostMapping("/web/order/diagnos")
	public Object diagnos(String orderNo) {
		DiagnosisVO diagnosisVO=consultantOrderDiagnosisService.getCaseDiagnos(orderNo);
		return ServiceResultUtils.success(diagnosisVO);
	}
	
	
	//后台套餐订单详情
	@PostMapping("/web/order/setMeal-orderNo")
	public Object setMealByorderNo(String orderNo) {
		MallTradeOrder mallTradeOrder=mallTradeOrderService.findOne(new MallTradeOrder(orderNo));
		return ServiceResultUtils.success(mallTradeOrder);
	}
	//后台套餐订单详情
	@PostMapping("/web/order/setMeal-orderNo-details")
	public Object setMealByorderNoDetails(String orderNo) {
		ConsultationSetMealOrder consultationSetMealOrder=new ConsultationSetMealOrder();
		consultationSetMealOrder.setOrderNo(orderNo);
		ConsultationSetMealOrder setMealOrder=consultationSetMealOrderService.findOne(consultationSetMealOrder);
		return ServiceResultUtils.success(setMealOrder);
	}
		
	
	//后台套餐订单详情
	@PostMapping("/web/order/member-cases-orderNo")
	public Object memberCase(String orderNo) {
		MallTradeOrder mallTradeOrder=mallTradeOrderService.findOne(new MallTradeOrder(orderNo));

		MemberCase memberCase=memberCaseService.findById(mallTradeOrder.getMemberCaseId());
		
		return ServiceResultUtils.success(memberCase);
	}
	@GetMapping("/web/order/member-refund")
	public Object doRefund(String orderNo,HttpServletRequest request) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
    	if (!userService.isAdmin(userId)) {
    		throw new ServiceException(-1, "权限不足");
		}
		Boolean result=accountService.doRefund(orderNo);
		
		return ServiceResultUtils.success(result);
	}
	
	@GetMapping("/web/order/member—update-cardNumber")
	public Object doUpdateCardNumber(String orderNo,HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
    	if (!userService.isAdmin(userId)) {
    		throw new ServiceException(-1, "权限不足");
		}
		MallTradeOrder order=mallTradeOrderService.findOne(new MallTradeOrder(orderNo));
		int result=mallTradeOrderService.canceBooktimeOrder(order);
		return ServiceResultUtils.success(result>0);
	}
	
	@GetMapping("/web/order/case-class")
	public Object orderCaseClass(String orderNo) {
		MallTradeOrder mallTradeOrder=mallTradeOrderService.findOne(new MallTradeOrder(orderNo));
		MemberCase memberCase=memberCaseService.findById(mallTradeOrder.getMemberCaseId());
		String consultantType=memberCase.getConsultantType();
		JSONArray jsonArray =JSONObject.parseArray(consultantType);
		Integer[] ids=new Integer[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			ids[i]=jsonArray.getInteger(i);
		}
		List<Common> list=commonService.listCommon(ids);
		return ServiceResultUtils.success(list);
	}
}


