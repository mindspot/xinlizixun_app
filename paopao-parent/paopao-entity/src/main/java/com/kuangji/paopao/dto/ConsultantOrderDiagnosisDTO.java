package com.kuangji.paopao.dto;

import java.util.List;

import com.kuangji.paopao.model.ConsultantOrderDiagnosis;

import lombok.Data;

@Data
public class ConsultantOrderDiagnosisDTO {

	private List<ConsultantOrderDiagnosis> list;
	
	private String  orderNo;

	@Override
	public String toString() {
		return "ConsultantOrderDiagnosisDTO [list=" + list + ", orderNo=" + orderNo + "]";
	}
	
	
	
}
