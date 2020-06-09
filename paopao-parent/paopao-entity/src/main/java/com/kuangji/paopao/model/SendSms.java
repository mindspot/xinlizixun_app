package com.kuangji.paopao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "send_sms")
@Data
public class SendSms implements Serializable {
	

  private static final long serialVersionUID = -3454550038468733268L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Column(name = "user_id")
  private Integer userId;
  
  @Column(name = "user_consultant_id")
  private Integer userConsultantId;
  
  @Column(name = "send_date")
  private String sendDate;
	
  
  @Column(name = "send_time")
  private Date sendTime;
 
  private Integer frequency;
  
  @Column(name = "insert_time")
  private Date insertTime;

  @Column(name = "update_time")
  private Date updateTime;

  @Column(name = "is_delete")
  private Integer isDelete;

}
