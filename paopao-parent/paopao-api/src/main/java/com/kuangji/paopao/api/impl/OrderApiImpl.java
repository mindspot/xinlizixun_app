package com.kuangji.paopao.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.api.OrderApi;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.dto.TradeOrderDTO;
import com.kuangji.paopao.enums.OrderStatusEnum;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.enums.PayTypeEnum;
import com.kuangji.paopao.mapper.ConsultationSetMealOrderMapper;
import com.kuangji.paopao.model.ConsultationSetMealOrder;
import com.kuangji.paopao.order.business.cust.ICustOrderService;
import com.kuangji.paopao.order.dto.ConsultantServiceDTO;
import com.kuangji.paopao.order.vo.BaseOrderFormVO.BuyGoodsFormVO;
import com.kuangji.paopao.pay.PayApi;
import com.kuangji.paopao.pay.WxpayConfig;
import com.kuangji.paopao.service.CouponService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.util.JwtUtils;

//各种订单购买 业务
@Service
public class OrderApiImpl implements OrderApi{

	
	@Autowired
	private MallTradeOrderService mallTradeOrderService;
	
	@Autowired
	private WxpayConfig wxpayConfig;
	
	@Autowired
	private CouponService couponService;

	@Autowired
	@Qualifier("consultingService")
	private ICustOrderService iCustOrderService;

	@Autowired
	@Qualifier("consultingSetMealService")
	private ICustOrderService consultingSetMealService;

	@Autowired
	private ConsultationSetMealOrderMapper consultationSetMealOrderMapper;
	
	
    private final Logger logger = LoggerFactory.getLogger(OrderApiImpl.class);

    private static final String CONTENT="用户取消两次订单后，当日不可继续下单。";
	
	@Autowired
	@Qualifier("defaultPayApi")
	private PayApi payApi;
	@Autowired
	private  RedissonClient redissonClient;
	
	
	private static String CANCEl_ORDER="已取消";
	
	@Override
	public Map<String, Object> dealUnifiedOrder(String token, ConsultantServiceDTO consultantServiceDTO) {
		
		Integer buyerId = JwtUtils.getUserId(token);
		if (consultantServiceDTO.getConsultantType()==null||consultantServiceDTO.getConsultantType().length<1) {
			throw new ServiceException(-1, "咨询问题不能为空");
		}
		if (consultantServiceDTO.getBuyGoods()==null
				||consultantServiceDTO.getBuyGoods().isEmpty()||consultantServiceDTO.getBuyGoods().size()!=1) {
			throw new ServiceException(-1, "购买的服务出错");
		}
		
		String date = consultantServiceDTO.getConsultantWorkDate();
		if (StringUtils.isBlank(date)) {
			throw new ServiceException(-1, "预订日期不能为空");
		}
		String time = consultantServiceDTO.getConsultantWorkTime();

		if (StringUtils.isBlank(time)) {
			throw new ServiceException(-1, "预订详细时间不能为空");
		}
		// 分割
		String[] strings = time.split("-");
		if (strings.length != 2) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		String start = strings[0];
		if (StringUtils.isBlank(start)) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		String end = strings[1];
		if (StringUtils.isBlank(end)) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);

		}
		Map<String, Object> map = new HashMap<String, Object>(16);
		// 工作方式 视频 语音 视频/语音
		List<BuyGoodsFormVO> buyGoodsFormVOs = consultantServiceDTO.getBuyGoods();
		if (!buyGoodsFormVOs.isEmpty() && buyGoodsFormVOs.size() == 1) {
			BuyGoodsFormVO buyGoodsFormVO = buyGoodsFormVOs.get(0);
			Integer shopId = buyGoodsFormVO.getShopId();
			consultantServiceDTO.setBuyerId(buyerId);
			this.checkBuyOrder(shopId, buyerId, date, time);
			TradeOrderDTO to = iCustOrderService.dealUnifiedOrder(consultantServiceDTO);
			
			map.put(CommonConstant.CONSULTATION_SERVER_ORDER, to);
			this.paymentInformation(consultantServiceDTO, map, to);
		}

		return map;
	}



	// 购买咨询套餐
	@Override
	public Map<String, Object> dealUnifiedSetMealOrder(String token, ConsultantServiceDTO consultantServiceDTO) {
		Integer buyerId = JwtUtils.getUserId(token);

		if (consultantServiceDTO.getConsultantType()==null||consultantServiceDTO.getConsultantType().length<1) {
			throw new ServiceException(-1, "咨询问题不能为空");
		}
		
		if (consultantServiceDTO.getBuyGoods()==null
				||consultantServiceDTO.getBuyGoods().isEmpty()||consultantServiceDTO.getBuyGoods().size()!=1) {
			throw new ServiceException(-1, "购买的服务错");
		}
		String date = consultantServiceDTO.getConsultantWorkDate();
		if (StringUtils.isBlank(date)) {
			throw new ServiceException(-1, "预订日期不能为空");
		}
		String time = consultantServiceDTO.getConsultantWorkTime();

		if (StringUtils.isBlank(time)) {
			throw new ServiceException(-1, "预订详细时间不能为空");
		}
		// 分割
		String[] strings = time.split("-");
		if (strings.length != 2) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		String start = strings[0];
		if (StringUtils.isBlank(start)) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		String end = strings[1];
		if (StringUtils.isBlank(end)) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);

		}
		Map<String, Object> map = new HashMap<String, Object>(16);
		// 工作方式 视频 语音 视频/语音
		List<BuyGoodsFormVO> buyGoodsFormVOs = consultantServiceDTO.getBuyGoods();
		if (!buyGoodsFormVOs.isEmpty() && buyGoodsFormVOs.size() == 1) {
			BuyGoodsFormVO buyGoodsFormVO = buyGoodsFormVOs.get(0);
			Integer shopId = buyGoodsFormVO.getShopId();
			consultantServiceDTO.setBuyerId(buyerId);
			this.checkBuyOrder(shopId, buyerId, date, time);

			List<ConsultationSetMealOrder> c=consultationSetMealOrderMapper.listSetMealConsultationOrder(shopId.intValue(),buyerId);
			if (c!=null&&!c.isEmpty()) {
				throw new ServiceException(ResultCodeEnum.ERROR_GOODS_SET_MEALS_BUY);
			}
			
			TradeOrderDTO to = consultingSetMealService.dealUnifiedOrder(consultantServiceDTO);
			
			map.put(CommonConstant.CONSULTATION_SERVER_ORDER, to);
			this.paymentInformation(consultantServiceDTO, map, to);

		}
		return map;
	}

	protected void paymentInformation(ConsultantServiceDTO dto,Map<String, Object> map,TradeOrderDTO to) {
		if (dto.getPayType()==PayTypeEnum.PAY_ZFB_LINE.code) {
			map.put(CommonConstant.PREPAYMENT, payApi.createAilPayData(to.getOrderNo()));
		}
		
		if (dto.getPayType()==PayTypeEnum.PAY_WX_MP.code) {
			Map<String, String> reqMap=payApi.createWxPayData(to.getOrderNo());
			Map<String, String> wxMap=new HashMap<>(6);
			wxMap.put("partnerId",wxpayConfig.getMchID());// 商户平台id
			wxMap.put("appId", wxpayConfig.getAppID());// 公众号id
			wxMap.put("prepayId",reqMap.get("prepay_id"));
			wxMap.put("nonceStr",reqMap.get("nonce_str"));
			wxMap.put("timeStamp",reqMap.get("timeStamp"));
			wxMap.put("package",reqMap.get("package"));
			wxMap.put("sign",reqMap.get("sign"));
			map.put(CommonConstant.PREPAYMENT,wxMap);
		}
	}
	
	public void  checkBuyOrder(Integer shopId,Integer buyerId,String date,String time){
		logger.info("shopId "+shopId+" buyerId "+buyerId+ " date"+date+" "+time);
		
		
		boolean bl=mallTradeOrderService.isBooking(shopId,date, time.split("-")[0]);
		if (!bl) {
			throw new ServiceException(ResultCodeEnum.ERROR_WORK_TIME);
		}
		mallTradeOrderService.checkPayOrderBuyUserId(buyerId);
		RLock lock = redissonClient.getLock(shopId+"-"+date+"-"+time);
		if (lock.isLocked()) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_WORK_TIME);
		}else{
			 lock.lock(30, TimeUnit.MINUTES);
		}
	}



	@Transactional(rollbackFor=Exception.class)
	@Override
	public int canceOrderUnlock(Integer userId,String orderNo) {
		StringBuffer appendStr = new StringBuffer();
		appendStr.append(CANCEl_ORDER)
		 		 .append("\n")
		 		 .append(CONTENT);
		return mallTradeOrderService.canceOrderUnlock(userId, orderNo,PayStatusEnum.PAY_WAIT.code,OrderStatusEnum.CREATE_SUCC.code,OrderStatusEnum.CANCEL_BUYER.code,appendStr.toString());
	}
	
	
	
}
