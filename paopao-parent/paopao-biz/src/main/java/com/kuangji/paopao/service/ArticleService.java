package com.kuangji.paopao.service;

import java.io.File;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.ArticleParam;
import com.kuangji.paopao.dto.result.ArticleResult;
import com.kuangji.paopao.dto.result.ExcelCheckResult;
import com.kuangji.paopao.model.Article;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.ArticleVO;

/**
 * Author 金威正 Date 2020-02-17
 */
public interface ArticleService extends BaseService<Article, Integer>{

	/**
	 * 根据id获取数据 Author 金威正 Date 2020-02-17
	 */
	public ArticleVO getArticle(Integer id);

	/**
	 * 获取协议数据 Author 金威正 Date 2020-02-17
	 */
	public Article getArticleAgreement();

	/**
	 * 服务须知 Author 金威正 Date 2020-02-17getServiceInstructions
	 */
	public Article getServiceInstructions(Integer code);

	/**
	 * 获取公司公司介绍  Author 金威正 Date 2020-02-17
	 */
	public Article getCompanyIntroduction();
	
	/**
	 * 获取详细说明 Author 金威正 Date 2020-02-17
	 */
	public Article getServiceDetailedDescription();

	/**
	 * 根据传入添加查询 Author 金威正 Date 2020-02-17
	 */
	public List<ArticleVO> listArticleVO(Article article);

	
	/**
	 * 火热推荐文章 Author 金威正 Date 2020-02-17
	 */
	public List<ArticleVO> listHotArticle();

	/**
	 * 根据传 Author 金威正 Date 2020-02-17
	 */
	public PageInfo<ArticleVO> pageListArticle(int parentId, int page);


    List<Article> listArticle(ArticleParam articleParam);

	int addArticle(Article article);

	List<ArticleResult> listArticleResult(ArticleParam articleParam);

    List<ExcelCheckResult> checkArticleExcel(File file, Integer userId);

	int batchAddArticleByExcel(File file, Integer userId);

    List<Common> listAllArticleCategory();
}
