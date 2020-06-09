package com.kuangji.paopao.dto;

import java.io.Serializable;

import lombok.Data;
/**
 * 修改界面
 * Author 金威正
 * Date  2020-02-25
 */
@Data
public class UserUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    //真是姓名
    private String realName;
    
    //头像
    private String headImg;
    
    //性别
    private Integer sex;
    
    //个性签名 
    private String sendWord;
    
    //婚姻状态  婚姻状态 0未婚 1已婚 2恋爱中
    private Integer maritalStatus;
    
    //职业
    private String occupation;
    
 

	@Override
	public String toString() {
		return "UserUpdateDTO [id=" + id + ", realName=" + realName + ", headImg=" + headImg + ", sex=" + sex
				+ ", sendWord=" + sendWord + ", maritalStatus=" + maritalStatus + ", occupation=" + occupation + "]";
	}

    
}