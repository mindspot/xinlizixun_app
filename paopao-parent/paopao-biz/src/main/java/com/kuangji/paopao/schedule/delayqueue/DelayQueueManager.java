package com.kuangji.paopao.schedule.delayqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.enums.DelayQueueManagerTypeEnum;
import com.kuangji.paopao.enums.OrderStatusEnum;
import com.kuangji.paopao.enums.PayStatusEnum;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.redis.send.SendService;
import com.kuangji.paopao.service.ConsultationSetMealOrderService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.PointHistoryService;

@Component
public class DelayQueueManager implements CommandLineRunner {
	
	private final Logger logger = LoggerFactory.getLogger(DelayQueueManager.class);
	
	private DelayQueue<DelayTask> delayQueue = new DelayQueue<>();

	@Autowired
	private MallTradeOrderService mallTradeOrderService;

	@Autowired
	private SendService sendService;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PointHistoryService pointHistoryService;
	
	@Autowired
	private ConsultationSetMealOrderService consultationSetMealOrderService;
	
	private static final String THIRTY_MINUTES_NOT_PAY = "30分钟未付款";

	private static final String CONTENT = "用户取消两次订单后，当日不可继续下单。";

	private static final String OVERTIME = "咨询师超时未确认";

	private static final String TWENTY_FOUR_HOURS = "咨询师未确认不影响用户当日继续下单";

	private static String CANCEl_ORDER="已取消";
	/**
	 * 加入到延时队列中
	 * 
	 * @param task
	 */
	public void put(DelayTask task) {
		logger.info("加入延时任务：{}", task);
		delayQueue.put(task);
	}

	/**
	 * 取消延时任务
	 * 
	 * @param task
	 * @return
	 */
	public boolean remove(DelayTask task) {
		logger.info("取消延时任务：{}", task);
		return delayQueue.remove(task);
	}

	/**
	 * 取消延时任务
	 * 
	 * @param taskid
	 * @return
	 */
	public boolean remove(String taskid, int type) {
		return remove(new DelayTask(new TaskBase(taskid, type), 0));
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("初始化延时队列");
		Executors.newFixedThreadPool(20).execute(new Thread(this::excuteThread));
	}

	/**
	 * 延时任务执行线程
	 */
	private void excuteThread() {
		while (true) {
			try {
				DelayTask task = delayQueue.take();
				processTask(task);
			} catch (InterruptedException e) {
				logger.info("执行延时任务：异常");
				break;
			}
		}
	}

	/**
	 * 内部执行延时任务
	 * 
	 * @param task
	 */
	@Transactional(rollbackFor = Exception.class)
	private void processTask(DelayTask task) {
		logger.info("执行延时任务：{}" + task);
		TaskBase taskBase = task.getData();
		String orderNo = taskBase.getIdentifier();
		int type = taskBase.getType();
		logger.info("消息类型 " + type);

		if (type == DelayQueueManagerTypeEnum.TEMP_UPDATE_ORDER.code) {

			StringBuffer appendStr = new StringBuffer();
			appendStr.append(THIRTY_MINUTES_NOT_PAY)
			 		 .append("\n")
					 .append(CANCEl_ORDER)
					 .append("\n").append(CONTENT);
			mallTradeOrderService.canceOrderUnlock(null, orderNo, PayStatusEnum.PAY_WAIT.code,
					OrderStatusEnum.CREATE_SUCC.code, OrderStatusEnum.CANCEL_BUYER.code, appendStr.toString());

			// 应该发半小时文案
			return;
		}

		// "监听到预约时间 开始前未确认
		if (type == DelayQueueManagerTypeEnum.TWENT_FOUR_HOURS.code) {

			logger.info("监听到预约时间 开始前未确认 " + orderNo);
			StringBuffer appendStr = new StringBuffer();
			appendStr.append(OVERTIME)
					.append("\n")
					.append(CANCEl_ORDER)
					.append("\n")
					.append(TWENTY_FOUR_HOURS);

			int result = mallTradeOrderService.canceOrderUnlock(null, orderNo, PayStatusEnum.PAY_SUCC.code,
					OrderStatusEnum.PAY_SUCC.code, OrderStatusEnum.REFUND.code, appendStr.toString());
			if (result >0) {
				logger.info("发送短信方法任务 订单号为 " + orderNo);
				sendService.sendConsultantOvertimeOrder(orderNo);
				pointHistoryService.refundPointHistory(orderNo);
			}
			return;
		}
		
		// 监听24小时 套餐卡订单未确定短信通知
		if (type == DelayQueueManagerTypeEnum.SET_MEAL_TWENT_FOUR_HOURS.code) {

					logger.info("24小时未接套餐单开始执行  订单号为 " + orderNo);
					StringBuffer appendStr = new StringBuffer();
					appendStr.append(OVERTIME)
							.append("\n")
							.append(CANCEl_ORDER)
							.append("\n")
							.append(TWENTY_FOUR_HOURS);

					int result = mallTradeOrderService.canceOrderUnlock(null, orderNo, PayStatusEnum.PAY_SUCC.code,
							OrderStatusEnum.PAY_SUCC.code, OrderStatusEnum.REFUND.code, appendStr.toString());
					if (result >0) {
						logger.info("发送套餐24小时未取消的  短信方法任务 订单号为 " + orderNo);
						sendService.sendConsultantOvertimeOrder(orderNo);
						consultationSetMealOrderService.updateAddResidualTimes(orderNo);
					}
					return;
		}
		// 提前半小时发送短信通知
		if (type == DelayQueueManagerTypeEnum.START_ORDER.code) {
			sendService.sendStartOrder(orderNo);
			return;
		}
	}
}