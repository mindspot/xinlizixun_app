package com.kuangji.paopao.vo;

import lombok.Data;

//咨询师个人页面显示 配合 map组装数据
@Data
public class IndividualConsultantVO {
	private int id;
    private String realName="";
    private String headImg="";
    private String headImgSize="";
    private String provName="";
    private int provCode;
    private String cityName="";
    private int cityCode;
    private String areaName="";
    private int areaCode;
    private String addrDetail="";
    private String  easemobId;
    //寄语
    private String sendWord="";
}
