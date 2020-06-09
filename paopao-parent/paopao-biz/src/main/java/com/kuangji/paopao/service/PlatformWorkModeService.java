package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.model.PlatformWorkMode;
import com.kuangji.paopao.service.base.BaseService;

/**
 * 工作模式
 * Author 金威正 Date 2020-02-17
 */
public interface PlatformWorkModeService extends BaseService<PlatformWorkMode, Integer> {

	 /**
	 * 根据id查询出 工作模式
	 * Author 金威正
	 * Date  2019-12-12
	 */
	public List<PlatformWorkMode> listWork(List<Integer> arr);

	    
    
}
