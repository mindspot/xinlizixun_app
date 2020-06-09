package com.kuangji.paopao.service;

import com.kuangji.paopao.model.SendSms;
import com.kuangji.paopao.service.base.BaseService;

/**
 * Author 金威正
 * Date  2020-02-16
 */
public interface SendSmsService extends BaseService<SendSms, Integer>{


	int  sendSms(Integer userId,String shopId);
}