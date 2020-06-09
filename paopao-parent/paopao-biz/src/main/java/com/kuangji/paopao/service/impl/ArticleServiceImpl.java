package com.kuangji.paopao.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.constant.ExcelConstant;
import com.kuangji.paopao.dto.param.ArticleParam;
import com.kuangji.paopao.dto.result.ArticleResult;
import com.kuangji.paopao.dto.result.ExcelCheckResult;
import com.kuangji.paopao.enums.ArticleEnum;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.mapper.ArticleMapper;
import com.kuangji.paopao.model.Article;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.service.ArticleService;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.DelTagsUtil;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.util.excel.ExcelUtil;
import com.kuangji.paopao.util.excel.PoiUtil;
import com.kuangji.paopao.vo.ArticleVO;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@Service
@Slf4j
public class ArticleServiceImpl extends BaseServiceImpl<Article, Integer> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommonService commonService;
    @Value("${platform.article-prefix-url}")
    private String article_Url;
    
	private static String defaultArticleImage = PropertiesFileUtil.getInstance().get("default_article_image");
	
	private static String imageUrl = PropertiesFileUtil.getInstance().get("image_url");

	
    /**
     * 根据获code取数据
     * Author 金威正
     * Date  2020-02-17
     */
    @Override
    public Article getArticleAgreement() {
        // TODO Auto-generated method stub
        return articleMapper.getArticleByCode(CommonEnum.CHECK_IN_AGREEMENT_TYPE.getVal());
    }


    @Override
    public PageInfo<ArticleVO> pageListArticle(int commonId, int page) {
        Article article = new Article();
        article.setCommonId(commonId);
        PageHelper.startPage(page, 10);
        List<ArticleVO> articles = articleMapper.listArticleVO(article);
        PageInfo<ArticleVO> pageInfo = new PageInfo<>(articles);
        return pageInfo;
    }

    @Override
    public List<ArticleVO> listHotArticle() {
        // TODO Auto-generated method stub
        return articleMapper.listHotArticle();
    }

    @Override
    public Article getServiceInstructions(Integer code) {
        return articleMapper.getArticleByCode(code);
    }

    @Override
    public Article getServiceDetailedDescription() {
        // TODO Auto-generated method stub
        return articleMapper.getArticleByCode(CommonEnum.SERVICE_DETAILED_DESCRIPTION.getVal());

    }

    @Override
    public Article getCompanyIntroduction() {
        // TODO Auto-generated method stub
        return articleMapper.getArticleByCode(CommonEnum.COMPANY_INTRODUCTION.getVal());
    }

    @Override
    public BaseMapper<Article> getMapper() {
        // TODO Auto-generated method stub
        return articleMapper;
    }


    @Override
    public ArticleVO getArticle(Integer id) {
    	articleMapper.addArticleBrowseVolume(id, 1);
        return articleMapper.getArticle(id);
    }


    @Override
    public List<ArticleVO> listArticleVO(Article article) {
        // TODO Auto-generated method stub
        return articleMapper.listArticleVO(article);
    }

    @Override
    public List<Article> listArticle(ArticleParam articleParam) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        if (articleParam.getCommonId() != null && articleParam.getCommonId() > 0) {
            criteria.andEqualTo("commonId", articleParam.getCommonId());
        }
        List<Integer> list = new ArrayList<Integer>();
        list.add(ArticleEnum.ARTICLE_TYPE.getVal());
        list.add(ArticleEnum.ADVERTISEMENT_TYPE.getVal());
        criteria.andIn("code", list);
        example.setOrderByClause("sort");
        PageHelper.startPage(articleParam.getPageNo(), articleParam.getPageSize());
        return articleMapper.selectByExample(example);
    }

    @Override
    public int addArticle(Article article) {
        if (article.getId() != null && article.getId() > 0) {
			Article article2=articleMapper.selectByPrimaryKey(article.getId());
			if (article2.getBrowseVolume()>article2.getRandomBrowse()) {
				article.setBrowseVolume(article2.getBrowseVolume()-article2.getRandomBrowse());
			}
		    articleMapper.updateByPrimaryKeySelective(article);
        } else {
    	  Random random = new Random();
          //随机0到9999
          int ran1 = random.nextInt(10000);
          article.setRandomBrowse(ran1);
          article.setStatus(2);
          if (StringUtils.isBlank(article.getArticleImg())) {
          	article.setArticleImg(imageUrl+defaultArticleImage);
          }
          if (StringUtils.isBlank(article.getArticleTitle())) {
        	  article.setArticleTitle(DelTagsUtil.getTextFromHtml(article.getContent()));
          }
          articleMapper.insertSelective(article);
        }
        Article article2=articleMapper.selectByPrimaryKey(article.getId());
    	if (article2.getBrowseVolume()>article2.getRandomBrowse()) {
			article.setBrowseVolume(article2.getBrowseVolume()-article2.getRandomBrowse());
		}
        article.setArticleLink(article_Url+article.getId());
        return articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public List<ArticleResult> listArticleResult(ArticleParam articleParam) {
        PageHelper.startPage(articleParam.getPageNo(), articleParam.getPageSize());
        return articleMapper.listArticleResult(articleParam);
    }

    @Override
    public List<Common> listAllArticleCategory() {
        return commonService.listAllArticleCategory();
    }

    @Override
    public List<ExcelCheckResult> checkArticleExcel(File file, Integer userId) {
        List<ExcelCheckResult> errorList = new ArrayList<>();
        List<List<Object>> dataList = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> tittleList = ExcelUtil.getTitle(inputStream);
        if (!ExcelConstant.ARTICLE_TEMPLATE_TITTLE.equals(String.join(",", tittleList))) {
            errorList.add(new ExcelCheckResult("表头", "Excel表头不正确"));
            return errorList;
        }
        try {
            dataList = PoiUtil.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //数据封装格式一，将表格中的数据遍历取出后封装进对象放进List
        for (int i = 0; i < dataList.size(); i++) {
            log.info(dataList.get(i).toString());
            if (dataList.get(i).size() == 5) {
                String commonName = (String) dataList.get(i).get(0);
                if (StringUtils.isNotEmpty(commonName)) {
                    Common common = commonService.getCommonByVal(commonName);
                    if (common == null) {
                        errorList.add(new ExcelCheckResult("文章分类", "文章分类不存在"));
                    }
                }
            }
        }
        return errorList;
    }

    @Override
    public int batchAddArticleByExcel(File file, Integer userId) {
        List<List<Object>> dataList = null;
        try {
            dataList = PoiUtil.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //数据封装格式一，将表格中的数据遍历取出后封装进对象放进List
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).size() == 5) {
                Article article = new Article();
                Common common = commonService.getCommonByVal((String) dataList.get(i).get(0));
                article.setCommonId(common.getId());
                article.setAuthor((String) dataList.get(i).get(1));
                article.setArticleVal((String) dataList.get(i).get(2));
                article.setArticleTitle((String) dataList.get(i).get(3));
                article.setContent((String) dataList.get(i).get(4));
                Random random = new Random();
                //随机0到9999
                int ran1 = random.nextInt(10000);
                article.setRandomBrowse(ran1);
                article.setStatus(2);
                if (StringUtils.isBlank(article.getArticleImg())) {
                	article.setArticleImg(imageUrl+defaultArticleImage);
				}
                articleMapper.insertSelective(article);
                article.setArticleLink(article_Url+article.getId());
                articleMapper.updateByPrimaryKeySelective(article);
            }
        }
        return 1;
    }
}
