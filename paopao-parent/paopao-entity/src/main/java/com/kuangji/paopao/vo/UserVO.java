package com.kuangji.paopao.vo;

import com.kuangji.paopao.model.User;

import lombok.Data;

@Data
public class UserVO extends User {
	
	private static final long serialVersionUID = 1L;
	
	private Integer consultantStatus;
}
