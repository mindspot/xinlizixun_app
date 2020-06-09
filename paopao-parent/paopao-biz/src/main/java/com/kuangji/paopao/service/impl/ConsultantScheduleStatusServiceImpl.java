package com.kuangji.paopao.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.dto.UserScheduleStatusDTO;
import com.kuangji.paopao.dto.UserScheduleStatusDTO.Body;
import com.kuangji.paopao.enums.BookingTimeEnum;
import com.kuangji.paopao.enums.UserScheduleStatusEnum;
import com.kuangji.paopao.mapper.BookingTimeMapper;
import com.kuangji.paopao.mapper.ConsultantScheduleRestStatusMapper;
import com.kuangji.paopao.model.BookingTime;
import com.kuangji.paopao.model.ConsultantScheduleRestStatus;
import com.kuangji.paopao.model.ConsultantScheduleStatus;
import com.kuangji.paopao.service.ConsultantScheduleService;
import com.kuangji.paopao.service.ConsultantScheduleStatusService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.DateUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Author 金威正 Date 2020-02-28
 */
@Service
public class ConsultantScheduleStatusServiceImpl extends BaseServiceImpl<ConsultantScheduleStatus, Integer>
		implements ConsultantScheduleStatusService {


	@Autowired
	private ConsultantScheduleService consultantScheduleService  ;
	
	@Autowired
	private BookingTimeMapper bookingTimeMapper;

	@Autowired
	private ConsultantScheduleRestStatusMapper consultantScheduleRestStatusMapper;

	// 10分钟 600秒
	@Override
	public boolean insertConsultantScheduleStatus(Integer  userId, UserScheduleStatusDTO userScheduleStatusDTO) {
		String date = userScheduleStatusDTO.getDate();
		if (StringUtils.isBlank(date)) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}

		if (!DateUtils.timeContrast(date)) {
			throw new ServiceException(ResultCodeEnum.ERROR_WRONG_WOKK_START_END);
		}

		List<Body> bodys = userScheduleStatusDTO.getBody();
		if (bodys == null || bodys.isEmpty()) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);

		}

		
		for (Body body : bodys) {
				String start = body.getConsultantWorkStartTime();
				if (StringUtils.isBlank(start)) {
					throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);

				}
				String end = body.getConsultantWorkEndTime();
				if (StringUtils.isBlank(end)) {
					throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);

				}
				Integer type = body.getTimeType();
				if (type == null || type == UserScheduleStatusEnum.BE_BOOKED.getIndex()) {
					continue;

				}
					
				BookingTime bookingTime =new BookingTime();
				StringBuffer unKey =new StringBuffer();
				unKey.append(userId)
				     .append("-")
				     .append(date)
				     .append("-")
				     .append(start);
				bookingTime.setTimeType(type);
				Example example =new Example(BookingTime.class);
				example.createCriteria().andEqualTo("unKey", unKey.toString());
				bookingTimeMapper.updateByExampleSelective(bookingTime, example);
	
			}
	
		return true;

	}

	@Override
	public boolean getConsultantScheduleStatus(Integer consultantId, String date, String start, String end) {
	

		return true;
	}

	@Override
	public BaseMapper<ConsultantScheduleStatus> getMapper() {
		// TODO Auto-generated method stub
		return null;
	}
	@Transactional
	@Override
	public boolean insertConsultantScheduleStatus(Integer userId, String date, Integer timeType) {
		if (StringUtils.isBlank(date)) {
			date=DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		}
		//休息
		if (timeType == BookingTimeEnum.REST.getValue()) {
			try {
				this.rest(userId, date);
			} catch (Exception e) {
				throw e;
			}
			return true;
		}

		// 恢复设置
		if (timeType == BookingTimeEnum.RESERVATIONS.getValue()) {
			try {
				this.recovery(userId, date);
			} catch (Exception e) {
				throw e;
			}
			return true;
		}

		return true;

	}

	// 休息
	protected void rest(Integer userId, String date) {
		Example example = new Example(BookingTime.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("consultantId", userId);
		criteria.andEqualTo("consultantWorkDate", date);
		BookingTime bookingTime =new BookingTime();
		bookingTime.setTimeType(BookingTimeEnum.REST.getValue());
		criteria.andNotEqualTo("timeType", BookingTimeEnum.BOOKING.getValue());
		bookingTimeMapper.updateByExampleSelective(bookingTime, example);
		int count=consultantScheduleRestStatusMapper.selectCount(new ConsultantScheduleRestStatus(userId, date));
		if (count<1) {
			consultantScheduleRestStatusMapper.insert(new ConsultantScheduleRestStatus(userId, date));
		}
	}

	// 恢复
	protected void recovery(Integer userId, String date) {

		
		List<BookingTime> bookingTimes=consultantScheduleService.listBookTime(userId);
		
		for (BookingTime bookingTime : bookingTimes) {
			Example example = new Example(BookingTime.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("consultantId", userId);
			criteria.andEqualTo("consultantWorkDate", date);
			criteria.andEqualTo("consultantWorkStartTime", bookingTime.getConsultantWorkStartTime());
			criteria.andEqualTo("consultantWorkEndTime", bookingTime.getConsultantWorkEndTime());
			criteria.andNotEqualTo("timeType", BookingTimeEnum.BOOKING.getValue());
			
			bookingTimeMapper.updateByExampleSelective(bookingTime, example);
			
		}
		
		consultantScheduleRestStatusMapper.delete(new ConsultantScheduleRestStatus(userId, date));

	}

	// 大于24:00点的转化
	protected long transformation(long time) {
		if (time >= CommonConstant.TWELVE_P_M_SECOND) {
			time = time - CommonConstant.TWELVE_P_M_SECOND;
		}
		return time;
	}
	
	//转map
	protected  	Map<String, ConsultantScheduleStatus> getStatusMap(List<ConsultantScheduleStatus> userScheduleStatusList){
		
		Map<String, ConsultantScheduleStatus> statsMap=new LinkedHashMap<String, ConsultantScheduleStatus>();
		
		for (ConsultantScheduleStatus status : userScheduleStatusList) {
			statsMap.put(status.getScheduleStartTime(), status);
		}
		return statsMap;
	}

}
