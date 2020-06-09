package com.kuangji.paopao.pay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.kuangji.paopao.enums.OrderStatusEnum;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.enums.PayTypeEnum;
import com.kuangji.paopao.enums.PointTypeEnum;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.mq.producer.order.OrderProducer;
import com.kuangji.paopao.pay.PayApi;
import com.kuangji.paopao.pay.WxpayConfig;
import com.kuangji.paopao.schedule.AsyncTaskJob;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.PointHistoryService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.ServiceResultUtils;

@Controller
public class WxpayController {

	@Autowired
	@Qualifier("defaultPayApi")
	private PayApi payApi;

	@Autowired
	private MallTradeOrderService mallTradeOrderService;
	@Autowired
	private WxpayConfig wxpayConfig;

	@Autowired
	@Qualifier("orderProducer")
	OrderProducer orderProducer;
	
	@Autowired
	DataSourceTransactionManager dataSourceTransactionManager;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PointHistoryService pointHistoryService;
	
	
	@Autowired
	TransactionDefinition transactionDefinition;
	@Autowired
	private AsyncTaskJob asyncTaskJob;
	private static final String FAIL="fail";
	
	@RequestMapping("/wxPay")
	@ResponseBody
	public Object wxPay(String orderNo) throws Exception {
				
		return ServiceResultUtils.success(payApi.createWxPayData(orderNo));
		
	}
	
	
	@RequestMapping("/wxpay_callback")
	@ResponseBody
	public Object callback(HttpServletRequest request) throws Exception {
		// 解析微信支付返回的信息
		String res = getNotifyXml(request);
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(res)) {
			return FAIL;
		}
		Map<String, String> resMap = WXPayUtil.xmlToMap(res);
		// 验证签名
		if (!this.checkSign(resMap)) {
			return FAIL;

		}
		
		MallTradeOrder mallTradeOrder = check(resMap);
		if (mallTradeOrder.getPayStatus() == PayStatusEnum.PAY_SUCC.getCode()) {
			return FAIL;
		}

		if ("SUCCESS".equalsIgnoreCase(resMap.get("return_code"))) {
		
			mallTradeOrder.setPayStatus(PayStatusEnum.PAY_SUCC.getCode());
			mallTradeOrder.setPayType(PayTypeEnum.PAY_WX_MP.code);
			mallTradeOrder.setPayTime(new Date());
			mallTradeOrder.setOrderStatus(OrderStatusEnum.PAY_SUCC.code);
			String outTradeNo = resMap.get("transaction_id");
			mallTradeOrder.setOutTradeNo(outTradeNo);
			this.send(mallTradeOrder);
			asyncTaskJob.genSubCommissionRecord(mallTradeOrder);
			User user=userService.findById(mallTradeOrder.getBuyerId());
			pointHistoryService.pointHistory(mallTradeOrder.getOrderNo(), PointTypeEnum.CONSULTATION_WX_PAYMENT.getType(),user.getAccount());
			return "<xml><return_code><![CDATA[" + resMap.get("return_code") + "]]></return_code><return_msg><![CDATA["
			+ resMap.get("return_msg") + "]]></return_msg></xml>";
		}
		return FAIL;

	}

	/**
	 * 获取xml内容 @Description: TODO @param @return: String @throws
	 */
	private String getNotifyXml(HttpServletRequest req) {
	
		BufferedReader reader = null;
		StringBuffer sbuf = new StringBuffer("");
		try {

			String line = "";
			reader = req.getReader();
			while ((line = reader.readLine()) != null) {
				sbuf.append(line);
			}
			return sbuf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sbuf.toString();
	}

	/**
	 * 支付回调签名验证
	 * 
	 * @param params
	 *            支付回调参数
	 * @return 签名一致返回true否则返回false
	 */
	public boolean checkSign(Map<String, String> params) {
		try {
			String wxSign=params.get("sign");
			params.remove("sign");
			String sign = WXPayUtil.generateSignature(params, wxpayConfig.getKey(), WXPayConstants.SignType.MD5);
			return wxSign.equals(sign);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			return false;
		}
	
	}

	protected MallTradeOrder check(Map<String, String> params) throws AlipayApiException {
		String orderNo = params.get("out_trade_no");
		MallTradeOrder mallTradeOrder = mallTradeOrderService.getMallTradeOrderByOrder(orderNo);
	
		if (mallTradeOrder == null) {
			throw new AlipayApiException("out_trade_no错误");
		}
		
		long total_amount = new BigDecimal(params.get("total_fee")).longValue();
	
		if (total_amount != mallTradeOrder.getOrderAmount().longValue()) {
			throw new AlipayApiException("error total_amount");
		}
		return mallTradeOrder;
	}
	
	protected void send( MallTradeOrder mallTradeOrder){
	  	   Map<String,Object> msg =new LinkedHashMap<String, Object>();
	  	   msg.put("orderNo", mallTradeOrder.getOrderNo());
	  	   msg.put("orderType", mallTradeOrder.getOrderType());
	  	   msg.put("tradeNo", mallTradeOrder.getOutTradeNo());
	  	   msg.put("payType", mallTradeOrder.getPayType());
		   orderProducer.sendMessage(msg);
		}
}
