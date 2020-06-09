package com.kuangji.paopao.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuangji.paopao.model.User;

import lombok.Data;

@Data
public class VisitorVO extends User{

	private static final long serialVersionUID = 7324665433501817252L;

	private Integer userId;
	
	@JsonFormat(pattern = "MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date visitorTime;
	
	//1 下个单  0没下过单
	private Integer isOrder;
}
