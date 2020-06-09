package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.dto.UserScheduleStatusDTO;
import com.kuangji.paopao.dto.UserScheduleStatusDTO.Body;
import com.kuangji.paopao.model.BookingTime;
import com.kuangji.paopao.model.ConsultantSchedule;
import com.kuangji.paopao.service.base.BaseService;

/**
 * Author 金威正
 * Date  2020-02-27
 */
public interface ConsultantScheduleService extends BaseService<ConsultantSchedule, Integer> {

	
	/**
	 * 批量插入
	 * Author 金威正
	 * Date  2020-02-27
	 */
    public int insertBatch(Integer userId,UserScheduleStatusDTO userScheduleStatusDTO);

    //转换
	List<BookingTime> listBookTime(Integer userId,List<Body> bodys);
	
	
    //获取自己定义的排班表
	List<BookingTime> listBookTime(Integer userId);

}
