package com.kuangji.paopao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.enums.OrderStatusEnum;
import com.kuangji.paopao.enums.PointTypeEnum;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.MemberMapper;
import com.kuangji.paopao.mapper.PointHistoryMapper;
import com.kuangji.paopao.mapper.SubCommissionRecordMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.model.consultant.PointHistory;
import com.kuangji.paopao.model.consultant.SubCommissionRecord;
import com.kuangji.paopao.service.AccountService;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Autowired
    private MallTradeOrderMapper mallTradeOrderMapper;
    @Autowired
    private SubCommissionRecordMapper subCommissionRecordMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ConsultantMapper consultantMapper;
    @Autowired
    private PointHistoryMapper pointHistoryMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Value("${platform.invitation-code}")
    private String PlatformInvitationCode;

    @Override
    @Transactional
    public Boolean doSubCommissionRecord(String orderNo) {
        log.info("订单:" + orderNo + "分佣开始");
        int flag = 0;
        MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOne(new MallTradeOrder(orderNo));
        Example example = new Example(SubCommissionRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", mallTradeOrder.getId());
        SubCommissionRecord subCommissionRecord = subCommissionRecordMapper.selectOneByExample(example);
        // 平台
        User oldPlatformUser = consultantMapper.getPlatformAccount(PlatformInvitationCode);
        PointHistory platformPointHistory = new PointHistory();
        platformPointHistory.setFromUserId(mallTradeOrder.getBuyerId());
        platformPointHistory.setToUserId(oldPlatformUser.getId());
        platformPointHistory.setPoint(subCommissionRecord.getPlatformAmount());
        platformPointHistory.setBalanceBefore(oldPlatformUser.getAccount());
        platformPointHistory.setPointType(PointTypeEnum.SETTLE.getType());
        flag = userMapper.updateAccount(oldPlatformUser.getId(), subCommissionRecord.getPlatformAmount());
        if (flag > 0) {
            User newSupervisorUser = userMapper.selectByPrimaryKey(oldPlatformUser.getId());
            platformPointHistory.setBalance(newSupervisorUser.getAccount());
            pointHistoryMapper.insertSelective(platformPointHistory);
        }
        // 渠道
        Integer channelUserId = memberMapper.getChannelUserId(mallTradeOrder.getBuyerId());
        User oldChannelUser;
        if (channelUserId != null && channelUserId > 0) {
            oldChannelUser = userMapper.selectByPrimaryKey(channelUserId);
        } else {
            oldChannelUser = consultantMapper.getPlatformAccount(PlatformInvitationCode);
        }
        PointHistory channelPointHistory = new PointHistory();
        channelPointHistory.setFromUserId(mallTradeOrder.getBuyerId());
        channelPointHistory.setToUserId(oldChannelUser.getId());
        channelPointHistory.setPoint(subCommissionRecord.getChannelAmount());
        channelPointHistory.setBalanceBefore(oldChannelUser.getAccount());
        channelPointHistory.setPointType(PointTypeEnum.SETTLE.getType());
        flag = userMapper.updateAccount(oldChannelUser.getId(), subCommissionRecord.getChannelAmount());
        if (flag > 0) {
            User newChannelUser = userMapper.selectByPrimaryKey(oldChannelUser.getId());
            channelPointHistory.setBalance(newChannelUser.getAccount());
            pointHistoryMapper.insertSelective(channelPointHistory);
        }
        // 督导师逻辑
        Consultant consultant = consultantMapper.getParentConsultant(subCommissionRecord.getConsultantUserId());
        User oldSupervisorUser = userMapper.selectByPrimaryKey(consultant.getUserId());
        PointHistory supervisorPointHistory = new PointHistory();
        supervisorPointHistory.setFromUserId(mallTradeOrder.getBuyerId());
        supervisorPointHistory.setToUserId(consultant.getUserId());
        supervisorPointHistory.setPoint(subCommissionRecord.getPartnerAmount());
        supervisorPointHistory.setBalanceBefore(oldSupervisorUser.getAccount());
        supervisorPointHistory.setPointType(PointTypeEnum.SETTLE.getType());
        flag = userMapper.updateAccount(consultant.getUserId(), subCommissionRecord.getPartnerAmount());
        if (flag > 0) {
            User newSupervisorUser = userMapper.selectByPrimaryKey(consultant.getUserId());
            supervisorPointHistory.setBalance(newSupervisorUser.getAccount());
            pointHistoryMapper.insertSelective(supervisorPointHistory);
        }

        // 咨询师逻辑
        User oldConsultantUser = userMapper.selectByPrimaryKey(subCommissionRecord.getConsultantUserId());
        PointHistory consultantPointHistory = new PointHistory();
        consultantPointHistory.setFromUserId(mallTradeOrder.getBuyerId());
        consultantPointHistory.setToUserId(subCommissionRecord.getConsultantUserId());
        consultantPointHistory.setPoint(subCommissionRecord.getConsultantAmount());
        consultantPointHistory.setBalanceBefore(oldConsultantUser.getAccount());
        consultantPointHistory.setPointType(PointTypeEnum.SETTLE.getType());
        flag = userMapper.updateAccount(subCommissionRecord.getConsultantUserId(), subCommissionRecord.getConsultantAmount());
        if (flag > 0) {
            User newConsultantUser = userMapper.selectByPrimaryKey(subCommissionRecord.getConsultantUserId());
            consultantPointHistory.setBalance(newConsultantUser.getAccount());
            pointHistoryMapper.insertSelective(consultantPointHistory);
        }
        // 分佣
        subCommissionRecord.setStatus(1);
        subCommissionRecordMapper.updateByPrimaryKey(subCommissionRecord);
        log.info("订单:" + orderNo + "分佣结束");
        return true;
    }

    @Override
    @Transactional
    public Boolean doRefund(String orderNo) {
        int flag = 0;
        MallTradeOrder mallTradeOrder = mallTradeOrderMapper.selectOne(new MallTradeOrder(orderNo));
        Example example = new Example(SubCommissionRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", mallTradeOrder.getId());
        criteria.andEqualTo("status", 1);
        SubCommissionRecord subCommissionRecord = subCommissionRecordMapper.selectOneByExample(example);
        if(subCommissionRecord==null) {
            throw new ServiceException(ResultCodeEnum.NO_SUB_COMMISSION_RECORD);
        }
        // 平台
        User oldPlatformUser = consultantMapper.getPlatformAccount(PlatformInvitationCode);
        PointHistory platformPointHistory = new PointHistory();
        platformPointHistory.setFromUserId(oldPlatformUser.getId());
        platformPointHistory.setToUserId(mallTradeOrder.getBuyerId());
        platformPointHistory.setPoint(subCommissionRecord.getPlatformAmount());
        platformPointHistory.setBalanceBefore(oldPlatformUser.getAccount());
        platformPointHistory.setPointType(PointTypeEnum.REFUND.getType());
        flag = userMapper.updateAccount(oldPlatformUser.getId(), -subCommissionRecord.getPlatformAmount());
        if (flag > 0) {
            User newSupervisorUser = userMapper.selectByPrimaryKey(oldPlatformUser.getId());
            platformPointHistory.setBalance(newSupervisorUser.getAccount());
            pointHistoryMapper.insertSelective(platformPointHistory);
        }
        // 渠道
        Integer channelUserId = memberMapper.getChannelUserId(mallTradeOrder.getBuyerId());
        User oldChannelUser;
        if (channelUserId != null && channelUserId > 0) {
            oldChannelUser = userMapper.selectByPrimaryKey(channelUserId);
        } else {
            oldChannelUser = consultantMapper.getPlatformAccount(PlatformInvitationCode);
        }
        PointHistory channelPointHistory = new PointHistory();
        channelPointHistory.setFromUserId(oldChannelUser.getId());
        channelPointHistory.setToUserId(mallTradeOrder.getBuyerId());
        channelPointHistory.setPoint(subCommissionRecord.getChannelAmount());
        channelPointHistory.setBalanceBefore(oldChannelUser.getAccount());
        channelPointHistory.setPointType(PointTypeEnum.REFUND.getType());
        flag = userMapper.updateAccount(oldChannelUser.getId(), -subCommissionRecord.getChannelAmount());
        if (flag > 0) {
            User newSupervisorUser = userMapper.selectByPrimaryKey(oldChannelUser.getId());
            channelPointHistory.setBalance(newSupervisorUser.getAccount());
            pointHistoryMapper.insertSelective(channelPointHistory);
        }
        // 督导师逻辑
        Consultant consultant = consultantMapper.getParentConsultant(subCommissionRecord.getConsultantUserId());
        User oldSupervisorUser = userMapper.selectByPrimaryKey(consultant.getUserId());
        PointHistory supervisorPointHistory = new PointHistory();
        supervisorPointHistory.setFromUserId(consultant.getUserId());
        supervisorPointHistory.setToUserId(mallTradeOrder.getBuyerId());
        supervisorPointHistory.setPoint(subCommissionRecord.getPartnerAmount());
        supervisorPointHistory.setBalanceBefore(oldSupervisorUser.getAccount());
        supervisorPointHistory.setPointType(PointTypeEnum.REFUND.getType());
        flag = userMapper.updateAccount(consultant.getUserId(), -subCommissionRecord.getPartnerAmount());
        if (flag > 0) {
            User newSupervisorUser = userMapper.selectByPrimaryKey(consultant.getUserId());
            supervisorPointHistory.setBalance(newSupervisorUser.getAccount());
            pointHistoryMapper.insertSelective(supervisorPointHistory);
        }

        // 咨询师逻辑
        User oldConsultantUser = userMapper.selectByPrimaryKey(subCommissionRecord.getConsultantUserId());
        PointHistory consultantPointHistory = new PointHistory();
        consultantPointHistory.setFromUserId(subCommissionRecord.getConsultantUserId());
        consultantPointHistory.setToUserId(mallTradeOrder.getBuyerId());
        consultantPointHistory.setPoint(subCommissionRecord.getConsultantAmount());
        consultantPointHistory.setBalanceBefore(oldConsultantUser.getAccount());
        consultantPointHistory.setPointType(PointTypeEnum.REFUND.getType());
        flag = userMapper.updateAccount(subCommissionRecord.getConsultantUserId(), -subCommissionRecord.getConsultantAmount());
        if (flag > 0) {
            User newConsultantUser = userMapper.selectByPrimaryKey(subCommissionRecord.getConsultantUserId());
            consultantPointHistory.setBalance(newConsultantUser.getAccount());
            pointHistoryMapper.insertSelective(consultantPointHistory);
        }
        //买家
        User memberUser = userMapper.selectByPrimaryKey(subCommissionRecord.getMemberUserId());
        PointHistory memberUserPointHistory = new PointHistory();
        memberUserPointHistory.setToUserId(mallTradeOrder.getBuyerId());
        memberUserPointHistory.setPoint(subCommissionRecord.getTotalAmount());
        memberUserPointHistory.setBalanceBefore(memberUser.getAccount());
        memberUserPointHistory.setPointType(PointTypeEnum.REFUND.getType());
        flag = userMapper.updateAccount(subCommissionRecord.getMemberUserId(), subCommissionRecord.getTotalAmount());
        if (flag > 0) {
            User newConsultantUser = userMapper.selectByPrimaryKey(subCommissionRecord.getMemberUserId());
            memberUserPointHistory.setBalance(newConsultantUser.getAccount());
            pointHistoryMapper.insertSelective(memberUserPointHistory);
        }
        // 分佣
        subCommissionRecord.setStatus(2);
        subCommissionRecordMapper.updateByPrimaryKey(subCommissionRecord);
        mallTradeOrder.setOrderStatus(OrderStatusEnum.REFUND.code);
        mallTradeOrderMapper.updateByPrimaryKeySelective(mallTradeOrder);
        return true;
    }
}
