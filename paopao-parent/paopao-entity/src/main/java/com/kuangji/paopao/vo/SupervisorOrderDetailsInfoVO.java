package com.kuangji.paopao.vo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class SupervisorOrderDetailsInfoVO {

	
	private String  orderNo;
	
	private String  createTime;

	private String  orderTime;
	
	private int  workType;
	
	private String  goodsName;
	
	private int goodsAmount;
	
	private String  coupon;
	
	private  int  orderAmount;
	
	private  int orderStatus;
	
	private  int orderCode;
	
	private  int proportion;
	
	private  Set<String>  labels;
	
	private  int supervisorOrderNum;

	private  int consultantId;
	
	private  String realName;
	
	private  String easemobId;
	
	private String headImg;
	
	private String headImgSize;
	
	private List<Map<String, Object>> listSupervisorOrderDetails;
}
