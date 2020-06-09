package com.kuangji.paopao.dto.param;

import com.kuangji.paopao.dto.common.PageParam;

import lombok.Data;

@Data
public class UserParam extends PageParam {
	
	private String userName;
	
	private String realName;
}
