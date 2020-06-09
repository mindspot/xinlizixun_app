package com.kuangji.paopao.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * Author 金威正
 * Date  2020-02-27
 */
@Data
public class BookingTimeVO implements Serializable {
    private static final long serialVersionUID = 1L;
   
    private String consultantWorkStartTime;
    
    private String consultantWorkEndTime;
    
    private int timeType;
    
    
    private String dateStatus="";

    private long sortTime;
    
    
	private int platformWorkingTimeId;




	public BookingTimeVO() {
		super();
	}


	@Override
	public String toString() {
		return "BookingTimeVO [consultantWorkStartTime=" + consultantWorkStartTime + ", consultantWorkEndTime="
				+ consultantWorkEndTime + ", timeType=" + timeType + ", dateStatus=" + dateStatus + ", sortTime="
				+ sortTime + "]";
	}


	public BookingTimeVO(String consultantWorkStartTime, String consultantWorkEndTime, int timeType, String dateStatus,
			long sortTime) {
		super();
		this.consultantWorkStartTime = consultantWorkStartTime;
		this.consultantWorkEndTime = consultantWorkEndTime;
		this.timeType = timeType;
		this.dateStatus = dateStatus;
		this.sortTime = sortTime;
	}



    
    
}