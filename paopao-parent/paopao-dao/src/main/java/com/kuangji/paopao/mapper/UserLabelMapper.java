package com.kuangji.paopao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.result.UserLabelInfo;
import com.kuangji.paopao.model.UserLabel;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface UserLabelMapper extends BaseMapper<UserLabel> {
    List<Integer> listLabelIdByUser(@Param("userId") Integer userId, @Param("labelType") Integer labelType);
    
    List<UserLabelInfo> listUserLabelInfo(@Param("userId") Integer userId, @Param("labelType") Integer labelType);
    
    List<Integer> listUserIdByUser(@Param("labelId") String  labelId);

}