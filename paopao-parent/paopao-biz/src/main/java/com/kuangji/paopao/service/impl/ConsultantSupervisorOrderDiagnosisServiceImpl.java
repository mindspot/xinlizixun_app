package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.mapper.ConsultantSupervisorOrderDiagnosisMapper;
import com.kuangji.paopao.model.ConsultantSupervisorOrderDiagnosis;
import com.kuangji.paopao.service.ConsultantSupervisorOrderDiagnosisService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@Service
public class ConsultantSupervisorOrderDiagnosisServiceImpl extends BaseServiceImpl<ConsultantSupervisorOrderDiagnosis, Integer> implements ConsultantSupervisorOrderDiagnosisService{

	@Autowired
	private ConsultantSupervisorOrderDiagnosisMapper consultantSupervisorOrderDiagnosisMapper;
	@Override
	public BaseMapper<ConsultantSupervisorOrderDiagnosis> getMapper() {
		// TODO Auto-generated method stub
		return consultantSupervisorOrderDiagnosisMapper;
	}
    
   
}
