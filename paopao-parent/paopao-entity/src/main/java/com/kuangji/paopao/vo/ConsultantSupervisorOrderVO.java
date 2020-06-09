package com.kuangji.paopao.vo;

import java.util.Set;

import lombok.Data;

//督导订单列表 模型  用于 咨询师 跟督导 之间聊天时候 用
@Data
public class ConsultantSupervisorOrderVO {
	
	//orderNo
	private String orderNo;
	
	private String createTime;
	
	private Integer consultantId;

	private String easemobId;
	
	private String headImg;
	
	private String headImgSize;
	
	private String realName;

	private Integer orderCode;
	
	private Integer payStatus;
	
	private Integer orderStatus;
	
	private String consultantTypes;

	private  Set<String>  labelVOs;
	
}
