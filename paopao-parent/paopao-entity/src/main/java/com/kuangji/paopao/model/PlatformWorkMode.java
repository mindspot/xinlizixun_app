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
@Table(name = "platform_work_mode")
@Data
public class PlatformWorkMode implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 存放的值
     */
    @Column(name = "work_val")
    private  Integer workVal;

    /**
     * 工作时间
     */
    @Column(name = "work_time")
    private Integer workTime;

    /**
     * 分类
     */
    private Integer type;

    /**
     * 有效期
     */
    @Column(name = "term_of_validity")
    private Integer termOfValidity;

    /**
     * 状态0 隐藏 1显示
     */
    private Integer status;

    @Column(name = "insert_time")
    private Date insertTime;

    @Column(name = "update_time")
    private Date updateTime;

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
        sb.append(", workVal=").append(workVal);
        sb.append(", workTime=").append(workTime);
        sb.append(", type=").append(type);
        sb.append(", termOfValidity=").append(termOfValidity);
        sb.append(", status=").append(status);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}