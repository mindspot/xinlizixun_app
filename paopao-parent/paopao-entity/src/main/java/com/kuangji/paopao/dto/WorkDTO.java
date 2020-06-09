package com.kuangji.paopao.dto;

import lombok.Data;

//入驻时候选择的 工作模式
@Data
public class WorkDTO {

	private Integer goodsId; 
	
	private Integer sellPrice=1;

	private Integer serviceTimes=1;

	private String serviceName;
	
	private Integer code =0;

	private String val;

}
