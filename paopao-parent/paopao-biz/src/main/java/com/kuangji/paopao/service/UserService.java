package com.kuangji.paopao.service;

import java.util.List;
import java.util.Map;

import com.kuangji.paopao.admin.vo.MemberVO;
import com.kuangji.paopao.base.Pager;
import com.kuangji.paopao.dto.ConsultationDTO;
import com.kuangji.paopao.dto.UserUpdateDTO;
import com.kuangji.paopao.dto.param.ConsultantParam;
import com.kuangji.paopao.dto.param.MemberParam;
import com.kuangji.paopao.dto.param.UserParam;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.AboutConsultantVO;
import com.kuangji.paopao.vo.ConsultantVO;
import com.kuangji.paopao.vo.PriceVO;
import com.kuangji.paopao.vo.UserUpdateVO;

/**
 * Author 金威正 Date 2020-02-17
 */
public interface UserService extends BaseService<User, Integer> {

	/**
	 * 短信登入 Author 金威正 Date 2020-02-17
	 */
	Map<String, Object> phoneLogin(String userName, String code,String spreadUrl);

	/**
	 * 根据id获取数据 Author 金威正 Date 2020-02-17
	 */
	Map<String, Object> Login(String userName, String pwd);
	
	/**
	 * 入驻的时候登入
	 */
	Map<String, Object> settledInLogin(String userName, String code);
	
	/**
	 * 根据id获取用户修改界面数据 Author 金威正 Date 2020-02-17
	 */
	UserUpdateVO getUserUpdateVO(String token);

	/**
	 * 根据id获取用户修改数据 Author 金威正 Date 2020-02-17
	 */
	UserUpdateDTO userUpdateVO(String token, UserUpdateDTO updateDTO);

	/**
	 * 统计user表数量 Author 金威正 Date 2020-02-17
	 */
	int countConsultant(Map<String, Object> map);

	/**
	 * 获取咨询师数据 Author 金威正 Date 2020-02-17
	 */
	Pager<List<ConsultantVO>> pageListConsultant(int pageNum, ConsultationDTO consultationDTO);
	
	/**
	 * 获取咨询师数据 Author 金威正 Date 2020-02-17
	 */
	Pager<List<ConsultantVO>> pageWorkListConsultant(int pageNum, ConsultationDTO consultationDTO);



	/**
	 * 搜索 获取咨询师数据 Author 金威正 Date 2020-02-17
	 */
	Pager<List<ConsultantVO>> pageListConsultantWd(int pageNum, String wd,String token);

	/**
	 * 获取收藏咨询师数据 Author 金威正 Date 2020-02-17
	 */
	Pager<List<ConsultantVO>> pageListCollectionConsultant(String token, int pageNum);

	/**
	 * 获取个人咨询师数据 Author 金威正 Date 2020-02-17
	 */
	AboutConsultantVO getConsultant(int id);

	/**
	 * 修改密码 Author 金威正 Date 2020-02-17
	 */
	int updatePwd(String token, String pwd, String code);

	/**
	 * 查询单词服务的最低价最高价 Author 金威正 Date 2020-02-17
	 */
	PriceVO getPriceVO();

	List<User> listConsultant(ConsultantParam consultantParam);

	List<User> listMember(MemberParam memberParam);

	User getUserInfo(String token);

	Map<String, Object> consultantPhoneLogin(String userName, String code);
	
	
	Map<String, Object> consultantPhoneLoginVO(String userName, String code);
	
	
	
	/**
	 * 修改金额
	 */
	int updateAccount(Integer consultantId, Integer account );

	
	/**
	 * 获取该对象 上锁
	 */
	User getUserforUpdate(Integer id );

	/**
	 * 后台获取会员
	 */
	List<MemberVO>listMemberVO(MemberParam memberParam);


    List<User> listUser(UserParam userParam);

	Integer updateUserStatus(Integer id, Integer status);
	
	//后台 查看是不是管理员
	boolean isAdmin(Integer userId);
	//不包含自己    
	List<Integer> userIds(Integer userId);
	//包含自己
	List<Integer> containSelfUserIds(Integer userId);
	
	//环信在状态
	String getStatus(String easemobId);
	
	//是否在线
	boolean isOnline(String easemobId);
}
