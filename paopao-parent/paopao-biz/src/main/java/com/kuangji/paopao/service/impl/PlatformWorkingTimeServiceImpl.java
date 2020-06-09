package com.kuangji.paopao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.PlatformWorkingTimeParam;
import com.kuangji.paopao.mapper.ConsultantScheduleMapper;
import com.kuangji.paopao.mapper.PlatformWorkingTimeMapper;
import com.kuangji.paopao.model.ConsultantSchedule;
import com.kuangji.paopao.model.PlatformWorkingTime;
import com.kuangji.paopao.service.PlatformWorkingTimeService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.vo.BookingTimeVO;

import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-02-27
 */
@Service
public class PlatformWorkingTimeServiceImpl extends BaseServiceImpl<PlatformWorkingTime, Integer> implements   PlatformWorkingTimeService {
    @Autowired
    private PlatformWorkingTimeMapper platformWorkingTimeMapper;
    
    
    @Autowired
    private ConsultantScheduleMapper consultantScheduleMapper;
    
	/**
	 * 真删除
	 * Author 金威正
	 * Date  2020-02-27
	 */
    public int delete(int id){
    return platformWorkingTimeMapper.deleteByPrimaryKey(id);
    }

	@Override
	public List<BookingTimeVO> listWorkingUserTime(Integer userId) {

		List<BookingTimeVO> bookingTimeVOs =new ArrayList<BookingTimeVO>();
	
		List<PlatformWorkingTime> platformWorkingTimes=platformWorkingTimeMapper.selectAll();

		Example example =new Example(ConsultantSchedule.class);
		example.createCriteria().andEqualTo("consultantId", userId);
		List<ConsultantSchedule> consultantSchedules=consultantScheduleMapper.selectByExample(example);
		
		for (PlatformWorkingTime platformWorkingTime : platformWorkingTimes) {
			BookingTimeVO bookingTimeVO =new BookingTimeVO();
			bookingTimeVO.setConsultantWorkStartTime(platformWorkingTime.getConsultantWorkStart());
			bookingTimeVO.setConsultantWorkEndTime(platformWorkingTime.getConsultantWorkEnd());
			bookingTimeVO.setPlatformWorkingTimeId(platformWorkingTime.getId());
			bookingTimeVO.setTimeType(platformWorkingTime.getStatus());
			for (ConsultantSchedule consultantSchedule : consultantSchedules) {
				if (platformWorkingTime.getId()==consultantSchedule.getPlatformWorkingTimeId()) {
					bookingTimeVO.setTimeType(consultantSchedule.getStatus());
				}
				
			}
			bookingTimeVOs.add(bookingTimeVO);
		}
			return bookingTimeVOs;
	}

	@Override
	public BaseMapper<PlatformWorkingTime> getMapper() {
		// TODO Auto-generated method stub
		return platformWorkingTimeMapper;
	}

	@Override
	public List<PlatformWorkingTime> listWorking(PlatformWorkingTimeParam param) {
		PageHelper.startPage(param.getPageNo(), param.getPageSize());
		List<PlatformWorkingTime> list=platformWorkingTimeMapper.selectAll();
		return list;
	}

}
