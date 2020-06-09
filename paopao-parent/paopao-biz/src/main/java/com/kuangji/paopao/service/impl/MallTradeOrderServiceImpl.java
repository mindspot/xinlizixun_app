package com.kuangji.paopao.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.admin.vo.OrderInfoVO;
import com.kuangji.paopao.admin.vo.SetMealInfoVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.dto.param.MallTradeOrderParam;
import com.kuangji.paopao.enums.BookingTimeEnum;
import com.kuangji.paopao.enums.ConsultantWorkEnum;
import com.kuangji.paopao.enums.DelayQueueManagerTypeEnum;
import com.kuangji.paopao.enums.OrderFromEnum;
import com.kuangji.paopao.enums.OrderStatusEnum;
import com.kuangji.paopao.enums.OrderTypeEnum;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.enums.PayTypeEnum;
import com.kuangji.paopao.mapper.BookingTimeMapper;
import com.kuangji.paopao.mapper.ConsultantSupervisorOrderMapper;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.BookingTime;
import com.kuangji.paopao.model.ConsultantSupervisorOrder;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.redis.AsyncTask;
import com.kuangji.paopao.redis.send.SendService;
import com.kuangji.paopao.redis.util.RedisUtils;
import com.kuangji.paopao.schedule.delayqueue.DelayQueueManager;
import com.kuangji.paopao.schedule.delayqueue.DelayTask;
import com.kuangji.paopao.schedule.delayqueue.TaskBase;
import com.kuangji.paopao.service.BookingTimeService;
import com.kuangji.paopao.service.ConsultantSupervisorOrderService;
import com.kuangji.paopao.service.ConsultationSetMealOrderService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.DateUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderDetailsVO;
import com.kuangji.paopao.vo.ConsultantMallTradeOrderVO;
import com.kuangji.paopao.vo.ConsultantOrderDiagnosisVO;
import com.kuangji.paopao.vo.ConsultantWorkMemberMallTradeOrderVO;
import com.kuangji.paopao.vo.MallTradeOrderVO;
import com.kuangji.paopao.vo.ReadOrderNumVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Author 金威正 Date 2020-02-20
 */
@Service
public class MallTradeOrderServiceImpl extends BaseServiceImpl<MallTradeOrder, Integer>
		implements MallTradeOrderService {
	@Autowired
	private MallTradeOrderMapper mallTradeOrderMapper;

	@Autowired
	private BookingTimeMapper bookingTimeMapper;

	@Autowired
	private RedisUtils redisUtils;

	@Value("${spring.redis.key.prefix.orderId}")
	private String REDIS_KEY_PREFIX_ORDER_ID;

	@Value("${spring.redis.key.prefix.orderCall}")
	private String REDIS_KEY_PREFIX_ORDER_CALL;

	@Value("${spring.redis.key.prefix.beforeTheEndOrderCall}")
	private String REDIS_KEY_PREFIX_BEFORE_THE_END_CALL;
	
	@Value("${spring.redis.key.prefix.stopOrderCall}")
	private String REDIS_KEY_PREFIX_STOP_ORDER_CALL;

	@Autowired
	private ConsultantSupervisorOrderMapper consultantSupervisorOrderMapper;

	@Autowired
	private ConsultantSupervisorOrderService consultantSupervisorOrderService;
	
	@Autowired
	private ConsultationSetMealOrderService consultationSetMealOrderService;
	
	@Autowired
	private DelayQueueManager delayQueueManager;
	
	@Autowired
	private UserMapper userMapper;

	// 通话之前8分钟 8*60
	private static final long BEFORE_ORDER_TIME = 480;

	// 过期往后延时间 3分钟 3*60
	private static final long AFTER_ORDER_TIME = 180;

	private static final Logger LOG = LoggerFactory.getLogger(MallTradeOrderServiceImpl.class);

	// 毫秒转30分钟时间戳 毫秒
	private static final long SUB_TIME = 10 * 60 * 1000;

	
	// 30分钟时间
	private static final long  THIRTY_MINUTES= 30 * 60;
	
	// 25分钟时间
	private static final long  THIRTY_FIVE_MINUTES= 25 * 60;

	@Autowired
	private AsyncTask asyncTask;
	
	
	@Autowired
	private SendService sendService;
	
	@Autowired
	private  RedissonClient redissonClient;
	
	@Autowired
	private BookingTimeService bookingTimeService;
	

	// 环信 用户 标志
	public static final String member = PropertiesFileUtil.getInstance().get("easemob_member");

	// 环信 咨询师标志easemob_consultant
	public static final String consultant = PropertiesFileUtil.getInstance().get("easemob_consultant");
	
	private static String END_ORDER="已完成";
	
	private static String CANCEl_ORDER="已取消";
	

	@Override
	public List<MallTradeOrderVO> listConsultantMallTradeOrder(Integer userId) {
		// TODO Auto-generated method stub
		return mallTradeOrderMapper.listConsultantMallTradeOrder(userId);
	}

	@Override
	public List<ConsultantMallTradeOrderVO> listConsultantMallTradeOrderWork(Integer consultantId) {
		// TODO Auto-generated method stub
		return mallTradeOrderMapper.listConsultantMallTradeOrderWork(consultantId);
	}

	@Override
	public BaseMapper<MallTradeOrder> getMapper() {
		// TODO Auto-generated method stub
		return mallTradeOrderMapper;
	}

	@Override
	public MallTradeOrder generateOrder(MallTradeOrder mallTradeOrder) {
		mallTradeOrder.setOrderFrom(OrderFromEnum.ON_LINE_MP.code);
		mallTradeOrder.setPayType(PayTypeEnum.PAY_ZFB_LINE.code);
		mallTradeOrder.setOrderNo(generateOrderSn(mallTradeOrder));
		return mallTradeOrder;
	}

	/**
	 * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
	 */
	private String generateOrderSn(MallTradeOrder order) {
		StringBuilder sb = new StringBuilder();
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String key = REDIS_KEY_PREFIX_ORDER_ID + date;
		Long increment = redisUtils.incr(key, 1);
		sb.append(date);
		sb.append(String.format("%02d", order.getOrderFrom()));
		sb.append(String.format("%02d", order.getPayType()));
		String incrementStr = increment.toString();
		if (incrementStr.length() <= 6) {
			sb.append(String.format("%06d", increment));
		} else {
			sb.append(incrementStr);
		}
		return sb.toString();
	}

	@Override
	public void memberConversation(String userEasemobId, String consultantEasemobId, Integer serviceType) {

		if (StringUtils.isBlank(userEasemobId) || StringUtils.isBlank(consultantEasemobId)) {
			throw new ServiceException(ResultCodeEnum.FAIL);
		}
		Integer memberId = 0;
		Integer shopId = 0;
		// 是督导订单
		if (userEasemobId.startsWith(consultant) && consultantEasemobId.startsWith(consultant)) {
			userEasemobId = userEasemobId.replaceAll("[a-zA-Z]", "");
			consultantEasemobId = consultantEasemobId.replaceAll("[a-zA-Z]", "");
			memberId = Integer.valueOf(userEasemobId);
			shopId = Integer.valueOf(consultantEasemobId);
			this.supervisedConsultantService(memberId, shopId);
			return;
		}
		userEasemobId = userEasemobId.replaceAll("[a-zA-Z]", "");
		consultantEasemobId = consultantEasemobId.replaceAll("[a-zA-Z]", "");
		memberId = Integer.valueOf(userEasemobId);
		shopId = Integer.valueOf(consultantEasemobId);
		this.memberConsultantService(memberId, shopId, serviceType);
	}

	@Override
	public int updateOrderRead(Integer consultantId) {

		return mallTradeOrderMapper.updateOrderRead(consultantId);
	}

	@Override
	public List<ConsultantMallTradeOrderVO> listConsultantMallTradeOrderWorkEnd(Integer consultantId) {
		// TODO Auto-generated method stub
		return mallTradeOrderMapper.listConsultantMallTradeOrderEnd(consultantId);
	}

	@Override
	public ConsultantMallTradeOrderDetailsVO getConsultantMallTradeOrderDetailsVO(Integer consultantId,
			String orderNo) {
		// TODO Auto-generated method stub
		return mallTradeOrderMapper.getConsultantMallTradeOrderDetailsVO(consultantId, orderNo);
	}

	@Override
	public List<ConsultantMallTradeOrderDetailsVO> listConsultantMallTradeOrderDetailsVO(Integer consultantId,
			Integer memberId) {
		// TODO Auto-generated method stub
		return mallTradeOrderMapper.listConsultantMallTradeOrderDetailsVO(consultantId, memberId);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int updateOrderConfirm(Integer consultantId, String orderNo) {

		int result = mallTradeOrderMapper.updateOrderConfirm(consultantId, orderNo);
		
		if (result>=1) {
			//验证该订单是不是套餐卡
			consultationSetMealOrderService.checkSetMealOrder(orderNo);
			// 1.咨询师确认订单信息 给用户发送窗口消息
			asyncTask.sendOrderConfirmServiceMsg(orderNo);
			// 2.咨询师确认订单信息 发送短信通知
			sendService.sendConsultantDetermineOrder(orderNo);
			// 3.监听订单前半小时提醒短信通知
			// 当前系统时间 == 确认订单时间
			long sysTime = new Date().getTime();
			Example example = new Example(MallTradeOrder.class);
			example.createCriteria().andEqualTo("orderNo", orderNo);
			MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOneByExample(example);
			String ext = mallTradeOrder.getExt();
			
			JSONObject jsonObject = JSONObject.parseObject(ext);
			String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
			String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
			StringBuffer stringBuffer = new StringBuffer(date);
			stringBuffer.append(" ").append(time);
			String bookingTime = date + " " + time.split("-")[0] + ":00";
			LOG.info("bookingTime  "+bookingTime);
			// 预约时间 转时间戳
			long timestamp = DateUtils.getTimestamp(bookingTime);
			LOG.info("bookingTime  timestamp "+ timestamp);
			// 发送提前半小时开始服务的短信通知
			long tx = timestamp - sysTime - SUB_TIME;
			TaskBase taskBase = new TaskBase(orderNo, DelayQueueManagerTypeEnum.START_ORDER.code);
			LOG.info("tx  "+tx);
			if (tx >= 0) {
				delayQueueManager.put(new DelayTask(taskBase, tx));
			} else {
				sendService.startimmediatelyOrderContent(orderNo);
			}
			
			return 1;
		}
		LOG.info("订单状态不是待确认状态  无法更改 ");
		throw  new ServiceException(ResultCodeEnum.ERROR_ORDER_CONFIRM);
	}

	@Override
	public List<ConsultantOrderDiagnosisVO> listConsultantMallTradeDiagnosis(String orderNo, Integer consultantId) {
		// TODO Auto-generated method stub
		return mallTradeOrderMapper.listConsultantMallTradeDiagnosis(orderNo, consultantId);
	}

	@Override
	public List<ConsultantWorkMemberMallTradeOrderVO> listConsultantWorkMemberMallTradeOrder(Integer consultantId) {
		// TODO Auto-generated method stub
		return mallTradeOrderMapper.listConsultantWorkMemberMallTradeOrder(consultantId);
	}

	// 客户 咨询师 发起服务
	protected void memberConsultantService(Integer userId, Integer shopId,Integer serviceType) {

		Example example =new Example(MallTradeOrder.class);
		
		List<Integer> listStatus=new ArrayList<Integer>();
		listStatus.add(OrderStatusEnum.SEND_SUCC.code);
		
		example.createCriteria()
			.andEqualTo("buyerId", userId)
			.andEqualTo("shopId", shopId)
			.andIn("orderStatus", listStatus);
		List<MallTradeOrder> orders = mallTradeOrderMapper.selectByExample(example);
		
		for (MallTradeOrder order : orders) {
			if (order.getOrderStatus()==OrderStatusEnum.SEND_SUCC.code) {
				if (this.checkOrderConsultantService(order,serviceType)) {
					return;
				}
			}
			
		}
		throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_FIN_FAIL);
	}

	protected boolean checkOrderConsultantService(MallTradeOrder order,Integer serviceType){
		
		String json = order.getExt();
		JSONObject jsonObject = JSONObject.parseObject(json);
		String consultantWorkDate = (String) jsonObject.get(CommonConstant.CONSULTANT_WORK_DATE);
		String consultantWorkTime = (String) jsonObject.get(CommonConstant.CONSULTANT_WORK_TIME);
		Date server = new Date();
		String strDateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		String serverdate = sdf.format(server);

		String strTimeFormat = "HH:mm";
		SimpleDateFormat format = new SimpleDateFormat(strTimeFormat);
		String time = format.format(server);

		if (!StringUtils.equals(serverdate, consultantWorkDate)) {
			return false;
		}

		String[] arr = consultantWorkTime.split("-");
		if (arr == null || arr.length != 2) {
			throw new ServiceException(ResultCodeEnum.FAIL);

		}
		long serverTime = DateUtils.formatSecond(time);
		long start = DateUtils.formatSecond(arr[0]);
		long end = DateUtils.formatSecond(arr[1]);
		if (serverTime >= start && serverTime <= end) {
			String workName = order.getSellerRemarks();
			if (!workName.equals(ConsultantWorkEnum.SET_MEAL.getValue())
					&& !workName.equals(ConsultantWorkEnum.FACING_EACH_OTHER.getValue())) {
				if (serviceType != ConsultantWorkEnum.getCode(workName)) {
					throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_FIND_ORDER);
				}

			}
			// 计算剩余时间
			long second = end - serverTime;
			long surplus = second + AFTER_ORDER_TIME;
			long noticeTime = 1;

			if (surplus >= BEFORE_ORDER_TIME) {
				noticeTime = surplus - BEFORE_ORDER_TIME;
				LOG.info("提前五分钟的过期时间 " + noticeTime);
				redisUtils.set(REDIS_KEY_PREFIX_BEFORE_THE_END_CALL + order.getOrderNo(), order.getOrderNo(),
						noticeTime);
			}
			redisUtils.set(REDIS_KEY_PREFIX_ORDER_CALL + order.getOrderNo(), order.getOrderNo(), surplus);
			return true;
		}
		return false;
	}
	
	@Override
	public void consultantConversation(Integer userId, String userEasemobId, Integer serviceType) {
		
		LOG.info("验证咨询师与客户服务通知 ,咨询师id"+ userId+" ,用户id "+userEasemobId+",服务类型"+serviceType);
		
		// 是督导订单
		if (userEasemobId.startsWith(consultant)) {
			userEasemobId = userEasemobId.replaceAll("[a-zA-Z]", "");
			this.supervisedConsultantService(userId, Integer.valueOf(userEasemobId));
			return;
		}
		Example example =new Example(MallTradeOrder.class);
		List<Integer> listStatus=new ArrayList<Integer>();
		listStatus.add(OrderStatusEnum.SEND_SUCC.code);
		listStatus.add(OrderStatusEnum.TRADE_SUCC.code);
		Integer buyerId=Integer.valueOf(userEasemobId.replaceAll("[a-zA-Z]", ""));
		example.createCriteria()
			.andEqualTo("buyerId",buyerId )
			.andEqualTo("shopId", userId)
			.andIn("orderStatus", listStatus);
		List<MallTradeOrder> orders = mallTradeOrderMapper.selectByExample(example);
	
		
		//验证 即将开始服务订单
		for (MallTradeOrder order : orders) {
			if (order.getOrderStatus()==OrderStatusEnum.SEND_SUCC.code) {
				if(this.checkOrderConsultantService(order,serviceType)){
					return;
				};
			}
		}

		//验证 完结订单
		for (MallTradeOrder order : orders) {	
			if (order.getOrderStatus()==OrderStatusEnum.TRADE_SUCC.code) {
				if(this.checkEndOrderConsultantService(order,serviceType)){
					return;
				};
			}
		}
		throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_FIN_FAIL);
		
	}
	
	protected boolean checkEndOrderConsultantService(MallTradeOrder order,Integer serviceType){
		
		String json = order.getExt();
		JSONObject jsonObject = JSONObject.parseObject(json);
		String consultantWorkDate = (String) jsonObject.get(CommonConstant.CONSULTANT_WORK_DATE);
		Date server = new Date();
		String strDateFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		String serverdate = sdf.format(server);
		LOG.info("验证完结日期  服务器日期  "+serverdate+",当前日期"+consultantWorkDate);
		if (serverdate.equals(consultantWorkDate)) {
			LOG.info("REDIS_KEY_PREFIX_STOP_ORDER_CALL set时间" +THIRTY_MINUTES);
			redisUtils.set(REDIS_KEY_PREFIX_STOP_ORDER_CALL + order.getOrderNo(), order.getOrderNo(), THIRTY_MINUTES);
			return true;
		}
		
		return false;
		
	}
	
	
	@Override
	public MallTradeOrder getMallTradeOrderByOrder(String orderNo) {
		return mallTradeOrderMapper.getMallTradeOrderByOrder(orderNo);
	}

	@Override
	public List<com.kuangji.paopao.admin.vo.MallTradeOrderVO> listConsultantMallTradeOrderByGoodsClass(
			MallTradeOrderParam mallTradeOrderParam) {
		PageHelper.startPage(mallTradeOrderParam.getPageNo(), mallTradeOrderParam.getPageSize());
		return mallTradeOrderMapper.listConsultantMallTradeOrderByGoodsClass(mallTradeOrderParam);
	}

	@Override
	public SetMealInfoVO getSetMealInfo(Integer id) {
		// TODO Auto-generated method stub
		return mallTradeOrderMapper.getSetMealInfo(id);
	}

	@Override
	public OrderInfoVO getOrderInfo(Integer id) {

		return mallTradeOrderMapper.getOrderInfo(id);
	}

	@SuppressWarnings("unused")
	protected void supervisedConsultantService(Integer userId, Integer shopId) {

		Example example = new Example(ConsultantSupervisorOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderStatus", OrderStatusEnum.SEND_SUCC.code);
		criteria.andEqualTo("consultantId", userId);
		criteria.andEqualTo("supervisorId", shopId);
		
		List<ConsultantSupervisorOrder> consultantSupervisorOrders = consultantSupervisorOrderMapper.selectByExample(example);
		
		Example example2 = new Example(ConsultantSupervisorOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria2 = example2.createCriteria();
		criteria2.andEqualTo("orderStatus", OrderStatusEnum.SEND_SUCC.code);
		criteria2.andEqualTo("consultantId", shopId);
		criteria2.andEqualTo("supervisorId", userId);
		
		List<ConsultantSupervisorOrder> consultantSupervisorOrders2 =consultantSupervisorOrderMapper.selectByExample(example2);

		if (consultantSupervisorOrders == null && consultantSupervisorOrders2 == null) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_FIN_FAIL);
		}
		
		int count=consultantSupervisorOrders.size();

		int count2 = consultantSupervisorOrders2.size();

		
		if (count <= 0 && count2 <= 0) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_FIN_FAIL);
		}
		
		for (ConsultantSupervisorOrder order : consultantSupervisorOrders) {
			String orderNo=order.getOrderNo();
			redisUtils.set(REDIS_KEY_PREFIX_ORDER_CALL + orderNo, orderNo, THIRTY_MINUTES);
		}
		
		for (ConsultantSupervisorOrder order : consultantSupervisorOrders2) {
			String orderNo=order.getOrderNo();
			redisUtils.set(REDIS_KEY_PREFIX_ORDER_CALL + orderNo, orderNo, THIRTY_MINUTES);
		}
	
	}

	@Override
	public boolean cancerOrderBuyUserId(Integer userId) {
		Example example = new Example(MallTradeOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria = example.createCriteria();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String toDayTime = format.format(new Date());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		String tomorrow = format.format(calendar.getTime());
		criteria.andEqualTo("buyerId", userId);
		criteria.andBetween("orderTime", toDayTime + " 00:00:00", tomorrow + " 00:00:00");
		criteria.andEqualTo("orderStatus", OrderStatusEnum.CANCEL_BUYER.code);
		int count = mallTradeOrderMapper.selectCountByExample(example);
		LOG.info("取消次数 " + count);
		return count <= 1;
	}

	@Override
	public boolean notPayOrderBuyUserId(Integer userId) {

		Example example = new Example(MallTradeOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("payStatus", PayStatusEnum.PAY_WAIT.code);
		criteria.andEqualTo("buyerId", userId);
		criteria.andNotEqualTo("orderStatus", OrderStatusEnum.CANCEL_BUYER.code);
		int count = mallTradeOrderMapper.selectCountByExample(example);
		return count < 1;
	}

	@Override
	public boolean isBooking(Integer userId, String date, String start) {
		Example example = new Example(BookingTime.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("consultantId", userId);
		criteria.andEqualTo("consultantWorkDate", date);
		criteria.andEqualTo("consultantWorkStartTime", start);
		BookingTime bookingTime = bookingTimeMapper.selectOneByExample(example);
		if (bookingTime.getTimeType() == BookingTimeEnum.RESERVATIONS.getValue()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkPayOrderBuyUserId(Integer userId) {

		if (!this.notPayOrderBuyUserId(userId)) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_PAY_ORDER);
		}
		if (!this.cancerOrderBuyUserId(userId)) {
			throw new ServiceException(ResultCodeEnum.ERROR_ORDER_NOT_BUY);
		}
		return true;
	}

	@Override
	public String getBookingUnKey(String orderNo) {
		Example example = new Example(MallTradeOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOneByExample(example);
		StringBuffer unkey = new StringBuffer();
		Integer shopId = mallTradeOrder.getShopId();
		String ext = mallTradeOrder.getExt();
		JSONObject jsonObject = JSONObject.parseObject(ext);
		String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		unkey.append(shopId).append("-").append(date).append("-").append(time.split("-")[0]);
		return unkey.toString();
	}

	@Override
	public int cancerOrderBuyUserId(String orderNo) {
		Example example = new Example(MallTradeOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		criteria.andEqualTo("payStatus", PayStatusEnum.PAY_WAIT.code);
		MallTradeOrder mallTradeOrder = new MallTradeOrder();
		mallTradeOrder.setOrderStatus(OrderStatusEnum.CANCEL_BUYER.code);
		return mallTradeOrderMapper.updateByExampleSelective(mallTradeOrder, example);
	}

	@Override
	public String getNotOrderPayStatusBookingUnKey(String orderNo) {
		Example example = new Example(MallTradeOrder.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		criteria.andEqualTo("payStatus", PayStatusEnum.PAY_WAIT.code);
		MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOneByExample(example);
		if (mallTradeOrder == null) {
			return "";
		}
		this.cancerOrderBuyUserId(orderNo);
		StringBuffer unkey = new StringBuffer();
		Integer shopId = mallTradeOrder.getShopId();
		String ext = mallTradeOrder.getExt();
		JSONObject jsonObject = JSONObject.parseObject(ext);
		String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		unkey.append(shopId).append("-").append(date).append("-").append(time.split("-")[0]);
		return unkey.toString();
	}


	@Override
	public int unReadOrderNum(Integer userId) {
		Example example =new  Example(MallTradeOrder.class);
		example.createCriteria()
			   .andEqualTo("shopId", userId)
			   .andEqualTo("isRead", 0);
		int readNum =mallTradeOrderMapper.selectCountByExample(example);
		return readNum;
	}

	@Override
	public ReadOrderNumVO unReadOrderNumVO(Integer userId) {
		ReadOrderNumVO readOrderNumVO =new ReadOrderNumVO();
		readOrderNumVO.setUnConsultationOrderNum(this.unReadOrderNum(userId));
		readOrderNumVO.setUnConsultantSupervisorOrderNum(this.consultantSupervisorOrderService.unReadOrderNumByConsultantId(userId));
		readOrderNumVO.setUnSupervisorOrderNum(this.consultantSupervisorOrderService.unReadOrderNumBySupervisorId(userId));
		return readOrderNumVO;
	}

	@Override
	public int canceOrderUnlock(Integer userId, String orderNo,int payStatus,int orderStatus,int  canceStatus,String content) {
		Example example =new Example(MallTradeOrder.class);
		Criteria  criteria=example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		if (userId!=null) {
			criteria.andEqualTo("buyerId", userId);
		}
		//PayStatusEnum.PAY_WAIT.code,
		criteria.andEqualTo("payStatus", payStatus);
		//OrderStatusEnum.CREATE_SUCC.code,
		criteria.andEqualTo("orderStatus",orderStatus);
		
	
		//OrderStatusEnum.CANCEL_BUYER.code
		MallTradeOrder order=mallTradeOrderMapper.selectOneByExample(example);
		LOG.info("order 准备解锁订单 "+order);
		//找到匹配订单
		if (order!=null) {
			MallTradeOrder updateMallTradeOrder=new MallTradeOrder();
			updateMallTradeOrder.setOrderStatus(canceStatus);
			int  result=mallTradeOrderMapper.updateByExampleSelective(updateMallTradeOrder, example);
			
			if (result>0) {
				MallTradeOrder mallTradeOrder=mallTradeOrderMapper.selectOne(new MallTradeOrder(orderNo));
				Integer shopId=mallTradeOrder.getShopId();
				String ext=mallTradeOrder.getExt();
				JSONObject jsonObject = JSONObject.parseObject(ext);
				String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
				String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
				RLock lock = redissonClient.getLock(shopId+"-"+date+"-"+time);
				if (lock.isLocked()) {
					lock.forceUnlock();
				}
				bookingTimeService.updateRecoveryStatusbuyUnKey(shopId+"-"+date+"-"+time.split("-")[0]);
				asyncTask.sendUpdateOrderMsg(orderNo,content);
			}
			
			return result;
		}
		return 0;
	}

	@Override
	public Boolean checkIfFirstOrder(MallTradeOrder mallTradeOrder) {
		Example example =new Example(MallTradeOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("orderStatus", OrderStatusEnum.TRADE_SUCC.code);
		criteria.andEqualTo("shopId", mallTradeOrder.getShopId());
		criteria.andEqualTo("buyerId", mallTradeOrder.getBuyerId());
		return mallTradeOrderMapper.selectCountByExample(example) == 0;
	}

	@Override
	public int closeOrder(String orderNo) {
	
		Example example =new Example(MallTradeOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("orderNo", orderNo);
		criteria.andEqualTo("payStatus", PayStatusEnum.PAY_SUCC.code);
		criteria.andEqualTo("orderStatus", OrderStatusEnum.SEND_SUCC.code);
		MallTradeOrder mallTradeOrder=new MallTradeOrder();
		mallTradeOrder.setOrderStatus(OrderStatusEnum.TRADE_SUCC.code);
		int result=mallTradeOrderMapper.updateByExampleSelective(mallTradeOrder, example);
		if (result>0) {
			asyncTask.sendUpdateOrderMsg(orderNo, END_ORDER);
		}
		
		return result;
	}

	@Override
	public List<MallTradeOrder> listNeedCloseOrder() {
		Example example =new Example(MallTradeOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("payStatus", PayStatusEnum.PAY_SUCC.code);
		criteria.andEqualTo("orderStatus", OrderStatusEnum.SEND_SUCC.code);
		List<MallTradeOrder> mallTradeOrders=mallTradeOrderMapper.selectByExample(example);
		return mallTradeOrders;
	}

	@Override
	public boolean checkOverBooktimeOrder(MallTradeOrder mallTradeOrder) {
		if (mallTradeOrder==null) {
			return false;
		}
		String ext =mallTradeOrder.getExt();
	
		JSONObject jsonObject = JSONObject.parseObject(ext);
		String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		StringBuffer bookingBuf = new StringBuffer(date);
		bookingBuf.append(" ")
			   .append(time.split("-")[1])
			   .append(":00");
		String bookingTime=bookingBuf.toString();
		//预定时间戳
		long bookingEndTimestamp=DateUtils.getTimestamp(bookingTime);
		//当前服务器时间戳
		long serverTimestamp=new Date().getTime();
		
		if (bookingEndTimestamp<serverTimestamp) {
			return true;
		}
		return false;
	}

	@Override
	public int overBooktimeOrder(MallTradeOrder mallTradeOrder) {
		
		boolean bl=this.checkOverBooktimeOrder(mallTradeOrder);
		int result=0;
		if (bl) {
			result=this.closeOrder(mallTradeOrder.getOrderNo());
		}
		return result;
	}

	@Override
	public List<MallTradeOrder> listNeedCancelOrder() {
		Example example =new Example(MallTradeOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("payStatus", PayStatusEnum.PAY_SUCC.code);
		criteria.andEqualTo("orderStatus", OrderStatusEnum.PAY_SUCC.code);
		List<MallTradeOrder> mallTradeOrders=mallTradeOrderMapper.selectByExample(example);
		return mallTradeOrders;
	}

	@Override
	public boolean checkCanceBooktimeOrder(MallTradeOrder mallTradeOrder) {
		if (mallTradeOrder==null) {
			return false;
		}
		String ext =mallTradeOrder.getExt();
	
		JSONObject jsonObject = JSONObject.parseObject(ext);
		String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
		String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
		StringBuffer bookingBuf = new StringBuffer(date);
		bookingBuf.append(" ")
			   .append(time.split("-")[0])
			   .append(":00");
		String bookingTime=bookingBuf.toString();
		//预定时间戳
		long bookingStartTimestamp=DateUtils.getTimestamp(bookingTime);
		//当前服务器时间戳
		long serverTimestamp=new Date().getTime();
		LOG.info("预约开始时间 "+bookingTime+" 转成时间戳 "+bookingStartTimestamp+" 当前时间 "+new Date() +" 转时间搓 "+serverTimestamp);
		
		if (bookingStartTimestamp<serverTimestamp) {
			return true;
		}
		return false;
	}
	@Override
	public int canceBooktimeOrder(MallTradeOrder mallTradeOrder) {
		
		boolean bl=this.checkCanceBooktimeOrder(mallTradeOrder);
		int result=0;
		if (bl) {
			result=this.canceOrderUnlock(mallTradeOrder);
		}
		return result;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int canceOrderUnlock(MallTradeOrder mallTradeOrder) {
	
		boolean bl=this.checkCanceBooktimeOrder(mallTradeOrder);
		if (bl) {
			mallTradeOrder.setOrderStatus(OrderStatusEnum.REFUND.code);
			int  result=mallTradeOrderMapper.updateByPrimaryKeySelective(mallTradeOrder);
			Integer shopId=mallTradeOrder.getShopId();
			String ext=mallTradeOrder.getExt();
			JSONObject jsonObject = JSONObject.parseObject(ext);
			String date = jsonObject.getString(CommonConstant.CONSULTANT_WORK_DATE);
			String time = jsonObject.getString(CommonConstant.CONSULTANT_WORK_TIME);
			RLock lock = redissonClient.getLock(shopId+"-"+date+"-"+time);
			if (lock.isLocked()) {
				lock.forceUnlock();
			}
			LOG.info("修改状态卖家取消  : "+mallTradeOrder.getOrderNo());
			if (result>0) {
				bookingTimeService.updateRecoveryStatusbuyUnKey(shopId+"-"+date+"-"+time.split("-")[0]);
				asyncTask.sendUpdateOrderMsg(mallTradeOrder.getOrderNo(),CANCEl_ORDER);
				if (mallTradeOrder.getOrderType()==OrderTypeEnum.PAY_SET_MEAL.code) {
					LOG.info("套餐增加次数订单号: "+mallTradeOrder.getOrderNo());
					consultationSetMealOrderService.updateAddResidualTimes(mallTradeOrder.getOrderNo());
				}else{
					LOG.info("取消退款订单号: "+mallTradeOrder.getOrderNo());
					userMapper.updateAccount(mallTradeOrder.getBuyerId(), mallTradeOrder.getOrderAmount());
				}
			}
			return result;
		}
		return 0;
	}

	@Override
	public int unReadOrderNum(String orderNo) {
		
		Example example =new Example(MallTradeOrder.class);
		tk.mybatis.mapper.entity.Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("orderNo",orderNo);
		MallTradeOrder mallTradeOrder=mallTradeOrderMapper.selectOneByExample(example);
		MallTradeOrder order=new MallTradeOrder();
		order.setId(mallTradeOrder.getId());
		order.setIsRead(1);
		return mallTradeOrderMapper.updateByPrimaryKeySelective(order);
	}



}
