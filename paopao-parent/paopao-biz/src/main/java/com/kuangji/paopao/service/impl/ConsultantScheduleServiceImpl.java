package com.kuangji.paopao.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.UserScheduleStatusDTO;
import com.kuangji.paopao.dto.UserScheduleStatusDTO.Body;
import com.kuangji.paopao.enums.BookingTimeEnum;
import com.kuangji.paopao.mapper.BookingTimeMapper;
import com.kuangji.paopao.mapper.ConsultantScheduleMapper;
import com.kuangji.paopao.mapper.ConsultantScheduleRestStatusMapper;
import com.kuangji.paopao.model.BookingTime;
import com.kuangji.paopao.model.ConsultantSchedule;
import com.kuangji.paopao.model.ConsultantScheduleRestStatus;
import com.kuangji.paopao.service.ConsultantScheduleService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正 Date 2020-02-27
 */
@Service
public class ConsultantScheduleServiceImpl extends BaseServiceImpl<ConsultantSchedule, Integer>
		implements ConsultantScheduleService {
	@Autowired
	private ConsultantScheduleMapper userScheduleMapper;

	
	@Autowired
	private BookingTimeMapper bookingTimeMapper;
	
	@Autowired
	private ConsultantScheduleRestStatusMapper consultantScheduleRestStatusMapper ;
	



	/**
	 * 批量插入 Author 金威正 Date 2019-12-12
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBatch( Integer  userId, UserScheduleStatusDTO userScheduleStatusDTO) {
			
		List<Body> bodys=userScheduleStatusDTO.getBody();
		
		for (Body body : bodys) {
			ConsultantSchedule consultantSchedule= new ConsultantSchedule();
			consultantSchedule.setStatus(body.getTimeType());
			Example example =new Example(ConsultantSchedule.class);
			example.createCriteria()
				.andEqualTo("consultantId",userId)
				.andEqualTo("platformWorkingTimeId",body.getPlatformWorkingTimeId())
				.andEqualTo("scheduleStartTime",body.getConsultantWorkStartTime())
				.andEqualTo("scheduleEndTime",body.getConsultantWorkEndTime());
			userScheduleMapper.updateByExampleSelective(consultantSchedule, example);
			
		}
		
	String[] calendars = new String[8];
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	for (int i = 0; i < calendars.length; i++) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, i);
		calendars[i] = sdf.format(calendar.getTime());
		String date =calendars[i];
		List<BookingTime> bookingTimes=	this.listBookTime(userId, bodys);
		for (BookingTime bookingTime : bookingTimes) {
			Example example =new Example(BookingTime.class);
			example.createCriteria()
				.andEqualTo("consultantId",userId)
				.andEqualTo("consultantWorkDate",date)
				.andEqualTo("consultantWorkStartTime",bookingTime.getConsultantWorkStartTime())
				.andEqualTo("consultantWorkEndTime",bookingTime.getConsultantWorkEndTime())
				.andNotEqualTo("timeType", BookingTimeEnum.BOOKING.getValue());
			bookingTimeMapper.updateByExampleSelective(bookingTime, example);
		}
	}

		consultantScheduleRestStatusMapper.delete(new ConsultantScheduleRestStatus(userId));
		return 1;

	}



	@Override
	public BaseMapper<ConsultantSchedule> getMapper() {
		// TODO Auto-generated method stub
		return userScheduleMapper;
	}



	@Override
	public List<BookingTime> listBookTime(Integer userId,List<Body> bodys) {
		// TODO Auto-generated method stub
		List<BookingTime> bookingTimes =new ArrayList<BookingTime>();

		for (Body body : bodys) {
			BookingTime bookingTime =new BookingTime();
				if (body.getTimeType()==BookingTimeEnum.BOOKING.getValue()) {
					continue;
				}
				bookingTime.setConsultantWorkStartTime(body.getConsultantWorkStartTime());
				bookingTime.setConsultantWorkEndTime(body.getConsultantWorkEndTime());
				bookingTime.setTimeType(body.getTimeType());
				bookingTimes.add(bookingTime);
		}
		
		return bookingTimes;
	}



	@Override
	public List<BookingTime> listBookTime(Integer userId) {
		Example example =new Example(ConsultantSchedule.class);
		example.createCriteria().andEqualTo("consultantId",userId );

		List<ConsultantSchedule> userScheduleStatus=userScheduleMapper.selectByExample(example);
		List<BookingTime> bookingTimes =new ArrayList<>();
		for (ConsultantSchedule consultantSchedule : userScheduleStatus) {

			BookingTime bookingTime =new BookingTime();
			bookingTime.setConsultantWorkStartTime(consultantSchedule.getScheduleStartTime());
			bookingTime.setConsultantWorkEndTime(consultantSchedule.getScheduleEndTime());
			bookingTime.setTimeType(consultantSchedule.getStatus());
			bookingTimes.add(bookingTime);
		}
		return bookingTimes;
	}

}
