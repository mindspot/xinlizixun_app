package com.kuangji.paopao.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * Author 金威正
 * Date  2020-02-20
 */
@Data
public class ConsultantWorkMemberMallTradeOrderVO implements Serializable {
  
	private static final long serialVersionUID = -1178622254858986473L;

	private long id;
    
    
    private String realName;

    private String headImg;
        
    private String goodsClassName;
 
    private String  orderNo;
   
    private String  ext;
    
    private String  orderTime;
    
    private Integer  orderCode;
    
    private Integer orderStatus;
    
    private Integer  orderAmount;
 
}