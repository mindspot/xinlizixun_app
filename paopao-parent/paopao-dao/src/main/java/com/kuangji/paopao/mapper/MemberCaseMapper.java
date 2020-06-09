package com.kuangji.paopao.mapper;

import java.util.List;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.MemberCase;
import com.kuangji.paopao.vo.MemberCaseVO;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface MemberCaseMapper extends BaseMapper<MemberCase> {
	
	/**
	 * 根据用户id 获取用户最后输入的一条记录
	 * Author 金威正
	 * Date  2020-02-23
	 */
    public MemberCase getLastMemberCase(int userId);
    
    
    
    /**
	 * 咨询师端 根据用户id 获取病例 
	 */
    public List<MemberCaseVO> listMemberCaseVO(int userId);
}