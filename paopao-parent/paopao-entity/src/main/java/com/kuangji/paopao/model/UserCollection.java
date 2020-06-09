package com.kuangji.paopao.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
* Created by Mybatis Generator 2020/03/14
*/
@Table(name = "user_collection")
@Data
public class UserCollection implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 收藏id
     */
    @Column(name = "collection_id")
    private Integer collectionId;

    /**
     * 收藏类型
     */
    @Column(name = "collection_type")
    private Byte collectionType;

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

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", collectionId=").append(collectionId);
        sb.append(", collectionType=").append(collectionType);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}