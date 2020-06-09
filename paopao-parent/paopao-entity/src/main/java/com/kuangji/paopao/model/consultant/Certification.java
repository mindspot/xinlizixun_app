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
* Created by Mybatis Generator 2020/04/07
*/
@Table(name = "consultant_certification")
@Data
public class Certification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "consultant_id")
    private Integer consultantId;

    /**
     * 资质名称
     */
    @Column(name = "certificate_type")
    private Integer certificateType;

    /**
     * 资质名称
     */
    @Column(name = "certificate_type_name")
    private String certificateTypeName;

    /**
     * 资质编号
     */
    @Column(name = "certificate_no")
    private String certificateNo;

    /**
     * 持证年限
     */
    @Column(name = "certificate_age")
    private Integer certificateAge;

    /**资质名称
     * 资质照片
     */
    @Column(name = "certificate_url")
    private String certificateUrl;
    
    
    @Column(name = "certificate_url_size")
    private String certificateUrlSize;
    
    /**
     * 状态
     */
    private Integer status;

    /**
     * 描述
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_dt")
    private Date createDt;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", consultantId=").append(consultantId);
        sb.append(", certificateType=").append(certificateType);
        sb.append(", certificateTypeName=").append(certificateTypeName);
        sb.append(", certificateAge=").append(certificateAge);
        sb.append(", certificateUrl=").append(certificateUrl);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createDt=").append(createDt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}