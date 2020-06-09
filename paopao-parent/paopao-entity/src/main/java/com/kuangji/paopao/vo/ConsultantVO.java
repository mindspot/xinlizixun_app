package com.kuangji.paopao.vo;

import java.util.List;

//咨询列表显示多条数据 模式
import lombok.Data;
@Data
public class ConsultantVO {
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
	    
	    private int  consultationFee;
	    
	    private String  easemobId;
	    
	    private List<ConsultantLabelVO> consultantLabelVOs;
	    
	    private String sendWord="";
	    
	    private String displayContent="";
	    
	    private String  title="";
	    
	    private int  workingSeniority=0;
	    
	    private int accumulativeCase=0;
}
