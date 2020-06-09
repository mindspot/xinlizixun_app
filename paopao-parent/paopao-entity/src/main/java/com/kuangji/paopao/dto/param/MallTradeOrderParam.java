package com.kuangji.paopao.dto.param;

import java.util.List;

import com.kuangji.paopao.dto.common.PageParam;
import com.kuangji.paopao.enums.MallGoodsClassEnum;

import lombok.Data;

@Data
public class MallTradeOrderParam extends PageParam {
	
	private String consultantName;
	
	private String orderNo;

	private String buyerName;
	
	private String userName;
	
	private String consultantUserName;
	
	private Integer  goodsClass=MallGoodsClassEnum.CONSULTANT_SERVICE.getValue();
	
	private String  orderTime;
	
	private Integer orderStatus;
	
	
	private List<Integer> userIds;

}
