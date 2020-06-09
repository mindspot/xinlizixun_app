package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.admin.vo.CommonVO;
import com.kuangji.paopao.dto.param.CommonParam;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.service.CommonService;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.util.ServiceResultUtils;

/**
 * Author 金威正
 * Date  2020-02-16
 */
@RestController
public class WebCommonController {
    @Autowired
    private CommonService commonService;
    
    
    private static String IMAGE_URL = PropertiesFileUtil.getInstance().get("image_url");

    //后台
    @PostMapping("/web/common/list")
    public Object listCommon(@RequestBody CommonParam commonParam) {
    	
    	List<Common> list=commonService.listCommon(commonParam);
        return ServiceResultUtils.success(new PageInfo<Common>(list));
    }
    
    //后台
    @PostMapping("/web/common/content/list")
    public Object list(@RequestBody CommonParam commonParam) {
    	
    	List<Common> list=commonService.listCommonContent(commonParam);
        return ServiceResultUtils.success(new PageInfo<Common>(list));
    }
    
    
    //后台
    @PostMapping("/web/common/addCommon")
    public Object addCommon(@RequestBody Common common) {
    	String icon=common.getIcon();
    	if (StringUtils.isNotBlank(icon)&&common.getType()==CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal()) {
    		common.setIcon(icon.replace(IMAGE_URL, ""));
		}
    	String banerVal=common.getVal();
    	if (StringUtils.isNotBlank(banerVal)&&common.getType()==CommonEnum.INDEX_IMAGE_TYPE.getVal()) {
    		common.setVal(banerVal.replace(IMAGE_URL, ""));
		}
    	
        return ServiceResultUtils.success(commonService.add(common)>0);
    }
    
    //后台
    @PostMapping("/web/common/delete")
    public Object deleteCommon(Integer id) {
    	
        int result=commonService.updateByPrimaryKeySelective(new Common(id,1));
        
        return ServiceResultUtils.success(result>0);
    }
    
    
    //后台
    @PostMapping("/web/common/updateCommon")
    public Object updateCommon(@RequestBody Common common) {
    	String val=common.getVal();
		if (val!=null) {
			common.setVal(val.replace(IMAGE_URL, ""));
		}
        return ServiceResultUtils.success(commonService.updateByPrimaryKeySelective(common)>0);
    }
    
    //获取入驻平台的标签
    @GetMapping("/web/common/settled/listLabel")
    public Object listLabel(HttpServletRequest request) {
    	String token = request.getHeader("authorization").replace("Bearer ", "");
    	CommonVO commonVO=commonService.setInLables(token);
    	
        return ServiceResultUtils.success(commonVO);
    }
    
}
