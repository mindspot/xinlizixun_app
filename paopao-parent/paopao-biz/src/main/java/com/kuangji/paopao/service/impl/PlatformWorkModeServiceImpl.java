package com.kuangji.paopao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.PlatformWorkModeMapper;
import com.kuangji.paopao.model.PlatformWorkMode;
import com.kuangji.paopao.service.PlatformWorkModeService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

/**
 * Author 金威正 Date 2020-02-17
 */
@Service
public class PlatformWorkModeServiceImpl extends BaseServiceImpl<PlatformWorkMode, Integer> implements   PlatformWorkModeService {

	@Autowired
	private PlatformWorkModeMapper workMapper;
	
	@Override
	public List<PlatformWorkMode> listWork(List<Integer> arr) {
		// TODO Auto-generated method stub
		return workMapper.listWork(arr);
	}

	@Override
	public BaseMapper<PlatformWorkMode> getMapper() {
		// TODO Auto-generated method stub
		return workMapper;
	}
	

}
