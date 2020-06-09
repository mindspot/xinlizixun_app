package com.kuangji.paopao.dto.param;

import java.util.List;

import com.kuangji.paopao.dto.common.PageParam;

import lombok.Data;

@Data
public class ConsultantParam extends PageParam {
	
	private String userName;
	
	private String realName;
	
	private Integer userType;
	
	private String invitationCode;
	
	private List<Integer> userIds;
	
	private Integer status;
}
