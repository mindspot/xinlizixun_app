package com.kuangji.paopao.admin.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * Author 金威正
 * Date  2020-02-20
 */
@Data
public class MallTradeOrderVO implements Serializable {
  
	private static final long serialVersionUID = -1178622254858986473L;

	private long id;
   
    private String orderNo;
   
    private String orderTime;
  
    private int payType;
    
    private int orderAmount;
    
    private String ext;

    private int  consultantId;
    
    private String consultantRealName;
    
    private  String buyerRealName;
    
    private int  buyerId;

    private int goodsClass;
    
    private String goodsClassName;
    
    private String goodsTime;

    private int  payStatus;
    
    private int  discountAmount;
    
    private int goodsAmount;
    
    private String goodsName;
    
    private int   stock;
    
    private int  orderStatus;
    
    private int  orderType;
    
}