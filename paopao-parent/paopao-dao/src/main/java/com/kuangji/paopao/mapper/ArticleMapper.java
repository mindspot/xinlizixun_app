package com.kuangji.paopao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.ArticleParam;
import com.kuangji.paopao.dto.result.ArticleResult;
import com.kuangji.paopao.model.Article;
import com.kuangji.paopao.vo.ArticleVO;

/**
 * Created by Mybatis Generator 2020/03/14
 */
public interface ArticleMapper extends BaseMapper<Article> {

	/**
	 * 根据id获取数据 Author 金威正 Date 2020-02-17
	 */
	 ArticleVO getArticle(int id);

	/**
	 * 根据获code取数据 Author 金威正 Date 2020-02-17
	 */
	 Article getArticleByCode(int code);

	/**
	 * 火热 Author 金威正 Date 2020-02-17
	 */
	 List<ArticleVO> listHotArticle();

	/**
	 * 根据条件查询 Author 金威正 Date 2020-02-17
	 */
	 List<ArticleVO> listArticleVO(Article article);


	List<ArticleResult> listArticleResult(ArticleParam articleParam);
	
	int addArticleBrowseVolume(@Param("id")Integer id,@Param("browseVolume")Integer browseVolume);

	
}