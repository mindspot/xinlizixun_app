package com.kuangji.paopao.dto.param;

import java.util.List;

import com.kuangji.paopao.dto.common.PageParam;

import lombok.Data;

@Data
public class ConsultantModelServiceParam extends PageParam {

	
	private Integer userId;
	
	
	private List<Integer> userIds;
}
