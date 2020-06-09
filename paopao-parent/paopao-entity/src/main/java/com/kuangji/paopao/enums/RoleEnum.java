package com.kuangji.paopao.enums;

import lombok.Getter;
public enum RoleEnum {
	
	 CONSULTANT(0,"咨询师"),
	 SUPERVISOR(1,"督导师"),
	 ADMIN(2,"管理员"),
	 CUSTOMER(3,"客服"), 
	 ARTICLE(4,"文章维护");
    @Getter
	private Integer type;
    @Getter
	private String desc;
	
	private RoleEnum(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	
}
