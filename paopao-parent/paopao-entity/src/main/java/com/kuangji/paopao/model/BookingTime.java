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
@Table(name = "booking_time")
@Data
public class BookingTime implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "un_key")
    private String unKey;
   
    @Column(name = "user_id")
    private Integer consultantId;

    /**
     * 排班开始时间
     */
    @Column(name = "consultant_work_date")
    private String consultantWorkDate;
    
    /**
     * 排班开始时间
     */
    @Column(name = "consultant_work_start_time")
    private String consultantWorkStartTime;

    /**
     * 删除标识：0=未删除，1=已删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 排班结束
     */
    @Column(name = "consultant_work_end_time")
    private String consultantWorkEndTime;

   
    /**
     * 时间排序
     */
    @Column(name = "sort_time")
    private Long sortTime;
    
    
    /**
     * 时间排序
     */
    @Column(name = "status")
    private Integer timeType;

    /**
     * 类型
     */
    private Long code;

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

    private static final long serialVersionUID = 1L;

    

	public BookingTime( Integer consultantId) {
		super();
		this.consultantId = consultantId;
	}
    
	public BookingTime() {
	
	}

	public BookingTime( Integer consultantId, String consultantWorkDate, String consultantWorkStartTime,
			String consultantWorkEndTime, Long sortTime, Long code) {
		super();
		this.consultantId = consultantId;
		this.consultantWorkDate = consultantWorkDate;
		this.consultantWorkStartTime = consultantWorkStartTime;
		this.consultantWorkEndTime = consultantWorkEndTime;
		this.sortTime = sortTime;
		this.code = code;
	}

	public BookingTime(Integer id, Integer consultantId, String consultantWorkDate, String consultantWorkStartTime,
			String consultantWorkEndTime, Long sortTime, Long code) {
		super();
		this.id = id;
		this.consultantId = consultantId;
		this.consultantWorkDate = consultantWorkDate;
		this.consultantWorkStartTime = consultantWorkStartTime;
		this.consultantWorkEndTime = consultantWorkEndTime;
		this.sortTime = sortTime;
		this.code = code;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookingTime other = (BookingTime) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (consultantId == null) {
			if (other.consultantId != null)
				return false;
		} else if (!consultantId.equals(other.consultantId))
			return false;
		if (consultantWorkDate == null) {
			if (other.consultantWorkDate != null)
				return false;
		} else if (!consultantWorkDate.equals(other.consultantWorkDate))
			return false;
		if (consultantWorkEndTime == null) {
			if (other.consultantWorkEndTime != null)
				return false;
		} else if (!consultantWorkEndTime.equals(other.consultantWorkEndTime))
			return false;
		if (consultantWorkStartTime == null) {
			if (other.consultantWorkStartTime != null)
				return false;
		} else if (!consultantWorkStartTime.equals(other.consultantWorkStartTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (insertTime == null) {
			if (other.insertTime != null)
				return false;
		} else if (!insertTime.equals(other.insertTime))
			return false;
		if (isDelete == null) {
			if (other.isDelete != null)
				return false;
		} else if (!isDelete.equals(other.isDelete))
			return false;
		if (sortTime == null) {
			if (other.sortTime != null)
				return false;
		} else if (!sortTime.equals(other.sortTime))
			return false;
		if (unKey == null) {
			if (other.unKey != null)
				return false;
		} else if (!unKey.equals(other.unKey))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((consultantId == null) ? 0 : consultantId.hashCode());
		result = prime * result + ((consultantWorkDate == null) ? 0 : consultantWorkDate.hashCode());
		result = prime * result + ((consultantWorkEndTime == null) ? 0 : consultantWorkEndTime.hashCode());
		result = prime * result + ((consultantWorkStartTime == null) ? 0 : consultantWorkStartTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((insertTime == null) ? 0 : insertTime.hashCode());
		result = prime * result + ((isDelete == null) ? 0 : isDelete.hashCode());
		result = prime * result + ((sortTime == null) ? 0 : sortTime.hashCode());
		result = prime * result + ((unKey == null) ? 0 : unKey.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		return result;
	}
	
	
	
}

