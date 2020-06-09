package com.kuangji.paopao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.admin.vo.UserCashWithdrawalVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.param.UserCashWithdrawalParam;
import com.kuangji.paopao.enums.PointTypeEnum;
import com.kuangji.paopao.enums.UserCashWithdrawalEnum;
import com.kuangji.paopao.mapper.UserCardMapper;
import com.kuangji.paopao.mapper.UserCashWithdrawalMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.UserCard;
import com.kuangji.paopao.model.UserCashWithdrawal;
import com.kuangji.paopao.redis.util.RedisUtils;
import com.kuangji.paopao.service.PointHistoryService;
import com.kuangji.paopao.service.UserCashWithdrawalService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.UserCardVO;

import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@Service
public class UserCashWithdrawalServiceImpl extends BaseServiceImpl<UserCashWithdrawal, Integer> implements UserCashWithdrawalService {
    @Autowired
    private UserCashWithdrawalMapper userCashWithdrawalMapper;
    
    @Autowired
    private UserCardMapper userCardMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RedisUtils redisUtils;
    
    @Autowired
    private PointHistoryService pointHistoryService;
    
	private static String cashWithdawat = PropertiesFileUtil.getInstance().get("cash_withdawat");


	@Override
	public BaseMapper<UserCashWithdrawal> getMapper() {
		// TODO Auto-generated method stub
		return userCashWithdrawalMapper;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int cashWithdrawal(Integer consultantId,Integer amount, String code) {
		if (amount<0) {
    		throw new ServiceException(-1,"金额不正确");
		}
    	UserCard card=userCardMapper.selectOne(new UserCard(consultantId,0));
    	if (card==null) {
    		throw new ServiceException(-1,"找不到此绑卡记录");
    		
		}
    	List<UserCashWithdrawal> withdrawal=userCashWithdrawalMapper.select(new UserCashWithdrawal(consultantId,card.getId(),UserCashWithdrawalEnum.TO_EXAMINE.getIndex()));
    	if (withdrawal!=null&&!withdrawal.isEmpty()) {
    		throw new ServiceException(-1,"已有提现审核记录,稍后再提现");
		}
    	User user=userMapper.getUserforUpdate(consultantId);
    	Integer account=user.getAccount();
    	if (account<0) {
    		throw new ServiceException(-1,"余额为0");
		}
    	String redisCode=String.valueOf(redisUtils.get(cashWithdawat+":"+user.getUserName()));
    	if (StringUtils.isBlank(redisCode)||StringUtils.isBlank(code)||!redisCode.equals(code)) {
    	
    		throw new ServiceException(-1,"验证码错误");
		}
    	if (amount>account) {
    		throw new ServiceException(-1,"余额不足");
		}
    	if (userMapper.updateAccount(consultantId,-amount)<1) {
    		throw new ServiceException(-1,"提现错误");
		}
    	UserCashWithdrawal 	u=new UserCashWithdrawal(consultantId,card.getId(),amount,new Date());
    	u.setPayAccount(card.getAlipayAccount());
    	Integer accountNow=account;
    	u.setAccountNow(accountNow);
    	u.setCashWithdrawalAmountNow(user.getCashWithdrawalAmount());
    	u.setRealName(card.getRealName());
    	int result=userCashWithdrawalMapper.insertSelective(u);
    	if (result>0) {
    		pointHistoryService.pointHistoryCashWithdrawal(u,PointTypeEnum.WITHDRAW.getType(),account);
		}
		return result;
	}

	@Override
	public List<UserCashWithdrawal> pagelist(Integer userId,Integer pageNum) {
		
		PageHelper.startPage(pageNum, 13);
		Example example =new Example(UserCashWithdrawal.class);
		example.createCriteria().andEqualTo("userId", userId);
		example.setOrderByClause("id DESC");
		List<UserCashWithdrawal> list=userCashWithdrawalMapper.selectByExample(example);
		return list;
	}

	@Override
	public List<UserCardVO> cardList(Integer consultantId) {
		// TODO Auto-generated method stub
		List<UserCardVO> userCardVOs=new ArrayList<UserCardVO>();
    	List<UserCard> cards=userCardMapper.select(new UserCard(consultantId,0));
    	if (cards==null) {
    		throw new ServiceException(-1,"找不到此绑卡记录");
		}
    	for (UserCard userCard : cards) {
    		UserCardVO userCardVO=new UserCardVO();
    		BeanUtils.copyProperties(userCard, userCardVO);
        	List<UserCashWithdrawal> withdrawal=userCashWithdrawalMapper.select(new UserCashWithdrawal(consultantId,userCard.getId(),UserCashWithdrawalEnum.TO_EXAMINE.getIndex()));
        	userCardVO.setCashWithdrawal(withdrawal==null||withdrawal.isEmpty());
        	userCardVOs.add(userCardVO);
		}	
		return userCardVOs;
	}

	@Override
	public List<UserCashWithdrawalVO> listUserCashWithdrawalVO(UserCashWithdrawalParam userCashWithdrawalParam) {
		PageHelper.startPage(userCashWithdrawalParam.getPageNo(), userCashWithdrawalParam.getPageSize());
		return userCashWithdrawalMapper.listUserCashWithdrawalVO(userCashWithdrawalParam);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int updateCashType(Integer id, Integer userId, Integer cashType) {
	
		User user=userMapper.getUserforUpdate(userId);
		UserCashWithdrawal cashWithdrawal=userCashWithdrawalMapper.selectByPrimaryKey(id);
		
		if (cashWithdrawal.getCardType()!=UserCashWithdrawalEnum.TO_EXAMINE.getIndex()) {
			throw new ServiceException(-1,"当前数据已经操作过");
		}
		int amount= cashWithdrawal.getAmount();
		
		if (amount<=0) {
			throw new ServiceException(-1,"提现错误");
		}
		if (cashType== UserCashWithdrawalEnum.REFUSE.getIndex()) {
			userMapper.updateAccount(userId, amount);
			pointHistoryService.refundPointHistoryCashWithdrawal(cashWithdrawal, PointTypeEnum.REJECT.getType(),user.getAccount());
		}
		UserCashWithdrawal userCashWithdrawal =new UserCashWithdrawal();
		userCashWithdrawal.setId(id);
		userCashWithdrawal.setUserId(userId);
		userCashWithdrawal.setCardType(cashType);
		return userCashWithdrawalMapper.updateByPrimaryKeySelective(userCashWithdrawal);
	}




    
}
