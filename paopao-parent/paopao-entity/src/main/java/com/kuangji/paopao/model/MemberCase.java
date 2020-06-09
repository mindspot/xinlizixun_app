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
@Table(name = "member_case")
@Data
public class MemberCase implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 购买人ID
     */
    @Column(name = "user_id")
    private Integer userId;

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

    /**
     * 是否咨询过 1是 0否
     */
    @Column(name = "is_consultant")
    private Integer isConsultant;

    /**
     * 咨询类型
     */
    @Column(name = "consultant_type")
    private String consultantType;

    /**
     * 详细质询
     */
    @Column(name = "detailed_description")
    private String detailedDescription;

    /**
     * 性别 0男 1女
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 姓名
     */
    @Column(name = "oper_name")
    private String operName;

    /**
     * 创建时间
     */
    @Column(name = "insert_time")
    private Date insertTime;
    
  

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [");
        sb.append("userId=").append(userId);
        sb.append(", isConsultant=").append(isConsultant);
        sb.append(", consultantType=").append(consultantType);
        sb.append(", detailedDescription=").append(detailedDescription);
        sb.append(", sex=").append(sex);
        sb.append(", age=").append(age);
        sb.append(", operName=").append(operName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

	public MemberCase(Integer userId) {
		super();
		this.userId = userId;
	}
    
    
	public MemberCase() {
		
	}
    
}