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
@Table(name = "article")
@Data
public class Article implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 文章名称
     */
    @Column(name = "article_val")
    private String articleVal;

    /**
     * 文章小标题
     */
    @Column(name = "article_title")
    private String articleTitle;

    /**
     * 文章跳转的链接
     */
    @Column(name = "article_link")
    private String articleLink;

    private String author;

    /**
     * 分类 比如文章 或者协议
     */
    private Integer code;

    /**
     * 图片地址
     */
    @Column(name = "article_img")
    private String articleImg;

    /**
     * 浏览量
     */
    @Column(name = "browse_volume")
    private Integer browseVolume;

    @Column(name = "random_browse")
    private Integer randomBrowse;

    
    /**
     * 类别
     */
    @Column(name = "common_id")
    private Integer commonId;

    /**
     * 0 平台发布 1咨询师发布
     */
    @Column(name = "is_platform")
    private Integer isPlatform;

    /**
     * 发布者的id
     */
    @Column(name = "user_id")
    private Integer publisherId;

    /**
     * 状态 0待审合 1通过
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    @Column(name = "is_delete")
    private Integer isDelete;

    @Column(name = "insert_time")
    private Date insertTime;

    @Column(name = "update_time")
    private Date updateTime;

    private String content;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", articleVal=").append(articleVal);
        sb.append(", articleTitle=").append(articleTitle);
        sb.append(", articleLink=").append(articleLink);
        sb.append(", author=").append(author);
        sb.append(", code=").append(code);
        sb.append(", articleImg=").append(articleImg);
        sb.append(", browseVolume=").append(browseVolume);
        sb.append(", commonId=").append(commonId);
        sb.append(", isPlatform=").append(isPlatform);
        sb.append(", publisherId=").append(publisherId);
        sb.append(", status=").append(status);
        sb.append(", sort=").append(sort);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
    

	public Article() {
		
	}

	public Article(Integer code) {
		super();
		this.code = code;
	}
    
    
}