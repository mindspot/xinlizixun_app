package com.kuangji.paopao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
* Created by Mybatis Generator 2020/03/25
*/
@Table(name = "user_card")
@Data
public class UserCard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 账户
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 支付宝
     */
    @Column(name = "pay_account")
    private String alipayAccount;
    

    @Column(name = "insert_time")
    private Date insertTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
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
        sb.append(", userId=").append(userId);
        sb.append(", realName=").append(realName);
        sb.append(", alipayAccount=").append(alipayAccount);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public UserCard(Integer userId, String realName, String alipayAccount) {
		super();
		this.userId = userId;
		this.realName = realName;
		this.alipayAccount = alipayAccount;
	}
    
	
	
	public UserCard() {
	
	}

	public UserCard(Integer userId, Integer isDelete) {
		super();
		this.userId = userId;
		this.isDelete = isDelete;
	}

	public UserCard(Integer id, Integer userId, String realName, String alipayAccount) {
		super();
		this.id = id;
		this.userId = userId;
		this.realName = realName;
		this.alipayAccount = alipayAccount;
	}

	
    
	
}