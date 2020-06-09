package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.dto.param.VisitorParam;
import com.kuangji.paopao.model.Visitor;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.VisitorVO;

/**
 * Author 金威正
 * Date  2020-02-16
 */
public interface VisitorService extends BaseService<Visitor, Integer>{

   

	int insertVisitor(Visitor visitor);
	
    List<VisitorVO> listVisitorVO(VisitorParam visitor);
	
}