package com.kuangji.paopao.pay.def;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.enums.OrderStatusEnum;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.enums.PayTypeEnum;
import com.kuangji.paopao.enums.PointTypeEnum;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.mq.producer.order.OrderProducer;
import com.kuangji.paopao.pay.AbstractPayApi;
import com.kuangji.paopao.pay.WxpayConfig;
import com.kuangji.paopao.schedule.AsyncTaskJob;
import com.kuangji.paopao.service.PointHistoryService;

//默认系统 套餐支付
@Service("defaultPayApi")
public class DefaultPayApi extends AbstractPayApi {

	@Autowired
	private AlipayClient alipayClient;

	@Autowired
	private WxpayConfig wxpayConfig;
	@Value("${alipay.notify-url}")
	private  String aliNotifyUrl;
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	@Qualifier("orderProducer")
	OrderProducer orderProducer;
	
	@Autowired
	private AsyncTaskJob asyncTaskJob;

	@Autowired
	private PointHistoryService pointHistoryService;
	
	private static final String SERVER_NAME="咨询服务";
	
	@Override
	public String createAilPayData(String orderNo) {
		AlipayTradeAppPayResponse response = null;

		// 查询订单信息
		MallTradeOrder order = mallTradeOrderService.findOne(new MallTradeOrder(orderNo));
		this.checkedPayOrder(order);
		JSONObject data = new JSONObject();
		data.put("out_trade_no", orderNo); // 商户订单号
		data.put("product_code", "KUANG_JI_PAY");
		Integer orderAmout = order.getOrderAmount();
		
		if (orderAmout == null || orderAmout == 0) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_EXIST);
		}
		BigDecimal bigDecimal = new BigDecimal(order.getOrderAmount());

		int scale = 2;// 保留2位小数
		BigDecimal bigDecimalDivide = bigDecimal.divide(new BigDecimal(100), scale, BigDecimal.ROUND_HALF_UP);
		double totalAmount = bigDecimalDivide.doubleValue();
		data.put("total_amount", totalAmount); // 小数位数（末尾的0会显示）、舍位模式
		data.put("subject", SERVER_NAME); // 订单标题
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setNotifyUrl(aliNotifyUrl); // 异步通知地址
		request.setBizContent(data.toJSONString());
		try {
			response = alipayClient.sdkExecute(request);
			if (response.isSuccess()) {
				return response.getBody();
			}
			throw new ServiceException(ResultCodeEnum.FAIL);
		} catch (AlipayApiException e) {
			throw new ServiceException(ResultCodeEnum.FAIL);
		}

	}

	
	
	
	@Override
	public Map<String, String> createWxPayData(String orderNo) {
	
		// 查询订单信息
		MallTradeOrder order = mallTradeOrderService.findOne(new MallTradeOrder(orderNo));
		this.checkedPayOrder(order);
		WXPay wxPay = new WXPay(wxpayConfig);
		// 加密，这里只列举必填字段

		String sign = null;
		try {
			SortedMap<String, String> map=this.sortedMap();
			map.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
			map.put("out_trade_no", order.getOrderNo());// 商品订单号
			map.put("total_fee", String.valueOf(order.getOrderAmount()));// 真实金额
			map.put("body", SERVER_NAME);
			sign = WXPayUtil.generateSignature(map, wxpayConfig.getKey(), WXPayConstants.SignType.MD5);
			map.put("sign", sign);
			Map<String, String> resp = wxPay.unifiedOrder(map);		
	
			String code = resp.get("return_code");
			if (StringUtils.isNotBlank(code) && code.equals("SUCCESS")) {
					//第二次加密  key 全部小写
					SortedMap<String, String> sortedMap = new TreeMap<>();
					sortedMap.put("appid", wxpayConfig.getAppID());
					sortedMap.put("partnerid", wxpayConfig.getMchID());// 商户平台id
					sortedMap.put("prepayid", resp.get("prepay_id"));
					sortedMap.put("noncestr", resp.get("nonce_str"));
					String timeStamp=String.valueOf(new Date().getTime()).substring(0, 10);
					String wxPackage="Sign=WXPay";
					sortedMap.put("timestamp",timeStamp);
					sortedMap.put("package",wxPackage);
				
					sign=WXPayUtil.generateSignature(sortedMap,wxpayConfig.getKey(),WXPayConstants.SignType.MD5);
					resp.put("timeStamp", timeStamp);
					resp.put("package", wxPackage);
					resp.put("sign",sign);
					return resp;
			}

		} catch (Exception e) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_FAIL);
		}

		throw new ServiceException(ResultCodeEnum.ERROR_ORDER_FAIL);
	}

	protected SortedMap<String, String> sortedMap() {
		SortedMap<String, String> map = new TreeMap<>();
		map.put("mch_id", wxpayConfig.getMchID());// 商户平台id
		map.put("appid", wxpayConfig.getAppID());// 公众号id
		map.put("trade_type", wxpayConfig.getTradeType());
		map.put("notify_url", wxpayConfig.getWxNotifyUrl());// 异步回调api
		map.put("spbill_create_ip", wxpayConfig.getSpbillCreateIp());// 支付ip
		
		return map;
	}


	@Transactional(rollbackFor=Exception.class)
	@Override
	public Object payBalancr(String orderNo, Integer userId) {
		// 查询订单信息
		MallTradeOrder order = mallTradeOrderService.findOne(new MallTradeOrder(orderNo));
		this.checkedPayOrder(order);
		
		Integer orderAccount=order.getOrderAmount();
		User user=userMapper.getUserforUpdate(userId);
	    Integer account=user.getAccount();
	    if (orderAccount<=0||account<=0||account<orderAccount) {
	    	throw new ServiceException(ResultCodeEnum.ERROR_PAY_FAIL);
		}
	    order.setPayStatus(PayStatusEnum.PAY_SUCC.getCode());
	    order.setPayType(PayTypeEnum.PAY_BALANCR.code);
	    order.setPayTime(new Date());
	    order.setOrderStatus(OrderStatusEnum.PAY_SUCC.code);
	    order.setOutTradeNo(UUID.randomUUID().toString().replace("-", ""));
		mallTradeOrderService.updateByPrimaryKeySelective(order);
	    if (userMapper.updateAccount(userId, -orderAccount)<1) {
	    	throw new ServiceException(ResultCodeEnum.ERROR_PAY_FAIL);
		}
	    pointHistoryService.pointHistory(orderNo, PointTypeEnum.CONSULTATION_PAYMENT.getType(),user.getAccount());
	    //余额支回调
	    Map<String,Object> msg =new LinkedHashMap<String, Object>();
 	   	msg.put("orderNo", order.getOrderNo());
 	   	msg.put("orderType", order.getOrderType());
 	 	msg.put("payType",PayTypeEnum.PAY_BALANCR.code);
 	 	msg.put("tradeNo", order.getOutTradeNo());
		orderProducer.sendMessage(msg);
		asyncTaskJob.genSubCommissionRecord(order);
		return null;
	}

	protected  void checkedPayOrder(MallTradeOrder mallTradeOrder ){
		
		if (mallTradeOrder == null || mallTradeOrder.getPayStatus() != PayStatusEnum.PAY_WAIT.code|| mallTradeOrder.getOrderStatus() != OrderStatusEnum.CREATE_SUCC.code) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_CONFIRM);
		}
	}

}
