package com.kuangji.paopao.dto;



import lombok.Data;


/*
 * 咨询师筛选入口
 * 
 * */
@Data
public class ConsultationDTO {
	
	//性别
	private Integer[] sex;
	
	//最小金额
	Integer minPrice;
	
	//最大金额
	Integer maxPrice;
	
	//年龄段
	Integer[] consultationAge;
	
	//分类 id
	Integer[] classification;
	
	//工作时间段
	Integer[] consultationTime;
	
	//省
	private String provName;
	
	//城市编码
    private int provCode;
    
    //城市名称
    private String cityName;
    
    //城市码
    private int cityCode;
    
    //区名称
    private String areaName;
    
    //区码
    private int areaCode;

    //分页 当前页
    private int pageNum;


    //当前咨询师id
    private Integer consultantId;
}
