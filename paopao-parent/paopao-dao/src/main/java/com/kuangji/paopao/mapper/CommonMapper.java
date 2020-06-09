package com.kuangji.paopao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.admin.vo.CommonVO.CommonLable;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.Common;

/**
* Created by Mybatis Generator 2020/03/14
*/
 public interface CommonMapper extends BaseMapper<Common> {

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
     List<Common> listCommonByType(int type);

    
    

	/**
	 * 根据首页情况
	 * Author 金威正
	 * Date  2020-02-16
	 */
     List<Common> listIndexCommon(List<Integer> types);
    
    
    
    
    /**
	 * 根据传入添加查询
	 * Author 金威正
	 * Date  2020-02-16
	 */
     List<Common> getCommonArticle(Common common);
    
    
    /**
     * 根据ids 查询多个 common 标签
     * 
     */
     List<Common> listCommon(@Param("ids")Integer[] ids);

     
     /**
      * 根据ids 查询多个 common 标签Name
      * 
      */
 	 List<String> listCommonName(@Param("ids")List<Integer> ids) ;
     
 	
 	 List<CommonLable> listSetInCommon(@Param("ids")List<Integer> ids,@Param("consultantId")Integer consultantId);
}