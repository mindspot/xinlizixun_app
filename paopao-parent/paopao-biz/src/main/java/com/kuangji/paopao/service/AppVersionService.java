package com.kuangji.paopao.service;

import com.kuangji.paopao.model.AppVersion;
import com.kuangji.paopao.service.base.BaseService;

/**
 * Author 金威正 Date 2020-02-17
 */
public interface AppVersionService extends BaseService<AppVersion, Integer>{

	//根据appName 找到最新版本
	AppVersion getNewestApp(String appName);
	
	
}
