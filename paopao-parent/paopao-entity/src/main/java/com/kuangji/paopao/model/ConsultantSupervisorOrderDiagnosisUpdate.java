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
* Created by Mybatis Generator 2020/03/24
*/
@Table(name = "consultant_order_diagnosis_update")
@Data
public class ConsultantSupervisorOrderDiagnosisUpdate implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 创建时间
     */
    @Column(name = "diagnosis_time")
    private Date diagnosisTime;

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
        sb.append(", orderNo=").append(orderNo);
        sb.append(", diagnosisTime=").append(diagnosisTime);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public ConsultantSupervisorOrderDiagnosisUpdate( String orderNo, Date diagnosisTime) {
		super();
		this.orderNo = orderNo;
		this.diagnosisTime = diagnosisTime;
	}
	public ConsultantSupervisorOrderDiagnosisUpdate() {
		
	}

	public ConsultantSupervisorOrderDiagnosisUpdate(String orderNo) {
		super();
		this.orderNo = orderNo;
	}
    
	
}