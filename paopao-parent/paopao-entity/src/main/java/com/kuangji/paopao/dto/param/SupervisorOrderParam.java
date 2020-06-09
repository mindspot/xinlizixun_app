package com.kuangji.paopao.dto.param;

import java.util.List;

import com.kuangji.paopao.dto.common.PageParam;

import lombok.Data;

@Data
public class SupervisorOrderParam extends PageParam {
	
	private String userName;
	
	private String consultantUserName;
	
	
	private String orderTime;
	
	
	private List<Integer> userIds;
}
