package com.kuangji.paopao.vo;

import java.io.Serializable;

import lombok.Data;
/**
 * Author 金威正
 * Date  2020-02-25
 */
@Data
public class UserUpdateVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id=0;
    private String userName="";
    private String realName="";
    private String headImg="";
    private String headImgSize="";
    private int sex=0;
    private String sendWord="";
    private int maritalStatus=0;
    private String occupation="";
    //余额
    private Integer account=0;
	@Override
	public String toString() {
		return "UserUpdateVO [id=" + id + ", userName=" + userName + ", realName=" + realName + ", headImg=" + headImg
				+ ", sex=" + sex + ", sendWord=" + sendWord + ", maritalStatus=" + maritalStatus + ", occupation="
				+ occupation + "]";
	}

   
}