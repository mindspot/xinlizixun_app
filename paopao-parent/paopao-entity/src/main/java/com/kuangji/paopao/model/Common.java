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
@Table(name = "common")
@Data
public class Common implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 存放的值
     */
    private String val;

    /**
     * val对应的值
     */
    @Column(name = "val_code")
    private Integer valCode;

    /**
     * 点击链接地址
     */
    private String link;

    /**
     * 图标
     */
    private String icon;

    /**
     * 样式
     */
    private String clazz;

    /**
     * 阶级关系
     */
    private Integer lever;

    /**
     * 是不是需要权限验证
     */
    @Column(name = "is_perms")
    private Integer isPerms;

    /**
     * 父级id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 分类
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remarks;

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
        sb.append(", val=").append(val);
        sb.append(", valCode=").append(valCode);
        sb.append(", link=").append(link);
        sb.append(", icon=").append(icon);
        sb.append(", clazz=").append(clazz);
        sb.append(", lever=").append(lever);
        sb.append(", isPerms=").append(isPerms);
        sb.append(", parentId=").append(parentId);
        sb.append(", type=").append(type);
        sb.append(", sort=").append(sort);
        sb.append(", remarks=").append(remarks);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public Common(Integer type) {
		super();
		this.type = type;
	}
	public Common() {
		
	}

	public Common(Integer id, Integer isDelete) {
		super();
		this.id = id;
		this.isDelete = isDelete;
	}
    
}