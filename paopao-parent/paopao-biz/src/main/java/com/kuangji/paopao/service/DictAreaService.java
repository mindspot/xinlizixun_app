package com.kuangji.paopao.service;

import java.util.List;

import com.kuangji.paopao.model.DictArea;
import com.kuangji.paopao.service.base.BaseService;
import com.kuangji.paopao.vo.DictAreaVO;

/**
 * Author 金威正
 * Date  2020-02-18
 */
public interface DictAreaService extends BaseService<DictArea, Integer>{

   
	List<DictAreaVO> listDictAreaVO();
	
}
