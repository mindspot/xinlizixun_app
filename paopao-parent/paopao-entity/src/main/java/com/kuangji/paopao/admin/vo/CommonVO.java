package com.kuangji.paopao.admin.vo;

import java.util.List;

import com.kuangji.paopao.vo.BookingTimeVO;

import lombok.Data;

@Data
public class CommonVO {

	private  List<CommonLable> classInfos;
	
	private  List<CommonLable> contentions;
	
	private  List<BookingTimeVO>  bookingTimes;
	
	private  List<WorkVO> workVOs;
	
	private  int workTime= 3000/60;
	
	private  String workTimeStr= "1次/"+workTime;
	
	private Integer[] frequencys={3,6,9,12,15,18};
	
	@Data
	public static class CommonLable {
		
		private Integer id;
		
		private String val;
		
		private Integer  labelId;
		
		private Integer  type;
	}
	
	@Data
	public static class WorkVO {
		
		private Integer code;
		
		private String val;
		
		private Integer sellPrice;
		
		private Integer serviceTimes;
	
	}
}
