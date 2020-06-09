package com.kuangji.paopao.admin.vo;

import java.util.List;

import lombok.Data;

@Data
public class ConsultantInfoVO {

	private Integer id;

	private String userName;

	private String realName;

	private String headImg;

	private Integer sex;

	private Integer age;
	
	private Integer account;
	
	private Integer cashWithdrawalAmount;
	
	private String provName;

	
	private Integer provCode;

	
	private String cityName;


	private Integer cityCode;

	
	private String areaName;


	private Integer areaCode;

	
	private String addrDetail;
	
	private String weixing;

	private String phone;

	private String mailbox;

	private String sendWord;

	private Integer status;
	

	private Integer userId;


	private Integer briefIntroductionId;
	
	private String content;

	private Integer consultationFee;

	private Integer userType;

	private Integer sort;

	private String invitationCode;

	private List<Certification> certifications;
	
	private List<IdCard> idCards;
	
	@Data
	public static class Certification {
		
		private Integer certificationId;
		
		private Integer  certificateType;
		
		private String certificateTypeName;
		
		private String certificateNo;
		
		private String  certificateAge;
		
		private String  certificateUrl;
		
		private Integer status;
		
		private String remark;
		
	}
	@Data
	public static class IdCard {
		
		private String imgUrl;
		
		
		private Integer imgType;
	}
	
}
