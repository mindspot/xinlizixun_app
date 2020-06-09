package com.kuangji.paopao.mapper;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.SendSms;


public interface SendSmsMapper extends BaseMapper<SendSms> {

	
	int sumSendSmsCount(@Param("userId")Integer userId,@Param("date")String date);
}