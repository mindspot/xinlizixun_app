package com.kuangji.paopao.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.kuangji.paopao.enums.LoginEnum;
import com.kuangji.paopao.enums.OrderTypeEnum;
import com.kuangji.paopao.mapper.ArticleMapper;
import com.kuangji.paopao.mapper.BookingTimeMapper;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.model.Article;
import com.kuangji.paopao.model.BookingTime;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.schedule.delayqueue.DelayQueueManager;
import com.kuangji.paopao.service.AccountService;
import com.kuangji.paopao.service.BookingTimeService;
import com.kuangji.paopao.service.ConsultantScheduleService;
import com.kuangji.paopao.service.MallTradeOrderService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Configuration
@EnableScheduling
public class SaticScheduleTask {
	@Autowired
	private ConsultantMapper consultantMapper;
	@Autowired
	private BookingTimeMapper bookingTimeMapper;

	@Autowired
	private ArticleMapper articleMapper;

	@Autowired
	private MallTradeOrderService mallTradeOrderService;

	@Autowired
	private ConsultantScheduleService consultantScheduleService;

	
	@Autowired
	private AccountService accountService;

	@Autowired
	private BookingTimeService bookingTimeService;
	
	public static SimpleDateFormat dateDtf = new SimpleDateFormat("yyyy-MM-dd");

	private final Logger logger = LoggerFactory.getLogger(DelayQueueManager.class);

	// 3.添加定时任务 12:05 启动
	@Scheduled(cron = "0 15 0 * * ?")
	private void configureTasks() {
		logger.info("开始跑 增加预约是的 任务"+ new Date());
		Example example = new Example(Consultant.class);
		Criteria criteria = example.createCriteria();
		// 通过状态下的咨询师
		criteria.andEqualTo("status", LoginEnum.LOGIN_NORMAL.getIndex());
		List<Consultant> consultants = consultantMapper.selectByExample(example);
		for (Consultant consultant : consultants) {
			Integer userId = consultant.getUserId();
			Date date = bookingTimeMapper.getUserMaxDate(userId);
			if (date == null) {
				date = new Date();
			}
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			date = c.getTime();
			List<BookingTime> list = consultantScheduleService.listBookTime(userId);

			for (BookingTime bookingTime : list) {
				StringBuffer unkey = new StringBuffer();
				unkey.append(userId).append("-").append(dateDtf.format(date)).append("-")
						.append(bookingTime.getConsultantWorkStartTime());
				bookingTime.setUnKey(unkey.toString());
				bookingTime.setConsultantId(userId);
				bookingTime.setConsultantWorkDate(dateDtf.format(date));
			}
			if (!list.isEmpty()) {
				bookingTimeMapper.insertBatch(list);
				bookingTimeService.deletetwoDaysAgo(new Date(),userId);
			}

		}
	}


	
	// 3.修改咨询师排序
	//@Scheduled(cron = "0 0 0 * * ?")
	@Scheduled(cron = "0 0/30 * * * ?")
	private void updateConsultantSort() {
		Example example = new Example(Consultant.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("status", LoginEnum.LOGIN_NORMAL.getIndex());
		List<Consultant> consultants = consultantMapper.selectByExample(example);
		for (Consultant consultant : consultants) {
			Consultant c = new Consultant();
			c.setId(consultant.getId());
			c.setSort(new Random().nextInt(1000));
			consultantMapper.updateByPrimaryKeySelective(c);
		}
	}

	// 3.修改文章排序
	//@Scheduled(cron = "0 0 0 * * ?")
	@Scheduled(cron = "0 0/30 * * * ?")
	private void updateArticletSort() {

		List<Article> list = articleMapper.selectAll();
		for (Article article : list) {
			Article article2 = new Article();
			article2.setId(article.getId());
			article2.setSort(new Random().nextInt(1000));
			articleMapper.updateByPrimaryKeySelective(article2);
		}
	}

	// 订单
	@Scheduled(cron = "0 15 0 * * ?")
	private void closeOrder() {
		List<MallTradeOrder> mallTradeOrders = mallTradeOrderService.listNeedCloseOrder();
		for (MallTradeOrder mallTradeOrder : mallTradeOrders) {
			logger.info("扫描到 未完结订单 "+mallTradeOrder.toString());
			int result=mallTradeOrderService.overBooktimeOrder(mallTradeOrder);
			if (result>0&&mallTradeOrder.getOrderType()!=OrderTypeEnum.PAY_SET_MEAL.code) {
				accountService.doSubCommissionRecord(mallTradeOrder.getOrderNo());
			}
		}
	}
	
	@Scheduled(cron = "0 15 0 * * ?")
	private void canceBooktimeOrderTask() {
		List<MallTradeOrder> mallTradeOrders = mallTradeOrderService.listNeedCancelOrder();
		for (MallTradeOrder mallTradeOrder : mallTradeOrders) {
			logger.info("扫描到 没有确定的订单 "+mallTradeOrder.toString());
			mallTradeOrderService.canceOrderUnlock(mallTradeOrder);
		}
		
	}
	
}