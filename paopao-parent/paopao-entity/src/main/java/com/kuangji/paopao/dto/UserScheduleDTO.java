package com.kuangji.paopao.dto;

import java.io.Serializable;

import lombok.Data;
/**
 * Author 金威正
 * Date  2020-02-27
 */
@Data
public class UserScheduleDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //platform_working_time 外键   系统规定的时间表
    private Integer platformWorkingTimeId;
    
    //工作开始时间
    private String scheduleStartTime;
    
    //工作结束时间
    private String scheduleEndTime;
    
    //0 可以预约  1已经被预约
    private int  timeType;

   
   
}