package com.kuangji.paopao.pay.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.enums.PayTypeEnum;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.pay.PayApi;
import com.kuangji.paopao.pay.WxpayConfig;
import com.kuangji.paopao.service.ConsultantScheduleStatusService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * 订单列表订单支付
 */
@RestController
@RequestMapping(value = "/v1/pay")
public class OrderPayOrderController  {

	@Autowired
	@Qualifier("defaultPayApi")
	private PayApi payApi;
	
	@Autowired
	private WxpayConfig wxpayConfig;
	
	@Autowired
	private MallTradeOrderService mallTradeOrderService;
	
	@Autowired
	private ConsultantScheduleStatusService consultantScheduleStatusService;
	/*
	 * 支付
	 */
	@PostMapping(value = { "/order" })
	public Object payOrder(HttpServletRequest request,String orderNo,Integer payType) {
		
		
		if (Strings.isBlank(orderNo)) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		if (payType==null) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer buyerId=JwtUtils.getUserId(token);
		MallTradeOrder mallTradeOrder= mallTradeOrderService.getMallTradeOrderByOrder(orderNo);
		
		if (mallTradeOrder==null) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_FIN_FAIL);

		}
		//已经是支付状态
		if (mallTradeOrder.getPayStatus()==PayStatusEnum.PAY_SUCC.code) {
			throw new ServiceException(ResultCodeEnum.ERROR_PAY_NO_TRADE_NO);
		}
		
		//验证该订单是不是已被预约
		Integer shopId=mallTradeOrder.getShopId();
		
		//预约开始时间
		String ext=mallTradeOrder.getExt();
		JSONObject jsonObject =JSONObject.parseObject(ext);
		String consultantDate=jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String consultantTime=jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		String[] strings=consultantTime.split("-");
		if (strings.length!=2) {
			throw new ServiceException(ResultCodeEnum.FAIL);
		}
		//预约结束时间
		String start=strings[0];
		String end=strings[1];
		boolean bl=consultantScheduleStatusService.getConsultantScheduleStatus(shopId, consultantDate, start, end);
		
		if (!bl) {
			throw new ServiceException(ResultCodeEnum.ERROR_WRONG_TIME_USE_SERVICE);
		}
		
		if (payType==PayTypeEnum.PAY_WX_MP.code) {
			Map<String, String> wxMap=this.wxMap(payApi.createWxPayData(orderNo));
			return ServiceResultUtils.success(wxMap);
		}
		if (payType==PayTypeEnum.PAY_ZFB_LINE.code) {
				
			return ServiceResultUtils.success(payApi.createAilPayData(orderNo));
		}
		if (payType==PayTypeEnum.PAY_BALANCR.code) {
		
			Object object=payApi.payBalancr(orderNo,buyerId);
			return ServiceResultUtils.success(object);
		}
			return 	ServiceResultUtils.failure(null);
		
	}

	
	protected Map<String, String> wxMap(Map<String, String> reqMap){
		Map<String, String> wxMap=new HashMap<>(6);
		wxMap.put("partnerId",wxpayConfig.getMchID());// 商户平台id
		wxMap.put("appId", wxpayConfig.getAppID());// 公众号id
		wxMap.put("prepayId",reqMap.get("prepay_id"));
		wxMap.put("nonceStr",reqMap.get("nonce_str"));
		wxMap.put("timeStamp",reqMap.get("timeStamp"));
		wxMap.put("package",reqMap.get("package"));
		wxMap.put("sign",reqMap.get("sign"));
		return wxMap;
	}
}
