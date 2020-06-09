package com.kuangji.paopao.order.dto;

import java.util.Arrays;

import com.kuangji.paopao.enums.OrderFromEnum;
import com.kuangji.paopao.order.vo.BaseOrderFormVO;

/**
 * 咨询师服务对象
 * 
 * @author Administrator
 */

public class ConsultantServiceDTO extends BaseOrderFormVO {

	private static final long serialVersionUID = 32271942995592752L;
	//日期
	private String consultantWorkDate;
	//时间段
	private String consultantWorkTime;
	//是否接受咨询
	private Integer isConsultant;
	//咨询的类别
	private Integer[] consultantType;
	//详细描述
	private String detailedDescription;
	//姓名
	private String realName;
	//性别    
    private Integer sex;
    //年龄
    private Integer age;
    //订单来源
	private Integer orderFrom=OrderFromEnum.ON_LINE_MP.code;
	//优惠劵id
	private Integer couponId;
	
	
	
	public String getConsultantWorkDate() {
		return consultantWorkDate;
	}



	public void setConsultantWorkDate(String consultantWorkDate) {
		this.consultantWorkDate = consultantWorkDate;
	}



	public String getConsultantWorkTime() {
		return consultantWorkTime;
	}



	public void setConsultantWorkTime(String consultantWorkTime) {
		this.consultantWorkTime = consultantWorkTime;
	}



	public Integer getIsConsultant() {
		return isConsultant;
	}



	public void setIsConsultant(Integer isConsultant) {
		this.isConsultant = isConsultant;
	}



	public Integer[] getConsultantType() {
		return consultantType;
	}



	public void setConsultantType(Integer[] consultantType) {
		this.consultantType = consultantType;
	}



	public String getDetailedDescription() {
		return detailedDescription;
	}



	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}



	public String getRealName() {
		return realName;
	}



	public void setRealName(String realName) {
		this.realName = realName;
	}



	public Integer getSex() {
		return sex;
	}



	public void setSex(Integer sex) {
		this.sex = sex;
	}



	public Integer getAge() {
		return age;
	}



	public void setAge(Integer age) {
		this.age = age;
	}



	public Integer getOrderFrom() {
		return orderFrom;
	}



	public void setOrderFrom(Integer orderFrom) {
		this.orderFrom = orderFrom;
	}



	public Integer getCouponId() {
		return couponId;
	}



	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}



	@Override
	public String toString() {
		return "ConsultantServiceDTO [consultantWorkDate=" + consultantWorkDate + ", consultantWorkTime="
				+ consultantWorkTime + ", isConsultant=" + isConsultant + ", consultantType="
				+ Arrays.toString(consultantType) + ", detailedDescription=" + detailedDescription + ", realName="
				+ realName + ", sex=" + sex + ", age=" + age + ", orderFrom=" + orderFrom + ", couponId=" + couponId
				+ "]";
	}
	
	

	
}
