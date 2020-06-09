package com.kuangji.paopao.dto.param;

import com.kuangji.paopao.dto.common.PageParam;

import lombok.Data;

@Data
public class ArticleParam extends PageParam {
    private Integer commonId;
    
    private String title;
}
