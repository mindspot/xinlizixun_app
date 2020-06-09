package com.kuangji.paopao.dto.param;

import com.kuangji.paopao.dto.common.PageParam;

import lombok.Data;

@Data
public class PointHistoryParam extends PageParam {
	
	private Integer userId;
	
	
	private Integer type;
}
