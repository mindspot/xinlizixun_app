package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/22
*/
@Table(name = "point_history")
@Data
public class PointHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "from_user_id")
    private Integer fromUserId;

    @Column(name = "to_user_id")
    private Integer toUserId;

    private Integer point;

    @Column(name = "balance_before")
    private Integer balanceBefore;

    private Integer balance;

    @Column(name = "point_type")
    private Integer pointType;

    @Column(name = "handle_by")
    private Long handleBy;

    private String remark;

    @Column(name = "create_by")
    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_dt")
    private Date createDt;

    @Column(name = "modified_by")
    private Long modifiedBy;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fromUserId=").append(fromUserId);
        sb.append(", toUserId=").append(toUserId);
        sb.append(", point=").append(point);
        sb.append(", balanceBefore=").append(balanceBefore);
        sb.append(", balance=").append(balance);
        sb.append(", pointType=").append(pointType);
        sb.append(", handleBy=").append(handleBy);
        sb.append(", remark=").append(remark);
        sb.append(", createBy=").append(createBy);
        sb.append(", createDt=").append(createDt);
        sb.append(", modifiedBy=").append(modifiedBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}