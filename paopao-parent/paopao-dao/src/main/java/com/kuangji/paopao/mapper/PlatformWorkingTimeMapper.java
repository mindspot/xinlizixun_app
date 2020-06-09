package com.kuangji.paopao.mapper;

import java.util.List;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.PlatformWorkingTime;
import com.kuangji.paopao.vo.PlatformWorkingTimeVO;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface PlatformWorkingTimeMapper extends BaseMapper<PlatformWorkingTime> {

    /**
     * 根据userId 获取工作时间
     *
     */
     List<PlatformWorkingTimeVO> listWorkingUserTime(Integer userId);
}