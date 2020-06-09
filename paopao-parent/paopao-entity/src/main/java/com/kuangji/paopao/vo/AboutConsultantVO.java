package com.kuangji.paopao.vo;

import java.util.List;

import lombok.Data;

//咨询师显示的页面简介
@Data
public class AboutConsultantVO {
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
    
    private String content="";
    
    private String easemobId="";
    
    private String userName="";
    //寄语
    private String sendWord="";
    //工作 领域标签
    List<WorkVO> workVOs;
    
	
  
}
