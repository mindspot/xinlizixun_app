package com.kuangji.paopao.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.constant.ExcelConstant;
import com.kuangji.paopao.dto.param.DispatchParam;
import com.kuangji.paopao.dto.result.ExcelCheckResult;
import com.kuangji.paopao.mapper.DispatchMapper;
import com.kuangji.paopao.model.Dispatch;
import com.kuangji.paopao.service.DispatchService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.util.excel.ExcelUtil;
import com.kuangji.paopao.util.excel.PoiUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@Service
public class DispatchServiceImpl extends BaseServiceImpl<Dispatch, Integer> implements DispatchService {
	@Autowired
	private DispatchMapper dispatchMapper;
	

	@Override
	public BaseMapper<Dispatch> getMapper() {
		
		return dispatchMapper;
	}
	
	@Override
	public List<Dispatch> listDispatch(DispatchParam dispatchParam) {
		Example example =new Example(Dispatch.class);
		example.createCriteria().andEqualTo("isDelete", 0);
		PageHelper.startPage(dispatchParam.getPageNo(), dispatchParam.getPageSize());
		List<Dispatch> dispatchs=dispatchMapper.selectByExample(example);
		return dispatchs;
	}

	@Override
	public List<ExcelCheckResult> checkDispatchExcel(File file, Integer userId) {
		 List<ExcelCheckResult> errorList = new ArrayList<>();
	        List<List<Object>> dataList = null;
	        InputStream inputStream = null;
	        try {
	            inputStream = new FileInputStream(file);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        List<String> tittleList = ExcelUtil.getTitle(inputStream);
	        if (!ExcelConstant.ARTICLE_TEMPLATE_TITTLE.equals(String.join(",", tittleList))) {
	            errorList.add(new ExcelCheckResult("表头", "Excel表头不正确"));
	            return errorList;
	        }
	        try {
	            dataList = PoiUtil.importExcel(file);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	        return errorList;
	}

	@Override
	public int batchAddDispatchByExcel(File file, Integer userId) {
		List<List<Object>> dataList = null;
        try {
            dataList = PoiUtil.importExcel(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //数据封装格式一，将表格中的数据遍历取出后封装进对象放进List
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).size() == 10) {
            	Dispatch dispatch =new Dispatch();
            	dispatch.setCustomerName((String) dataList.get(i).get(1));
            	dispatch.setChannel((String) dataList.get(i).get(2));
            	dispatch.setSex((String) dataList.get(i).get(3));
            	dispatch.setAge((Integer)dataList.get(i).get(4));
            	dispatch.setPhone((String) dataList.get(i).get(5));
            	dispatch.setWx((String) dataList.get(i).get(6));
            	dispatch.setQq((String) dataList.get(i).get(7));
            	dispatch.setUserId((Integer)dataList.get(i).get(8));
            	dispatch.setNumberOfContacts(0);
            	dispatch.setConsultantUserId(0);
            	//查询一次 看看出现重复数据没 
            	dispatchMapper.insertSelective(dispatch);
            }
        }
        return 1;
	}  
}
