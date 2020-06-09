package com.kuangji.paopao.vo;

import java.util.List;
import java.util.Set;

import lombok.Data;


//督导订单列表
@Data
public class SupervisorOrderVO {

	private String orderNo="";
	
	private Integer orderStatus=0;
	
	private String realName="";
	
	private String headImg="";
	
	private String headImgSize="";
	
	private Integer orderCode=0;
	
	private Set<String>  labelVOs;
	
	private List<ConsultantSupervisorOrderDetailsVO> listConsultantSupervisorOrderDetailsVO;
}
