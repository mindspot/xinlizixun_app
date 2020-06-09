package com.kuangji.paopao.dto;

import com.kuangji.paopao.enums.UserImageTypeEnum;

import lombok.Data;

@Data
public class EducationDTO {

	private String infoName;
	
	
	private String infoValue;
	
	private Integer imgType=UserImageTypeEnum.DIPLOMA.getIndex();
	
	
	private String  imgUrl;

}
