package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/05
*/
@Table(name = "consultant_training_experience")
@Data
public class TrainingExperience implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "consultant_id")
    private Integer consultantId;

    /**
     * 培训机构 
     */
    private String organization;

    /**
     * 开始时间
     */
    @Column(name = "start_date")
    private String startDate;

    /**
     * 结束时间
     */
    @Column(name = "end_date")
    private String endDate;

    /**
     * 课程名称
     */
    @Column(name = "course_name")
    private String courseName;

    /**
     * 培训证书
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
        sb.append(", organization=").append(organization);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", courseName=").append(courseName);
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