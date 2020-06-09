package com.kuangji.paopao.admin.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ConsultantVO {

	private Integer id;

	private String userName;

	private String pwd;

	private String realName;

	private String headImg;

	private Integer sex;

	private Integer age;
	
	private Integer status;

	private Integer account;

	private Integer cashWithdrawalAmount;

	private Integer userId;

	private String provName;

	
	private Integer provCode;

	
	private String cityName;


	private Integer cityCode;

	
	private String areaName;


	private Integer areaCode;

	
	private String addrDetail;

	/**
	 * 微信号
	 */
	private String weixing;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String mailbox;

	private String sendWord;

	private Integer briefIntroductionId;

	private Integer consultationFee;

	private Integer userType;

	private Integer sort;

	private String invitationCode;

	private String supervisorName;
	
	private Date insertTime;

	private Date updateTime;

	private Integer isDelete ;
	

}
