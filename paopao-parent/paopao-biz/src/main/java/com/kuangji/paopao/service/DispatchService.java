package com.kuangji.paopao.service;

import java.io.File;
import java.util.List;

import com.kuangji.paopao.dto.param.DispatchParam;
import com.kuangji.paopao.dto.result.ExcelCheckResult;
import com.kuangji.paopao.model.Dispatch;
import com.kuangji.paopao.service.base.BaseService;

/**
 * Author 金威正 Date 2020-02-17
 */
public interface DispatchService extends BaseService<Dispatch, Integer>{
	
	List<Dispatch>  listDispatch(DispatchParam dispatchParam);
	
	List<ExcelCheckResult> checkDispatchExcel(File file,Integer userId);
	
	int batchAddDispatchByExcel(File file, Integer userId)	;
}
