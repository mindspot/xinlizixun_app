package com.kuangji.paopao.order.business.cust.consulting;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.DelayQueueManagerTypeEnum;
import com.kuangji.paopao.enums.OrderStatusEnum;
import com.kuangji.paopao.enums.OrderTypeEnum;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.mapper.ConsultationSetMealOrderMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.ConsultationSetMealOrder;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.MallTradeOrderGoods;
import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.mq.factory.MallOrderFactory;
import com.kuangji.paopao.mq.listener.order.AbstractMessageListener;
import com.kuangji.paopao.order.business.cust.def.DefaultCustOrderServiceImpl;
import com.kuangji.paopao.order.dto.ConsultantServiceDTO;
import com.kuangji.paopao.order.vo.BaseOrderFormVO;
import com.kuangji.paopao.order.vo.BaseOrderFormVO.BuyGoodsFormVO;
import com.kuangji.paopao.order.vo.OrderAmountVO;
import com.kuangji.paopao.schedule.delayqueue.DelayQueueManager;
import com.kuangji.paopao.schedule.delayqueue.DelayTask;
import com.kuangji.paopao.schedule.delayqueue.TaskBase;
import com.kuangji.paopao.service.BookingTimeService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.util.DateUtils;
import com.kuangji.paopao.vo.MQTradeOrderVO;

//套餐支付下单
@Service("consultingOrderPayService")
public class ConsultingOrderPayServiceImpl extends DefaultCustOrderServiceImpl {

	@Autowired
	MallTradeOrderService mallTradeOrderService;

	@Autowired
	BookingTimeService bookingTimeService;

	@Autowired
	ConsultationSetMealOrderMapper consultationSetMealOrderMapper;

	@Autowired
	protected DataSourceTransactionManager dataSourceTransactionManager;
	@Autowired
	protected TransactionDefinition transactionDefinition;

	@Autowired
	MallOrderFactory mallOrderFactory;

	
	@Autowired
	DelayQueueManager  delayQueueManager;
	
	@Autowired
	UserMapper  userMapper;

	@Autowired
	RedissonClient redissonClient;
	
     final Logger logger = LoggerFactory.getLogger(AbstractMessageListener.class);
	
	@Override
	protected void onCancelSuccEvent(MallTradeOrder order) {

	}

	@Override
	protected void onConfirmAcceSuccEvent(MallTradeOrder order) {

	}

	@Override
	protected void onUnifiedOrderSuccEvent(BaseOrderFormVO fvo, MallTradeOrder order) {
		String orderNo = order.getOrderNo();
		String unKey = mallTradeOrderService.getBookingUnKey(orderNo);
		bookingTimeService.updateBookingStatusbuyUnKey(unKey);
		
		if (!checkSetMeal(fvo, order)) {
			throw new ServiceException(ResultCodeEnum.ERROR_PAY_NO_SET_MEAL);
		}

	
		
		
		
	}

	protected void rollbackOrder(String orderNo){
		MallTradeOrder mallTradeOrder=mallTradeOrderService.findOne(new MallTradeOrder(orderNo));
		//支付时间
		long payTime=new Date().getTime();
		
		String ext = mallTradeOrder.getExt();
		JSONObject jsonObject = JSONObject.parseObject(ext);
		String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		StringBuffer stringBuffer = new StringBuffer(date);
		stringBuffer.append(" ").append(time);
		
		//预约开始时间
		String start=date+" "+time.split("-")[0]+":00";
		long timestamp=DateUtils.getTimestamp(start);
		
		
		long temp=timestamp-payTime;
		logger.info("套餐支付订单直接 支付 到预约时间伪接单  "+temp);
		TaskBase taskBase2 =new TaskBase(orderNo, DelayQueueManagerTypeEnum.SET_MEAL_TWENT_FOUR_HOURS.code);
	
		//确认到预约 改1分钟
		DelayTask twentyFourTask =new DelayTask(taskBase2,temp);
		delayQueueManager.put(twentyFourTask);
	}
	
	
	protected boolean checkSetMeal(BaseOrderFormVO fvo, MallTradeOrder order) {
		List<ConsultationSetMealOrder> list = consultationSetMealOrderMapper
				.listSetMealConsultationOrder(order.getShopId(), order.getBuyerId());
		logger.info("当前用户的套餐卡情况  " + list.size());
		for (ConsultationSetMealOrder setMealOrder : list) {
			int result = consultationSetMealOrderMapper.updateResidualTimes(setMealOrder.getOrderNo());
			if (result >= 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected int getOrderType() {

		return OrderTypeEnum.PAY_SET_MEAL.code;
	}

	public MallTradeOrder dealCreateOrder(BaseOrderFormVO fvo) {
		// 校验下单
		fvo.setOrderType(this.getOrderType());

		// 内存中创建订单明细/订单中的商品
		List<MallTradeOrderGoods> orderGoodsList = this.genOrderGoods(fvo);

		// 内存中创建订单主体信息
		MallTradeOrder mallTradeOrder = this.genOrder(fvo);
		// 并且关联关系
		this.generateOrderGoodsList(fvo, orderGoodsList, mallTradeOrder);

		// 计算订单金额等信息
		OrderAmountVO amount = this.calcOrderAmount(mallTradeOrder, orderGoodsList);
		TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
		try {
			if (amount.getDiscountItems() != null && !amount.getDiscountItems().isEmpty()) {
				this.mallTradeOrderDiscountMapper.insertBatch(amount.getDiscountItems());
			}

			// 订单明细入库
			this.mallTradeOrderGoodsMapper.insertBatch(orderGoodsList);
			// 订单入库
			mallTradeOrder.setOrderDetail(
					orderGoodsList.stream().map(p -> p.getGoodsName()).collect(Collectors.joining(",")));
			
			mallTradeOrder.setSellerRemarks(orderGoodsList.stream().map(p -> p.getGoodsAttrs()).collect(Collectors.joining(",")));

			ConsultantServiceDTO dto = (ConsultantServiceDTO) fvo;
			MemberCase userCase = new MemberCase();
			BeanUtils.copyProperties(dto, userCase);
			int userId = dto.getBuyerId().intValue();
			userCase.setUserId(userId);
			userCase.setConsultantType(JSONArray.toJSONString(dto.getConsultantType()));

			memberCaseMapper.insertSelective(userCase);
			mallTradeOrder.setMemberCaseId(userCase.getId());
			mallTradeOrder.setPayTime(new Date());
			mallTradeOrder.setOrderType(OrderTypeEnum.PAY_SET_MEAL.code);
			mallTradeOrder.setOrderStatus(OrderStatusEnum.PAY_SUCC.code);
			mallTradeOrder.setPayStatus(PayStatusEnum.PAY_SUCC.code);
			if (this.mallTradeOrderMapper.insertSelective(mallTradeOrder) != 1) {

				throw new ServiceException(ResultCodeEnum.ERROR_ORDER_FAIL);
			}
			this.onUnifiedOrderSuccEvent(fvo, mallTradeOrder);
		} catch (Exception e) {
			dataSourceTransactionManager.rollback(transactionStatus);
			if (e instanceof ServiceException) {
				ServiceException serviceException = (ServiceException) e;
				throw serviceException;
			}
			throw e;
		}
		dataSourceTransactionManager.commit(transactionStatus);
		
		this.rollbackOrder(mallTradeOrder.getOrderNo());
		
		MQTradeOrderVO mo = this.mallOrderFactory.create(mallTradeOrder.getOrderNo());
		String ext = mo.getExt();
		JSONObject jsonObject = JSONObject.parseObject(ext);
		String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		StringBuffer stringBuffer = new StringBuffer(date);
		stringBuffer.append(" ").append(time);
		asyncTask.sendOrder(mo.getOrderNo(),mo.getBuyerId(),mo.getShopId(),stringBuffer.toString(),mo.getSellPointText());
		return mallTradeOrder;
	}

	/**
	 * 创建订单中的商品信息<br>
	 * 商品金额采用数据库中的商品金额
	 * 
	 * @param
	 */
	protected List<MallTradeOrderGoods> genOrderGoods(BaseOrderFormVO fvo) {
		List<MallTradeOrderGoods> orderGoodsList = null;
		if (fvo.getBuyGoods() != null && !fvo.getBuyGoods().isEmpty()) {
			orderGoodsList = new LinkedList<MallTradeOrderGoods>();
			// 默认商品金额
			for (BuyGoodsFormVO vo : fvo.getBuyGoods()) {
				MallTradeOrderGoods ogs = this.mallTradeOrderGoodsMapper.getBuyGoods(vo);

				// 验证商品信息
				if (ogs == null) {
					throw new ServiceException(ResultCodeEnum.ERROR_TRADE_ORDER_DOWN_GOODS);
				}
				if (ogs.getGoodsClass() != OrderTypeEnum.CONSULTANT.code) {
					throw new ServiceException(ResultCodeEnum.ERROR_ORDER_BAD_TYPE);

				}
				orderGoodsList.add(ogs);
			}
		}
		return orderGoodsList;
	}

	/**
	 * 验证优惠劵 内存管理 计算商品的购买价格 优惠 价格 生成内存总订单
	 * 
	 */
	@Override
	protected List<MallTradeOrderGoods> generateOrderGoodsList(BaseOrderFormVO fvo, List<MallTradeOrderGoods> list,
			MallTradeOrder mallTradeOrder) {

		ConsultantServiceDTO dto = (ConsultantServiceDTO) fvo;
		list.stream().forEach(c -> {
			c.setDiscountAmount(0);
			c.setOrderNo(mallTradeOrder.getOrderNo());
			c.setBuyAmount(0);
			c.setCouponId(0);
		});
		// 总订单设置价格
		mallTradeOrder.setGoodsAmount(0);
		// 价格
		mallTradeOrder.setDiscountAmount(0);
		mallTradeOrder.setOrderAmount(0);
		mallTradeOrder.setPayStatus(PayStatusEnum.PAY_SUCC.code);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(CommonConstant.CONSULTANT_WORK_DATE, dto.getConsultantWorkDate());
		map.put(CommonConstant.CONSULTANT_WORK_TIME, dto.getConsultantWorkTime());
		mallTradeOrder.setExt(JSON.toJSONString(map));
		return list;
	}
}
