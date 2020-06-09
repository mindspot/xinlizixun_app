package com.kuangji.paopao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.param.PointHistoryParam;
import com.kuangji.paopao.dto.result.PointHistoryResult;
import com.kuangji.paopao.enums.PointTypeEnum;
import com.kuangji.paopao.mapper.ConsultantSupervisorOrderMapper;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.PointHistoryMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.ConsultantSupervisorOrder;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.UserCashWithdrawal;
import com.kuangji.paopao.model.consultant.PointHistory;
import com.kuangji.paopao.service.PointHistoryService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.vo.PointHistoryVO;

import tk.mybatis.mapper.entity.Example;

@Service
public class PointHistoryServiceImpl extends BaseServiceImpl<PointHistory, Integer> implements PointHistoryService{
	
    @Autowired
    private PointHistoryMapper pointHistoryMapper;

    @Autowired
    private UserMapper userMapper;
  

    @Autowired
    private MallTradeOrderMapper mallTradeOrderMapper;
    
    
    @Autowired
    private ConsultantSupervisorOrderMapper consultantSupervisorOrderMapper;
    
    
    @Override
    public List<PointHistoryResult> listPointHistoryResult(PointHistoryParam pointHistoryParam) {
        PageHelper.startPage(pointHistoryParam.getPageNo(), pointHistoryParam.getPageSize());
        return pointHistoryMapper.listPointHistoryResult(pointHistoryParam);
    }
    
    
    
 @Override  
 public String pointHistory(String orderNo,Integer pointType,Integer befBalance) {
    	
    	Example example =new Example(MallTradeOrder.class);
    	example.createCriteria().andEqualTo("orderNo", orderNo);
    	MallTradeOrder mallTradeOrder =mallTradeOrderMapper.selectOneByExample(example);
    	PointHistory pointHistory =new PointHistory();
    	pointHistory.setFromUserId(mallTradeOrder.getBuyerId());
    	pointHistory.setPoint(mallTradeOrder.getOrderAmount());
    	User user=userMapper.selectByPrimaryKey(mallTradeOrder.getBuyerId());
    	pointHistory.setBalance(user.getAccount());
    	pointHistory.setBalanceBefore(befBalance);
    	pointHistory.setPointType(pointType);
    	pointHistoryMapper.insertSelective(pointHistory);
		return  "消费记录同步成功";
    }
    
    
    @Override
    public String pointHistoryCashWithdrawal(UserCashWithdrawal u,Integer pointType,Integer befBalance) {
 
    	PointHistory pointHistory =new PointHistory();
    	pointHistory.setFromUserId(u.getUserId());
    	pointHistory.setPoint(u.getAmount());
    	User user=userMapper.selectByPrimaryKey(u.getUserId());
    	pointHistory.setBalanceBefore(befBalance);
    	pointHistory.setBalance(user.getAccount());
    	pointHistory.setPointType(pointType);
    	pointHistoryMapper.insertSelective(pointHistory);
		return "消费记录同步成功";
    }


    

    @Transactional(rollbackFor=Exception.class)
	@Override
	public String refundPointHistory(String orderNo) {
		Example example =new Example(MallTradeOrder.class);
    	example.createCriteria().andEqualTo("orderNo", orderNo);
    	MallTradeOrder mallTradeOrder =mallTradeOrderMapper.selectOneByExample(example);
    	PointHistory pointHistory =new PointHistory();
    	pointHistory.setToUserId(mallTradeOrder.getBuyerId());
    	pointHistory.setPoint(mallTradeOrder.getOrderAmount());
    	pointHistory.setPointType(PointTypeEnum.REFUND.getType());
    	User user=userMapper.selectByPrimaryKey(mallTradeOrder.getBuyerId());
    	pointHistory.setBalanceBefore(user.getAccount());    	
    	if (userMapper.updateAccount(mallTradeOrder.getBuyerId(), mallTradeOrder.getOrderAmount())<1) {
			throw new ServiceException(-1,"操作失败");
		};
		User u=userMapper.selectByPrimaryKey(mallTradeOrder.getBuyerId());
		pointHistory.setBalance(u.getAccount());
    	pointHistoryMapper.insertSelective(pointHistory);
		return "退款成功";
	}

	@Override
	public String refundPointHistoryCashWithdrawal(UserCashWithdrawal u, Integer pointType, Integer befBalance) {
		PointHistory pointHistory =new PointHistory();
    	pointHistory.setPoint(u.getAmount());
    	User user=userMapper.selectByPrimaryKey(u.getUserId());
    	pointHistory.setBalanceBefore(befBalance);
    	pointHistory.setToUserId(u.getUserId());
    	pointHistory.setBalance(user.getAccount());
    	pointHistory.setPointType(pointType);
    	pointHistoryMapper.insertSelective(pointHistory);
    	return "驳回成功";
	}



	@Override
	public String superPointHistory(String orderNo, Integer pointType, Integer befBalance) {
		Example example =new Example(ConsultantSupervisorOrder.class);
		example.createCriteria()
			   .andEqualTo("orderNo", orderNo);
		ConsultantSupervisorOrder order=consultantSupervisorOrderMapper.selectOneByExample(example);
		PointHistory pointHistory =new PointHistory();
    	pointHistory.setPoint(order.getOrderAmount());
    	pointHistory.setFromUserId(order.getConsultantId());
    	User user=userMapper.selectByPrimaryKey(order.getConsultantId());
    	pointHistory.setBalanceBefore(befBalance);
    	pointHistory.setBalance(user.getAccount());
    	pointHistory.setPointType(pointType);
    	pointHistoryMapper.insertSelective(pointHistory);
		return "督导下单";
	}



	@Override
	public String refundSuperPointHistory(String orderNo, Integer pointType, Integer befBalance) {
		Example example =new Example(ConsultantSupervisorOrder.class);
		example.createCriteria()
			   .andEqualTo("orderNo", orderNo);
		ConsultantSupervisorOrder order=consultantSupervisorOrderMapper.selectOneByExample(example);
		PointHistory pointHistory =new PointHistory();
		pointHistory.setToUserId(order.getConsultantId());
    	pointHistory.setPoint(order.getOrderAmount());
    	User user=userMapper.selectByPrimaryKey(order.getConsultantId());
    	pointHistory.setBalanceBefore(befBalance);
    	pointHistory.setBalance(user.getAccount());
    	pointHistory.setPointType(pointType);
    	pointHistoryMapper.insertSelective(pointHistory);
		return "督导订单拒绝或者取消";
	}



	@Override
	public String endSuperPointHistory(String orderNo, Integer pointType, Integer befBalance) {
		Example example =new Example(ConsultantSupervisorOrder.class);
		example.createCriteria()
			   .andEqualTo("orderNo", orderNo);
		ConsultantSupervisorOrder order=consultantSupervisorOrderMapper.selectOneByExample(example);
		PointHistory pointHistory =new PointHistory();
    	pointHistory.setPoint(order.getOrderAmount());
    	User user=userMapper.selectByPrimaryKey(order.getSupervisorId());
    	pointHistory.setBalanceBefore(befBalance);
    	pointHistory.setBalance(user.getAccount());
    	pointHistory.setPointType(pointType);
    	pointHistory.setToUserId(order.getSupervisorId());
    	pointHistoryMapper.insertSelective(pointHistory);
		return "督导订单结束记录金额变动";
	}



	@Override
	public BaseMapper<PointHistory> getMapper() {
		
		return pointHistoryMapper;
	}



	@Override
	public List<PointHistory> listPointHistory(PointHistoryParam pointHistoryParam) {
		
		PageHelper.startPage(pointHistoryParam.getPageNo(), pointHistoryParam.getPageSize());
	/*	Example example =new Example(PointHistory.class);
		example.setOrderByClause("id desc");
		example.createCriteria()
				.orEqualTo("fromUserId", pointHistoryParam.getUserId())
				.orEqualTo("toUserId", pointHistoryParam.getUserId());*/
		List<PointHistory> pointHistories=pointHistoryMapper.listPointHistory(pointHistoryParam);
		return pointHistories;
	}



	@Override
	public List<PointHistoryVO> listPointHistoryVO(PointHistoryParam pointHistoryParam) {
		List<PointHistory> pointHistories=this.listPointHistory(pointHistoryParam);
		List<PointHistoryVO> pointHistoryVOs=new ArrayList<PointHistoryVO>();
		for (PointHistory pointHistory : pointHistories) {
			 PointHistoryVO pointHistoryVO =new PointHistoryVO();
			 BeanUtils.copyProperties(pointHistory, pointHistoryVO);
			 String pointTypeValue=PointTypeEnum.getPointTypeEnumValue(pointHistory.getPointType());
			 pointHistoryVO.setPointTypeName(pointTypeValue);
			 pointHistoryVOs.add(pointHistoryVO);
		}
		return pointHistoryVOs;
	}

	@Override
	public List<PointHistoryVO> listWorkPointHistoryVO(PointHistoryParam pointHistoryParam) {
		List<PointHistory> pointHistories=this.listWorkPointHistory(pointHistoryParam);
		List<PointHistoryVO> pointHistoryVOs=new ArrayList<PointHistoryVO>();
		for (PointHistory pointHistory : pointHistories) {
			 PointHistoryVO pointHistoryVO =new PointHistoryVO();
			 BeanUtils.copyProperties(pointHistory, pointHistoryVO);
			 String pointTypeValue=PointTypeEnum.getPointTypeEnumValue(pointHistory.getPointType());
			 pointHistoryVO.setPointTypeName(pointTypeValue);
			 pointHistoryVOs.add(pointHistoryVO);
		}
		return pointHistoryVOs;
	}



	@Override
	public List<PointHistory> listWorkPointHistory(PointHistoryParam pointHistoryParam) {
		PageHelper.startPage(pointHistoryParam.getPageNo(), pointHistoryParam.getPageSize());
		Example example =new Example(PointHistory.class);
		example.setOrderByClause("id desc");
		example.createCriteria()
				.orEqualTo("fromUserId", pointHistoryParam.getUserId())
				.orEqualTo("toUserId", pointHistoryParam.getUserId());
		List<PointHistory> pointHistories=pointHistoryMapper.selectByExample(example);
		return pointHistories;
	}


    
}
