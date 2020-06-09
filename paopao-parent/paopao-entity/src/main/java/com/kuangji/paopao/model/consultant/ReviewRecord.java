package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/11
*/
@Table(name = "review_record")
@Data
public class ReviewRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ref_id")
    private Integer refId;

    @Column(name = "ref_type")
    private Integer refType;

    @Column(name = "ref_type_name")
    private String refTypeName;

    @Column(name = "review_user_id")
    private Long reviewUserId;

    @Column(name = "review_user_name")
    private String reviewUserName;

    @Column(name = "review_remark")
    private String reviewRemark;

    private Integer status;

    @Column(name = "review_dt")
    private Date reviewDt;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", refId=").append(refId);
        sb.append(", refType=").append(refType);
        sb.append(", refTypeName=").append(refTypeName);
        sb.append(", reviewUserId=").append(reviewUserId);
        sb.append(", reviewUserName=").append(reviewUserName);
        sb.append(", reviewRemark=").append(reviewRemark);
        sb.append(", status=").append(status);
        sb.append(", reviewDt=").append(reviewDt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}