package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/21
*/
@Table(name = "consultant_rate")
@Data
public class ConsultantRate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "consultant_id")
    private Integer consultantId;

    @Column(name = "platform_rate")
    private Integer platformRate;

    @Column(name = "channel_rate")
    private Integer channelRate;

    @Column(name = "consultant_rate")
    private Integer consultantRate;

    @Column(name = "partner_rate")
    private Integer partnerRate;

    /**
     * 类型 1 首单 2 复单
     */
    private Integer type;

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
        sb.append(", platformRate=").append(platformRate);
        sb.append(", channelRate=").append(channelRate);
        sb.append(", consultantRate=").append(consultantRate);
        sb.append(", partnerRate=").append(partnerRate);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createDt=").append(createDt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}