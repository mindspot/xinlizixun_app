package com.kuangji.paopao.dto;

import java.util.List;

import lombok.Data;

@Data
public class SettledInConsultantDTO {

	private String headImge = "";

	private String realName = "";

	private Integer sex = 0;

	private Integer age = 0;

	private String provName = "";

	private Integer provCode = 0;

	private String cityName="";

	private Integer cityCode=0;

	private String areaName="";
	
	private Integer areaCode=0;

	private String addrDetail="";
	
	private String weixing="";
	
	private String mailbox="";
	
	private String sendWord="";
	
	private String content="";
	
	private List<String> cards;

}
