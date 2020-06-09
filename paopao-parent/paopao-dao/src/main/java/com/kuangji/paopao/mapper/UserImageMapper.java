package com.kuangji.paopao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.UserImage;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface UserImageMapper extends BaseMapper<UserImage> {
	
	
	 int insertBatchUserImageBatch(@Param("imgs")List<String> images,@Param("userId")Integer userId,@Param("type")Integer type);
	 
	 
	 //删除 资质 
	 int deleteConsultantExtraInfoImgs(@Param("userId")Integer userId);

	 List<UserImage> listIdCards(Integer userId);
}