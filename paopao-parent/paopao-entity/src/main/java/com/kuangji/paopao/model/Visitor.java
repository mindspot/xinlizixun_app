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
* Created by Mybatis Generator 2020/03/14
*/
@Table(name = "visitor")
@Data
public class Visitor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "shop_id")
    private Integer shopId;


    @Column(name = "insert_time")
    private Date insertTime;
    
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "is_delete")
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

  

    
}