package com.kuangji.paopao.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * Author 金威正
 * Date  2020-02-25
 */
@Data
public class ConsultantUpdateVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String userName="";
    private String realName="";
    private String headImg="";
    private String headImgSize="";
    private Integer sex;
    private String sendWord;
    private String content;
    private int account;
    private String faceAddress;
    private String title;
}