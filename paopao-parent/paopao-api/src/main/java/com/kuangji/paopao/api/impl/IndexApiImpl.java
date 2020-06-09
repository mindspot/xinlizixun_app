package com.kuangji.paopao.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kuangji.paopao.api.IndexApi;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.enums.BannerEnum;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.service.ArticleService;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.ArticleVO;

@Component
public class IndexApiImpl implements IndexApi {
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	ArticleService articleService;
	
    private static String testMoreUrl = PropertiesFileUtil.getInstance().get("index_test_more_url");
	
	private static  String  imageUrl = PropertiesFileUtil.getInstance().get("image_url");
	@Override
	public Map<String, Object> index() {
		// TODO Auto-generated method stub
		Map<String, Object> map =new HashMap<String, Object>();
		List<Integer> types =new ArrayList<Integer>();
		types.add(CommonEnum.INDEX_IMAGE_TYPE.getVal());
		types.add(CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal());
		types.add(CommonEnum.INDEX_TEST_TYPE.getVal());
		List<Common> list = commonService.listCommon(types);
        
        List<Common> indexImages=list.stream()
        			.filter(c->
        				c.getType()==CommonEnum.INDEX_IMAGE_TYPE.getVal()&&
        				c.getValCode()==BannerEnum.SHOW.getValue()
        			).collect(Collectors.toList());
        indexImages.forEach(c->c.setVal(ImageUtils.getImgagePath(imageUrl, c.getVal())));
        map.put(CommonConstant.INDEX_IMAGES, indexImages);
        
		//分类
        List<Common> classification = list.stream()
    			.filter(c->c.getType()==CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal())
    			.collect(Collectors.toList());
        classification.forEach(c->c.setIcon(ImageUtils.getImgagePath(imageUrl, c.getIcon())));
        map.put(CommonConstant.ClASSIFICATION, classification);
        
		//test
        List<Common> testQuestions = list.stream()
    			.filter(c->c.getType()==CommonEnum.INDEX_TEST_TYPE.getVal())
    			.collect(Collectors.toList());
        map.put(CommonConstant.TEST_QUESTIONS, testQuestions);

        List<ArticleVO> articleVOs  = articleService.listHotArticle();    
        map.put(CommonConstant.ARTICLES, articleVOs);
        map.put(CommonConstant.TEST_MORE_URL, testMoreUrl);     
		return map;
	}



}
