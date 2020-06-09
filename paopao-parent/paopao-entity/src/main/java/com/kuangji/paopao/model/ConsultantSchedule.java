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
* Created by Mybatis Generator 2020/03/14
*/
@Table(name = "consultant_schedule")
@Data
public class ConsultantSchedule implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 购买人ID
     */
    @Id
    @Column(name = "user_id")
    private Integer consultantId;

    /**
     * 排班开始时间时间
     */
    @Column(name = "schedule_start_time")
    private String scheduleStartTime;

    /**
     * 排班结束时间
     */
    @Column(name = "schedule_end_time")
    private String scheduleEndTime;

    /**
     * 工作平台id
     */
    @Column(name = "platform_working_time_id")
    private Integer platformWorkingTimeId;

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
    
    @Column(name = "un_key")
    private String unKey;

    @Column(name = "status")
    private Integer status;
    
    
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", consultantId=").append(consultantId);
        sb.append(", scheduleStartTime=").append(scheduleStartTime);
        sb.append(", scheduleEndTime=").append(scheduleEndTime);
        sb.append(", platformWorkingTimeId=").append(platformWorkingTimeId);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public ConsultantSchedule(Integer consultantId) {
		super();
		this.consultantId = consultantId;
	}

	public ConsultantSchedule() {
		super();
	}
    
    
    
}