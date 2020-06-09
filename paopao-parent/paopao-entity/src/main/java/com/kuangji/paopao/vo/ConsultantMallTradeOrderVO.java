package com.kuangji.paopao.vo;

import java.io.Serializable;

import lombok.Data;

//咨询师端查询的订单信息
@Data
public class ConsultantMallTradeOrderVO  implements Serializable{
	
	private static final long serialVersionUID = -5823787807081207835L;

	private long id;
   
    private String orderNo;
   
    private String orderTime;
  
    private int orderAmount;
    
    private String ext;

    private int  memberId;
    
    private String realName;

    private String headImg;
    
    private String headImgSize;
  
    private int goodsClass;
    
    private String goodsClassName;
    
    private String goodsTime;

    private int  payStatus;
    
    private int  discountAmount;
    
    private int   stock;
    
    private int  orderStatus;
    
    //1待支付  2 已经取消   3 已支付 4已完成 5已确认
    private int  orderCode;

    private String  easemobId;
    
    private int  memberCaseId;
    
    
}
