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
@Table(name = "consultant_schedule_status")
@Data
public class ConsultantScheduleStatus implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 咨询师id
     */
    @Column(name = "user_id")
    private Integer consultantId;

    /**
     * 预约日期
     */
    @Column(name = "schedule_date")
    private String scheduleDate;

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
     * 0 可预约 1 已经被预约 2休息
     */
    @Column(name = "time_type")
    private Integer timeType;

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
    
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", consultantId=").append(consultantId);
        sb.append(", scheduleDate=").append(scheduleDate);
        sb.append(", scheduleStartTime=").append(scheduleStartTime);
        sb.append(", scheduleEndTime=").append(scheduleEndTime);
        sb.append(", timeType=").append(timeType);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public ConsultantScheduleStatus() {
		
	}
    
	public ConsultantScheduleStatus(Integer consultantId,String  scheduleDate) {
		super();
		this.consultantId = consultantId;
		this.scheduleDate=scheduleDate;
		
	}

	public ConsultantScheduleStatus(String unKey,Integer consultantId, String scheduleDate, String scheduleStartTime,
			String scheduleEndTime, Integer timeType) {
		super();
		this.unKey=unKey;
		this.consultantId = consultantId;
		this.scheduleDate = scheduleDate;
		this.scheduleStartTime = scheduleStartTime;
		this.scheduleEndTime = scheduleEndTime;
		this.timeType = timeType;
	}
	
	public ConsultantScheduleStatus(Integer consultantId,Integer timeType) {
		super();
		this.consultantId = consultantId;
		this.timeType = timeType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsultantScheduleStatus other = (ConsultantScheduleStatus) obj;
		if (consultantId == null) {
			if (other.consultantId != null)
				return false;
		} else if (!consultantId.equals(other.consultantId))
			return false;
		if (unKey == null) {
			if (other.unKey != null)
				return false;
		} else if (!unKey.equals(other.unKey))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((consultantId == null) ? 0 : consultantId.hashCode());
		result = prime * result + ((unKey == null) ? 0 : unKey.hashCode());
		return result;
	}
	
	
	
}