package com.kuangji.paopao.service;

import com.kuangji.paopao.model.UserCollection;
import com.kuangji.paopao.service.base.BaseService;

/**
 * Author 金威正
 * Date  2020-02-22
 */
public interface UserCollectionService extends BaseService<UserCollection, Integer>{
    
    /**
	 * 查看收藏状态
	 * Author 金威正
	 * Date  2020-02-22
	 */
    public boolean isCollected(Integer userId,Integer collectionId);
    
    
    /**
 	 * 取消收藏状态
 	 * Author 金威正
 	 * Date  2019-12-12
 	 */
 	public int deleteCollection(String token,Integer collectionId);
    
    /**
	 * 根据userId 跟 collectionId 查询数据
	 * Author 金威正
	 * Date  2020-02-22
	 */
    public UserCollection getCollect(Integer userId,Integer collectionId);

	/**
	 * 插入
	 * Author 金威正
	 * Date  2020-02-22
	 */
    public int insert(String token,Integer collectionId);

}
