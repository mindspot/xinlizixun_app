package com.kuangji.paopao.mapper;

import java.util.List;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.VisitorParam;
import com.kuangji.paopao.model.Visitor;
import com.kuangji.paopao.vo.VisitorVO;

/**
* Created by Mybatis Generator 2020/03/14
*/
 public interface VisitorMapper extends BaseMapper<Visitor> {


	 List<VisitorVO> listVisitorVO(VisitorParam visitor);
		
}