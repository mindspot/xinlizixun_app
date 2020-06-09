package com.kuangji.paopao.api;

import java.util.Map;

public interface ConsultationApi {

	
	//咨询页面聚合数据
	 Map<String, Object> listConsultationLabel(String token);

	//个人页面聚合数据
	 Map<String, Object> getConsultation(Integer userId,Integer shopId,Integer serviceCode);
	
	
	//填写咨询资料时候的数据
	 Map<String, Object> getConsultationMaterials(String token);

	//获取支付type  详细说明
	 Map<String, Object> getPayTypeDetailedDescription(String token);
	
	
	//咨询师端  咨询师个人页面
	 Map<String, Object> getConsultant(String token);

	
}
