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
 * Created by Mybatis Generator 2020/04/12
 */
@Table(name = "user")
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 0 咨询师 1客户
     */
    private Integer type;

    /**
     * 账户
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    private String pwd;

    @Column(name = "real_name")
    private String realName;

    /**
     * 头像图片地址
     */
    @Column(name = "head_img")
    private String headImg;
    
    
    /**
     * 头像图片地址
     */
    @Column(name = "head_img_size")
    private String headImgSize;


    /**
     * 性别 0男 1女
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 限制登入 0审核中 1限制中  2正常
     */
    private Integer status;

    /**
     * 账户钱
     */
    private Integer account;

    /**
     *环信id 
     */
    @Column(name = "easemob_id")
    private String easemobId;
    
    /**
     * 提现金额
     */
    @Column(name = "cash_withdrawal_amount")
    private Integer cashWithdrawalAmount;

    @Column(name = "insert_time")
    private Date insertTime;

    @Column(name = "is_delete")
    private Integer isDelete;
    
    @Column(name = "first_login")
    private Integer firstLogin;
    

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", userName=").append(userName);
        sb.append(", pwd=").append(pwd);
        sb.append(", realName=").append(realName);
        sb.append(", headImg=").append(headImg);
        sb.append(", sex=").append(sex);
        sb.append(", age=").append(age);
        sb.append(", status=").append(status);
        sb.append(", account=").append(account);
        sb.append(", cashWithdrawalAmount=").append(cashWithdrawalAmount);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}