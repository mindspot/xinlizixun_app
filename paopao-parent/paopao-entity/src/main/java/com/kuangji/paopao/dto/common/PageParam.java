package com.kuangji.paopao.dto.common;

import lombok.Data;

@Data
public class PageParam {
    private Integer pageNo;
    private Integer pageSize;
    private Boolean ifPage;
    public PageParam() {
        pageNo = 1;
        pageSize = 20;
        ifPage = true;
    }
}
