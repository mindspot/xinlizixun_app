package com.kuangji.paopao.dto.param;

import com.kuangji.paopao.dto.common.PageParam;
import lombok.Data;

@Data
public class CouponTemplateParam extends PageParam {
    private String keywords;
    private Integer status;
}
