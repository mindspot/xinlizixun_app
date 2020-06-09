package com.kuangji.paopao.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/16
*/
@Table(name = "platform_working_time")
@Data
public class PlatformWorkingTime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 开始时间
     */
    @Column(name = "consultant_work_start")
    private String consultantWorkStart;

    /**
     * 接收时间
     */
    @Column(name = "consultant_work_end")
    private String consultantWorkEnd;

    /**
     * 1 上午 2下午 3晚上 4凌晨
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "insert_time")
    private Date insertTime;

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
        sb.append(", consultantWorkStart=").append(consultantWorkStart);
        sb.append(", consultantWorkEnd=").append(consultantWorkEnd);
        sb.append(", status=").append(status);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}