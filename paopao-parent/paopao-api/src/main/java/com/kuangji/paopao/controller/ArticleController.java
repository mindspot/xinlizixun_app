package com.kuangji.paopao.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.api.ArticleApi;
import com.kuangji.paopao.dto.param.ArticleCategoryParam;
import com.kuangji.paopao.dto.param.ArticleParam;
import com.kuangji.paopao.model.Article;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.service.ArticleService;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.ArticleVO;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@RestController
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleApi  articleApi;
    @Autowired
    private CommonService commonService;
    
    //获取协议
    @GetMapping(value = {"/v1/article/list/{commonId}/{pageNum}"})
    public Object list(@PathVariable("commonId") int commonId,@PathVariable("pageNum")int  pageNum) {
  
    	Map<String,Object> listArticle=articleApi.listArticle(commonId, pageNum);
        
        return ServiceResultUtils.success(listArticle);
    }
    
    //获取文章内容
    @GetMapping(value = {"/article/get/{id}"})
    public Object get(@PathVariable("id") int id) {
    
        ArticleVO articleVO = articleService.getArticle(id);
        
        return ServiceResultUtils.success(articleVO);
    }
    //获取协议
    @GetMapping(value = {"/v1/article/settledIn/agreement"})
    public Object getSettledInAgreement() {
  
        Article article = articleService.getArticleAgreement();
        
        return ServiceResultUtils.success(article);
    }
    //获取详细须知
    @GetMapping(value = {"/v1/article/companyIntroduction"})
    public Object companyIntroduction() {
  
        Article article = articleService.getCompanyIntroduction();
        
        return ServiceResultUtils.success(article);
    }

    @PostMapping("/article/category/list")
    public Object listArticleCategory(@RequestBody ArticleCategoryParam articleCategoryParam) {
        List<Common> list=commonService.listArticleCategory(articleCategoryParam);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }
    
    @PostMapping("/article/category/add")
    public Object addArticleCategory(@RequestBody Common common) {
        int result=commonService.addArticleCategory(common);
        return ServiceResultUtils.success(result>0);
    }
    @PostMapping("/article/category/update")
    public Object updateArticleCategory(@RequestBody Common common) {
        int result=commonService.updateArticleCategory(common);
        return ServiceResultUtils.success(result>0);
    }
    @PostMapping("/article/category/delete")
    public Object deleteArticleCategory( Integer id) {
        int result=commonService.updateByPrimaryKeySelective(new Common(id,1));
        return ServiceResultUtils.success(result>0);
    }
    @PostMapping("/article/list")
    public Object listArticle(@RequestBody ArticleParam articleParam) {
        List<Article> list=articleService.listArticle(articleParam);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }
    
/*  @GetMapping("/article/listUpdate")
    public Object updateListArticle() {
        List<Article> list=articleService.findAll();
        for (Article article : list) {
        	String img=article.getArticleImg();
        	if (img!=null) {
        		String articleImg=img.replace("http://182.92.72.104", "http://pp.psyooo.com");
            	article.setArticleImg(articleImg);
			}
        	String link=article.getArticleLink();
        	String articleLink=link.replace("https://pp.psyooo.com/api/#/article/", "https://pp.psyooo.com/#/article/");
        	article.setArticleLink(articleLink);
        	articleService.updateByPrimaryKeySelective(article);
		}
        
        return ServiceResultUtils.success(new PageInfo<>(list));
    }*/
}
