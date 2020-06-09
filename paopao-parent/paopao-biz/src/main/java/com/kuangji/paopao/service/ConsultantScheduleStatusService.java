package com.kuangji.paopao.service;

import com.kuangji.paopao.dto.UserScheduleStatusDTO;
import com.kuangji.paopao.model.ConsultantScheduleStatus;
import com.kuangji.paopao.service.base.BaseService;

/**
 * Author 金威正
 * Date  2020-02-28
 */
public interface ConsultantScheduleStatusService  extends BaseService<ConsultantScheduleStatus, Integer>{

   
    
	/**
	 * 咨询师端 操作休息 或者 被预约
	 * Date  2020-02-28
	 */
     boolean insertConsultantScheduleStatus(Integer userId,UserScheduleStatusDTO userScheduleStatusDTO);
     
    
	/**
	 * 根据子咨询师id 开始时间 结束时间 看是不是已经有数据了
	 * Author 金威正
	 * Date  2020-02-28
	 */
     boolean getConsultantScheduleStatus(Integer userId,String date,String start,String end);
    
    
    
	/**
	 * 咨询师端 操作休息 或者 被预约
	 * Date  2020-02-28
	 */
     boolean insertConsultantScheduleStatus(Integer userId,String date,Integer timeType);
     
    

}
