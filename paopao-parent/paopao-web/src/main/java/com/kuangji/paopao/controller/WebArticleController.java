package com.kuangji.paopao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.ArticleParam;
import com.kuangji.paopao.dto.result.ArticleResult;
import com.kuangji.paopao.model.Article;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.service.ArticleService;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.ArticleVO;

@RestController
@RequestMapping(value = "/web/article")
public class WebArticleController {
    @Autowired
    private ArticleService articleService;
    //获取文章内容
    @GetMapping(value = {"/get/{id}"})
    public Object get(@PathVariable("id") int id) {
        ArticleVO article = articleService.getArticle(id);
        return ServiceResultUtils.success(article);
    }
    @PostMapping("/add")
    public Object addArticle(@RequestBody Article article) {
        int result=articleService.addArticle(article);
        return ServiceResultUtils.success(result>0);
    }
    @PostMapping("/list")
    public Object listArticle(@RequestBody ArticleParam articleParam) {
        List<ArticleResult> list=articleService.listArticleResult(articleParam);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }
    @GetMapping("/category/all")
    public Object allArticleCategory() {
        List<Common>  list=articleService.listAllArticleCategory();
        return ServiceResultUtils.success(list);
    }
    @PostMapping("/delete")
    public Object delete(Integer id) {
    	Article article =new Article();
    	article.setId(id);
    	article.setIsDelete(1);
        int result=articleService.updateByPrimaryKeySelective(article);
        return ServiceResultUtils.success(result>0);
    }
    
    
}
