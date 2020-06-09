package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/05/26
*/
@Table(name = "promotional_activities")
@Data
public class PromotionalActivities implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 关联模板
     */
    @Column(name = "template_ids")
    private String templateIds;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动图片
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 活动规则
     */
    private String rules;

    /**
     * 活动链接
     */
    private String url;

    /**
     * 活动状态
     */
    private Integer status;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Integer createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_dt")
    private Date createDt;

    /**
     * 修改人
     */
    @Column(name = "modified_by")
    private Integer modifiedBy;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", templateIds=").append(templateIds);
        sb.append(", name=").append(name);
        sb.append(", title=").append(title);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", rules=").append(rules);
        sb.append(", url=").append(url);
        sb.append(", status=").append(status);
        sb.append(", createBy=").append(createBy);
        sb.append(", createDt=").append(createDt);
        sb.append(", modifiedBy=").append(modifiedBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}