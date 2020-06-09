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
@Table(name = "user_image")
@Data
public class UserImage implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 图片地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 图片类型
     */
    @Column(name = "img_type")
    private Integer imgType;

    /**
     * 是否是主图  1默认是 0不是
     */
    @Column(name = "is_main")
    private Integer isMain;

    /**
     * 存在阶级关系 比如主图下面的副图
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 关联的id(user_id)
     */
    @Column(name = "user_id")
    private Integer userId;

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
        sb.append(", imgUrl=").append(imgUrl);
        sb.append(", imgType=").append(imgType);
        sb.append(", isMain=").append(isMain);
        sb.append(", parentId=").append(parentId);
        sb.append(", userId=").append(userId);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public UserImage() {
		
	}
    
    
	public UserImage(Integer userId,Integer imgType) {
		super();
		this.userId = userId;
	}

	public UserImage(Integer userId, String imgUrl,Integer imgType) {
		super();
		this.userId = userId;
		this.imgType = imgType;
		this.imgUrl = imgUrl;
	}

	
    
    
}