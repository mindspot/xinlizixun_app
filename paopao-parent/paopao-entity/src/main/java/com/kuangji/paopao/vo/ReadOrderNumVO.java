package com.kuangji.paopao.vo;

import lombok.Data;

@Data
public class ReadOrderNumVO {

	//普通订单消息数量
	private  int  unConsultationOrderNum;
	//我发起的督导订单数量
	private  int  unConsultantSupervisorOrderNum;
	//我收到的督导订单 消息数量
	private  int unSupervisorOrderNum;
	
}
