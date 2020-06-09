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
* Created by Mybatis Generator 2020/03/27
*/
@Table(name = "consultant_schedule_rest_status")
@Data
public class ConsultantScheduleRestStatus implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 咨询师id
     */
    @Column(name = "consultant_id")
    private Integer consultantId;

    /**
     * 休息日期
     */
    @Column(name = "rest_date")
    private String restDate;

    /**
     * 创建时间
     */
    @Column(name = "insert_time")
    private Date insertTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 删除标识：0=未删除，1=已删除
     */
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
        sb.append(", consultantId=").append(consultantId);
        sb.append(", restDate=").append(restDate);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public ConsultantScheduleRestStatus( Integer consultantId, String restDate) {
		super();
		this.consultantId = consultantId;
		this.restDate = restDate;
	}

	public ConsultantScheduleRestStatus() {
		super();
	}

	public ConsultantScheduleRestStatus(Integer consultantId) {
		super();
		this.consultantId = consultantId;
	}
    
    
}

