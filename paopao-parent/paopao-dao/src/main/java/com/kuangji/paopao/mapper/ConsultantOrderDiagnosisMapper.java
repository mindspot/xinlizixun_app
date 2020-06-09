package com.kuangji.paopao.mapper;

import com.kuangji.paopao.admin.vo.OrderDiagnosisVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.ConsultantOrderDiagnosis;

/**
* Created by Mybatis Generator 2020/03/23
*/
public interface ConsultantOrderDiagnosisMapper extends BaseMapper<ConsultantOrderDiagnosis> {
	
	
	OrderDiagnosisVO	getCaseDiagnosis(String orderNo);
}