package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.dto.param.PlatformWorkingTimeParam;
import com.kuangji.paopao.model.PlatformWorkingTime;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.BookingTimeVO;

/**
 * Author 金威正
 * Date  2020-02-27
 */
public interface PlatformWorkingTimeService extends BaseService<PlatformWorkingTime, Integer>{

  

    
    /**
     * 根据token  获取工作时间
     * 
     */
    public List<BookingTimeVO> listWorkingUserTime(Integer userId);

	
    /**
     * 根据token  获取工作时间
     * 
     */
    public List<PlatformWorkingTime> listWorking(PlatformWorkingTimeParam param);
}
