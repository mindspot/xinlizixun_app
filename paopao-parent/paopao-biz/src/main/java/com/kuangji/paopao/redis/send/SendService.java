package com.kuangji.paopao.redis.send;

public interface SendService {

	//客户登入发送验证码
	 String  sendMsg(String phone);
	
	//咨询师端登入发送验证码
	 String  sendConsultationWorkMsg(String phone);
	 
	//发送修改消息
	 String  updatePwdSendMsg(String token);
	 
	//发送提现消息
	String  sendCashWithdrawalMsg(String phone);
	
	//咨询师入驻注册发送验证码
	String  sendsettledInLoginMsg(String phone);
	
	//提前30分钟发送 短信给咨询师
	String  sendStartOrder(String orderNo);

	//支付成功后发送短信给咨询师 
	String sendConsultantOrder(String orderNo);

	//咨询师确定
	String sendConsultantDetermineOrder(String orderNo);
	
	//超时取消订单
	String sendConsultantOvertimeOrder(String orderNo);
	
	//新订单
	String sendNewOrder(String orderNo);
	
	//立即发送的短信
	String startimmediatelyOrderContent(String orderNo);
	
	//根据手机号发送消息 
	String  sendMsg(String phone,String content);
	
}
