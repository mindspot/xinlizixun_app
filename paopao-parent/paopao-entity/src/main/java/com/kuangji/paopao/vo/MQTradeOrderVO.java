package com.kuangji.paopao.vo;

import lombok.Data;

@Data
public class MQTradeOrderVO {

	private String orderNo;
	
	
	private Integer orderStatus;
	
	private Integer shopId;
	
	private Integer buyerId;

    private String ext;
    
    private Integer goodsId;
    
 
	private Integer buyCount;
	
	private Integer  couponId;

	private Integer  expires;

	private String  sellPointText;
	
}
