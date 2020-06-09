package com.kuangji.paopao.model.consultant;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/04/05
*/
@Table(name = "consultant_publish_material")
@Data
public class PublishMaterial implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "consultant_id")
    private Integer consultantId;

    /**
     * 1 书籍 2 论文
     */
    private Integer type;

    /**
     * 书名和论文标题
     */
    @Column(name = "material_name")
    private String materialName;

    /**
     * 出版社或者论文网址
     */
    @Column(name = "publish_site")
    private String publishSite;

    /**
     * 出版时间
     */
    @Column(name = "publish_time")
    private String publishTime;

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
        sb.append(", type=").append(type);
        sb.append(", materialName=").append(materialName);
        sb.append(", publishSite=").append(publishSite);
        sb.append(", publishTime=").append(publishTime);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createDt=").append(createDt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}