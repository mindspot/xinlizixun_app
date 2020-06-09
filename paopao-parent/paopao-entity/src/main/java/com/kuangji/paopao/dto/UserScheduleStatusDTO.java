package com.kuangji.paopao.dto;

import java.util.List;

import lombok.Data;

//咨询师修改工作
@Data
public class UserScheduleStatusDTO {

	//设置的时间
	private  String date;
	
	
	private List<Body> body ;


	@Data
	public static class Body {
		
		
		//关联的工作室平台时间id  
		private  Integer  platformWorkingTimeId;

		
	    private String consultantWorkStartTime;

	    private String consultantWorkEndTime;
		
	    private Integer timeType;

		@Override
		public String toString() {
			return "Body [consultantWorkStartTime=" + consultantWorkStartTime + ", consultantWorkEndTime="
					+ consultantWorkEndTime + ", timeType=" + timeType + "]";
		}
	    
	    
	}
		
	
}
