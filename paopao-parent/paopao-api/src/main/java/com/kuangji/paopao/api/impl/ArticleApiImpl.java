package com.kuangji.paopao.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.api.ArticleApi;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.service.ArticleService;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.vo.ArticleVO;

@Component
public class ArticleApiImpl implements ArticleApi {
	
	@Autowired
	ArticleService articleService;
	@Autowired
	CommonService commonService;
    
	@Override
	public Map<String, Object> listArticle(int commonId, int pageNum) {
		// TODO Auto-generated method stub
		Map<String, Object> map =new HashMap<String, Object>();
		PageInfo<ArticleVO> pageInfoArticle=articleService.pageListArticle(commonId, pageNum);
		map.put(CommonConstant.PAGE_INFO_ARTICLECLE, pageInfoArticle);

		List<Integer> types=new ArrayList<Integer>();
		types.add(CommonEnum.CATEGORY_CLASSIFICATION_TYPE.getVal());
		List<Common> classifications=commonService.listCommon(types);
		map.put(CommonConstant.ClASSIFICATION, classifications);
		
		return map;
	}

	

}
