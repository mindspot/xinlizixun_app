package com.kuangji.paopao.admin.vo;

import lombok.Data;

@Data
public class SetMealInfoVO {
	
    private String orderNo;
    
    private Integer orderType;
    
    private Integer orderFrom;
    
    private String operName;
    
    private String orderTime;
    
    private String  consultantRealName;
    
    private String  memberRealName;
    
    private Integer payType;
    
    private Integer payStatus;
    
    private String payTime;
    
    private Integer orderStatus;
    
    private Integer goodsAmount;
    
    private Integer discountAmount;
    
    private Integer orderAmount;
    
    private String outTradeNo;
    
    private String  orderDetail;
   
    private String  ext;
    
    private String  detailedDescription;
    
    private Integer  consultatioNumber;
    
    private String consultantType;
    
    private Integer isConsultant ;
    
    private Integer  sex;
    
}