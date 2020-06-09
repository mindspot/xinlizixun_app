package com.kuangji.paopao.dto;

import java.io.Serializable;
import java.util.Arrays;

import lombok.Data;
/**
 * 
 * 用户病例
 * Author 金威正
 * Date  2020-02-23
 * 
 */
@Data
public class UserCaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //是否咨询过 0没有 1有
    private Integer isConsultant;
    
    //咨询填写的类型
    private Integer[] consultantType;
    
    //详细信息
    private String detailedDescription;
    
    //性别
    private Integer sex;
    
    //年龄
    private Integer age;
    
    //病人名称
    private String operName;

    //预约日期  2019-10-12
    private String consultantWorkDate;
   
    //预约时间 小时分钟
    private String consultantWorkTime;

	@Override
	public String toString() {
		return "UserCaseDTO [isConsultant=" + isConsultant + ", consultantType="
				+ Arrays.toString(consultantType) + ", detailedDescription=" + detailedDescription + ", sex=" + sex
				+ ", age=" + age + ", operName=" + operName +"]";
	}
    
    
}