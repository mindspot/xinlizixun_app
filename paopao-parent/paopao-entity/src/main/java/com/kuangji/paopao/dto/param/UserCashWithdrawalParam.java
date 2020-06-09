package com.kuangji.paopao.dto.param;

import com.kuangji.paopao.dto.common.PageParam;

import lombok.Data;

@Data
public class UserCashWithdrawalParam extends PageParam {
	
    private String  userName;
    
    private Integer cashType;
    
    private String alipayAccount;
   
    private String  cashTime;
    
    private Integer type;
}
