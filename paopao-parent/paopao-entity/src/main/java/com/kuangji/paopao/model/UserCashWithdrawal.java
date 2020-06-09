package com.kuangji.paopao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
* Created by Mybatis Generator 2020/03/25
*/
@Table(name = "user_cash_withdrawal")
@Data
public class UserCashWithdrawal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_card_id")
    private Integer userCardId;

    private Integer amount;

    //当时提现账户
    @Column(name = "pay_account")
    private String payAccount;
    
    //当时的账户金额
    @Column(name = "account_now")
    private Integer accountNow;
    
    //当时的冻结金额
    @Column(name = "cash_withdrawal_amount_now")
    private Integer cashWithdrawalAmountNow;
    
    @Column(name = "cash_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cashTime;

    @Column(name = "insert_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Column(name = "cash_type")
    private Integer cardType;
    
    @Column(name = "real_name")
    private String realName;
    
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Column(name = "is_delete")
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userCardId=").append(userCardId);
        sb.append(", amount=").append(amount);
        sb.append(", cashTime=").append(cashTime);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
    

	public UserCashWithdrawal() {
		super();
	}


	public UserCashWithdrawal( Integer userId, Integer userCardId, Integer amount, Date cashTime) {
		this.userId = userId;
		this.userCardId = userCardId;
		this.amount = amount;
		this.cashTime = cashTime;
	}


	public UserCashWithdrawal(Integer userId) {
		super();
		this.userId = userId;
	}


	public UserCashWithdrawal(Integer userId, Integer userCardId, Integer cardType) {
		super();
		this.userId = userId;
		this.userCardId = userCardId;
		this.cardType = cardType;
	}

	
	
	
    
    
}