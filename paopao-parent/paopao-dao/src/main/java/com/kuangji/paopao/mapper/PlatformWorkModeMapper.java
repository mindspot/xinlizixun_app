package com.kuangji.paopao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.PlatformWorkMode;
import com.kuangji.paopao.vo.PlatformWorkModeVO;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface PlatformWorkModeMapper extends BaseMapper<PlatformWorkMode> {
	

    /**
   	 * 修改状态
   	 * Author 金威正
   	 * Date  2020-03-02
   	 */
     public int updateWorkStatus(PlatformWorkMode work);
    
    /**
	 * 逻辑删除
	 * Author 金威正
	 * Date  2019-12-12
	 */
	public int logicDeleteById(int id);
	
	
	 /**
	 * 根据id查询出 工作模式
	 * Author 金威正
	 * Date  2019-12-12
	 */
	public List<PlatformWorkMode> listWork(@Param("ids")List<Integer> ids);


	 /**
		 * 根据id查询出 工作模式
		 * Author 金威正
		 * Date  2019-12-12
		 */
	public List<PlatformWorkModeVO> listWorkLable();
	
}