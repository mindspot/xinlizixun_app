package com.kuangji.paopao.dto.result;

import com.kuangji.paopao.model.Article;
import com.kuangji.paopao.model.Common;
import lombok.Data;

@Data
public class ArticleResult extends Article {
    private Common common;
}
