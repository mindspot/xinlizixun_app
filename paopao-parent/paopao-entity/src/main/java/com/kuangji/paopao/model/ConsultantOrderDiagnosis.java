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
@Table(name = "consultant_order_diagnosis")
@Data
public class ConsultantOrderDiagnosis implements Serializable {
  
	/**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_no")
    private String orderNo;

   
    @Column(name = "content")
    private String content;
    
    
    @Column(name = "content_type")
    private Integer contentType;
    
    
    @Column(name = "is_delete")
    private Integer isDelete;
    
    @Column(name = "insert_time")
    private Date insertTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public ConsultantOrderDiagnosis() {
		
	}

    
	public ConsultantOrderDiagnosis(String orderNo) {
		super();
		this.orderNo = orderNo;
	}

    
    
}