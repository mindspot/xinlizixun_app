package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/22
*/
@Table(name = "consultant")
@Data
public class Consultant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    /**
     * 出生年月
     */
    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "prov_name")
    private String provName;

    @Column(name = "prov_code")
    private Integer provCode;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "city_code")
    private Integer cityCode;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "area_code")
    private Integer areaCode;

    @Column(name = "addr_detail")
    private String addrDetail;

    /**
     * 微信号
     */
    private String weixing;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mailbox;

    /**
     * 寄语
     */
    @Column(name = "send_word")
    private String sendWord;

    /**
     * 简介
     */
    @Column(name = "brief_introduction")
    private String briefIntroduction;

    /**
     * 咨询页面显示 一次咨询费用
     */
    @Column(name = "consultation_fee")
    private Integer consultationFee;

    /**
     * 分类 0默认咨询师 1默认督导
     */
    @Column(name = "user_type")
    private Integer userType;

    /**
     * 排序 数字越排序越高
     */
    private Integer sort;

    /**
     * 0 默认1 等待审核 2 驳回 3 通过
     */
    private Integer status;

    /**
     * 邀请码
     */
    @Column(name = "invitation_code")
    private String invitationCode;

    @Column(name = "working_seniority")
    private Float workingSeniority;

    @Column(name = "accumulative_case")
    private Integer accumulativeCase;

    /**
     * 面对面咨询地址
     */
    @Column(name = "face_address")
    private String faceAddress;

    /**
     * 头衔
     */
    @Column(name = "title")
    private String title;

    /**
     * 推广url
     */
    @Column(name = "spread_url")
    private String spreadUrl;
    /**
     * 创建时间
     */
    @Column(name = "insert_time")
    private Date insertTime;

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
        sb.append(", birthDate=").append(birthDate);
        sb.append(", provName=").append(provName);
        sb.append(", provCode=").append(provCode);
        sb.append(", cityName=").append(cityName);
        sb.append(", cityCode=").append(cityCode);
        sb.append(", areaName=").append(areaName);
        sb.append(", areaCode=").append(areaCode);
        sb.append(", addrDetail=").append(addrDetail);
        sb.append(", weixing=").append(weixing);
        sb.append(", phone=").append(phone);
        sb.append(", mailbox=").append(mailbox);
        sb.append(", sendWord=").append(sendWord);
        sb.append(", briefIntroduction=").append(briefIntroduction);
        sb.append(", consultationFee=").append(consultationFee);
        sb.append(", userType=").append(userType);
        sb.append(", sort=").append(sort);
        sb.append(", status=").append(status);
        sb.append(", invitationCode=").append(invitationCode);
        sb.append(", workingSeniority=").append(workingSeniority);
        sb.append(", accumulativeCase=").append(accumulativeCase);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}