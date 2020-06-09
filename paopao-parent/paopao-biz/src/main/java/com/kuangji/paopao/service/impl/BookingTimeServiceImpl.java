package com.kuangji.paopao.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.enums.BookingTimeEnum;
import com.kuangji.paopao.enums.DeleteStatusEnum;
import com.kuangji.paopao.mapper.BookingTimeMapper;
import com.kuangji.paopao.mapper.ConsultantScheduleRestStatusMapper;
import com.kuangji.paopao.model.BookingTime;
import com.kuangji.paopao.model.ConsultantScheduleRestStatus;
import com.kuangji.paopao.service.BookingTimeService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.DateUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.BookingTimeVO;
import com.kuangji.paopao.vo.MapBookingTimeVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Author 金威正 Date 2020-02-27
 */
@Service
public class BookingTimeServiceImpl extends BaseServiceImpl<BookingTime, Integer> implements BookingTimeService {
	@Autowired
	private BookingTimeMapper bookingTimeMapper;

	
	@Autowired
	private ConsultantScheduleRestStatusMapper restStatusMapper;
	
	
	
	private static final int platformWorkingTime = 
			Integer.valueOf(PropertiesFileUtil.getInstance().get("platform_working_time"));

	@Override
	public BaseMapper<BookingTime> getMapper() {
		return bookingTimeMapper;
	}

	/**
	 * 逻辑删除 Author 金威正 Date 2019-12-12
	 */
	public int logicDeleteById(int id) {
		BookingTime bookingTime = new BookingTime();
		bookingTime.setId(id);
		bookingTime.setIsDelete(DeleteStatusEnum.YES.getStatus());
		return bookingTimeMapper.updateByPrimaryKeySelective(bookingTime);
	}

	/**
	 * 真删除 Author 金威正 Date 2020-02-27
	 */
	public int delete(List<Integer> ids) {
		Example example = new Example(BookingTime.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", ids);
		return bookingTimeMapper.deleteByExample(example);
	}

	@Override
	public Map<String, Object> listWorkBookingTime(int userId, String date) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date bookDate = null;
		try {
			bookDate = sdf.parse(date);
		} catch (Exception e) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		long millss = System.currentTimeMillis();
		if (bookDate.getTime() < millss) {
			bookDate = new Date();
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String[] calendars = new String[7];
		for (int i = 0; i < calendars.length; i++) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(bookDate);
			calendar.add(Calendar.DATE, i);
			calendars[i] = sdf.format(calendar.getTime());
		}
		Example example =new Example(BookingTime.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("consultantId", userId);
		criteria.andBetween("consultantWorkDate", date, calendars[calendars.length-1]);
		List<BookingTime> list = bookingTimeMapper.selectByExample(example);
		for (int i = 0; i < calendars.length; i++) {
			String dstr = calendars[i];
			List<BookingTime> lVos = list.stream().filter(c -> c.getConsultantWorkDate().equals(dstr)).filter(c -> {
				String start = c.getConsultantWorkStartTime();
				return DateUtils.greaterThanService(dstr, start)&&c.getTimeType() == BookingTimeEnum.RESERVATIONS.getValue();
			}).sorted(Comparator.comparing(BookingTime::getSortTime)).collect(Collectors.toList());
			// 统计可预约人数
		
			MapBookingTimeVO mapVO = new MapBookingTimeVO();
			mapVO.setWeek(DateUtils.dateToWeek(dstr));
			mapVO.setList(this.parseBookingTimeVOList(lVos));
			map.put(dstr, mapVO);
		}
		return map;
	}



	@Override
	public BookingTime getConsultantBookingTime(Integer userId, String start, String end) {
		return null;
	}

	@Override
	public Map<String, Object> listConsultantWorkBookingTime(Integer userId, String date) {
		// TODO Auto-generated method stub

		Map<String, Object> map = new LinkedHashMap<String, Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date bookDate = null;
		try {
			bookDate = sdf.parse(date);
		} catch (Exception e) {
			throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
		}
		long millss = System.currentTimeMillis();
		if (bookDate.getTime() < millss) {
			bookDate = new Date();
		}
		String[] calendars = new String[7];
		for (int i = 0; i < calendars.length; i++) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(bookDate);
			calendar.add(Calendar.DATE, i);
			calendars[i] = sdf.format(calendar.getTime());
		}
		// 工作时间表
		Example example =new Example(BookingTime.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("consultantId",userId);
		criteria.andBetween("consultantWorkDate", date, calendars[calendars.length-1]);		
		List<BookingTime> list = bookingTimeMapper.selectByExample(example);

		for (int i = 0; i < calendars.length; i++) {
			String dstr = calendars[i];
			List<BookingTime> lVos = list.stream().filter(c -> c.getConsultantWorkDate().equals(dstr))
					.sorted(Comparator.comparing(BookingTime::getSortTime)).collect(Collectors.toList());
			
			MapBookingTimeVO mapVO = new MapBookingTimeVO();
			mapVO.setWeek(DateUtils.dateToWeek(dstr));
			mapVO.setList(this.parseBookingTimeVOList(lVos));
			Example rExample =new Example(ConsultantScheduleRestStatus.class);
			Criteria  rCriteria=rExample.createCriteria();
			rCriteria.andEqualTo("consultantId", userId);
			rCriteria.andEqualTo("restDate", dstr);
			int count=restStatusMapper.selectCountByExample(rExample);
			mapVO.setStopBooking(count>=1);
			map.put(dstr, mapVO);
		}
		return map;
	
	}

	protected Map<String, BookingTimeVO> getBookingTimeVO(List<BookingTimeVO> listBooking) {
		Map<String, BookingTimeVO> bMap=new LinkedHashMap<>();
		for (BookingTimeVO bookingTimeVO : listBooking) {
			bMap.put(bookingTimeVO.getConsultantWorkStartTime(), bookingTimeVO);
		}
		return bMap;
	}

	@Override
	public List<BookingTimeVO> listWorkBookingTime(Integer userId, String date) {
	
		return null;
	}
	
	protected List<BookingTimeVO> parseBookingTimeVOList(List<BookingTime> lVos){
		
		List<BookingTimeVO>  bTimeVOs= new ArrayList<BookingTimeVO>();
		// 统计可预约人数
		for (BookingTime bookingTime : lVos) {
			BookingTimeVO bookingTimeVO =new BookingTimeVO();
			bookingTimeVO.setDateStatus(bookingTime.getConsultantWorkDate());
			bookingTimeVO.setConsultantWorkStartTime(bookingTime.getConsultantWorkStartTime());
			bookingTimeVO.setConsultantWorkEndTime(bookingTime.getConsultantWorkEndTime());
			bookingTimeVO.setTimeType(bookingTime.getTimeType());
			bookingTimeVO.setSortTime(bookingTime.getSortTime());
			bTimeVOs.add(bookingTimeVO);
		}
		return bTimeVOs;

	}

	@Override
	public List<BookingTime> listWorkBookingTime(Integer userId) {
		List<BookingTime> list=new ArrayList<BookingTime>();
		Map<String,String> map=DateUtils.getTimetable(platformWorkingTime);
		for (Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
            BookingTime bookingTime =new BookingTime();
        	bookingTime.setConsultantId(userId);
			bookingTime.setSortTime(DateUtils.formatSecond(key));
			bookingTime.setCode(DateUtils.bookTimeCode(key));
			bookingTime.setConsultantWorkStartTime(key);
			bookingTime.setConsultantWorkEndTime(value);
			list.add(bookingTime);
		}
		return list;
	}

	@Override
	public Map<String, BookingTime> mapWorkBookingTime(Integer userId) {
		List<BookingTime> list=this.listWorkBookingTime(userId);
		Map<String, BookingTime> map=new LinkedHashMap<>();
		for (BookingTime bookingTime : list) {
			map.put(bookingTime.getConsultantWorkStartTime(), bookingTime);
		}
		
		return map;
	}

	@Override
	public int updateBookingStatusbuyUnKey(String unKey) {
		Example example =new Example(BookingTime.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("unKey",unKey);
		BookingTime bookingTime =new BookingTime();
		bookingTime.setTimeType(BookingTimeEnum.BOOKING.getValue());
		int result=bookingTimeMapper.updateByExampleSelective(bookingTime, example);
		return result;
	}

	@Override
	public int updateRecoveryStatusbuyUnKey(String unKey) {
		Example example =new Example(BookingTime.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("unKey",unKey);
		BookingTime bookingTime =new BookingTime();
		bookingTime.setTimeType(BookingTimeEnum.RESERVATIONS.getValue());
		int result=bookingTimeMapper.updateByExampleSelective(bookingTime, example);
		
		return result;
	}

	@Override
	public int deletetwoDaysAgo(Date date,Integer userId) {
		Example bookingExample =new Example(BookingTime.class);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date);
		c2.add(Calendar.DATE, -2);
		Date  twoDaysAgo= c2.getTime();
		bookingExample.createCriteria()
					  .andEqualTo("consultantId", userId)
					  .andLessThan("consultantWorkDate", twoDaysAgo);
		return bookingTimeMapper.deleteByExample(bookingExample);
	}



	
}
