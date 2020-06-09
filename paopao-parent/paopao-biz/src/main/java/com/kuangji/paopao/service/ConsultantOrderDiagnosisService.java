package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.admin.vo.OrderDiagnosisVO.DiagnosisVO;
import com.kuangji.paopao.dto.ConsultantOrderDiagnosisDTO;
import com.kuangji.paopao.model.ConsultantOrderDiagnosis;
import com.kuangji.paopao.service.base.BaseService;

public interface ConsultantOrderDiagnosisService extends BaseService<ConsultantOrderDiagnosis, Integer>{

	
	 void updateMemberCaseDiagnosis(ConsultantOrderDiagnosisDTO dto);

	 List<DiagnosisVO> getCaseDiagnosis(String  orderNo);
	 
	 
	 DiagnosisVO getCaseDiagnos(String  orderNo);
}
