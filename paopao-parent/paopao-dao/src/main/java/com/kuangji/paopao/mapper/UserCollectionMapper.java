package com.kuangji.paopao.mapper;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.UserCollection;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface UserCollectionMapper extends BaseMapper<UserCollection> {
	

	
	/**
	 * 根据 userId 跟收藏id
	 * Author 金威正
	 * Date  2020-02-22
	 */
    public UserCollection getCollect(@Param("userId")Integer userId,@Param("shopId")Integer collectionId);
    

	/**
	 * 获取收藏状态
	 * Author 金威正
	 * Date  2020-02-22
	 */
    public boolean isCollected(@Param("userId")Integer userId,@Param("shopId")Integer collectionId);
    
    
	
	/**
	 * 获取收藏状态
	 * Author 金威正
	 * Date  2020-02-22
	 */
    public Integer deleteCollection(@Param("userId")Integer userId,@Param("shopId")Integer collectionId);
    
}