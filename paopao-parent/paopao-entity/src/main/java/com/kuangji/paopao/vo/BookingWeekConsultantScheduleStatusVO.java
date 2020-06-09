package com.kuangji.paopao.vo;

import lombok.Data;

//查看 当前日期 七天后的时间(包含今天) 用于咨询师列表筛选
@Data
public class BookingWeekConsultantScheduleStatusVO {
	
	private Integer  consultantId;
	
	
	private String  scheduleStartTime;
	
}
