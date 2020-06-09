package com.kuangji.paopao.mapper;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.ConsultantSchedule;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface ConsultantScheduleMapper extends BaseMapper<ConsultantSchedule> {
	/**
	 * 根据用户id 跟时间段id 查询
	 * Author 金威正
	 * Date  2020-02-27
	 */
     ConsultantSchedule getConsultantScheduleTime(@Param("consultantId")Integer consultantId,@Param("platformWorkingTimeId") Integer platformWorkingTimeId);
    
    
     /**
      * 根据用户id 删除
      * Author 金威正
      * Date  2020-02-27
      */
     
     int   deleteByConsultantId(@Param("consultantId")Integer userId);
     
     
     
   
}