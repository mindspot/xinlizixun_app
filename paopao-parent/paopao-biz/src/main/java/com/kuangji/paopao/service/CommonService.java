package com.kuangji.paopao.service;

import java.util.List;
import java.util.Map;

import com.kuangji.paopao.admin.vo.CommonVO;
import com.kuangji.paopao.dto.param.ArticleCategoryParam;
import com.kuangji.paopao.dto.param.CommonParam;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.CustomeServiceVO;

/**
 * Author 金威正
 * Date  2020-02-16
 */
public interface CommonService extends BaseService<Common, Integer>{

   
    
    /**
	 * 根据type获取数据
	 * Author 金威正
	 * Date  2020-02-16
	 */
     Common getCommonByType(int type);

    /**
	 * 根据type获取多个数据
	 * Author 金威正
	 * Date  2020-02-16
	 */
     List<Common> listCommonByType();
    

    /**
   	 *
   	 * Author 金威正
   	 * Date  2020-02-16
   	 */
     Map<String,Object> listIndexCommon(String token);

    

	/**
	 * 根据首页情况
	 * Author 金威正
	 * Date  2020-02-16
	 */
     List<Common> listCommon(List<Integer> types);
    
    
    
    /**
   	 * 根据baseCode码获取质询页面  的公共数据
   	 * Author 金威正
   	 * Date  2020-02-16
   	 */
     Map<String,List<Common>> listConsultationCommon();


	List<Common> listArticleCategory(ArticleCategoryParam articleCategoryParam);

	int addArticleCategory(Common common);

	int updateArticleCategory(Common common);
	
	 /**
   	 * 获取对象所有参数
   	 * Author 金威正
   	 * Date  2020-02-16
   	 */
	
	List<Common> listCommon(Integer[] ids);
	
	 /**
   	 * 根据ids 获取name
   	 * Author 金威正
   	 * Date  2020-02-16
   	 */

	List<String> listCommonName(List<Integer> classificationIds);

	
	List<Common> listCommon(CommonParam commonParam);
	
	
	List<Common> listCommonContent(CommonParam commonParam);
	
	
	CommonVO setInLables(String token);

	List<Common> listAllArticleCategory();

    Common getCommonByVal(String commonName);

	//显示客服咨询师信息
	CustomeServiceVO getCustomeServiceVO();
}