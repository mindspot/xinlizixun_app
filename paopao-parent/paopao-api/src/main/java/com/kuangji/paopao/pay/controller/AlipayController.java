package com.kuangji.paopao.pay.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.enums.PayTypeEnum;
import com.kuangji.paopao.enums.PointTypeEnum;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.mq.producer.order.OrderProducer;
import com.kuangji.paopao.pay.PayApi;
import com.kuangji.paopao.schedule.AsyncTaskJob;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.PointHistoryService;
import com.kuangji.paopao.service.UserService;

@Controller
public class AlipayController {

	@Autowired
	@Qualifier("defaultPayApi")
	private PayApi payApi;

	@Value("${alipay.sign-type}")
	private   String SIGN_TYPE;
	
	@Value("${alipay.alipay-public-key}")
	private   String  PRIVATE_KEY;
	
	@Autowired
	private MallTradeOrderService mallTradeOrderService;
	
	@Autowired
	private PointHistoryService pointHistoryService;
	
	@Autowired
	@Qualifier("orderProducer")
	OrderProducer orderProducer;

	@Autowired
	private AsyncTaskJob asyncTaskJob;
	
	@Autowired
	UserService userService;	
	private static final String SUCCESS="success";

	
	private static final String FAIL="fail";
	/**
	 * <pre>
	 * 第一步:验证签名,签名通过后进行第二步
	 * 第二步:按一下步骤进行验证
	 * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	 * 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
	 * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
	 * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
	 * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
	 * </pre>
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/alipay_callback")
	@ResponseBody
	public String callback(HttpServletRequest request ) {

		Map<String, String> map=this.valuseOfMap(request);
		
		map.remove("sign_type");
		try {
          
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(map,PRIVATE_KEY ,"utf-8",SIGN_TYPE);
            //如果验证上面的boolean为true的话，我们就应该更改下订单的状态，减少下库存这些操作
            if(!alipayRSACheckedV2){
                return FAIL ;
            }
         
           MallTradeOrder mallTradeOrder=this.check(map);
           if (mallTradeOrder.getPayStatus()==PayStatusEnum.PAY_SUCC.getCode()) {
        	   return SUCCESS;
           }
           String tradeStatus = map.get("trade_status"); 
           if (tradeStatus.equals("TRADE_SUCCESS")||tradeStatus.equals("TRADE_FINISHED")) {
        	   mallTradeOrder.setPayStatus(PayStatusEnum.PAY_SUCC.getCode());
        	   mallTradeOrder.setPayType(PayTypeEnum.PAY_ZFB_LINE.code);
        	   //支付宝流水账号
        	   String tradeNo = map.get("trade_no"); 
        	   mallTradeOrder.setOutTradeNo(tradeNo);
        	   this.send(mallTradeOrder);
			   asyncTaskJob.genSubCommissionRecord(mallTradeOrder);
			   User user=userService.findById(mallTradeOrder.getBuyerId());
			   pointHistoryService.pointHistory(mallTradeOrder.getOrderNo(), PointTypeEnum.CONSULTATION_ZFB_PAYMENT.getType(),user.getAccount());
        	   return SUCCESS;
           }
           
		  } catch (AlipayApiException e) {
				return FAIL;
        }
		return FAIL;
	

	}

	protected  Map<String, String> valuseOfMap(HttpServletRequest request) {
		Map<String,String> params = new HashMap<String,String>();

        //支付宝的回调都是把信息放到request里面
        Map<String, String[]> requestParams = request.getParameterMap();
        //keyset()是获取所有的key值，iterator()是迭代遍历
        for(Iterator<String> iter = requestParams.keySet().iterator();iter.hasNext();){
            String name = (String)iter.next();
            //这里把key放到数组里面
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            //这个for循环的尊用就是把上面那个String中的值都遍历一遍
            for(int i = 0 ; i <values.length;i++){
                //这个是三元运算符
                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            //把数据全部加进map集合中   name就是key  valueStr就是value
            params.put(name,valueStr);
        }
		return params;
	}
	
	
	protected MallTradeOrder  check(Map<String, String> params) throws AlipayApiException {      
        String orderNo = params.get("out_trade_no");

        MallTradeOrder mallTradeOrder= mallTradeOrderService.getMallTradeOrderByOrder(orderNo);
        if (mallTradeOrder==null) {
        	throw new AlipayApiException("out_trade_no错误");
        }
            
        long total_amount = new BigDecimal(params.get("total_amount")).multiply(new BigDecimal(100)).longValue();       
        
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
  	   msg.put("payType", PayTypeEnum.PAY_ZFB_LINE.code);
	   orderProducer.sendMessage(msg);
	}
}
