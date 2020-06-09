package com.kuangji.paopao.vo;

import java.util.List;

import com.kuangji.paopao.model.Common;

import lombok.Data;

@Data
public class CustomeServiceVO {
	//咨询师客服
	private  List<Common> consultantCustomers;
	//用户客服
	private  List<Common> userCustomers;
	
	//咨询师在线客服
	private  List<Common> consultantCustomersNow;
	
	//用户在线客服
	private  List<Common> userCustomersNow;
	
	//客服服务时间段
	private  Common  customerServicePeriod;
}
