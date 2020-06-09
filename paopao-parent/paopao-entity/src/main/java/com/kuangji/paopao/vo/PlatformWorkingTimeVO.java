package com.kuangji.paopao.vo;

import lombok.Data;
/**
 * Author 金威正
 * Date  2020-02-27
 */
@Data
public class PlatformWorkingTimeVO {
	private Integer platformWorkingTimeId;
	private String consultantWorkStart;
    private String consultantWorkEnd;
    private Integer status;
}