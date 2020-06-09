package com.kuangji.paopao.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kuangji.paopao.model.BookingTime;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.BookingTimeVO;

/**
 * Author 金威正
 * Date  2020-02-27
 */
public interface BookingTimeService extends BaseService<BookingTime, Integer> {


	/**
	 * 真删除
	 * Author 金威正
	 * Date  2020-02-27
	 */
    int delete(List<Integer> ids);



	BookingTime getConsultantBookingTime(Integer userId, String start, String end);
	
	
	/**
	 * 客户端获取  预约世界 过滤已经预约的
	 * Author 金威正
	 * Date  2020-02-27
	 */
	
	Map<String, Object> listWorkBookingTime(int consultantId, String date);
	
	
	/**
	 * 咨询师端获取  预约世界 
	 * Author 金威正
	 * Date  2020-02-27
	 */
	Map<String, Object> listConsultantWorkBookingTime(Integer userId, String date);
	
	List<BookingTimeVO> listWorkBookingTime(Integer userId,String date);
	
	
	
	//获得0 - 24点的 booking
	List<BookingTime> listWorkBookingTime(Integer userId);
	
	//获得0 - 24点的 booking
	Map<String,BookingTime> mapWorkBookingTime(Integer userId);
	
	//当前订单修改为 已经预约状态
	int updateBookingStatusbuyUnKey(String unKey);
	
	//当前订单修改为 已经预约状态
	int updateRecoveryStatusbuyUnKey(String unKey);
	
	//删除当前的 系统时间 前2天数据 
	int deletetwoDaysAgo(Date date,Integer userId);
	
}
