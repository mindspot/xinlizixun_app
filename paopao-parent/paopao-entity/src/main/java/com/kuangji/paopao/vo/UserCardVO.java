package com.kuangji.paopao.vo;

import java.io.Serializable;

import com.kuangji.paopao.model.UserCard;

import lombok.Data;

/**
* Created by Mybatis Generator 2020/03/25
*/
@Data
public class UserCardVO extends UserCard implements Serializable {
	
	private static final long serialVersionUID = 1034227303349333026L;
   
	//true 可以提现 false 不可以提现
	private boolean  cashWithdrawal;
    
	
}