package com.kuangji.paopao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.admin.vo.MemberVO;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.UserUpdateDTO;
import com.kuangji.paopao.dto.param.MemberParam;
import com.kuangji.paopao.dto.param.UserParam;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.vo.AboutConsultantVO;
import com.kuangji.paopao.vo.ConsultantVO;
import com.kuangji.paopao.vo.ExtVO;
import com.kuangji.paopao.vo.PriceVO;
import com.kuangji.paopao.vo.UserUpdateVO;
import com.kuangji.paopao.vo.UserVO;

/**
 * Created by Mybatis Generator 2020/03/14
 */
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 根据id获取数据 Author 金威正 Date 2020-02-17
	 */
	User login(@Param("userName") String userName, @Param("type") Integer type);

	/**
	 * 根据id获取数据 Author 金威正 Date 2020-02-17
	 */
	User consultantLogin(String userName);
	
	UserVO consultantLoginVO(String userName);
	

	/**
	 * 获取数量 Author 金威正 Date 2020-02-17
	 */
	int countConsultant(Map<String, Object> map);

	/**
	 * 获取收藏夹数量 Author 金威正 Date 2020-02-17
	 */
	int countCollectionConsultant(Map<String, Object> map);

	/**
	 * 获取咨询师数据 Author 金威正 Date 2020-02-17
	 */
	List<ConsultantVO> listConsultant(Map<String, Object> map);

	/**
	 * 搜索时候获取数量 Author 金威正 Date 2020-02-17
	 */
	int countConsultantWd(Map<String, Object> map);

	/**
	 * 搜索 获取咨询师数据 Author 金威正 Date 2020-02-17
	 */
	List<ConsultantVO> listConsultantWd(Map<String, Object> map);

	/**
	 * 获取收藏咨询师 Author 金威正 Date 2020-02-17
	 */
	List<ConsultantVO> listCollectionConsultant(Map<String, Object> map);

	/**
	 * 获取个人咨询师数据 Author 金威正 Date 2020-02-17
	 */
	AboutConsultantVO getConsultant(int id);

	/**
	 * 根据id获取用户修改界面数据 Author 金威正 Date 2020-02-17
	 */
	UserUpdateVO getUserUpdateVO(int id);

	/**
	 * 根据id获取用户修改数据 Author 金威正 Date 2020-02-17
	 */
	int userUpdateVO(UserUpdateDTO updateDTO);

	/**
	 * 修改密码 Author 金威正 Date 2020-02-17
	 */
	int updatePwd(int id, String pwd);

	/**
	 * 返回查询的邀请码数量 Author 金威正 Date 2020-02-17
	 */
	int getUserInvitationCode(String code);

	/**
	 * 查询单词服务的最低价最高价 Author 金威正 Date 2020-02-17
	 */
	PriceVO getPriceVO();

	/**
	 * 发送环信订单消息 Author 金威正 Date 2020-02-17
	 */
	ExtVO getExtVO(@Param("id") Integer id);

	/**
	 * 修改金额
	 */
	int updateAccount(@Param("id") Integer id, @Param("account") Integer account);

	/**
	 * 获取该对象 上锁
	 */
	User getUserforUpdate(Integer id);

	//冻结
	int updateCashWithdrawalAmount(@Param("id") Integer id, @Param("amount") Integer amount);
	
	//去除冻结钱
	int removeCashWithdrawalAmount(@Param("id") Integer id, @Param("amount") Integer amount);
	/**
	 * 后台获取会员
	 */
	List<MemberVO> listMemberVO(MemberParam memberParam);

	/**
	 * 后台拒绝退款
	 **/
	int adminRefuseCashWithdrawal(@Param("userId") Integer userId, @Param("account") Integer account);

	List<User> listUser(UserParam userParam);
	
	//解冻
	int thawAmount(@Param("id") Integer id, @Param("amount") Integer amount);

}