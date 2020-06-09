package com.kuangji.paopao.vo;

import lombok.Data;

//订单确定消息 
@Data
public class ExtVO {

	private String nickname;
	
	private String userphoto;
	
	//1 true  0 false
	//0 文字               1 按钮  
	private String msgType ="0";
	
	private String serviceType="语/视/面";
	
	private String serviceTime;
	
	private String serviceOrderid;
	
	//0 普通咨询订单  1督导订单
	private int orderType;
	
}
