package com.kuangji.paopao.admin.vo;

import lombok.Data;

@Data
public class UserCashWithdrawalVO {

	
	 	private Integer id;
	   
	    private Integer cashType;
	    
	    private Integer cardType;

	    private Integer amount;
	  
	    private String userId;
	    
	    private String userName;
	    
	    private String realName;

	    private String payAccount;
	    
	    private String cashTime;
	    
	    private String  cardRealName;
	    
	    private Integer type;
	    
	    private Integer  accountNow;
	    
	    private Integer  cashWithdrawalAmountNow;
	    
}
