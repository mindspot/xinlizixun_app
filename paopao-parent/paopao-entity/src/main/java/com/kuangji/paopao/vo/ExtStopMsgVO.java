package com.kuangji.paopao.vo;

import lombok.Data;

//服务让停止消息
@Data
public class ExtStopMsgVO {

	private String nickname;
	
	private String userphoto;
	
	private String serviceType="语音";
	
	private String serviceTime;
	
	private String serviceOrderid;
	
	//内容
	private String content;
	
	//2 即将停止消息 不关闭服务
	//1 发送之后服务立即停止 
	private String code="1";
}
