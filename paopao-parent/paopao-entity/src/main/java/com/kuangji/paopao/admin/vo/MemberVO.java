package com.kuangji.paopao.admin.vo;

import lombok.Data;

@Data
public class MemberVO {

	private Integer id;

	private String userName;

	private String pwd;

	private String realName;

	private String headImg;

	private Integer sex;

	private Integer age;

	/**
     * 婚姻状态 0未婚 1已婚
     */
    private Integer maritalStatus;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 简介
     */
    private String sendWord;

    private Integer status ;

	private Integer isDelete ;
	
	private String  insertTime;

}
