package com.kuangji.paopao.dto;

import lombok.Data;

@Data
public class ConsultantExtraInfoDTO {

	    private Integer userId=0;


	    private String infoName="";

	    /**
	     * 专业
	     */
	    private String value="";

	    /**
	     * 资质
	     */
	    private Integer number=0;

	    /**
	     * 资质编号
	     */
	    private String startTime="";

	    /**
	     * 持证年限
	     */
	
	    private String endTime="";

	    /**
	     * 年限
	     */
	    private Integer years=0;

	    private String witness="";

	    /**
	     * 0个人 1团体
	     */
	
	    private Integer isPersonal=0;

	    /**
	     * 类型
	     */
	   
	    private Integer infoType=0;

	
	    private Integer isDelete=0;

}
