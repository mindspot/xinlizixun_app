package com.kuangji.paopao.mapper;

import java.util.List;

import com.kuangji.paopao.admin.vo.ConsultantServiceVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.ConsultantModelServiceParam;
import com.kuangji.paopao.model.ConsultantModelService;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface ConsultantServiceMapper extends BaseMapper<ConsultantModelService> {
	
	 List<ConsultantServiceVO> listConsultantService(ConsultantModelServiceParam consultantModelServiceParam);
	 
	 
	 
	 
}