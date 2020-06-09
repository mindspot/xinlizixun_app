package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.MemberCaseVO;

/**
 * Author 金威正
 * Date  2020-02-23
 */
public interface MemberCaseService extends BaseService<MemberCase, Integer>{


    
	/**
	 * 根据用户id 获取用户最后输入的一条记录
	 * Author 金威正
	 * Date  2020-02-23
	 */
    MemberCase getLastMemberCase(Integer userId);

	/**
	 * 咨询师端 根据用户id 获取病例
	 * Author 金威正
	 * Date  2020-02-23
	 */

    List<MemberCaseVO> listMemberCaseVO(Integer mermberId);
}
