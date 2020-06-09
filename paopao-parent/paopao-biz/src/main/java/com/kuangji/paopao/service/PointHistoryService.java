package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.dto.param.PointHistoryParam;
import com.kuangji.paopao.dto.result.PointHistoryResult;
import com.kuangji.paopao.model.UserCashWithdrawal;
import com.kuangji.paopao.model.consultant.PointHistory;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.PointHistoryVO;

public interface PointHistoryService extends BaseService<PointHistory, Integer>{
	
    List<PointHistoryResult> listPointHistoryResult(PointHistoryParam pointHistoryParam);
    
    //订单下单支付记录
    String pointHistory(String orderNo,Integer pointType,Integer balance);
    
    //提现记录
    String pointHistoryCashWithdrawal(UserCashWithdrawal u,Integer pointType,Integer befBalance);
    
    
    //提现记录
    String refundPointHistoryCashWithdrawal(UserCashWithdrawal u,Integer pointType,Integer befBalance);
    
    //订单取消记录
    String refundPointHistory(String orderNo);
    
    
    //督导订单 下单支付
    String superPointHistory(String orderNo,Integer pointType,Integer balance);
    
    //督导订单 拒绝 取消 
    String refundSuperPointHistory(String orderNo,Integer pointType,Integer balance);

    //督导师完结订单 记录金额变动
    String endSuperPointHistory(String orderNo,Integer pointType,Integer balance);
    
    //app端查询得到结果
    List<PointHistory> listPointHistory(PointHistoryParam pointHistoryParam);
    
    //work端查询得到结果
    List<PointHistory> listWorkPointHistory(PointHistoryParam pointHistoryParam);
    
    //转化
    List<PointHistoryVO> listPointHistoryVO(PointHistoryParam pointHistoryParam);
    
    //work显示
    List<PointHistoryVO> listWorkPointHistoryVO(PointHistoryParam pointHistoryParam);
}
