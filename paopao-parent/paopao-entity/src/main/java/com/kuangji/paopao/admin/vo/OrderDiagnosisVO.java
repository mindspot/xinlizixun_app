package com.kuangji.paopao.admin.vo;

import java.util.List;

import lombok.Data;

@Data
public class OrderDiagnosisVO {

	
	
	private String  orderNo="";
	
	private String  updateTime="";
	
	private List<DiagnosisVO> diagnosisVOs;
	
	private  DiagnosisVO diagnosisVO;
	
	@Data
	public static class DiagnosisVO{
		
		private String content;
		
		private Integer  contentType;
	}
}
