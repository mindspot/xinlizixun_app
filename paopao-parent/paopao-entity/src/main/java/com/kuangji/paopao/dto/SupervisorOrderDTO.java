package com.kuangji.paopao.dto;

import java.util.List;

import lombok.Data;

//督导订单
@Data
public class SupervisorOrderDTO {
	
	private Integer proportion;
	
	private List<String> orderNos;
	
	private Integer  consultantId;
	
	
	
}
