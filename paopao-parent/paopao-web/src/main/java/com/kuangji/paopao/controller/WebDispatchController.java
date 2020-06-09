package com.kuangji.paopao.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.DispatchParam;
import com.kuangji.paopao.dto.result.ExcelCheckResult;
import com.kuangji.paopao.model.Dispatch;
import com.kuangji.paopao.service.DispatchService;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.util.excel.FileUtil;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@RestController
public class WebDispatchController {
	
    @Autowired
    private DispatchService dispatchService;
   
  
    //增加派单记录
    @PostMapping(value = {"/web/dispatch/list"})
    public Object add(DispatchParam dispatchParam ) {
    	List<Dispatch> result=dispatchService.listDispatch(dispatchParam);
        return ServiceResultUtils.success(new PageInfo<Dispatch>(result));
    }
      
    
    //增加派单记录
    @PostMapping(value = {"/web/dispatch/add"})
    public Object add(@RequestBody Dispatch dispatch ) {
    	int result=dispatchService.add(dispatch);
        return ServiceResultUtils.success(result>0);
    }
    
    
    //修改派单记录
    @PostMapping(value = {"/web/dispatch/update"})
    public Object update(Dispatch dispatch ) {
    	int result=dispatchService.updateByPrimaryKeySelective(dispatch);
        return ServiceResultUtils.success(result>0);
    }
    
    //删除派单记录
    @PostMapping(value = {"/web/dispatch/delete"})
    public Object delete(Integer id ) {
    	Dispatch dispatch =new Dispatch();
    	dispatch.setId(id);
    	dispatch.setIsDelete(1);
    	int result=dispatchService.updateByPrimaryKeySelective(dispatch);
        return ServiceResultUtils.success(result>0);
    }
    @PostMapping("/web/upload/dispatch")
    public Object uploadStation( @RequestParam("file") MultipartFile multipartfile) {
        File file = FileUtil.convertMultipartFileToFile(multipartfile);
        if (file == null) {
            return ServiceResultUtils.failure("导入失败！");
        }
        List<ExcelCheckResult> list = dispatchService.checkDispatchExcel(file, null);
        if (list.size() > 0) {
            return ServiceResultUtils.failure(list);
        } else {
            int result = dispatchService.batchAddDispatchByExcel(file, null);
            return ServiceResultUtils.success(result>0);
        }

    }
}
