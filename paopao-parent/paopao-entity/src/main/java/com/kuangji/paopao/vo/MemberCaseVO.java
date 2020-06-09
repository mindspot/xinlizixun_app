package com.kuangji.paopao.vo;

import java.io.Serializable;
import java.util.List;

import com.kuangji.paopao.model.MemberCase;

import lombok.Data;



@Data
public class MemberCaseVO extends MemberCase implements Serializable {
	

	private static final long serialVersionUID = 6492782000275227975L;


	private String consultationTime="";
	
	
	private String consultatName="";

	
	private List<CommonVO> commonVOs;
	
	
	private ConsultantOrderDiagnosisVO  consultantOrderDiagnosisVO;
	
	private List<ConsultantOrderDiagnosisVO> consultantOrderDiagnosisVOs;
 	
}