package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/05
*/
@Table(name = "consultant_education_experience")
@Data
public class EducationExperience implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "consultant_id")
    private Integer consultantId;

    /**
     * 学历
     */
    @Column(name = "education_type")
    private Integer educationType;

    /**
     * 学历
     */
    @Column(name = "education_type_desc")
    private String educationTypeDesc;

    /**
     * 专业
     */
    private String major;

    /**
     * 证书
     */
    @Column(name = "certificate_url")
    private String certificateUrl;

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
        sb.append(", educationType=").append(educationType);
        sb.append(", educationTypeDesc=").append(educationTypeDesc);
        sb.append(", major=").append(major);
        sb.append(", certificateUrl=").append(certificateUrl);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createDt=").append(createDt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}