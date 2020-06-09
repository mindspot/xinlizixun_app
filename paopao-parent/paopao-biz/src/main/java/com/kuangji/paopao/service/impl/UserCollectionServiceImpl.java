package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.mapper.UserCollectionMapper;
import com.kuangji.paopao.model.UserCollection;
import com.kuangji.paopao.service.UserCollectionService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.JwtUtils;

/**
 * Author 金威正
 * Date  2020-02-22
 */
@Service
public class UserCollectionServiceImpl extends BaseServiceImpl<UserCollection, Integer> implements UserCollectionService {
    @Autowired
    private UserCollectionMapper userCollectionMapper;
    
   
    @Override
    public int insert(String token,Integer collectionId) {
    	if (collectionId<=0) {
			throw new ServiceException(ResultCodeEnum.ERROR_COLLECTION_CONSULTANT);
		}
    
    	Integer userId=JwtUtils.getUserId(token);
    	
    	UserCollection  collect=userCollectionMapper.getCollect(userId.intValue(), collectionId);
    	//已经收藏了
    	if (collect!=null) {
			throw new ServiceException(ResultCodeEnum.ERROR_COLLECTION_NO);
		}
    	UserCollection userCollection =new  UserCollection();	
    	userCollection.setUserId(userId);
    	userCollection.setCollectionId(collectionId);
    	//查询一下是不是已经添加过了
        return userCollectionMapper.insertSelective(userCollection);
    	
    	
    }

   
	/**
	 * 真删除
	 * Author 金威正
	 * Date  2020-02-22
	 */
    public int delete(int id){
    return userCollectionMapper.deleteByPrimaryKey(id);
    }

	@Override
	public UserCollection getCollect(Integer userId, Integer collectionId) {
		// TODO Auto-generated method stub
		return userCollectionMapper.getCollect(userId, collectionId);
	}

	@Override
	public boolean isCollected(Integer userId, Integer collectionId) {
		
	
		return userCollectionMapper.isCollected(userId, collectionId);
	}

	@Override
	public int deleteCollection(String token, Integer collectionId) {
		// TODO Auto-generated method stub、
		Integer userId=JwtUtils.getUserId(token);
		return userCollectionMapper.deleteCollection(userId, collectionId);
	}


	@Override
	public BaseMapper<UserCollection> getMapper() {
		// TODO Auto-generated method stub
		return userCollectionMapper;
	}

}
