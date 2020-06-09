package com.kuangji.paopao.api;

import java.util.Map;

public interface ArticleApi {

	//聚合 文章 加 分类
	public Map<String, Object> listArticle(int commonId,int pageNum);

}
