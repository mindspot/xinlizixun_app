package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.admin.vo.ConsultantServiceVO;
import com.kuangji.paopao.dto.param.ConsultantModelServiceParam;
import com.kuangji.paopao.model.ConsultantModelService;
import com.kuangji.paopao.service.base.BaseService;

/**
 * 咨询师审核的服务
 * Author 金威正
 * Date  2020-02-20
 */
public interface ConsultantServiceService extends BaseService<ConsultantModelService, Integer>{

	List<ConsultantServiceVO> listConsultantService(ConsultantModelServiceParam consultantModelServiceParam);
	
	List<ConsultantModelService> listConsultantService(Integer shopId);
	
	int addConsultantService(Integer shopId);

	int deleteConsultantService(Integer shopId);

	int countConsultantService(Integer shopId);
}
