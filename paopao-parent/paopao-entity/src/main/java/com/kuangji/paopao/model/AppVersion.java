package com.kuangji.paopao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
* Created by Mybatis Generator 2020/03/14
*/
@Table(name = "app_version")
@Data
public class AppVersion implements Serializable {
  private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 版本号
     */
    @Column(name = "app_version")
    private String appVersion;
    
    
    /**
     * app名称
     */
    @Column(name = "bundle_id")
    private String bundleId;
    
    
    /**
     * 需不需强制更新
     */
    @Column(name = "force_update")
    private Integer forceUpdate;
    
    
    /**
     * 下载地址
     */
    @Column(name = "download_address")
    private String downloadAddress;
    
    
    /**
     *更新内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 删除标记
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 插入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "insert_time")
    private Date insertTime;
    
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private Date updateTime;
 
    
}