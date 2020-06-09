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
@Table(name = "dict_area")
@Data
public class DictArea implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 地区编码
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 地区类型：1=省，2=市，3=区/县
     */
    @Column(name = "area_type")
    private Boolean areaType;

    /**
     * 地区名称
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 上级ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 是否为最后一级：0=不是，1=是
     */
    @Column(name = "is_last")
    private Integer isLast;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @Column(name = "create_author")
    private String createAuthor;

    /**
     * 修改人
     */
    @Column(name = "update_author")
    private String updateAuthor;

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
        sb.append(", areaCode=").append(areaCode);
        sb.append(", areaType=").append(areaType);
        sb.append(", areaName=").append(areaName);
        sb.append(", parentId=").append(parentId);
        sb.append(", isLast=").append(isLast);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createAuthor=").append(createAuthor);
        sb.append(", updateAuthor=").append(updateAuthor);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}