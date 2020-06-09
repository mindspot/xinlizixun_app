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
* Created by Mybatis Generator 2020/03/14
*/
@Table(name = "dispatch")
@Data
public class Dispatch implements Serializable {
  private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 通道
     */
    @Column(name = "channel")
    private String channel;
   
    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;
    
    
    /**
     * 性别
     */
    @Column(name = "sex")
    private String sex;
    
    
    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;
    
    
    /**
     *手机号
     */
    @Column(name = "phone")
    private String phone;

    
    @Column(name = "secret_number")
    private String secretNumber ;   
    
    
    @Column(name = "wx")
    private String wx;

    @Column(name = "qq")
    private String qq;
    
    
    @Column(name = "user_id")
    private Integer userId ;   
    
    
    @Column(name = "number_of_contacts")
    private Integer numberOfContacts ;   
    
    @Column(name = "consultant_user_id")
    private Integer consultantUserId ;   
    
    @Column(name = "import_date")
    private Integer importDate ; 
    
    /**
     * 删除标记
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 插入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "insert_time")
    private Date insertTime;
    
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private Date updateTime;
 
    
}