package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/05
*/
@Table(name = "consultant_supervised_experience")
@Data
public class SupervisedExperience implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "consultant_id")
    private Integer consultantId;

    /**
     * 督导者 
     */
    private String supervisor;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    /**
     * 督导取向
     */
    @Column(name = "supervision_orientation")
    private String supervisionOrientation;

    /**
     * 督导形式 1 个人 2 团体
     */
    @Column(name = "supervision_method")
    private Integer supervisionMethod;

    /**
     * 证明材料
     */
    @Column(name = "certificate_url")
    private String certificateUrl;

    /**
     * 证明人
     */
    private String certifier;

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
        sb.append(", supervisor=").append(supervisor);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", supervisionOrientation=").append(supervisionOrientation);
        sb.append(", supervisionMethod=").append(supervisionMethod);
        sb.append(", certificateUrl=").append(certificateUrl);
        sb.append(", certifier=").append(certifier);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createDt=").append(createDt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}